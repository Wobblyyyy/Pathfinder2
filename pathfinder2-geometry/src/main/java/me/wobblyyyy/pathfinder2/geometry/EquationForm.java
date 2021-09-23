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
 * An {@code EquationForm} is an object for storing a linear equation.
 * You could do some other type of equation - a quadratic, for example,
 * but the "slope" method wouldn't really work.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public interface EquationForm {
    /**
     * Get the equation's slope.
     *
     * @return the equation's slope.
     */
    double getSlope();

    /**
     * Get a Y value for an X value.
     *
     * @param x the X value to get a Y value for.
     * @return the Y value associated with that X value.
     */
    double getY(double x);

    /**
     * Get a point for a specific X value.
     *
     * @param x the X value to get a point for.
     * @return the point associated with that X value.
     */
    PointXY getPoint(double x);

    /**
     * Get the equation's slope, as an angle.
     *
     * @return the equation's slope.
     */
    Angle getSlopeAngle();

    /**
     * Get a slope that's perpendicular to this equation's slope, as
     * an angle.
     *
     * @return a slope, perpendicular to the equation's slope.
     */
    Angle getPerpendicularSlopeAngle();

    /**
     * Convert the equation to a line.
     *
     * @param minX the line's minimum X value.
     * @param maxX the line's maximum X value.
     * @return the {@code EquationForm} represented as a line segment.
     */
    Line toLine(double minX, double maxX);

    /**
     * Convert a {@code Line} into an {@code EquationForm}.
     *
     * @param line the line to convert.
     * @return the line, as an {@code EquationForm}.
     */
    static EquationForm lineToEquation(Line line) {
        return new PointSlopeForm(
                line.getStartPoint(),
                line.slope()
        );
    }

    /**
     * Convert a {@code Ray} into an {@code EquationForm}.
     *
     * @param ray the ray to convert.
     * @return the ray, as an {@code EquationForm}.
     */
    static EquationForm rayToEquation(Ray ray) {
        PointXY point = ray.getPoint();
        PointXY target = point.inDirection(10, ray.getDirection());

        return new PointSlopeForm(
                point,
                PointXY.slope(
                        point,
                        target
                )
        );
    }

    /**
     * Get the unbounded point of intersection between two equations. If these
     * two equations intersect at any point, this will return their point of
     * intersection. If the equations are parallel, it'll return a point
     * with an X and Y value of positive infinity.
     *
     * @param equation1 one of the two equations.
     * @param equation2 one of the two equations.
     * @return the unbounded point of intersection between the two equations.
     */
    static PointXY unboundedIntersection(EquationForm equation1,
                                         EquationForm equation2) {
        SlopeInterceptForm slopeIntercept1 = SlopeInterceptForm.getSlopeInterceptForm(equation1);
        SlopeInterceptForm slopeIntercept2 = SlopeInterceptForm.getSlopeInterceptForm(equation2);

        return SlopeInterceptForm.getIntersection(
                slopeIntercept1,
                slopeIntercept2
        );
    }

    /**
     * Try to get the bounded point of intersection between the two equations.
     * If the points intersect, but that point of intersection doesn't fit
     * within the designated range for X/Y values, this method will return
     * null.
     *
     * @param equation1 one of the two equations.
     * @param equation2 one of the two equations.
     * @param minimumX the minimum X value.
     * @param minimumY the minimum Y value.
     * @param maximumX the maximum X value.
     * @param maximumY the maximum Y value.
     * @return the bounded point of intersection between the two equations.
     * If the points do not intersect within the bounds, this will return
     * null.
     */
    static PointXY boundedIntersection(EquationForm equation1,
                                       EquationForm equation2,
                                       double minimumX,
                                       double minimumY,
                                       double maximumX,
                                       double maximumY) {
        PointXY unbounded = unboundedIntersection(equation1, equation2);

        boolean validMinX = unbounded.x() >= minimumX;
        boolean validMinY = unbounded.y() >= minimumY;
        boolean validMaxX = unbounded.x() <= maximumX;
        boolean validMaxY = unbounded.y() <= maximumY;

        return validMinX &&
                validMinY &&
                validMaxX &&
                validMaxY ? unbounded : null;
    }

    /**
     * Get the bounded point of intersection between the two equations. If the
     * bounded point is not contained within the minimum and maximum X/Y
     * values, return null.
     *
     * @param equation1 one of the two equations.
     * @param equation2 one of the two equations.
     * @param min a point with the minimum X and Y values.
     * @param max a point with the maximum X and Y values.
     * @return the bounded point of intersection between the two equations. If
     * the equations do not intersect within the bounds, return null.
     */
    static PointXY boundedIntersection(EquationForm equation1,
                                       EquationForm equation2,
                                       PointXY min,
                                       PointXY max) {
        return boundedIntersection(
                equation1,
                equation2,
                min.x(),
                min.y(),
                max.x(),
                max.y()
        );
    }

    /**
     * Are two equations parallel?
     *
     * @param equation1 one of the two equations.
     * @param equation2 one of the two equations.
     * @return true if the equations have the same slope, otherwise, false.
     */
    static boolean areParallel(EquationForm equation1,
                               EquationForm equation2) {
        return Equals.soft(
                equation1.getSlope(),
                equation2.getSlope(),
                0.01
        );
    }
}
