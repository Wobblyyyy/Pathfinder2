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

import java.util.ArrayList;
import java.util.List;

/**
 * A ray is essentially an infinite line starting at a given point and
 * going on in a given direction forever. This is different from a
 * {@link Line}, but the two classes are pretty similar in terms of
 * functionality.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Ray {
    private final PointXY point;
    private final Angle direction;

    /**
     * Create a new {@code Ray}.
     *
     * @param point     the ray's point.
     * @param direction the direction the ray should go in.
     */
    public Ray(PointXY point,
               Angle direction) {
        this.point = point;
        this.direction = direction;
    }

    /**
     * Create a new {@code Ray} by providing a start point and two target
     * points in case one of them doesn't work. You'll probably only ever
     * need this method if you're creating your own shapes and want to
     * determine if a point is inside a shape.
     *
     * @param start    the ray's start point.
     * @param center   one of the two target points.
     * @param fallback one of the two target points.
     * @return a {@code Ray} based on the start point, having a direction
     * equal to the angle FROM start to target is.
     */
    public static Ray createRay(PointXY start,
                                PointXY center,
                                PointXY fallback) {
        PointXY target = center;

        if (start.isNear(center, 0.01)) {
            target = fallback;
        }

        return new Ray(
                start,
                start.angleTo(target)
        );
    }

    public static boolean intersectsOdd(PointXY start,
                                        PointXY center,
                                        PointXY fallback,
                                        List<Line> lines) {
        return createRay(
                start,
                center,
                fallback
        ).intersectsOdd(lines);
    }

    public static boolean intersectsEven(PointXY start,
                                         PointXY center,
                                         PointXY fallback,
                                         List<Line> lines) {
        return createRay(
                start,
                center,
                fallback
        ).intersectsEven(lines);
    }

    public Line toLine(double length) {
        return new Line(
                point,
                point.inDirection(
                        length,
                        direction
                )
        );
    }

    public Line toLine(double endPointX,
                       double endPointY) {
        return new Line(
                point,
                new PointXY(
                        endPointX,
                        endPointY
                )
        );
    }

    public Line toLine(double minimumX,
                       double minimumY,
                       double maximumX,
                       double maximumY) {
        PointXY minimumPoint = new PointXY(minimumX, minimumY);
        PointXY maximumPoint = new PointXY(maximumX, maximumY);

        double distanceToMin = point.distance(minimumPoint);
        double distanceToMax = point.distance(maximumPoint);

        Angle angleToMin = point.angleTo(minimumPoint);
        Angle angleToMax = point.angleTo(maximumPoint);

        return new Line(
                point.inDirection(
                        distanceToMin,
                        angleToMin
                ),
                point.inDirection(
                        distanceToMax,
                        angleToMax
                )
        );
    }

    /**
     * Do a ray and line intersect?
     *
     * @param ray  the ray to test.
     * @param test the line to test.
     * @return true if they intersect, otherwise, false.
     */
    public static boolean doesIntersect(Ray ray,
                                        Line test) {
        Line rayAsLine = ray.toLine(
                test.minimumX(),
                test.minimumY(),
                test.maximumX(),
                test.maximumY()
        );

        return test.doesIntersectWith(rayAsLine);
    }

    /**
     * Do a ray and ray intersect?
     *
     * @param ray  the ray to test.
     * @param test the other ray to test.
     * @return true if they intersect, otherwise, false.
     */
    public static boolean doesIntersect(Ray ray,
                                        Ray test) {
        Line rayAsLine = ray.toLine(1);
        Line testAsLine = ray.toLine(1);

        PointXY intersection = Line.unboundedPointOfIntersection(
                rayAsLine,
                testAsLine
        );

        boolean xValid = !(intersection.x() >= Double.MAX_VALUE) &&
                !(intersection.x() <= Double.MIN_VALUE) &&
                !Double.isNaN(intersection.x());
        boolean yValid = !(intersection.y() >= Double.MAX_VALUE) &&
                !(intersection.y() <= Double.MIN_VALUE) &&
                !Double.isNaN(intersection.y());

        return xValid && yValid;
    }

    /**
     * From a list of lines, return a list only containing lines that
     * are intersected by the {@code Ray}.
     *
     * @param ray      the ray to test with.
     * @param allLines the set of lines.
     * @return a list of lines intersected by the ray.
     */
    public static List<Line> getIntersectingLines(Ray ray,
                                                  List<Line> allLines) {
        List<Line> intersectingLines = new ArrayList<>();

        for (Line line : allLines)
            if (ray.doesIntersect(line)) intersectingLines.add(line);

        return intersectingLines;
    }

    /**
     * For a given list of lines, count how many of those lines are
     * intersected by the ray.
     *
     * @param ray      the ray to test with.
     * @param allLines all the lines to test.
     * @return a count of how many of the lines in the list are intersected
     * by the ray.
     */
    public static int howManyIntersections(Ray ray,
                                           List<Line> allLines) {
        return getIntersectingLines(
                ray,
                allLines
        ).size();
    }

    /**
     * Does a given ray intersect with an even or odd number of the
     * lines in the list?
     *
     * @param ray      the ray to test.
     * @param allLines all the lines to test.
     * @return true if there's an odd number of intersections, false if
     * there's an even number of intersections.
     */
    public static boolean intersectsOdd(Ray ray,
                                        List<Line> allLines) {
        return howManyIntersections(ray, allLines) % 2 == 1;
    }

    /**
     * Does a given ray intersect with an even or odd number of the
     * lines in the list?
     *
     * @param ray      the ray to test.
     * @param allLines all the lines to test.
     * @return false if there's an odd number of intersections, true if
     * there's an even number of intersections.
     */
    public static boolean intersectsEven(Ray ray,
                                         List<Line> allLines) {
        return howManyIntersections(ray, allLines) % 2 == 0;
    }

    public boolean doesIntersect(Line test) {
        return doesIntersect(this, test);
    }

    public boolean doesIntersect(Ray test) {
        return doesIntersect(this, test);
    }

    /**
     * Does a given ray intersect with an even or odd number of the
     * lines in the list?
     *
     * @param allLines all the lines to test.
     * @return true if there's an odd number of intersections, false if
     * there's an even number of intersections.
     */
    public boolean intersectsOdd(List<Line> allLines) {
        return intersectsOdd(this, allLines);
    }

    /**
     * Does a given ray intersect with an even or odd number of the
     * lines in the list?
     *
     * @param allLines all the lines to test.
     * @return false if there's an odd number of intersections, true if
     * there's an even number of intersections.
     */
    public boolean intersectsEven(List<Line> allLines) {
        return intersectsEven(this, allLines);
    }
}
