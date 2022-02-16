/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

/**
 * Deadzone utilities.
 *
 * @author Colin Robertson
 * @since 0.15.0
 */
public class Deadzone {
    private Deadzone() {

    }

    /**
     * Apply a deadzone to a value.
     *
     * @param positiveDeadzone the positive deadzone.
     * @param negativeDeadzone the negative deadzone.
     * @param deadValue        the value that will be returned if the inputted
     *                         value is in the deadzone.
     * @param value            the value to apply the deadzone to.
     * @return a value, that's been deadband-ed?
     */
    public static double apply(double positiveDeadzone,
                               double negativeDeadzone,
                               double deadValue,
                               double value) {
        if (value > 0)
            return value > positiveDeadzone ? value : deadValue;
        else
            return value < negativeDeadzone ? value : deadValue;
    }

    /**
     * Apply a deadzone to a value.
     *
     * @param deadzone the deadzone to apply.
     * @param value    the value to apply the deadzone to.
     * @return a value, that's been deadband-ed?
     */
    public static double apply(double deadzone,
                               double value) {
        return apply(deadzone, -deadzone, 0, value);
    }
}

