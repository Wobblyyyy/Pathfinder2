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

import java.util.ArrayList;
import java.util.List;

public class MultiTargetBuilder {
    private final List<TrajectoryTarget> targets = new ArrayList<>();

    private TargetPrecision precision;
    private double speed;
    private double tolerance;
    private Angle angleTolerance;

    public MultiTargetBuilder() {

    }

    public MultiTargetBuilder setPrecision(TargetPrecision precision) {
        this.precision = precision;

        return this;
    }

    public MultiTargetBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public MultiTargetBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public MultiTargetBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    public MultiTargetBuilder addTargetPoint(PointXYZ targetPoint) {
        targets.add(
                new TrajectoryTarget(
                        targetPoint,
                        precision,
                        speed,
                        tolerance,
                        angleTolerance
                )
        );

        return this;
    }

    public TrajectoryTarget[] build() {
        TrajectoryTarget[] targetArray = new TrajectoryTarget[targets.size()];

        return targets.toArray(targetArray);
    }
}
