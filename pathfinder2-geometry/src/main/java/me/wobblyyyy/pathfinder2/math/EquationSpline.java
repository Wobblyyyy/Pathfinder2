/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

import me.wobblyyyy.pathfinder2.geometry.PointXY;

/**
 * Implementation of the {@code Spline} interface that utilizes an
 * {@link Equation} to determine points.
 *
 * @author Colin Robertson
 * @since 1.3.0
 */
public class EquationSpline implements Spline {
    private final Equation equation;
    private final MinMax clipper;
    private final PointXY startPoint;
    private final PointXY endPoint;

    public EquationSpline(Equation equation) {
        this.equation = equation;
        this.clipper = MinMax.doNotClip();
        this.startPoint = null;
        this.endPoint = null;
    }

    public EquationSpline(Equation equation,
                          double minimumX,
                          double maximumX) {
        this.equation = equation;
        this.clipper = new MinMax(minimumX, maximumX);
        this.startPoint = interpolate(minimumX);
        this.endPoint = interpolate(minimumX);
    }

    @Override
    public double interpolateY(double x) {
        x = clipper.clip(x);
        return equation.getY(x);
    }

    @Override
    public PointXY getStartPoint() {
        if (startPoint == null)
            throw new NullPointerException("Could not access the start " +
                    "point because it's null! Make sure to use the " +
                    "constructor that accepts 3 parameters to specify " +
                    "minimum and maximum X values.");

        return startPoint;
    }

    @Override
    public PointXY getEndPoint() {
        if (endPoint == null)
            throw new NullPointerException("Could not access the end " +
                    "point because it's null! Make sure to use the " +
                    "constructor that accepts 3 parameters to specify " +
                    "minimum and maximum X values.");

        return endPoint;
    }
}
