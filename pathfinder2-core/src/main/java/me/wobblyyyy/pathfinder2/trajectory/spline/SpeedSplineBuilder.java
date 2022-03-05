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

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;

/**
 * A builder used for creating a specially-tuned {@link Spline}, meant
 * specifically for controlling the speed of a robot.
 *
 * @author Colin Robertson
 * @since 0.6.1.
 */
public class SpeedSplineBuilder {
    private final double length;
    private final List<PointXY> points = new ArrayList<>();

    /**
     * Create a new {@code SpeedSplineBuilder}.
     *
     * @param startX the spline's start X value.
     * @param endX   the spline's end X value.
     */
    public SpeedSplineBuilder(double startX, double endX) {
        this.length = endX - startX;
    }

    /**
     * Add a new control point to the spline, using the provided X value,
     * as well as the provided speed value.
     *
     * @param x     the X value to use.
     * @param speed the speed at that specific X value.
     * @return {@code this}, used for method chaining.
     */
    public SpeedSplineBuilder addExact(double x, double speed) {
        points.add(new PointXY(x, speed));

        return this;
    }

    /**
     * Add a new control point to the spline, using the provided X value,
     * as well as the provided speed value.
     *
     * @param xPercent a percentage value. This value should be greater than
     *                 or equal to 0 and less than or equal to 1.0. 0.5, for
     *                 example, would be exactly in the middle of the spline's
     *                 X values.
     * @param speed    the speed at that specific X value.
     * @return {@code this}, used for method chaining.
     */
    public SpeedSplineBuilder addPercent(double xPercent, double speed) {
        double x = xPercent * length;

        return addExact(x, speed);
    }

    /**
     * Convert the {@code SpeedSplineBuilder} into a {@link Spline}.
     *
     * @return a new {@code Spline}.
     */
    public Spline build() {
        return MonotoneCubicSpline.create(points);
    }
}
