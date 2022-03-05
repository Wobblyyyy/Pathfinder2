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

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * A builder used for building arrays of {@link TrajectoryTarget}, designed
 * for use with {@link MultiTargetTrajectory}. In order to change the
 * precision type, speed, tolerance, or angle tolerance values of the builder,
 * you need to use the {@link #setPrecision(TargetPrecision)},
 * {@link #setSpeed(double)}, {@link #setTolerance(double)}, and
 * {@link #setAngleTolerance(Angle)} methods. Calling the
 * {@link #addTargetPoint(PointXYZ)} method will create a new
 * {@link TrajectoryTarget} instance with those target precision, angle
 * tolerance, tolerance, and speed values, as well as the provided point.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class MultiTargetBuilder {
    private final List<TrajectoryTarget> targets = new ArrayList<>();

    private TargetPrecision precision;
    private double speed;
    private double tolerance;
    private Angle angleTolerance;

    /**
     * Create a new {@code MultiTargetBuilder}.
     */
    public MultiTargetBuilder() {}

    /**
     * Set the precision of the builder.
     *
     * @param precision the precision to use for the builder.
     * @return {@code this}, used for method chaining.
     */
    public MultiTargetBuilder setPrecision(TargetPrecision precision) {
        this.precision = precision;

        return this;
    }

    /**
     * Set the speed of the builder.
     *
     * @param speed the precision to use for the builder.
     * @return {@code this}, used for method chaining.
     */
    public MultiTargetBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    /**
     * Set the tolerance of the builder.
     *
     * @param tolerance the tolerance to use for the builder.
     * @return {@code this}, used for method chaining.
     */
    public MultiTargetBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    /**
     * Set the angle tolerance of the builder.
     *
     * @param angleTolerance the angle tolerance to use for the builder.
     * @return {@code this}, used for method chaining.
     */
    public MultiTargetBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    /**
     * Add a target point using the previously-set precision, speed, tolerance,
     * and angle tolerance values.
     *
     * @param targetPoint the target point.
     * @return {@code this}, used for method chaining.
     */
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

    /**
     * Convert the {@code MultiTargetBuilder} into an array of
     * {@link TrajectoryTarget}s.
     *
     * @return an array of {@link TrajectoryTarget}s.
     */
    public TrajectoryTarget[] build() {
        TrajectoryTarget[] targetArray = new TrajectoryTarget[targets.size()];

        return targets.toArray(targetArray);
    }
}
