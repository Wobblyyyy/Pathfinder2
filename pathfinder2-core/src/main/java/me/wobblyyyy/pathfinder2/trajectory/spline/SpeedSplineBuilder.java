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

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;

import java.util.ArrayList;
import java.util.List;

public class SpeedSplineBuilder {
    private final double length;
    private final List<PointXY> points = new ArrayList<>();

    public SpeedSplineBuilder(double startX,
                              double endX) {
        this.length = endX - startX;
    }

    public SpeedSplineBuilder addExact(double x,
                                       double speed) {
        points.add(new PointXY(x, speed));

        return this;
    }

    public SpeedSplineBuilder addPercent(double xPercent,
                                         double speed) {
        double x = xPercent * length;

        return addExact(x, speed);
    }

    public Spline build() {
        return MonotoneCubicSpline.create(points);
    }
}
