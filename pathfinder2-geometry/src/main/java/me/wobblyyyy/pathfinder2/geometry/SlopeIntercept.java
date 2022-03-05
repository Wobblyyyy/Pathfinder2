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

import me.wobblyyyy.pathfinder2.math.Equals;

/**
 * The most simple implementation of the {@link LinearEquation} interface,
 * the {@code SlopeIntercept} form is a classic I'm sure you've heard of before.
 * {@code y = mx + b}
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class SlopeIntercept implements LinearEquation {
    private final double slope;
    private final double intercept;
    private final boolean isVertical;
    private final double verticalX;

    public SlopeIntercept(double slope, double intercept) {
        if (!validateDouble(slope)) throw new IllegalArgumentException(
            "Invalid slope! Must be a finite number."
        );
        if (!validateDouble(intercept)) throw new IllegalArgumentException(
            "Invalid intercept! Must be a finite number."
        );

        this.slope = slope;
        this.intercept = intercept;
        this.isVertical = false;
        this.verticalX = 0;
    }

    private SlopeIntercept(double x) {
        if (!validateDouble(x)) throw new IllegalArgumentException(
            "Invalid X value! Must be a finite number."
        );

        this.slope = 0;
        this.intercept = 0;
        this.isVertical = true;
        this.verticalX = x;
    }

    /**
     * Create a new vertical linear equation. This has a slope of infinity.
     *
     * @param x the line's X value.
     * @return a new linear equation.
     */
    public static SlopeIntercept newVertical(double x) {
        return new SlopeIntercept(x);
    }

    private static boolean validateDouble(double value) {
        return Double.isFinite(value) && !Double.isNaN(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getY(double x) {
        if (!validateDouble(x)) throw new IllegalArgumentException(
            "Illegal X value! Must be a finite real number."
        );

        // y = mx + b
        return (slope * x) + intercept;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXY getPoint(double x) {
        return new PointXY(x, getY(x));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXY getPoint() {
        return getPoint(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getSlope() {
        return slope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getIntercept() {
        return intercept;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVertical() {
        return isVertical;
    }

    @Override
    public double getVertical() {
        return verticalX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXY getIntersection(LinearEquation equation) {
        if (isVertical() && equation.isVertical()) {
            // if they're both vertical, check to see if they have the same
            // X axis intercept - if they don't, the lines don't intersect,
            // and we should return null

            if (
                Equals.soft(
                    getVertical(),
                    equation.getVertical(),
                    Geometry.toleranceIsVertical
                )
            ) {
                return new PointXY(getVertical(), 0);
            }

            return null;
        } else if (isVertical() || equation.isVertical()) {
            // if either of the equations are vertical, find the point
            // of intersection

            if (isVertical()) {
                double x = getVertical();

                return equation.getPoint(x);
            }

            if (equation.isVertical()) {
                double x = equation.getVertical();

                return getPoint(x);
            }
        } else {
            // if neither of the two equations are vertical, determine the
            // point of intersection using the following math:
            // y1 = m1x + b1
            // y2 = m2x + b2
            // m1x + b1 = m2x + b2
            // m1x - m2x = b2 - b1
            // x(m1 - m2) = b2 - b1
            // x = (b2 - b1) / (m1 - m2)

            double numerator = equation.getIntercept() - getIntercept();
            double denominator = getSlope() - equation.getSlope();
            double x = numerator / denominator;

            // if x isn't valid, y won't be either - return null
            if (!validateDouble(x)) return null;

            double y = equation.getY(x);

            // if y IS valid, return our lovely point of intersection
            if (validateDouble(y)) return new PointXY(x, y);
        }

        // if none of the other branches were executed, return null,
        // indicating that there is no point of intersection.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersectsWith(LinearEquation equation) {
        return getIntersection(equation) != null;
    }
}
