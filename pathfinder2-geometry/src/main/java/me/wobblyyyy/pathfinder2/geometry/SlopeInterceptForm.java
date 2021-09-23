/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

import me.wobblyyyy.pathfinder2.math.Average;
import me.wobblyyyy.pathfinder2.math.Equals;

/**
 * You know it, you love it - slope-intercept form! The good old...
 * <code>
 * y = mx + b
 * </code>
 * A classic, truly.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class SlopeInterceptForm implements EquationForm {
    private final double slope;
    private final double intercept;

    /**
     * Create a new slope intercept form.
     *
     * @param slope     the equation's slope.
     * @param intercept the equation's Y intercept.
     */
    public SlopeInterceptForm(double slope,
                              double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    /**
     * Get the slope intercept form of an {@code EquationForm}.
     *
     * @param equation the equation to use.
     * @return that equation, in slope-intercept form.
     */
    public static SlopeInterceptForm getSlopeInterceptForm(EquationForm equation) {
        double slope = equation.getSlope();
        double intercept = equation.getY(0);

        return new SlopeInterceptForm(slope, intercept);
    }

    /**
     * Get the intersection of two slope-intercept forms.
     *
     * @param a one of the two slope-intercept forms.
     * @param b one of the two slope-intercept forms.
     * @return the point of intersection between the two equations.
     */
    public static PointXY getIntersection(SlopeInterceptForm a,
                                          SlopeInterceptForm b) {
        if (Equals.soft(a.slope, b.slope, 0.001)) {
            return new PointXY(
                    Double.POSITIVE_INFINITY,
                    Double.NEGATIVE_INFINITY
            );
        }

        double x = (b.intercept - a.intercept) / (a.slope - b.slope);
        double y1 = a.getY(x);
        double y2 = b.getY(x);

        return new PointXY(
                x,
                Average.of(y1, y2)
        );
    }

    @Override
    public double getSlope() {
        return slope;
    }

    @Override
    public double getY(double x) {
        return (slope * x) + intercept;
    }

    @Override
    public PointXY getPoint(double x) {
        return new PointXY(
                x,
                getY(x)
        );
    }

    @Override
    public Angle getSlopeAngle() {
        return null;
    }

    @Override
    public Angle getPerpendicularSlopeAngle() {
        return null;
    }

    @Override
    public Line toLine(double minX,
                       double maxX) {
        PointXY start = getPoint(minX);
        PointXY end = getPoint(maxX);

        return new Line(
                start,
                end
        );
    }

    public double getIntercept() {
        return intercept;
    }
}
