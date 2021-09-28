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

public class TestCircle {
    @Test
    public void testIsPointInShape() {
        PointXY center = new PointXY(0, 0);
        double radius = 10;
        Circle circle = new Circle(center, radius);

        PointXY test1 = new PointXY(0, 0);
        PointXY test2 = new PointXY(10, 10);
        PointXY test3 = new PointXY(5, 5);

        Assertions.assertTrue(test1.isInside(circle));
        Assertions.assertFalse(test2.isInside(circle));
        Assertions.assertTrue(test3.isInside(circle));
    }
}
