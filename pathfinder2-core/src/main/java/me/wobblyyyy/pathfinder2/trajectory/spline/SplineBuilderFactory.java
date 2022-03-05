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

/**
 * A factory used for producing {@link AdvancedSplineTrajectoryBuilder}. It's
 * pretty complicated, I know - a factory for producing builders for producing
 * spline trajectories? You know how it be. This is the suggested way to create
 * splines, as it's the easiest and makes code the cleanest.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class SplineBuilderFactory {
    double step;
    double speed;
    double tolerance;
    Angle angleTolerance;

    public SplineBuilderFactory() {}

    /**
     * Set the factory's step value.
     *
     * @param step the factory's step value.
     * @return {@code this}, used for method chaining.
     */
    public SplineBuilderFactory setStep(double step) {
        this.step = step;

        return this;
    }

    /**
     * Set the factory's speed value.
     *
     * @param speed the factory's speed value.
     * @return {@code this}, used for method chaining.
     */
    public SplineBuilderFactory setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    /**
     * Set the factory's tolerance value.
     *
     * @param tolerance the factory's tolerance value.
     * @return {@code this}, used for method chaining.
     */
    public SplineBuilderFactory setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    /**
     * Set the factory's angle tolerance value.
     *
     * @param angleTolerance the factory's angle tolerance value.
     * @return {@code this}, used for method chaining.
     */
    public SplineBuilderFactory setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    /**
     * Create a new {@link AdvancedSplineTrajectoryBuilder} without setting
     * the speed, step, tolerance, or angle tolerance.
     *
     * @return a new {@link AdvancedSplineTrajectoryBuilder}.
     */
    public AdvancedSplineTrajectoryBuilder emptyBuilder() {
        return new AdvancedSplineTrajectoryBuilder();
    }

    /**
     * Create a new {@link AdvancedSplineTrajectoryBuilder}, using the provided
     * values. By "automatically," I mean the values will be copied
     * over from {@code this} factory's values.
     *
     * @param autoSetStep           should step be set automatically?
     * @param autoSetSpeed          should speed be set automatically?
     * @param autoSetTolerance      should tolerance be set automatically?
     * @param autoSetAngleTolerance should angle tolerance be set automatically?
     * @return new {@link AdvancedSplineTrajectoryBuilder}.
     */
    public AdvancedSplineTrajectoryBuilder builder(
        boolean autoSetStep,
        boolean autoSetSpeed,
        boolean autoSetTolerance,
        boolean autoSetAngleTolerance
    ) {
        AdvancedSplineTrajectoryBuilder builder = new AdvancedSplineTrajectoryBuilder();
        if (autoSetStep) builder.setStep(step);
        if (autoSetSpeed) builder.setSpeed(speed);
        if (autoSetTolerance) builder.setTolerance(tolerance);
        if (autoSetAngleTolerance) builder.setAngleTolerance(angleTolerance);
        return builder;
    }

    /**
     * Create a new {@link AdvancedSplineTrajectoryBuilder} and copy over
     * the speed, step, tolerance, and angle tolerance values.
     *
     * @return a new {@link AdvancedSplineTrajectoryBuilder}.
     */
    public AdvancedSplineTrajectoryBuilder builder() {
        return builder(true, true, true, true);
    }
}
