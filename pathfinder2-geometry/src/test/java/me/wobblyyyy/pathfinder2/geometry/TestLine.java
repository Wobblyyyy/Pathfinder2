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
    private static final PointXY B = new PointXY(10, 0);
    private static final PointXY C = new PointXY(10, 10);
    private static final PointXY D = new PointXY(0, 10);

    private static final Line AB = new Line(A, B);
    private static final Line BC = new Line(B, C);
    private static final Line CD = new Line(C, D);
    private static final Line DA = new Line(D, A);

    @Test
    public void testNormalIntersection() {
        PointXY intersection1 = AB.getIntersectionWith(BC);
        PointXY intersection2 = BC.getIntersectionWith(AB);

        double x1 = intersection1.x();
        double y1 = intersection1.y();
        double x2 = intersection2.x();
        double y2 = intersection2.y();

        Assertions.assertEquals(10d, x1);
        Assertions.assertEquals(0d, y1);
        Assertions.assertEquals(10d, x2);
        Assertions.assertEquals(0d, y2);
    }

    @Test
    public void testMoreIntersections() {
        PointXY a = new PointXY(0, 0);
        PointXY b = new PointXY(10, 10);
        PointXY c = new PointXY(0, 10);
        PointXY d = new PointXY(10, 0);

        Line ab = new Line(a, b);
        Line cd = new Line(c, d);

        PointXY intersection1 = ab.getIntersectionWith(cd);
        PointXY intersection2 = cd.getIntersectionWith(ab);

        double x1 = intersection1.x();
        double y1 = intersection1.y();
        double x2 = intersection2.x();
        double y2 = intersection2.y();

        Assertions.assertTrue(Equals.soft(5d, x1, 0.01));
        Assertions.assertTrue(Equals.soft(5d, y1, 0.01));
        Assertions.assertTrue(Equals.soft(5d, x2, 0.01));
        Assertions.assertTrue(Equals.soft(5d, y2, 0.01));
    }

    @Test
    public void testVerticalNormalIntersection() {
        PointXY a = new PointXY(0, 0);
        PointXY b = new PointXY(0, 10);
        PointXY c = new PointXY(-5, 5);
        PointXY d = new PointXY(5, 5);

        Line ab = new Line(a, b);
        Line cd = new Line(c, d);

        PointXY intersection1 = ab.getIntersectionWith(cd);
        PointXY intersection2 = cd.getIntersectionWith(ab);

        double x1 = intersection1.x();
        double y1 = intersection1.y();
        double x2 = intersection2.x();
        double y2 = intersection2.y();

        Assertions.assertTrue(Equals.soft(0d, x1, 0.01));
        Assertions.assertTrue(Equals.soft(5d, y1, 0.01));
        Assertions.assertTrue(Equals.soft(0d, x2, 0.01));
        Assertions.assertTrue(Equals.soft(5d, y2, 0.01));
    }

    @Test
    public void testVerticalVerticalIntersection() {
        PointXY a = new PointXY(0, 0);
        PointXY b = new PointXY(0, 10);
        PointXY c = new PointXY(0, 10);
        PointXY d = new PointXY(0, -100);

        Line ab = new Line(a, b);
        Line cd = new Line(c, d);

        PointXY intersection1 = ab.getIntersectionWith(cd);
        PointXY intersection2 = cd.getIntersectionWith(ab);

        double x1 = intersection1.x();
        double y1 = intersection1.y();
        double x2 = intersection2.x();
        double y2 = intersection2.y();

        Assertions.assertTrue(Equals.soft(0d, x1, 0.01));
        Assertions.assertTrue(Equals.soft(0d, y1, 0.01));
        Assertions.assertTrue(Equals.soft(0d, x2, 0.01));
        Assertions.assertTrue(Equals.soft(0d, y2, 0.01));
    }

    @Test
    public void testNoIntersection() {
        PointXY a = new PointXY(10, 0);
        PointXY b = new PointXY(10, 10);
        PointXY c = new PointXY(0, 10);
        PointXY d = new PointXY(0, -100);

        Line ab = new Line(a, b);
        Line cd = new Line(c, d);

        PointXY intersection1 = ab.getIntersectionWith(cd);
        PointXY intersection2 = cd.getIntersectionWith(ab);

        Assertions.assertNull(intersection1);
        Assertions.assertNull(intersection2);
    }

    @Test
    public void testGetClosestPoint() {
        Line line = new Line(
                new PointXY(0, 0),
                new PointXY(10, 10)
        );

        PointXY a = new PointXY(9, 1);

        PointXY closest = Line.getClosestPoint(a, line);

        Assertions.assertTrue(Equals.soft(5d, closest.x(), 0.01));
        Assertions.assertTrue(Equals.soft(5d, closest.y(), 0.01));
    }

    @Test
    public void testGetClosestEndPoint() {
        Line line = new Line(
                new PointXY(0, 0),
                new PointXY(10, 10)
        );

        PointXY a = new PointXY(-10, -10);
        PointXY b = new PointXY(20, 20);
        PointXY c = new PointXY(15, 12);

        PointXY ca = Line.getClosestPoint(a, line);
        PointXY cb = Line.getClosestPoint(b, line);
        PointXY cc = Line.getClosestPoint(c, line);

        Assertions.assertTrue(Equals.soft(0d, ca.x(), 0.01));
        Assertions.assertTrue(Equals.soft(0d, ca.y(), 0.01));

        Assertions.assertTrue(Equals.soft(10d, cb.x(), 0.01));
        Assertions.assertTrue(Equals.soft(10d, cb.y(), 0.01));

        Assertions.assertTrue(Equals.soft(10d, cc.x(), 0.01));
        Assertions.assertTrue(Equals.soft(10d, cc.y(), 0.01));
    }
}
