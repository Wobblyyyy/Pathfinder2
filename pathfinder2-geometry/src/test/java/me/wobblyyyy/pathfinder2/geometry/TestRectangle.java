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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestRectangle {
    @Test
    public void testSimpleRectangle() {
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        PointXY testPoint1 = new PointXY(0, 0);
        PointXY testPoint2 = new PointXY(5, 5);
        PointXY testPoint3 = new PointXY(16, 13);
        PointXY testPoint4 = new PointXY(15, 15);

        Assertions.assertTrue(testPoint1.isInside(rectangle));
        Assertions.assertTrue(testPoint2.isInside(rectangle));
        Assertions.assertFalse(testPoint3.isInside(rectangle));
        Assertions.assertFalse(testPoint4.isInside(rectangle));
    }

    @Test
    public void testRotatedRectangle() {
        Rectangle rectangle = new Rectangle(0, 0, 10, 10).rotate(Angle.fromDeg(45));

        PointXY testPoint1 = new PointXY(0, 0);
        PointXY testPoint2 = new PointXY(5, 5);
        PointXY testPoint3 = new PointXY(16, 13);
        PointXY testPoint4 = new PointXY(15, 15);

        Assertions.assertFalse(testPoint1.isInside(rectangle));
        Assertions.assertTrue(testPoint2.isInside(rectangle));
        Assertions.assertFalse(testPoint3.isInside(rectangle));
        Assertions.assertFalse(testPoint4.isInside(rectangle));
    }

    @Test
    public void testVeryRotatedRectangle() {
        Rectangle rectangle = new Rectangle(0, 0, 10, 10)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45)
                .rotate(Angle.DEG_45);

        PointXY testPoint1 = new PointXY(0, 0);
        PointXY testPoint2 = new PointXY(5, 5);
        PointXY testPoint3 = new PointXY(16, 13);
        PointXY testPoint4 = new PointXY(15, 15);

        Assertions.assertTrue(testPoint1.isInside(rectangle));
        Assertions.assertTrue(testPoint2.isInside(rectangle));
        Assertions.assertFalse(testPoint3.isInside(rectangle));
        Assertions.assertFalse(testPoint4.isInside(rectangle));
    }

    private List<Rectangle> constructRectangles(double x,
                                                double y,
                                                int amount) {
        List<Rectangle> rectangles = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            double minX = x + i;
            double minY = y + i;
            double maxX = minX + 10;
            double maxY = minY + 10;

            Rectangle rectangle = new Rectangle(minX, minY, maxX, maxY);
            rectangles.add(rectangle);
            rectangles.add(rectangle.rotate(Angle.DEG_45));
            rectangles.add(rectangle.rotate(Angle.fromDeg(63.17)));
        }

        return rectangles;
    }

    @Test
    public void testRectanglePerformance() {
        List<Rectangle> rectangles = constructRectangles(0, 0, 1000);

        PointXY point1 = new PointXY(33, 12);
        PointXY point2 = new PointXY(-33, 12);
        PointXY point3 = new PointXY(100, 100);

        int vp1 = 0;
        int vp2 = 0;
        int vp3 = 0;

        for (Rectangle rectangle : rectangles) {
            boolean validPoint1 = point1.isInside(rectangle);
            boolean validPoint2 = point2.isInside(rectangle);
            boolean validPoint3 = point3.isInside(rectangle);

            if (validPoint1) vp1++;
            if (validPoint2) vp2++;
            if (validPoint3) vp3++;
        }

        Assertions.assertEquals(1, vp1);
        Assertions.assertEquals(1, vp2);
        Assertions.assertEquals(25, vp3);
    }
}
