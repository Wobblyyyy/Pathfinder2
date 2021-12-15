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

import me.wobblyyyy.pathfinder2.math.Spline;

public class SplineController extends AbstractController {
    private final Spline spline;

    public SplineController(Spline spline) {
        this.spline = spline;
    }

    /**
     * OVERRIDE THIS METHOD TO CHANGE INPUTS!
     *
     * @param value the input.
     * @return the output.
     */
    public double modifyInput(double value) {
        return value;
    }

    @Override
    public double calculate(double value) {
        return spline.interpolateY(modifyInput(value));
    }
}
