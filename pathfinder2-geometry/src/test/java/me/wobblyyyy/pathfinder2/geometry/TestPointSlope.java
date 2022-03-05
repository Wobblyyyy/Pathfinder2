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

public class TestPointSlope {

    @Test
    public void testIntersection() {
        PointSlope a = new PointSlope(new PointXY(0, 0), 1);
        PointSlope b = new PointSlope(new PointXY(0, 10), -1);

        PointXY intersection1 = a.getIntersection(b);
        PointXY intersection2 = b.getIntersection(a);

        Assertions.assertEquals(5d, intersection1.x());
        Assertions.assertEquals(5d, intersection1.y());
        Assertions.assertEquals(5d, intersection2.x());
        Assertions.assertEquals(5d, intersection2.y());
    }
}
