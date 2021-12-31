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

public class Min {
    private Min() {

    }

    public static double of(double... values) {
        double m = values[0];

        for (int i = 1; i < values.length; i++) {
            m = Math.min(values[i], m);
        }

        return m;
    }

    public static double absoluteOf(double... values) {
        double m = Math.abs(values[0]);

        for (int i = 1; i < values.length; i++) {
            m = Math.min(Math.abs(values[i]), m);
        }

        return m;
    }

    @SuppressWarnings("DuplicatedCode")
    public static double magnitude(double... values) {
        double value = values[0];
        double magnitude = Math.abs(values[0]);

        for (int i = 1; i < values.length; i++) {
            double val = values[i];
            double mag = Math.abs(values[i]);

            if (mag < magnitude) {
                value = val;
            }
        }

        return value;
    }
}
