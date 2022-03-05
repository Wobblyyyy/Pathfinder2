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

import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Builder for creating a {@link TrajectoryTarget}.
 *
 * <p>
 * The default values are as follows:
 * <ul>
 *     <li>target: null</li>
 *     <li>precision: {@link TargetPrecision#FAST}</li>
 *     <li>speed: 0.5</li>
 *     <li>tolerance: 2</li>
 *     <li>angleTolerance: 5 degrees</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class TargetBuilder {
    private PointXYZ target = null;
    private TargetPrecision precision = TargetPrecision.FAST;
    private double speed = 0.5;
    private double tolerance = 2;
    private Angle angleTolerance = Angle.fromDeg(5);

    public TargetBuilder() {}

    public TargetBuilder setTarget(PointXYZ target) {
        this.target = target;

        return this;
    }

    public TargetBuilder setPrecision(TargetPrecision precision) {
        this.precision = precision;

        return this;
    }

    public TargetBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public TargetBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public TargetBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    public TrajectoryTarget build() {
        if (target == null) {
            throw new NullPointException(
                "Attempted to build a TrajectoryTarget without a " +
                "target point!"
            );
        }

        return new TrajectoryTarget(
            target,
            precision,
            speed,
            tolerance,
            angleTolerance
        );
    }
}
