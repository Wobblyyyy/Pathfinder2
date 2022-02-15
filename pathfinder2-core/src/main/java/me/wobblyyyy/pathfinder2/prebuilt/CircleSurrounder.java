/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * Helper class for utilizing {@link CircleSurround}.
 *
 * @author Colin Robertson
 * @see CircleSurround
 * @since 0.1.0
 */
public class CircleSurrounder {
    private final Pathfinder pathfinder;
    private final PointXY center;
    private final double radius;

    /**
     * Create a new {@code CircleSurrounder}.
     *
     * @param pathfinder the Pathfinder instance the surrounder should use.
     * @param center     the center of the circle.
     * @param radius     the radius of the circle.
     */
    public CircleSurrounder(Pathfinder pathfinder,
                            PointXY center,
                            double radius) {
        this.pathfinder = pathfinder;
        this.center = center;
        this.radius = radius;
    }

    /**
     * Surround the circle. This will find the closest point, create a new
     * {@link me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory} to that
     * point, and then instruct Pathfinder to follow it.
     */
    public void surround() {
        PointXYZ robotPosition = pathfinder.getPosition();
        double speed = pathfinder.getSpeed();
        double tolerance = pathfinder.getTolerance();
        Angle angleTolerance = pathfinder.getAngleTolerance();

        Trajectory trajectory = CircleSurround.trajectoryToClosestPoint(
                robotPosition,
                center,
                radius,
                speed,
                tolerance,
                angleTolerance
        );

        pathfinder.followTrajectory(trajectory);
    }

    /**
     * Surround the circle. This will find the closest point, create a new
     * {@link me.wobblyyyy.pathfinder2.trajectory.FastTrajectory} to that
     * point, and then instruct Pathfinder to follow it.
     */
    public void fastSurround() {
        PointXYZ robotPosition = pathfinder.getPosition();
        double speed = pathfinder.getSpeed();
        double tolerance = pathfinder.getTolerance();
        Angle angleTolerance = pathfinder.getAngleTolerance();

        Trajectory trajectory = CircleSurround.fastTrajectoryToClosestPoint(
                robotPosition,
                center,
                radius,
                speed
        );

        pathfinder.followTrajectory(trajectory);
    }
}
