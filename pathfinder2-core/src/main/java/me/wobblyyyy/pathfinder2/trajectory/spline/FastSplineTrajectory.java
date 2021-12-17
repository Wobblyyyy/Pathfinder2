/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.spline;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Spline;
import me.wobblyyyy.pathfinder2.trajectory.FastTrajectory;

/**
 * A wrapper for {@link FastTrajectory} that uses a spline to determine
 * the speed of the robot, allowing for very epic acceleration and deceleration.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class FastSplineTrajectory extends FastTrajectory {
    private final Spline speedSpline;

    /**
     * Create a new {@code FastSplineTrajectory}.
     *
     * @param start       the robot's current position.
     * @param end         the robot's target position.
     * @param speedSpline the spline responsible for controlling the
     *                    robot's speed.
     */
    public FastSplineTrajectory(PointXYZ start,
                                PointXYZ end,
                                Spline speedSpline) {
        super(start, end, 0.5);

        this.speedSpline = speedSpline;
    }

    @Override
    public double speed(PointXYZ current) {
        return speedSpline.interpolateY(current.x());
    }
}
