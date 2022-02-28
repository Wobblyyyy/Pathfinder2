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

import me.wobblyyyy.pathfinder2.geometry.PointXY;

public interface Equation {
    /**
     * Get a Y value at a specific X value according to the equation.
     *
     * @param x the X value to "look up."
     * @return the Y value, according to the inputted X value.
     */
    double getY(double x);

    /**
     * Get a point based on an X value.
     *
     * @param x the X value to "look up."
     * @return the point, according to the inputted X value.
     */
    default PointXY getPoint(double x) {
        return new PointXY(x, getY(x));
    }
}
