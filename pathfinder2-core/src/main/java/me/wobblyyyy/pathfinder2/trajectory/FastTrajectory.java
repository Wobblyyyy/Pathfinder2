/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Another rather simple type of trajectory. Like the {@link LinearTrajectory},
 * the {@code FastTrajectory} operates in a linear manner - it'll continue
 * driving towards the target point in a straight line until it reaches the
 * point.
 *
 * <p>
 * Unlike the {@link LinearTrajectory}, however, the {@code FastTrajectory}
 * does two things different:
 * <ul>
 *     <li>
 *         It doesn't require the headings to match for the trajectory to
 *         count as being completed.
 *     </li>
 *     <li>
 *         The only condition that determines whether a {@code FastTrajectory}
 *         has finished is whether or not the elapsed X and Y distances of the
 *         robot are greater than the difference between the X and Y values
 *         of the start and end points, respectively.
 *     </li>
 * </ul>
 * In other words, whenever you create a new {@code FastTrajectory}, two
 * values will be generated - {@link #minDistanceX} and {@link #minDistanceY}.
 * Whenever the {@link #isDone(PointXYZ)} method is called, two more values
 * will be generated - we'll call them {@code elapsedX} and {@code elapsedY}.
 * If both the elapsed X and elapsed Y values are greater than the minimum
 * X and minimum Y values, the trajectory has been completed.
 * </p>
 *
 * <p>
 * This significantly decreases accuracy, as the robot will not automatically
 * correct its position if it overshoots the target. However, this makes the
 * trajectory require significantly less time in situations where high degrees
 * of accuracy aren't really needed.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class FastTrajectory implements Trajectory {
    /**
     * The trajectory's start point.
     */
    private final PointXYZ start;

    /**
     * The trajectory's end point.
     */
    private final PointXYZ end;

    /**
     * The minimum X distance that must be elapsed.
     */
    private final double minDistanceX;

    /**
     * The minimum Y distance that must be elapsed.
     */
    private final double minDistanceY;

    /**
     * The speed at which the trajectory should operate at.
     */
    private final double speed;

    /**
     * Create a new {@code FastTrajectory}. To learn more about what a fast
     * trajectory does differently compared to a {@link LinearTrajectory},
     * check out the {@link FastTrajectory} class JavaDoc.
     *
     * @param start the trajectory's start point. This should usually be the
     *              robot's current position when the trajectory is created.
     * @param end   the trajectory's end point.
     * @param speed the speed at which the trajectory should operate.
     */
    public FastTrajectory(PointXYZ start,
                          PointXYZ end,
                          double speed) {
        // Exceptions!

        NullPointException.throwIfInvalid(
                "Attempted to create FastTrajectory with " +
                        "a null start point!",
                start
        );
        NullPointException.throwIfInvalid(
                "Attempted to create FastTrajectory with " +
                        "a null end point!",
                end
        );
        InvalidSpeedException.throwIfInvalid(
                "Attempted to create FastTrajectory with " +
                        "an invalid speed!",
                speed
        );

        this.start = start;
        this.end = end;
        this.speed = speed;

        // The minimum distances are absolute values because the greater than
        // comparison performed in the isDone method only works if the distance
        // travelled is, in fact, greater.
        this.minDistanceX = Math.abs(PointXYZ.distanceX(start, end));
        this.minDistanceY = Math.abs(PointXYZ.distanceY(start, end));
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return end;
    }

    @Override
    public boolean isDone(PointXYZ current) {
        // We take the absolute values of each of these measurements so we
        // don't have to deal with any negative numbers - they're not cool.
        double elapsedX = Math.abs(PointXYZ.distanceX(start, current));
        double elapsedY = Math.abs(PointXYZ.distanceY(start, current));

        // For this method to return true, the robot must have travelled at
        // least the minimum X and Y distances respectively.
        return (elapsedX >= minDistanceX) && (elapsedY >= minDistanceY);
    }

    @Override
    public double speed(PointXYZ current) {
        return speed;
    }
}
