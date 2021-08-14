/*
 * Copyright (c) 2021.
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

public class TestProportionalController {
    private Controller controller;

    @Test
    public void testPositiveNumber() {
        controller = new ProportionalController(10);

        Assertions.assertEquals(10, controller.calculate(0, 1));
        Assertions.assertEquals(100, controller.calculate(0, 10));
        Assertions.assertEquals(1, controller.calculate(0, 0.1));
    }

    @Test
    public void testNegativeNumber() {
        controller = new ProportionalController(10);

        Assertions.assertEquals(-10, controller.calculate(0, -1));
        Assertions.assertEquals(-100, controller.calculate(0, -10));
        Assertions.assertEquals(-1, controller.calculate(0, -0.1));
    }

    @Test
    public void testZeroCoefficient() {
        controller = new ProportionalController(0);

        Assertions.assertEquals(0, controller.calculate(0, 100));
    }
}
