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

/**
 * A representation of a linear equation.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public interface LinearEquation extends Serializable {
    /**
     * Get a Y value at a specific X value according to the equation.
     *
     * @param x the X value to "look up."
     * @return the Y value, according to the inputted X value.
     */
    double getY(double x);

    /**
     * Get a point based on an X value.
     *
     * @param x the X value to "look up."
     * @return the point, according to the inputted X value.
     */
    PointXY getPoint(double x);

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
     * {@code LinearEquation}.
     *
     * @param equation the other equation.
     * @return the point of intersection between the two equations. If the
     * equations do not intersect (if they have parallel slopes) this method
     * will return null instead of a point.
     */
    PointXY getIntersection(LinearEquation equation);

    /**
     * Check if two equations intersect.
     *
     * @param equation the equation to test.
     * @return if the equations intersect, true. Otherwise, false.
     */
    boolean intersectsWith(LinearEquation equation);
}
