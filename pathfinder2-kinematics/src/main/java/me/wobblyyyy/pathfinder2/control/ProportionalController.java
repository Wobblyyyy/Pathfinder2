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

import me.wobblyyyy.pathfinder2.math.MinMax;

/**
 * The most simple controller - a directly proportional controller. The value
 * outputted this controller will be directly proportional to the error (or
 * the values inputted to the controller).
 *
 * @author Colin Robertson
 */
public class ProportionalController extends AbstractController {
    /**
     * The controller's coefficient.
     */
    private final double coefficient;

    /**
     * Create a new {@code ProportionalController}.
     *
     * @param coefficient the controller's coefficient. Higher coefficients
     *                    mean higher outputs, lower coefficients mean lower
     *                    outputs. A negative coefficient "inverts" the
     *                    controller. A coefficient of 0 renders the controller
     *                    completely useless.
     */
    public ProportionalController(double coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * Get the controller's coefficient.
     *
     * @return the controller's coefficient.
     */
    public double getCoefficient() {
        return this.coefficient;
    }

    /**
     * Calculate an output value by multiplying the distance from the target
     * value (target - input) by the controller's {@link #coefficient}.
     *
     * @param value the input the controller should calculate for.
     * @return the difference between the target value and the input value
     * multiplied by {@link #coefficient}.
     */
    @Override
    public double calculate(double value) {
        double delta = getTarget() - value;

        return MinMax.clip(
                delta * coefficient,
                getMin(),
                getMax()
        );
    }
}
