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
    private final List<Line> lines;

    private final double sizeX;
    private final double sizeY;

    private Rectangle(PointXY a,
                      PointXY b,
                      PointXY c,
                      PointXY d) {
        assert a != null;
        assert b != null;
        assert c != null;
        assert d != null;

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        this.ab = new Line(a, b);
        this.bc = new Line(b, c);
        this.cd = new Line(c, d);
        this.da = new Line(d, a);

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
        assert test != null;
        assert start != null;
        assert end != null;

        return test.isCollinearWith(start, end);
    }

    private static boolean validate(PointXY test,
                                    PointXY start,
                                    PointXY end) {
        assert test != null;
        assert start != null;
        assert end != null;

        double minX = PointXY.minimumX(start, end);
        double minY = PointXY.minimumY(start, end);
        double maxX = PointXY.maximumX(start, end);
        double maxY = PointXY.maximumY(start, end);

        boolean validX = BoundingBox.validate(test.x(), minX, maxX);
        boolean validY = BoundingBox.validate(test.y(), minY, maxY);

        return validX && validY;
    }

    @Override
    public PointXY getClosestPoint(PointXY reference) {
        assert reference != null;

        return PointXY.getClosestPoint(
                reference,
                ab.getClosestPoint(reference),
                bc.getClosestPoint(reference),
                cd.getClosestPoint(reference),
                da.getClosestPoint(reference)
        );
    }

    @Override
    public boolean isPointInShape(PointXY reference) {
        assert reference != null;

        if (PointXY.isNear(reference, 0.01, a, b, c, d))
            return true;

        if (testIsCollinear(reference, a, b)) {
            return validate(reference, a, b);
        } else if (testIsCollinear(reference, b, c)) {
            return validate(reference, b, c);
        } else if (testIsCollinear(reference, c, d)) {
            return validate(reference, c, d);
        } else if (testIsCollinear(reference, d, a)) {
            return validate(reference, d, a);
        } else if (testIsCollinear(reference, a, c)) {
            return validate(reference, a, c);
        } else if (testIsCollinear(reference, b, d)) {
            return validate(reference, b, d);
        }

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

    @Override
    public boolean doesCollideWith(Shape shape) {
        return shape.getClosestPoint(center).isInside(shape);
    }

    @Override
    public PointXY getCenter() {
        return center;
    }

    public Rectangle rotate(Angle rotation) {
        assert rotation != null;

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
}
