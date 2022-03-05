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

import java.io.Serializable;
import me.wobblyyyy.pathfinder2.math.Equation;

/**
 * A representation of a linear equation. A linear equation is a
 * first-degree function, meaning the highest power X is raised to is 1.
 * Linear equations are used for - you guessed it - {@link Line}s! You
 * can also do some other math with them, but I just haven't found a use for
 * it yet. So yeah.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public interface LinearEquation extends Serializable, Equation {
    /**
     * Get the line's "point." This is mostly useful for vertical lines.
     *
     * <p>
     * The line's "point" can be any point along the line.
     * </p>
     *
     * @return the line's "point."
     */
    PointXY getPoint();

    /**
     * Get the line's slope.
     *
     * @return the line's slope, as a double.
     */
    double getSlope();

    /**
     * Get the equation's intercept with the Y axis.
     *
     * <p>
     * {@link #getY(double)} with an input of 0 returns the same number
     * as this method.
     * </p>
     *
     * @return the equation's intercept with the Y axis.
     */
    double getIntercept();

    /**
     * Is the line vertical, meaning it has a slope of +- infinity?
     *
     * @return true if the line is vertical, otherwise, false.
     */
    boolean isVertical();

    /**
     * If the line is vertical, get the line's X position.
     *
     * <p>
     * If the line is not vertical, return 0.0.
     * </p>
     *
     * @return the line's X position.
     */
    double getVertical();

    /**
     * Get the point of intersection between this equation and another
     * {@code LinearEquation}. If the lines do not intersect (say, for
     * example, they're parallel lines) this method should return {@code null}.
     * This method does not take into account domain or range - any intersection
     * between the two lines is considered valid and will be returned.
     *
     * @param equation the other equation.
     * @return the point of intersection between the two equations. If the
     * equations do not intersect (if they have parallel slopes) this method
     * will return null instead of a point.
     */
    PointXY getIntersection(LinearEquation equation);

    /**
     * Check if two equations intersect. This should, generally, always return
     * true unless the equations are parallel.
     *
     * @param equation the equation to test.
     * @return if the equations intersect, true. Otherwise, false.
     */
    boolean intersectsWith(LinearEquation equation);

    /**
     * Create a linear equation based on two points.
     *
     * @param x1 the first x value.
     * @param y1 the first y value.
     * @param x2 the second x value.
     * @param y2 the second y value.
     * @return a new {@code LinearEquation} based on the two points.
     */
    static LinearEquation getEquation(
        double x1,
        double y1,
        double x2,
        double y2
    ) {
        double rise = y2 - y1;
        double run = x2 - x1;
        double slope = rise / run;

        return new PointSlope(new PointXY(x1, y1), slope);
    }

    /**
     * Create a linear equation based on two points.
     *
     * @param a one of the two points.
     * @param b one of the two points.
     * @return a new {@code LinearEquation} based on the two points.
     */
    static LinearEquation getEquation(PointXY a, PointXY b) {
        return getEquation(a.x(), a.y(), b.x(), b.y());
    }
}
