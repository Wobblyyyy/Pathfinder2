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

public class TestRollingAverage {
    @Test
    public void testWithEmptyValues() {
        RollingAverage avg = new RollingAverage(10);

        avg.add(0.0);
        avg.add(1.0);

        Assertions.assertEquals(0.5, avg.average());
    }

    @Test
    public void testWithCoolValues() {
        double[] numbers = new double[1_000];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i * 17.3;
        }

        RollingAverage avg = new RollingAverage(1_000);
        for (double number : numbers) {
            avg.add(number);
        }

        Assertions.assertEquals(
                Average.of(numbers),
                avg.average()
        );
    }
}
