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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Rectangle rectangle = new Rectangle(0, 0, 10, 10)
        .rotate(Angle.fromDeg(45));

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

    private List<Rectangle> constructRectangles(
        double x,
        double y,
        int amount
    ) {
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
    public void testIsPointInShape() {
        Rectangle rectangle = new Rectangle(0, 2, 10, 3);

        PointXY test1 = new PointXY(0, 0); // false
        PointXY test2 = new PointXY(10, 10); // false
        PointXY test3 = new PointXY(0, 2.5); // true
        PointXY test4 = new PointXY(1, 2.51); // true
        PointXY test5 = new PointXY(10, 3); // true
        PointXY test6 = new PointXY(5, 3.5); // false

        Assertions.assertFalse(test1.isInside(rectangle));
        Assertions.assertFalse(test2.isInside(rectangle));
        Assertions.assertTrue(test3.isInside(rectangle));
        Assertions.assertTrue(test4.isInside(rectangle));
        Assertions.assertTrue(test5.isInside(rectangle));
        Assertions.assertFalse(test6.isInside(rectangle));
    }
}
