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
 * A line is a two-dimensional entity that exists between two {@code PointXY}s.
 * There's some pretty neat things you can do with lines if that's your cup
 * of tea, but there's a fundamental part of geometry beyond single points.
 *
 * <p>
 * This is not actually a LINE, but rather, a LINE SEGMENT.
 * </p>
 *
 * @author Colin Robertson
 * @see #isPointOnLine(PointXY)
 * @since 0.1.0
 */
public class Line {
    private final PointXY startPoint;
    private final PointXY midPoint;
    private final PointXY endPoint;

    /**
     * Create a new {@code Line} using two points.
     *
     * @param startPoint the line's start point. (initial point)
     * @param endPoint   the line's end point. (final point)
     */
    public Line(PointXY startPoint,
                PointXY endPoint) {
        if (startPoint == null)
            throw new IllegalArgumentException("Start point cannot be null!");
        if (endPoint == null)
            throw new IllegalArgumentException("End point cannot be null!");
        if (startPoint.x() == endPoint.x() && startPoint.y() == endPoint.y())
            throw new IllegalArgumentException("Cannot create a line between two of the same point!");

        this.startPoint = startPoint;
        // calculate the midpoint only once, memory usage isn't really a problem
        this.midPoint = PointXY.avg(startPoint, endPoint);
        this.endPoint = endPoint;
    }

    public static Line avg(Line line1,
                           Line line2) {
        return new Line(
                PointXY.avg(line1.startPoint, line2.startPoint),
                PointXY.avg(line1.endPoint, line2.endPoint)
        );
    }

    public static Line add(Line line1,
                           Line line2) {
        return new Line(
                PointXY.add(line1.startPoint, line2.startPoint),
                PointXY.add(line1.endPoint, line2.endPoint)
        );
    }

    public static boolean higherThan(Line line1,
                                     Line line2) {
        double xs1 = line1.startPoint.x();
        double xs2 = line2.startPoint.x();
        double xe1 = line1.endPoint.x();
        double xe2 = line2.endPoint.x();
        double ys1 = line1.startPoint.y();
        double ys2 = line2.startPoint.y();
        double ye1 = line1.endPoint.y();
        double ye2 = line2.endPoint.y();

        boolean higherThanX = xs2 > xs1 && xe2 > xe1;
        boolean higherThanY = ys2 > ys1 && ye2 > ye1;

        return higherThanX && higherThanY;
    }

    public static boolean isBetweenLines(PointXY point,
                                         Line line1,
                                         Line line2) {
        double x = point.x();
        double y = point.y();

        boolean isLine1HigherThanLine2 = Line.higherThan(line1, line2);
        Line highLine = isLine1HigherThanLine2 ? line1 : line2;
        Line lowLine = isLine1HigherThanLine2 ? line2 : line1;

        boolean isPointLowerThanHighLine = highLine.isPointBelow(point);
        boolean isPointHigherThanLowLine = lowLine.isPointAbove(point);

        return isPointLowerThanHighLine && isPointHigherThanLowLine;
    }

    public static boolean isNotBetweenLines(PointXY point,
                                            Line line1,
                                            Line line2) {
        return !isBetweenLines(point, line1, line2);
    }

    public static double perpendicularTo(double slope) {
        return -(slope * -1);
    }

    public static Orientation getOrientation(PointXY p,
                                             PointXY q,
                                             PointXY r) {
        double v = (q.y() - p.y()) * (r.x() - q.x()) -
                (q.x() - p.x()) * (r.y() - q.y());

        if (Equals.soft(v, 0.0, 0.1)) return Orientation.COLLINEAR;
        else if (v > 0) return Orientation.CLOCKWISE;
        else return Orientation.COUNTERCLOCKWISE;
    }

    public static boolean doLinesIntersect(PointXY firstLineStartPoint,
                                           PointXY firstLineEndPoint,
                                           PointXY secondLineStartPoint,
                                           PointXY secondLineEndPoint) {
        Orientation o1 = getOrientation(
                firstLineStartPoint,
                firstLineEndPoint,
                secondLineStartPoint
        );
        Orientation o2 = getOrientation(
                firstLineStartPoint,
                firstLineEndPoint,
                secondLineEndPoint
        );
        Orientation o3 = getOrientation(
                secondLineStartPoint,
                secondLineEndPoint,
                firstLineStartPoint
        );
        Orientation o4 = getOrientation(
                secondLineStartPoint,
                secondLineEndPoint,
                firstLineEndPoint
        );

        boolean collinear1 = o1 == Orientation.COLLINEAR && PointXY.areCollinear(
                firstLineStartPoint,
                firstLineEndPoint,
                secondLineStartPoint
        );
        boolean collinear2 = o2 == Orientation.COLLINEAR && PointXY.areCollinear(
                firstLineStartPoint,
                secondLineEndPoint,
                firstLineEndPoint
        );
        boolean collinear3 = o3 == Orientation.COLLINEAR && PointXY.areCollinear(
                secondLineStartPoint,
                firstLineStartPoint,
                secondLineEndPoint
        );
        boolean collinear4 = o4 == Orientation.COLLINEAR && PointXY.areCollinear(
                secondLineStartPoint,
                firstLineStartPoint,
                secondLineEndPoint
        );

        return (o1 != o2 && o3 != o4) ||
                collinear1 ||
                collinear2 ||
                collinear3 ||
                collinear4;
    }

