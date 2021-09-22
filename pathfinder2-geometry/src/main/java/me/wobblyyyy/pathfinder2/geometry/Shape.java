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

/**
 * A shape is any enclosed polygon (or, in the case of circles, non-polygons?)
 * Regardless, it's for any enclosed shapes.
 *
 * <p>
 * Shapes aren't very useful if you're just using the basic features of
 * Pathfinder because you don't need them. However, if you're planning on
 * doing things such as collision prevention and dynamic pathing, you'll
 * want to use shapes to represent physical objects so you can mathematically
 * make sure you don't slam into a wall at Mach 3.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public interface Shape {
    /**
     * Is the provided point contained within the shape?
     *
     * @param point the point to test.
     * @return true if the point is contained within the shape, otherwise,
     * true.
     */
    boolean isPointInShape(PointXY point);

    /**
     * Is the provided point not contained within the shape?
     *
     * @param point the point to test.
     * @return false if the point is contained within the shape, otherwise,
     * true.
     */
    boolean isPointNotInShape(PointXY point);

    /**
     * Find the point contained within the shape that's closest to the
     * reference point's center.
     *
     * @param referencePoint the point to use for comparison.
     * @return the closest point to the reference point.
     */
    PointXY getClosestPoint(PointXY referencePoint);

    /**
     * Find the point contained within the shape that's farthest from the
     * reference point's center.
     *
     * @param referencePoint the point to use for comparison.
     * @return the farthest point from the reference point.
     */
    PointXY getFurthestPoint(PointXY referencePoint);

    /**
     * Does this shape collide with another shape?
     *
     * @param shape the shape to test.
     * @return true if there are any collisions (any areas that are in
     * BOTH shapes are collisions) and false if there are not.
     */
    boolean collidesWith(Shape shape);

    /**
     * Get the center point of the shape.
     *
     * @return the shape's center.
     */
    PointXY getCenter();
}
