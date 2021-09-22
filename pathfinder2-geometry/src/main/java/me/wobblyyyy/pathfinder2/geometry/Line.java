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
 * A line is a two-dimensional entity that exists between two {@code PointXY}s.
 * There's some pretty neat things you can do with lines if that's your cup
 * of tea, but there's a fundamental part of geometry beyond single points.
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

    public PointXY getClosestPoint(PointXY referencePoint) {
        double distance1 = referencePoint.distance(startPoint);
        double distance2 = referencePoint.distance(endPoint);
        return distance1 > distance2 ? endPoint : startPoint;
    }

    public PointXY getFurthestPoint(PointXY referencePoint) {
        double distance1 = referencePoint.distance(startPoint);
        double distance2 = referencePoint.distance(endPoint);
        return distance1 < distance2 ? endPoint : startPoint;
    }
}
