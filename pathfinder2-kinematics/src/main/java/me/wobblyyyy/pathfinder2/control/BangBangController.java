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

import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * Potentially the most incredibly simple type of controller.
 *
 * <p>
 * The calculate method functions like so:
 * <ul>
 *     <li>
 *         If it's given 0 as an input, it will return 0 as an output.
 *     </li>
 *     <li>
 *         If it's given a number greater than 0, it will return
 *         {@link #outputWhenPositive}.
 *     </li>
 *     <li>
 *         If it's given a number less than 0, it will return
 *         {@link #outputWhenNegative}.
 *     </li>
 * </ul>
 * The controller's "input" is specified by {@code getTarget() - value}.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class BangBangController extends AbstractController {
    private final double outputWhenPositive;
    private final double outputWhenNegative;

    /**
     * Create a new {@code BangBangController}.
     *
     * @param outputWhenPositive the controller's output when its input is
     *                           greater than 0.
     * @param outputWhenNegative the controller's output when its input is
     *                           less than 0.
     */
    public BangBangController(
        double outputWhenPositive,
        double outputWhenNegative
    ) {
        this.outputWhenPositive = outputWhenPositive;
        this.outputWhenNegative = outputWhenNegative;
    }

    @Override
    public double calculate(double value) {
        ValidationUtils.validate(value, "value");

        double delta = getTarget() - value;

        if (delta == 0) {
            return 0;
        } else if (delta > 0) {
            return outputWhenPositive;
        } else {
            return outputWhenNegative;
        }
    }
}
