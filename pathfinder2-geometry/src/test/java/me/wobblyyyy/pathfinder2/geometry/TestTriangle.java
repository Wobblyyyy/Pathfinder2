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

public class TestTriangle {

    @Test
    public void testIsInsideTriangle() {
        PointXY a = new PointXY(0, 0);
        PointXY b = a.inDirection(5, Angle.fromDeg(0));
        PointXY c = a.inDirection(5, Angle.fromDeg(60));

        Triangle triangle = new Triangle(a, b, c);

        PointXY testPoint1 = new PointXY(0, 0);
        PointXY testPoint2 = new PointXY(10, 10);
        PointXY testPoint3 = new PointXY(3, 3);

        Assertions.assertTrue(testPoint1.isInside(triangle));
        Assertions.assertFalse(testPoint2.isInside(triangle));
        Assertions.assertTrue(testPoint3.isInside(triangle));
    }

    @Test
    public void testIsInsideRotatedTriangle() {
        PointXY a = new PointXY(0, 0);
        PointXY b = a.inDirection(5, Angle.fromDeg(0));
        PointXY c = a.inDirection(5, Angle.fromDeg(60));

        Triangle triangle = new Triangle(a, b, c).rotate(Angle.fromDeg(45));

        PointXY testPoint1 = new PointXY(0, 0);
        PointXY testPoint2 = new PointXY(10, 10);
        PointXY testPoint3 = new PointXY(2.5, 2.5);

        Assertions.assertFalse(testPoint1.isInside(triangle));
        Assertions.assertFalse(testPoint2.isInside(triangle));
        Assertions.assertTrue(testPoint3.isInside(triangle));
    }
}
