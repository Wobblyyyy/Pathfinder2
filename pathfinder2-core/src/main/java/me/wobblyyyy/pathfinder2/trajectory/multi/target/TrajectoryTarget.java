/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.multi.target;

import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * A single target for a multi-target trajectory.
 *
 * <p>
 * Each target has several values:
 * <ul>
 *     <li>
 *         <b>Type of precision</b> -
 *         which type of precision the target point uses. There are two types
 *         of precision - {@code PRECISE} and {@code FAST}. The differences
 *         between these is documented here: {@link TargetPrecision}
 *     </li>
 *     <li>
 *         <b>Speed</b> -
 *         how fast the robot should move towards the target point. This speed
 *         value should be greater than 0 and less than or equal to 1.
 *     </li>
 *     <li>
 *         <b>Tolerance</b> -
 *         the tolerance the target should have. This value only impacts
 *         {@code PRECISE} trajectories.
 *     </li>
 *     <li>
 *         <b>Angle tolerance</b> -
 *         the same thing as regular tolerance, but for angles. Likewise,
 *         this only impacts {@code PRECISE} trajectories.
 *     </li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class TrajectoryTarget {
    private final PointXYZ target;
    private final TargetPrecision precision;
    private final double speed;
    private final double tolerance;
    private final Angle angleTolerance;

    /**
     * Create a new {@code TrajectoryTarget}.
     *
     * @param target         the point that the trajectory target will try
     *                       to reach.
     * @param precision      the type of precision the target should use.
     *                       Either {@code FAST} or {@code PRECISE}.
     * @param speed          the speed at which the robot will move towards
     *                       the target. This value should be greater than 0
     *                       and less than or equal to 1.
     * @param tolerance      the tolerance used in determining whether the robot
     *                       has reached its target point. This only impacts
     *                       {@code PRECISE} targets.
     * @param angleTolerance the angle tolerance used in determining whether the
     *                       robot has reached its target point. This only
     *                       impacts {@code PRECISE} targets.
     */
    public TrajectoryTarget(PointXYZ target,
                            TargetPrecision precision,
                            double speed,
                            double tolerance,
                            Angle angleTolerance) {
        if (target == null) throw new NullPointException(
                "Cannot create a trajectory target with a null point!");

        if (speed <= 0 || speed > 1) throw new InvalidSpeedException(
                "Cannot create a trajectory target with a speed value " +
                        "less than or equal to 0 or greater than 1!");

        if (tolerance < 0) throw new InvalidToleranceException(
                "Cannot create a trajectory target with a tolerance " +
                        "value less than 0!");

        if (angleTolerance == null) throw new NullAngleException(
                "Cannot create a trajectory target with a null angle!");

        this.target = target;
        this.precision = precision;
        this.speed = speed;
        this.tolerance = tolerance;
        this.angleTolerance = angleTolerance;
    }

    public PointXYZ target() {
        return this.target;
    }

    public TargetPrecision precision() {
        return this.precision;
    }

    public double speed() {
        return this.speed;
    }

    public double tolerance() {
        return this.tolerance;
    }

    public Angle angleTolerance() {
        return this.angleTolerance;
    }
}
