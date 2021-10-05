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
 * A rectangle! It has four vertices and four lines. There's not much
 * more to say, to be honest, but they're pretty cool.
 *
 * <p>
 * Rectangles can be rotated! Check out the following methods:
 * <ul>
 *     <li>{@link #newRotatedRectangle(double, double, double, double, Angle)}</li>
 *     <li>{@link #rotate(Angle)}</li>
 *     <li>{@link #rotate(Angle, PointXY)}</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Rectangle implements Shape<Rectangle> {
    private final PointXY a;
    private final PointXY b;
    private final PointXY c;
    private final PointXY d;

    private final PointXY center;

    private final Line ab;
    private final Line bc;
    private final Line cd;
    private final Line da;
    private final Line ac;
    private final Line bd;

    private final double sizeX;
    private final double sizeY;

    /**
     * Create a new rectangle based on four points.
     *
     * <p>
     * It's suggested that you use the other constructor,
     * {@link Rectangle#Rectangle(double, double, double, double)}, instead
     * of this constructor, unless you have a specific reason to use
     * this constructor instead.
     * </p>
     *
     * <p>
     * Rectangles must be constructed as follows. As a visual example:
     * <code>
     * <pre>
     *   B                   C
     *   #####################
     *   #                   #
     *   #                   #
     *   #                   #
     *   #####################
     *   A                   D
     * </pre>
     * </code>
     * As a text-based example, the following points should be adjacent.
     * <ul>
     *     <li>A should be adjacent to B and D.</li>
     *     <li>B should be adjacent to A and C.</li>
     *     <li>C should be adjacent to B and D.</li>
     *     <li>D should be adjacent to C and A.</li>
     * </ul>
     * </p>
     *
     * <p>
     * The following lines MUST be parallel - if they're not, you'll get
     * an {@link IllegalArgumentException}.
     * <ul>
     *     <li>AB should be parallel to CD.</li>
     *     <li>BC should be parallel to AD.</li>
     * </ul>
     * </p>
     *
     * @param a one of the rectangle's four points.
     *          Should be adjacent to B and D.
     * @param b one of the rectangle's four points.
     *          Should be adjacent to A and C.
     * @param c one of the rectangle's four points.
     *          Should be adjacent to B and D.
     * @param d one of the rectangle's four points.
     *          Should be adjacent to C and A.
     */
    public Rectangle(PointXY a,
                     PointXY b,
                     PointXY c,
                     PointXY d) {
        PointXY.checkArgument(a);
        PointXY.checkArgument(b);
        PointXY.checkArgument(c);
        PointXY.checkArgument(d);

        if (PointXY.areDuplicatesPresent(a, b, c, d)) {
            throw new IllegalArgumentException(
                    "Cannot create a rectangle with duplicate points! " +
                            "Make sure your rectangle has > 0 width and > 0 " +
                            "height."
            );
        }

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        this.ab = new Line(a, b);
        this.bc = new Line(b, c);
        this.cd = new Line(c, d);
        this.da = new Line(d, a);
        this.ac = new Line(c, a);
        this.bd = new Line(b, d);

        if (!(ab.isParallelWith(cd) && bc.isParallelWith(da))) {
            throw new IllegalArgumentException(
                    "Invalid points! Please make sure that 1. AB and CD are " +
                            "parallel, and 2. BC and DA are parallel."
            );
        }

        this.center = Line.getIntersection(ac, bd);

        double minX = PointXY.minimumX(a, b, c, d);
        double minY = PointXY.minimumY(a, b, c, d);
        double maxX = PointXY.maximumX(a, b, c, d);
        double maxY = PointXY.maximumY(a, b, c, d);

        this.sizeX = maxX - minX;
        this.sizeY = maxY - minY;
    }

    /**
     * Create a new rectangle by giving minimum and maximum values.
     *
     * @param minX the minimum X value.
     * @param minY the minimum Y value.
     * @param maxX the maximum X value.
     * @param maxY the maximum Y value.
     */
    public Rectangle(double minX,
                     double minY,
                     double maxX,
                     double maxY) {
        this(
                new PointXY(minX, minY),
                new PointXY(minX, maxY),
                new PointXY(maxX, maxY),
                new PointXY(maxX, minY)
        );
    }

    public static Rectangle newRotatedRectangle(double minX,
                                                double minY,
                                                double maxX,
                                                double maxY,
                                                Angle rotationAngle) {
        Angle.checkArgument(rotationAngle);

        return new Rectangle(
                minX,
                minY,
                maxX,
                maxY
        ).rotate(rotationAngle);
    }

    private static boolean testIsCollinear(PointXY test,
                                           PointXY start,
                                           PointXY end) {
        return test.isCollinearWith(start, end);
    }

    private static boolean validate(PointXY test,
                                    PointXY start,
                                    PointXY end) {
        PointXY.checkArgument(test);
        PointXY.checkArgument(start);
        PointXY.checkArgument(end);

        double minX = PointXY.minimumX(start, end);
        double minY = PointXY.minimumY(start, end);
        double maxX = PointXY.maximumX(start, end);
        double maxY = PointXY.maximumY(start, end);

        boolean validX = BoundingBox.validate(test.x(), minX, maxX);
        boolean validY = BoundingBox.validate(test.y(), minY, maxY);

        return validX && validY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXY getClosestPoint(PointXY reference) {
        return PointXY.getClosestPoint(
                reference,
                ab.getClosestPoint(reference),
                bc.getClosestPoint(reference),
                cd.getClosestPoint(reference),
                da.getClosestPoint(reference)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPointInShape(PointXY reference) {
        PointXY.checkArgument(reference);

        if (PointXY.isNear(reference, 0.01, a, b, c, d))
            return true;

        if (
                ab.shouldReturnFalse(reference) ||
                        bc.shouldReturnFalse(reference) ||
                        cd.shouldReturnFalse(reference) ||
                        da.shouldReturnFalse(reference) ||
                        ac.shouldReturnFalse(reference) ||
                        bd.shouldReturnFalse(reference)
        ) return false;

        PointXY target = center;

        if (PointXY.isNear(reference, center, 0.01))
            target = ab.midpoint();

        Line line = new Line(
                reference,
                reference.angleTo(target).add(Angle.fromDeg(17.31313204182)),
                sizeX + sizeY
        );

        if (
                ab.isPointOnLineSegment(reference) ||
                        bc.isPointOnLineSegment(reference) ||
                        cd.isPointOnLineSegment(reference) ||
                        da.isPointOnLineSegment(reference) ||
                        ac.isPointOnLineSegment(reference) ||
                        bc.isPointOnLineSegment(reference)
        ) return true;

        double x = reference.x();
        double y = reference.y();

        boolean sameTargetX = Equals.soft(target.x(), x, 0.01);
        boolean sameTargetY = Equals.soft(target.y(), y, 0.01);

        if (sameTargetX) target.withX(x + 0.69);
        if (sameTargetY) target.withY(y + 0.69);

        double minX = getMinimumX();
        double minY = getMinimumY();
        double maxX = getMaximumX();
        double maxY = getMaximumY();

        if (
                x < minX ||
                        x > maxX ||
                        y < minY ||
                        y > maxY
        ) return false;

        boolean sameMinX = Equals.soft(minX, x, 0.01);
        boolean sameMinY = Equals.soft(minY, x, 0.01);
        boolean sameMaxX = Equals.soft(maxX, x, 0.01);
        boolean sameMaxY = Equals.soft(maxY, x, 0.01);

        if (sameMinX || sameMaxX) return minY <= y && y <= maxY;
        if (sameMinY || sameMaxY) return minX <= x && x <= maxX;

        return Shape.doesIntersectOdd(line, ab, bc, cd, da);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesCollideWith(Shape<?> shape) {
        return shape.getClosestPoint(center).isInside(shape);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXY getCenter() {
        return center;
    }

    /**
     * Rotate the rectangle around the rectangle's center.
     *
     * @param rotation how much to rotate the rectangle by.
     * @return a rotated rectangle.
     */
    @Override
    public Rectangle rotate(Angle rotation) {
        return rotate(rotation, center);
    }

    /**
     * Rotate the rectangle around the rectangle's center.
     *
     * @param rotation         how much to rotate the rectangle by.
     * @param centerOfRotation the center of rotation.
     * @return a rotated rectangle.
     */
    @Override
    public Rectangle rotate(Angle rotation,
                            PointXY centerOfRotation) {
        PointXY rotatedA = a.rotate(centerOfRotation, rotation);
        PointXY rotatedB = b.rotate(centerOfRotation, rotation);
        PointXY rotatedC = c.rotate(centerOfRotation, rotation);
        PointXY rotatedD = d.rotate(centerOfRotation, rotation);

        return new Rectangle(
                rotatedA,
                rotatedB,
                rotatedC,
                rotatedD
        );
    }

    /**
     * Shift the rectangle by an X and Y value.
     *
     * @param shiftX the X shift.
     * @param shiftY the Y shift.
     * @return a shifted rectangle.
     */
    public Rectangle shift(double shiftX,
                           double shiftY) {
        PointXY shift = new PointXY(shiftX, shiftY);

        PointXY adjustedA = a.add(shift);
        PointXY adjustedB = a.add(shift);
        PointXY adjustedC = a.add(shift);
        PointXY adjustedD = a.add(shift);

        return new Rectangle(
                adjustedA,
                adjustedB,
                adjustedC,
                adjustedD
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle moveTo(PointXY newCenter) {
        double dx = center.distanceX(newCenter);
        double dy = center.distanceY(newCenter);

        return shift(dx, dy);
    }

    private PointXY redrawPoint(PointXY point,
                                double scale) {
        Angle fromCenter = center.angleTo(point);
        double originalDistance = center.distance(point);

        return center.inDirection(
                scale * originalDistance,
                fromCenter
        );
    }

    private PointXY addDistance(PointXY point,
                                double distance) {
        Angle fromCenter = center.angleTo(point);
        double originalDistance = center.distance(point);

        return center.inDirection(
                originalDistance + distance,
                fromCenter
        );
    }

    @Override
    public Rectangle scale(double scale) {
        PointXY newA = redrawPoint(a, scale);
        PointXY newB = redrawPoint(b, scale);
        PointXY newC = redrawPoint(c, scale);
        PointXY newD = redrawPoint(d, scale);

        return new Rectangle(newA, newB, newC, newD);
    }

    @Override
    public Rectangle growBy(double growth) {
        PointXY newA = addDistance(a, growth);
        PointXY newB = addDistance(b, growth);
        PointXY newC = addDistance(c, growth);
        PointXY newD = addDistance(d, growth);

        return new Rectangle(newA, newB, newC, newD);
    }

    /**
     * Get the minimum X value in the rectangle.
     *
     * @return the minimum X value.
     */
    public double getMinimumX() {
        return PointXY.minimumX(a, b, c, d);
    }

    /**
     * Get the minimum Y value in the rectangle.
     *
     * @return the minimum Y value.
     */
    public double getMinimumY() {
        return PointXY.minimumY(a, b, c, d);
    }

    /**
     * Get the maximum X value in the rectangle.
     *
     * @return the maximum X value.
     */
    public double getMaximumX() {
        return PointXY.maximumX(a, b, c, d);
    }

    /**
     * Get the maximum Y value in the rectangle.
     *
     * @return the maximum Y value.
     */
    public double getMaximumY() {
        return PointXY.maximumY(a, b, c, d);
    }

    /**
     * Get the rectangle's X size.
     *
     * @return the rectangle's X size.
     */
    public double getSizeX() {
        return sizeX;
    }

    /**
     * Get the rectangle's Y size.
     *
     * @return the rectangle's Y size.
     */
    public double getSizeY() {
        return sizeY;
    }
}
