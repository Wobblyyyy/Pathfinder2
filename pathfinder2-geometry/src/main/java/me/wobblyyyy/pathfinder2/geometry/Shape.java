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
import java.util.ArrayList;
import java.util.List;

/**
 * A shape is... well, a shape. I don't know how else to describe it.
 *
 * <p>
 * The {@code Shape} interface uses a ray casting algorithm to determine
 * if a point is inside of a polygon.
 * <a href="https://en.wikipedia.org/wiki/Point_in_polygon">
 * Click here to learn more.
 * </a>
 * </p>
 *
 * @author Colin Robertson
 * @see Rectangle
 * @see Triangle
 * @see Circle
 * @see #getIntersections(Line, List)
 * @see #getIntersections(Line, Line...)
 * @see #getIntersectionsCount(Line, List)
 * @see #getIntersectionsCount(Line, Line...)
 * @see #doesIntersectOdd(Line, List)
 * @see #doesIntersectOdd(Line, Line...)
 * @see #doesIntersectEven(Line, List)
 * @see #doesIntersectEven(Line, Line...)
 * @since 0.1.0
 */
public interface Shape<T> extends Serializable {
    static List<Line> getIntersections(Line line,
                                       List<Line> allLines) {
        List<Line> intersectingLines = new ArrayList<>();

        for (Line l : allLines)
            if (line.doesIntersectWith(l))
                intersectingLines.add(l);

        return intersectingLines;
    }

    static int getIntersectionsCount(Line line,
                                     List<Line> allLines) {
        int i = 0;

        for (Line l : allLines)
            if (line.doesIntersectWith(l))
                i++;

        return i;
    }

    static boolean doesIntersectOdd(Line line,
                                    List<Line> allLines) {
        return getIntersectionsCount(line, allLines) % 2 == 1;
    }

    static boolean doesIntersectEven(Line line,
                                     List<Line> allLines) {
        return getIntersectionsCount(line, allLines) % 2 == 0;
    }

    static List<Line> getIntersections(Line line,
                                       Line... allLines) {
        List<Line> intersectingLines = new ArrayList<>();

        for (Line l : allLines)
            if (line.doesIntersectWith(l))
                intersectingLines.add(l);

        return intersectingLines;
    }

    static int getIntersectionsCount(Line line,
                                     Line... allLines) {
        int i = 0;

        for (Line l : allLines)
            if (line.doesIntersectWith(l))
                i++;

        return i;
    }

    static boolean doesIntersectOdd(Line line,
                                    Line... allLines) {
        return getIntersectionsCount(line, allLines) % 2 == 1;
    }

    static boolean doesIntersectEven(Line line,
                                     Line... allLines) {
        return getIntersectionsCount(line, allLines) % 2 == 0;
    }

    /**
     * Get the point in the shape that's closest to the reference point.
     *
     * @param reference the reference point.
     * @return the point in the shape that's closest to the reference point.
     */
    PointXY getClosestPoint(PointXY reference);

    /**
     * Is a given point inside the shape?
     *
     * @param reference the point to test.
     * @return true if the point is inside the shape, otherwise, false.
     */
    boolean isPointInShape(PointXY reference);

    /**
     * Does the shape collide with another shape?
     *
     * @param shape the shape to test.
     * @return if the shapes collide, true. Otherwise, false.
     */
    boolean doesCollideWith(Shape<?> shape);

    /**
     * Get the center of the shape.
     *
     * @return the shape's center.
     */
    PointXY getCenter();

    /**
     * Rotate the shape around the point's center.
     *
     * @param rotation how much to rotate the shape by.
     * @return a rotated shape.
     */
    T rotate(Angle rotation);

    /**
     * Rotate the shape around a given center point.
     *
     * @param rotation         how much to rotate the shape by.
     * @param centerOfRotation the center of rotation.
     * @return a rotated shape.
     */
    T rotate(Angle rotation, PointXY centerOfRotation);

    /**
     * Shift a shape by X and Y values.
     *
     * @param shiftX the X shift.
     * @param shiftY the Y shift.
     * @return the shifted shape.
     */
    T shift(double shiftX, double shiftY);

    /**
     * Move the shape's center to the {@code newCenter} point. This will
     * shift all the shape's points and lines.
     *
     * @param newCenter the new center for the shape.
     * @return a shifted shape.
     */
    T moveTo(PointXY newCenter);

    /**
     * Resize the shape.
     *
     * @param scale the new scale for the shape. If this number is 2, the shape
     *              will double in size. If this number is 0.5, the shape will
     *              be half the original size.
     * @return the scaled shape.
     */
    T scale(double scale);

    /**
     * Resize the shape by a specific value.
     *
     * @param growth how much to grow the shape by. All the shape's points
     *               will be moved this distance away from their original
     *               positions.
     * @return the resized shape.
     */
    T growBy(double growth);
}
