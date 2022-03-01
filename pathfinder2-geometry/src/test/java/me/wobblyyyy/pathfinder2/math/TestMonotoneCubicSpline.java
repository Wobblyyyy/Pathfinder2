/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMonotoneCubicSpline {
    @Test
    public void testNonReflectedSpline() {
        Spline spline = new MonotoneCubicSpline(
                new double[] { 0, 5, 10, 15 },
                new double[] { 0, 10, 15, 25 }
        );

        Assertions.assertEquals(0, spline.interpolateY(0));
        Assertions.assertEquals(10, spline.interpolateY(5));
        Assertions.assertEquals(15, spline.interpolateY(10));
        Assertions.assertEquals(25, spline.interpolateY(15));
    }

    @Test
    public void testReflectedSpline() {
        Spline spline = new MonotoneCubicSpline(
                new double[] { 0, -5, -10, -15 },
                new double[] { 0, 10, 15, 25 }
        );

        Assertions.assertEquals(0, spline.interpolateY(0));
        Assertions.assertEquals(10, spline.interpolateY(-5));
        Assertions.assertEquals(15, spline.interpolateY(-10));
        Assertions.assertEquals(25, spline.interpolateY(-15));
    }
}
