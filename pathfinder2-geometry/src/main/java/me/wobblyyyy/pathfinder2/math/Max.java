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

public class Max {
    private Max() {

    }

    public static double of(double... values) {
        double m = values[0];

        for (int i = 1; i < values.length; i++) {
            m = Math.max(values[i], m);
        }

        return m;
    }

    public static double absoluteOf(double... values) {
        double m = Math.abs(values[0]);

        for (int i = 1; i < values.length; i++) {
            m = Math.max(Math.abs(values[i]), m);
        }

        return m;
    }
}
