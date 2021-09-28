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

public class Line implements Serializable {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    private final PointXY start;
    private final PointXY end;

    private final LinearEquation equation;

    public Line(PointXY start,
                PointXY end) {
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

    public static PointXY getUnboundedIntersection(Line a,
                                                   Line b) {
        return a.getEquation().getIntersection(b.getEquation());
    }

    public static boolean areLinesParallel(Line a,
                                           Line b) {
        return Equals.soft(a.slope(), b.slope(), 0.001) &&
                getUnboundedIntersection(a, b) == null;
    }

    public static boolean doLinesIntersect(Line a,
                                           Line b) {
        PointXY unboundedIntersection = getUnboundedIntersection(a, b);

        boolean onLineA = a.isPointOnLineSegment(unboundedIntersection);
        boolean onLineB = b.isPointOnLineSegment(unboundedIntersection);

        return onLineA && onLineB;
    }

    public static PointXY getIntersection(Line a,
                                          Line b) {
        PointXY unboundedIntersection = getUnboundedIntersection(a, b);

        boolean validA = BoundingBox.isPointInLine(unboundedIntersection, a);
        boolean validB = BoundingBox.isPointInLine(unboundedIntersection, b);

        return validA && validB ? unboundedIntersection : null;
    }

    public static boolean isPointOnLine(Line line,
                                        PointXY point) {
        if (point == null) return false;

        return point.isCollinearWith(line.start, line.end);
    }

    public static boolean isPointOnLineSegment(Line line,
                                               PointXY point) {
        if (point == null) return false;

        return isPointOnLine(line, point) &&
                BoundingBox.isPointInLine(point, line);
    }

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

    public static PointXY getClosestEndpoint(PointXY reference,
                                             Line line) {
        double distanceToStart = reference.distance(line.start);
        double distanceToEnd = reference.distance(line.end);

        return distanceToStart < distanceToEnd
                ? line.start
                : line.end;
    }

    public static PointXY getFurthestPoint(PointXY reference,
                                           Line line) {
        double distanceToStart = reference.distance(line.start);
        double distanceToEnd = reference.distance(line.end);

        return distanceToStart > distanceToEnd
                ? line.start
                : line.end;
    }

    public static PointXY midpoint(Line line) {
        return line.start.midpoint(line.end);
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public PointXY getStart() {
        return start;
    }

    public PointXY getEnd() {
        return end;
    }

    public LinearEquation getEquation() {
        return equation;
    }

    public double deltaX() {
        return maxX - minX;
    }

    public double deltaY() {
        return maxY - minY;
    }

    public double slope() {
        return deltaY() / deltaX();
    }

    public Angle angleSlope() {
        return Angle.atan2(deltaY(), deltaX());
    }

    public double perpendicularSlope() {
        return slope() / -1;
    }

    public Angle anglePerpendicularSlope() {
        return angleSlope().fixedRotate180Deg();
    }

    public PointXY getUnboundedIntersectionWith(Line line) {
        return getUnboundedIntersection(this, line);
    }

    public PointXY getIntersectionWith(Line line) {
        return getIntersection(this, line);
    }

    public boolean doesIntersectWith(Line line) {
        return doLinesIntersect(this, line);
    }

    public boolean isParallelWith(Line line) {
        return areLinesParallel(this, line);
    }

    public PointXY midpoint() {
        return midpoint(this);
    }

    public PointXY getClosestPoint(PointXY reference) {
        return getClosestPoint(reference, this);
    }

    public PointXY getClosestEndpoint(PointXY reference) {
        return getClosestEndpoint(reference, this);
    }

    public PointXY getFurthestPoint(PointXY reference) {
        return getFurthestPoint(reference, this);
    }

    public boolean isPointOnLine(PointXY reference) {
        return isPointOnLine(this, reference);
    }

    public boolean isPointOnLineSegment(PointXY reference) {
        return isPointOnLineSegment(this, reference);
    }
}
