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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLine {
    private static final PointXY A = new PointXY(0, 0);
    private static final PointXY B = new PointXY(10, 10);
    private static final PointXY C = new PointXY(0, 10);
    private static final PointXY D = new PointXY(10, 0);
    private static final PointXY A1 = new PointXY(-100, -100);
    private static final PointXY B1 = new PointXY(-90, -80);
    private static final PointXY C1 = new PointXY(0, 0);
    private static final PointXY D1 = new PointXY(5, 5);

    private static final Line AB = new Line(A, B);
    private static final Line CD = new Line(C, D);
    private static final Line A1B1 = new Line(A1, B1);
    private static final Line C1D1 = new Line(C1, D1);

    @Test
    public void testBoundedIntersection() {
        PointXY point = Line.pointOfIntersection(AB, CD);

        Assertions.assertNotNull(point);

        Assertions.assertEquals(5d, point.x());
        Assertions.assertEquals(5d, point.y());
    }

    @Test
    public void testUnboundedIntersection() {
        PointXY point = Line.unboundedPointOfIntersection(A1B1, C1D1);

        Assertions.assertNotNull(point);

        Assertions.assertEquals(-100d, point.x());
        Assertions.assertEquals(-100d, point.y());
    }

    @Test
    public void testOddUnboundedIntersection() {
        Line line1 = new Line(
                new PointXY(0, 0),
                new PointXY(17, 19)
        );

        Line line2 = new Line(
                new PointXY(3, -9),
                new PointXY(171, 193)
        );

        PointXY point = Line.unboundedPointOfIntersection(line1, line2);

        Assertions.assertNotNull(point);

        Assertions.assertTrue(Equals.soft(148.7, point.x(), 0.1));
        Assertions.assertTrue(Equals.soft(166.2, point.y(), 0.1));
    }

    @Test
    public void testClosestPoint() {
        PointXY a = new PointXY(-10, 10);
        PointXY b = new PointXY(10, 10);
        Line line = new Line(a, b);

        PointXY predicted = new PointXY(0, 10);
        PointXY actual = line.getClosestPoint(PointXY.ZERO);

        Assertions.assertNotNull(predicted);
        Assertions.assertNotNull(actual);

        Assertions.assertTrue(
                Equals.soft(
                        predicted.x(),
                        actual.x(),
                        0.01
                )
        );

        Assertions.assertTrue(
                Equals.soft(
                        predicted.y(),
                        actual.y(),
                        0.01
                )
        );
    }

    @Test
    public void testFurthestPoint() {
        PointXY a = new PointXY(-10, 10);
        PointXY b = new PointXY(10, 10);
        Line line = new Line(a, b);

        double distanceA = PointXY.ZERO.absDistance(a);
        double distanceB = PointXY.ZERO.absDistance(b);

        Assertions.assertEquals(distanceA, distanceB);

        PointXY furthest = line.getFurthestPoint(PointXY.ZERO);

        Assertions.assertTrue(
                (furthest.x() == a.x() && furthest.y() == a.y()) ||
                        (furthest.x() == b.x() && furthest.y() == b.y())
        );
    }
}
