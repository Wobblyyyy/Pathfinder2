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
}