    public static boolean doLinesIntersect(Line line1,
                                           Line line2) {
        return doLinesIntersect(
                line1.getStartPoint(),
                line1.getEndPoint(),
                line2.getStartPoint(),
                line2.getEndPoint()
        );
    }

    public static Angle angleBetweenLines(Line line1,
                                          Line line2) {
        double rad1 = Math.atan2(
                line1.deltaY() * -1,
                line1.deltaX() * -1
        );

        double rad2 = Math.atan2(
                line2.deltaY() * -1,
                line2.deltaX() * -1
        );

        return Angle.fixedRad(Math.abs(rad1) - Math.abs(rad2));
    }

    public static PointXY unboundedPointOfIntersection(Line line1,
                                                       Line line2) {
        return EquationForm.unboundedIntersection(
                EquationForm.lineToEquation(line1),
                EquationForm.lineToEquation(line2)
        );
    }

    /**
     * Get the point of intersection between two lines. If the lines are
     * parallel, this will return a point with {@link Double#MAX_VALUE} as
     * both the X and Y positions. If the lines do not intersect, this method
     * will return null. If the lines do intersect, this method will
     * return the point of intersection between the two lines.
     *
     * @param line1 the first of the two lines.
     * @param line2 the second of the two lines.
     * @return the point of intersection between the two lines. If the lines
     * do not intersect, this will be null.
     * @see #doLinesIntersect(Line, Line)
     * @see #doLinesIntersect(PointXY, PointXY, PointXY, PointXY)
     */
    public static PointXY pointOfIntersection(Line line1,
                                              Line line2) {
        PointXY point = unboundedPointOfIntersection(line1, line2);

        boolean existsOnLine1 = line1.isPointOnSegment(point);
        boolean existsOnLine2 = line2.isPointOnSegment(point);

        if (existsOnLine1 || existsOnLine2) return point;
        else return null;
    }

    /**
     * Get the line's start point.
     *
     * @return the line's start point.
     */
    public PointXY getStartPoint() {
        return startPoint;
    }

    /**
     * Get the line's mid-point.
     *
     * @return the line's mid-point.
     */
    public PointXY getMidPoint() {
        return midPoint;
    }

    /**
     * Get the line's end point.
     *
     * @return the line's end point.
     */
    public PointXY getEndPoint() {
        return endPoint;
    }

    /**
     * Is a given point on a line?
     *
     * <p>
     * To determine whether the point lies on the line, we use the
     * {@link PointXY#areCollinear(PointXY, PointXY, PointXY)} method to
     * check. Very fancy, very cool, very epic. I know.
     * </p>
     *
     * @param point the point to test.
     * @return true if the point is on the line, false if the point is
     * not on the line.
     */
    public boolean isPointOnLine(PointXY point) {
        return PointXY.areCollinear(
                startPoint,
                point,
                endPoint
        );
    }

    /**
     * Is a point ON the specific line segment?
     *
     * @param point the point to test.
     * @return true if the point is on the line segment, otherwise, false.
     */
    public boolean isPointOnSegment(PointXY point) {
        return BoundingBox.isInBox(
                point,
                new PointXY(minimumX(), minimumY()),
                new PointXY(maximumX(), maximumY())
        );
    }

    /**
     * Get the change in X (end point X - start point X).
     *
     * @return the change in X.
     */
    public double deltaX() {
        return endPoint.x() - startPoint.x();
    }

    /**
     * Get the change in Y (end point Y - start point Y).
     *
     * @return the change in Y.
     */
    public double deltaY() {
        return endPoint.y() - startPoint.y();
    }

    /**
     * Get the slope of the line. This is calculated by dividing
     * {@link #deltaY()} by {@link #deltaX()}.
     *
     * @return the slope of the line.
     */
    public double slope() {
        return deltaY() / deltaX();
    }

    public double perpendicularSlope() {
        return perpendicularTo(slope());
    }

    /**
     * Get the slope of the line, as an angle.
     *
     * @return the slope of the line, as an angle.
     */
    public Angle angleSlope() {
        return Angle.atan2(
                deltaY(),
                deltaX()
        );
    }

    /**
     * Get a slope perpendicular to this line's slope.
     *
     * @return a slope, perpendicular to this line's slope.
     */
    public Angle perpendicularAngleSlope() {
        return angleSlope().fixedRotate90Deg();
    }

