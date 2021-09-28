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

public class TestSlopeIntercept {
    @Test
    public void testIntersection() {
        SlopeIntercept a = new SlopeIntercept(1, 0);
        SlopeIntercept b = new SlopeIntercept(-1, 10);

        PointXY intersection1 = a.getIntersection(b);
        PointXY intersection2 = b.getIntersection(a);

        Assertions.assertEquals(5d, intersection1.x());
        Assertions.assertEquals(5d, intersection1.y());
        Assertions.assertEquals(5d, intersection2.x());
        Assertions.assertEquals(5d, intersection2.y());
    }

    @Test
    public void testInvalidIntersection() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SlopeIntercept a = new SlopeIntercept(Double.POSITIVE_INFINITY, 0);
                }
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SlopeIntercept a = new SlopeIntercept(Double.NaN, 0);
                }
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SlopeIntercept a = new SlopeIntercept(Double.NEGATIVE_INFINITY, 0);
                }
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SlopeIntercept a = new SlopeIntercept(0, Double.POSITIVE_INFINITY);
                }
        );
    }

    @Test
    public void testVerticalIntersection() {
        SlopeIntercept a = SlopeIntercept.newVertical(10);
        SlopeIntercept b = new SlopeIntercept(1, 0);

        PointXY intersection1 = a.getIntersection(b);
        PointXY intersection2 = b.getIntersection(a);

        Assertions.assertEquals(10d, intersection1.x());
        Assertions.assertEquals(10d, intersection1.y());
        Assertions.assertEquals(10d, intersection2.x());
        Assertions.assertEquals(10d, intersection2.y());
    }

    @Test
    public void testSameVerticalIntersection() {
        SlopeIntercept a = SlopeIntercept.newVertical(10);
        SlopeIntercept b = SlopeIntercept.newVertical(10);

        PointXY intersection1 = a.getIntersection(b);
        PointXY intersection2 = b.getIntersection(a);

        Assertions.assertEquals(10d, intersection1.x());
        Assertions.assertEquals(0d, intersection1.y());
        Assertions.assertEquals(10d, intersection2.x());
        Assertions.assertEquals(0d, intersection2.y());
    }

    @Test
    public void testNoVerticalIntersection() {
        SlopeIntercept a = SlopeIntercept.newVertical(0);
        SlopeIntercept b = SlopeIntercept.newVertical(10);

        Assertions.assertNull(a.getIntersection(b));
    }

    @Test
    public void testNoIntersection() {
        SlopeIntercept a = new SlopeIntercept(1, 0);
        SlopeIntercept b = new SlopeIntercept(1, 1);

        Assertions.assertNull(a.getIntersection(b));
    }
}
