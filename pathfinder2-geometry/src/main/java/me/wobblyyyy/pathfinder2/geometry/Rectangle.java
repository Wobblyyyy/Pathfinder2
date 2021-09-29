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

public class Rectangle implements Shape {
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
    private final List<Line> lines;

    private final double sizeX;
    private final double sizeY;

    private Rectangle(PointXY a,
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

        this.lines = new ArrayList<>() {{
            add(ab);
            add(bc);
            add(cd);
            add(da);
        }};

        this.center = PointXY.avg(a, b, c, d);

        double minX = PointXY.minimumX(a, b, c, d);
        double minY = PointXY.minimumY(a, b, c, d);
        double maxX = PointXY.maximumX(a, b, c, d);
        double maxY = PointXY.maximumY(a, b, c, d);

        this.sizeX = maxX - minX;
        this.sizeY = maxY - minY;
    }

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
                target.inDirection(
                        sizeX + sizeY,
                        reference.angleTo(target)
                )
        );

        return Shape.doesIntersectOdd(line, lines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesCollideWith(Shape shape) {
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
    public Rectangle rotate(Angle rotation) {
        PointXY rotatedA = a.rotate(center, rotation);
        PointXY rotatedB = b.rotate(center, rotation);
        PointXY rotatedC = c.rotate(center, rotation);
        PointXY rotatedD = d.rotate(center, rotation);

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
}
