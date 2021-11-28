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

public class Magnitude {
    public static boolean higherMagnitude(double a,
                                          double b) {
        boolean greaterAbs = Math.abs(a) >= Math.abs(b);
        if (greaterAbs) {
            return (a <= 0 && b <= 0) || (a >= 0 && b >= 0);
        }
        return false;
    }

    public static boolean lowerMagnitude(double a,
                                         double b) {
        return !higherMagnitude(a, b);
    }
}