    /**
     * Get the length of the line as determined by the distance formula.
     *
     * @return the length of the line.
     */
    public double length() {
        return PointXY.distance(startPoint, endPoint);
    }

    /**
     * Is a given point above or below the line? A point is "above" the line
     * if, were it drawn on a piece of paper, would appear "above" the line.
     * Likewise, a point below the line would appear to be literally "below"
     * the line.
     *
     * @param point the point to test.
     * @return true if the point is above the line, false if it's not above
     * the line.
     */
    public boolean isPointAbove(PointXY point) {
        double x = point.x();
        double y = point.y();
        double slope = slope();

        return (slope * x) > y;
    }

    /**
     * Is a given point above or below the line? A point is "above" the line
     * if, were it drawn on a piece of paper, would appear "above" the line.
     * Likewise, a point below the line would appear to be literally "below"
     * the line.
     *
     * @param point the point to test.
     * @return false if the point is above the line, true if it's not above
     * the line.
     */
    public boolean isPointBelow(PointXY point) {
        return !isPointAbove(point);
    }

    /**
     * Get the distance from the reference point to the line's
     * start point.
     *
     * @param referencePoint the point to use.
     * @return the distance from {@code referencePoint} to this line's
     * start point.
     */
    public double distanceToStart(PointXY referencePoint) {
        return referencePoint.distance(startPoint);
    }

    /**
     * Get the distance from the reference point to the line's
     * end point.
     *
     * @param referencePoint the point to use.
     * @return the distance from {@code referencePoint} to this line's
     * end point.
     */
    public double distanceToEnd(PointXY referencePoint) {
        return referencePoint.distance(endPoint);
    }

    /**
     * Get the point on the line that's closest to the provided reference
     * point.
     *
     * @param referencePoint the point to use.
     * @return the line that's closest to the reference point.
     */
    public PointXY getClosestPoint(PointXY referencePoint) {
        if (referencePoint.isCollinearWith(startPoint, endPoint)) {
            return closestEndPoint(referencePoint);
        }

        double toStart = Math.abs(distanceToStart(referencePoint));
        double toEnd = Math.abs(distanceToEnd(referencePoint));
        double sum = toStart + toEnd; // sum of two legs is always longer than the third side

        Line ray = new Line(
                referencePoint,
                referencePoint.inDirection(
                        sum,
                        perpendicularAngleSlope()
                )
        );

        if (Line.doLinesIntersect(this, ray)) return Line.pointOfIntersection(this, ray);
        else return closestEndPoint(referencePoint);
    }

    /**
     * Get the point on the line the furthest away from the reference point.
     *
     * @param referencePoint the point to use for reference.
     * @return the furthest point on the line.
     */
    public PointXY getFurthestPoint(PointXY referencePoint) {
        return furthestEndPoint(referencePoint);
    }

    /**
     * Get the closest end point.
     *
     * @param referencePoint the point to use for reference.
     * @return either start point or end point depending on which one is
     * closer.
     */
    public PointXY closestEndPoint(PointXY referencePoint) {
        double toStart = distanceToStart(referencePoint);
        double toEnd = distanceToEnd(referencePoint);

        return toStart < toEnd ? startPoint : endPoint;
    }

    /**
     * Get the furthest end point.
     *
     * @param referencePoint the point to use for reference.
     * @return either start point or end point depending on which one is
     * further.
     */
    public PointXY furthestEndPoint(PointXY referencePoint) {
        double toStart = distanceToStart(referencePoint);
        double toEnd = distanceToEnd(referencePoint);

        return toStart > toEnd ? startPoint : endPoint;
    }

    /**
     * Get the line's min X value.
     *
     * @return the line's min X value.
     */
    public double minimumX() {
        return Math.min(startPoint.x(), endPoint.x());
    }

    /**
     * Get the line's min Y value.
     *
     * @return the line's min Y value.
     */
    public double minimumY() {
        return Math.min(startPoint.y(), endPoint.y());
    }

    /**
     * Get the line's max Y value.
     *
     * @return the line's max Y value.
     */
    public double maximumX() {
        return Math.max(startPoint.x(), endPoint.x());
    }

    /**
     * Get the line's max Y value.
     *
     * @return the line's max Y value.
     */
    public double maximumY() {
        return Math.max(startPoint.y(), endPoint.y());
    }

    /**
     * Does this line intersect with another line?
     *
     * @param line the line to test.
     * @return true if the lines intersect, otherwise, false.
     */
    public boolean doesIntersectWith(Line line) {
        return doLinesIntersect(this, line);
    }

    /**
     * Get the point of intersection for this line and another line.
     *
     * @param line the line to test.
     * @return the point of intersection for the two lines. If there is no
     * valid point of intersection, this will return null.
     */
    public PointXY getIntersectionPoint(Line line) {
        return pointOfIntersection(this, line);
    }

    public enum Orientation {
        COLLINEAR,
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
}
