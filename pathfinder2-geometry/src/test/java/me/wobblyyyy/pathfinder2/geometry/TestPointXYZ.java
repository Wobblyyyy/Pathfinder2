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

public class TestPointXYZ {
    @Test
    public void testReflectX() {
        PointXYZ point = new PointXYZ(10, 10, 0);

        Assertions.assertEquals(new PointXYZ(-10, 10, 0), point.reflectOverX(0));
    }

    @Test
    public void testReflectY() {
        PointXYZ point = new PointXYZ(10, 10, 0);

        Assertions.assertEquals(new PointXYZ(10, -10, 0), point.reflectOverY(0));
    }
}
