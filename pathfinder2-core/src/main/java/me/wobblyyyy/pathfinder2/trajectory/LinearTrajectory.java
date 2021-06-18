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
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * The most simple type of trajectory. A linear trajectory does nothing other
 * than go to a point at a linear speed. Such, there's not much you can
 * customize here. But it's simple, and it works. Hopefully, that is.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class LinearTrajectory implements Trajectory {
    /**
     * The trajectory's target point.
     */
    private final PointXYZ target;

    /**
     * The speed at which the robot should follow the trajectory.
     */
    private final double speed;

    /**
     * The X/Y tolerance.
     */
    private final double tolerance;

    /**
     * The heading tolerance.
     */
    private final Angle angleTolerance;

    /**
     * Create a new {@code LinearTrajectory}.
     *
     * @param target         the trajectory's target point.
     * @param speed          the speed at which the robot should move to the
     *                       target point. Note that this speed value can not
     *                       be changed while the trajectory is being followed.
     * @param tolerance      the tolerance used in determining whether the
     *                       robot's X and Y coordinates match up with those
     *                       of the target point.
     * @param angleTolerance the tolerance used in determining whether the
     *                       robot's heading matches up with whatever heading
     *                       the robot is supposed to be facing.
     */
    public LinearTrajectory(PointXYZ target,
                            double speed,
                            double tolerance,
                            Angle angleTolerance) {
        // If the speed value isn't within a range of 0.0 to 1.0, throw a
        // runtime exception basically telling the user they're an idiot.
        // Haha. Nerd!
        if (speed < 0 || speed > 1) {
            throw new InvalidSpeedException(
                    "Attempted to create LinearFollower instance with speed " +
                            "(" + speed + "). Speed values must be greater " +
                            "than 0. and less than 1.0."
            );
        }

        this.target = target;
        this.speed = speed;
        this.tolerance = tolerance;
        this.angleTolerance = angleTolerance;
    }

    /**
     * Get the follower's target point.
     *
     * @return the follower's target point.
     */
    public PointXYZ getTarget() {
        return this.target;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return this.target;
    }

    private boolean isDoneXY(PointXYZ current) {
        return current.isNear(
                this.target,
                this.tolerance
        );
    }

    private boolean isDoneHeading(PointXYZ current) {
        return current.z().isCloseDeg(
                target.z(),
                angleTolerance.deg()
        );
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return isDoneXY(current) && isDoneHeading(current);
    }

    @Override
    public double speed(PointXYZ current) {
        return speed;
    }
}
