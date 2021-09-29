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

import java.io.Serializable;

/**
 * A line SEGMENT - this is not a line, this is a line segment. If you want
 * an infinite line, see {@link LinearEquation}. This is the basis for
 * polygonal geometry.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Line implements Serializable {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    private final PointXY start;
    private final PointXY end;

    private final LinearEquation equation;

    /**
     * Create a new line.
     *
     * @param start the line's start point.
     * @param end   the line's end point.
     */
    public Line(PointXY start,
                PointXY end) {
        PointXY.checkArgument(start);
        PointXY.checkArgument(end);

        if (PointXY.areDuplicatesPresent(start, end)) {
            throw new IllegalArgumentException(
                    "Cannot create a line between the same point! " +
                            "Make sure the start and end points are both " +
                            "different from each other."
            );
        }

        minX = PointXY.minimumX(start, end);
        minY = PointXY.minimumY(start, end);
        maxX = PointXY.maximumX(start, end);
        maxY = PointXY.maximumY(start, end);

        this.start = start;
        this.end = end;

        if (Equals.soft(minX, maxX, 0.01)) {
            // line is vertical

            equation = SlopeIntercept.newVertical(minX);
        } else {
            // line is not vertical

            double slope = start.angleTo(end).tan();
            equation = new PointSlope(start, slope);
        }
    }

    /**
     * Get the unbounded point of intersection between two lines. This ignores
     * the line's "boundaries" - it doesn't matter if the point is actually
     * on the line segment, it just matters that it's on the line's linear
     * equation.
     *
     * @param a one of the two lines.
     * @param b one of the two lines.
     * @return the unbounded point of intersection between the two lines.
     * If the lines do not intersect, return null.
     */
    public static PointXY getUnboundedIntersection(Line a,
                                                   Line b) {
        return a.getEquation().getIntersection(b.getEquation());
    }

    /**
     * Are two lines parallel? (Do they have the same slope?)
     *
     * @param a one of the two lines.
     * @param b one of the two lines.
     * @return if the lines are parallel, true. Otherwise, false.
     */
    public static boolean areLinesParallel(Line a,
                                           Line b) {
        return Equals.soft(a.slope(), b.slope(), 0.001) &&
                getUnboundedIntersection(a, b) == null;
    }

    /**
     * Do two lines intersect ON the line segment?
     *
     * @param a one of the two lines.
     * @param b one of the two lines.
     * @return if the lines intersect at a point on both of the segments,
     * return true. Otherwise, return false.
     */
    public static boolean doLinesIntersect(Line a,
                                           Line b) {
        PointXY unboundedIntersection = getUnboundedIntersection(a, b);

        boolean onLineA = a.isPointOnLineSegment(unboundedIntersection);
        boolean onLineB = b.isPointOnLineSegment(unboundedIntersection);

        return onLineA && onLineB;
    }

    /**
     * Get the point of intersection between two lines. If the lines intersect
     * at a point not on both of the lines, this method will return null.
     *
     * @param a one of the two lines.
     * @param b one of the two lines.
     * @return get the point of intersection between two lines. If the lines
     * don't have any valid points of intersection, return null.
     */
    public static PointXY getIntersection(Line a,
                                          Line b) {
        PointXY unboundedIntersection = getUnboundedIntersection(a, b);

        boolean validA = BoundingBox.isPointInLine(unboundedIntersection, a);
        boolean validB = BoundingBox.isPointInLine(unboundedIntersection, b);

        return validA && validB ? unboundedIntersection : null;
    }

    /**
     * Is a point on a line? This checks to see if the given point is
     * collinear with the given line's start and end points. In other words,
     * it doesn't check to see if the point is on the line SEGMENT - just
     * the line.
     *
     * @param line  the line to test.
     * @param point the point to test.
     * @return if the point is collinear with both the start and end points,
     * true. Otherwise, false.
     */
    public static boolean isPointOnLine(Line line,
                                        PointXY point) {
        if (point == null) return false;

        return point.isCollinearWith(line.start, line.end);
    }

    /**
     * Is a point on a line segment?
     *
     * @param line  the line to test.
     * @param point the point to test.
     * @return if the point is on the line segment, true. Otherwise, false.
     */
    public static boolean isPointOnLineSegment(Line line,
                                               PointXY point) {
        if (point == null) return false;

        return isPointOnLine(line, point) &&
                BoundingBox.isPointInLine(point, line);
    }

    /**
     * Get the closest point on a line to the reference point.
     *
     * @param reference the reference point.
     * @param line      the line to use.
     * @return the closest point (to reference) on the line.
     */
    public static PointXY getClosestPoint(PointXY reference,
                                          Line line) {
        if (
                isPointOnLine(line, reference) ||
                        !BoundingBox.isPointInLine(reference, line)
        )
            return getClosestEndpoint(reference, line);

        LinearEquation perpendicular = new PointSlope(
                reference,
                line.perpendicularSlope()
        );

        return perpendicular.getIntersection(line.getEquation());
    }

    /**
     * Get the closest endpoint on the line.
     *
     * @param reference the reference point.
     * @param line      the line to get the closest endpoint of.
     * @return the closest endpoint on the line.
     */
    public static PointXY getClosestEndpoint(PointXY reference,
                                             Line line) {
        PointXY.checkArgument(reference);

        double distanceToStart = reference.distance(line.start);
        double distanceToEnd = reference.distance(line.end);

        return distanceToStart < distanceToEnd
                ? line.start
                : line.end;
    }

    /**
     * Get the furthest point on a line.
     *
     * @param reference the reference point.
     * @param line      the line to test.
     * @return the furthest point on the line.
     */
    public static PointXY getFurthestPoint(PointXY reference,
                                           Line line) {
        PointXY.checkArgument(reference);

        double distanceToStart = reference.distance(line.start);
        double distanceToEnd = reference.distance(line.end);

        return distanceToStart > distanceToEnd
                ? line.start
                : line.end;
    }

    /**
     * Get a line's midpoint.
     *
     * @param line the line.
     * @return the line's midpoint.
     */
    public static PointXY midpoint(Line line) {
        return line.start.midpoint(line.end);
    }

    /**
     * Get the line's minimum X value.
     *
     * @return the line's minimum X value.
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Get the line's minimum Y value.
     *
     * @return the line's minimum Y value.
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Get the line's maximum X value.
     *
     * @return the line's maximum X value.
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Get the line's maximum Y value.
     *
     * @return the line's maximum Y value.
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * Get the line's start point.
     *
     * @return the line's start point.
     */
    public PointXY getStart() {
        return start;
    }

    /**
     * Get the line's end point.
     *
     * @return the line's end point.
     */
    public PointXY getEnd() {
        return end;
    }

    /**
     * Get the line's equation.
     *
     * @return the line's equation.
     */
    public LinearEquation getEquation() {
        return equation;
    }

    /**
     * Get end X - start X.
     *
     * @return end X minus start X.
     */
    public double deltaX() {
        return maxX - minX;
    }

    /**
     * Get end Y - start Y.
     *
     * @return end Y minus start Y.
     */
    public double deltaY() {
        return maxY - minY;
    }

    /**
     * Get the slope of the line.
     *
     * @return the line's slope.
     */
    public double slope() {
        return deltaY() / deltaX();
    }

    /**
     * Get the line's slope as an angle.
     *
     * @return the line's slope as an angle.
     */
    public Angle angleSlope() {
        return Angle.atan2(deltaY(), deltaX());
    }

    /**
     * Get a slope perpendicular to this line's slope.
     *
     * @return a slope that's perpendicular to this line.
     */
    public double perpendicularSlope() {
        return slope() / -1;
    }

    /**
     * Get a slope perpendicular to this line's slope, as an angle.
     *
     * @return a slope that's perpendicular to this line, as an angle.
     */
    public Angle anglePerpendicularSlope() {
        return angleSlope().fixedRotate180Deg();
    }

    /**
     * Get the unbounded point of intersection between two lines.
     *
     * @param line the other line.
     * @return the unbounded point of intersection between the two lines.
     * @see #getUnboundedIntersection(Line, Line)
     */
    public PointXY getUnboundedIntersectionWith(Line line) {
        return getUnboundedIntersection(this, line);
    }

    /**
     * Get the bounded point of intersection between two lines.
     *
     * @param line the other line.
     * @return the bounded point of intersection between the two lines. If there
     * is no valid point of intersection, return null.
     */
    public PointXY getIntersectionWith(Line line) {
        return getIntersection(this, line);
    }

    /**
     * Do two line segments intersect?
     *
     * @param line the other line segment to test.
     * @return if the line segments intersect, true. Otherwise, false.
     */
    public boolean doesIntersectWith(Line line) {
        return doLinesIntersect(this, line);
    }

    /**
     * Are two lines parallel?
     *
     * @param line the other line.
     * @return true if the lines have the same slope, otherwise, false.
     */
    public boolean isParallelWith(Line line) {
        return areLinesParallel(this, line);
    }

    /**
     * Get the line's midpoint.
     *
     * @return the line's midpoint.
     */
    public PointXY midpoint() {
        return midpoint(this);
    }

    /**
     * Get the closest point (to a given reference point) on the line.
     *
     * @param reference the reference point.
     * @return the point on the line that's closest to the reference point.
     */
    public PointXY getClosestPoint(PointXY reference) {
        return getClosestPoint(reference, this);
    }

    /**
     * Get the closest endpoint (to a given reference point) on the line.
     *
     * @param reference the reference point.
     * @return the endpoint that's closest to the reference point.
     */
    public PointXY getClosestEndpoint(PointXY reference) {
        return getClosestEndpoint(reference, this);
    }

    /**
     * Get the point on the line that's the furthest away from the reference
     * point.
     *
     * @param reference the reference point.
     * @return the furthest point.
     */
    public PointXY getFurthestPoint(PointXY reference) {
        return getFurthestPoint(reference, this);
    }

    /**
     * Is a point collinear with this line's start and end points?
     *
     * @param reference the point to test.
     * @return if the point is on the line, true. Otherwise, false.
     * @see #isPointOnLineSegment(PointXY)
     */
    public boolean isPointOnLine(PointXY reference) {
        return isPointOnLine(this, reference);
    }

    /**
     * Is a given point on this line segment?
     *
     * @param reference the reference point to use.
     * @return true if the point is on the line segment, otherwise, false.
     */
    public boolean isPointOnLineSegment(PointXY reference) {
        return isPointOnLineSegment(this, reference);
    }

    /**
     * Get the length of the line.
     *
     * @return the line's length.
     */
    public double length() {
        return start.absDistance(end);
    }

    public boolean shouldReturnFalse(PointXY reference) {
        if (reference.isCollinearWith(start, end)) {
            double x = reference.x();
            double y = reference.y();

            boolean validX = minX <= x && x <= maxX;
            boolean validY = minY <= y && y <= maxY;

            return !validX && !validY;
        }

        return false;
    }
}
