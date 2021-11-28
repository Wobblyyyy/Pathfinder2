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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class TrajectoryTarget {
    private final PointXYZ target;
    private final TargetPrecision precision;
    private final double speed;
    private final double tolerance;
    private final Angle angleTolerance;

    public TrajectoryTarget(PointXYZ target,
                            TargetPrecision precision,
                            double speed,
                            double tolerance,
                            Angle angleTolerance) {
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
