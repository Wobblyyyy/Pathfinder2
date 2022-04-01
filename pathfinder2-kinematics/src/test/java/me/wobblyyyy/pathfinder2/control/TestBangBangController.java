/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBangBangController {
    private final Controller[] controllers = new BangBangController[] {
        new BangBangController(0, 0),
        new BangBangController(-1, 1),
        new BangBangController(1, -1)
    };

    private void runTests(double target, double value, double[] expected) {
        for (int i = 0; i < controllers.length; i++) {
            Controller controller = controllers[i];
            controller.setTarget(target);
            Assertions.assertEquals(expected[i], controller.calculate(value));
        }
    }

    @Test
    public void testWhenZero() {
        runTests(0, 0, new double[] { 0, 0, 0 });
    }

    @Test
    public void testWhenPositive() {
        runTests(100, 0, new double[] { 0, -1, 1 });
    }

    @Test
    public void testWhenNegative() {
        runTests(-100, 0, new double[] { 0, 1, -1 });
    }
}
