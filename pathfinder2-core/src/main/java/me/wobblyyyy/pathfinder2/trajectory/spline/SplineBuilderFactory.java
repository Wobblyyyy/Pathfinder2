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

import me.wobblyyyy.pathfinder2.geometry.Angle;

public class SplineBuilderFactory {
    double step;
    double speed;
    double tolerance;
    Angle angleTolerance;

    public SplineBuilderFactory() {

    }

    public SplineBuilderFactory setStep(double step) {
        this.step = step;

        return this;
    }

    public SplineBuilderFactory setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public SplineBuilderFactory setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public SplineBuilderFactory setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    public AdvancedSplineTrajectoryBuilder emptyBuilder() {
        return new AdvancedSplineTrajectoryBuilder();
    }

    public AdvancedSplineTrajectoryBuilder builder(boolean autoSetStep,
                                                   boolean autoSetSpeed,
                                                   boolean autoSetTolerance,
                                                   boolean autoSetAngleTolerance) {
        AdvancedSplineTrajectoryBuilder builder = new AdvancedSplineTrajectoryBuilder();
        if (autoSetStep) builder.setStep(step);
        if (autoSetSpeed) builder.setSpeed(speed);
        if (autoSetTolerance) builder.setTolerance(tolerance);
        if (autoSetAngleTolerance) builder.setAngleTolerance(angleTolerance);
        return builder;
    }

    public AdvancedSplineTrajectoryBuilder builder() {
        return builder(true, true, true, true);
    }
}
