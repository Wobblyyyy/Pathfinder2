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

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Tools for rounding numbers.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Rounding {
    /**
     * Round a number value to an integer.
     *
     * @param value the value to round.
     * @return the rounded value, as an integer.
     */
    public static int roundInt(double value) {
        return (int) Math.round(value);
    }

    /**
     * Round a number to a certain amount of decimal places.
     *
     * @param value  the value to round.
     * @param places how many decimal places to round to.
     * @return the rounded decimal.
     */
    public static double round(double value,
                               int places) {
        if (places < 0)
            throw new IllegalArgumentException("Places must be greater than 0");

        BigDecimal decimal = new BigDecimal(Double.toString(value))
                .setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    /**
     * Round a value to the default number of places (3).
     *
     * @param value the value to round.
     * @return the value, rounded to 3 places.
     */
    public static double round(double value) {
        return round(value, 3);
    }

    /**
     * Quickly round a value to a specific number of places.
     *
     * @param value  the value to round.
     * @param places how many places to round to.
     * @return the rounded value.
     */
    public static double fastRound(double value,
                                   int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    /**
     * Quickly round a value to the default number of places (3).
     *
     * @param value the value to round.
     * @return the rounded value.
     */
    public static double fastRound(double value) {
        return fastRound(value, 3);
    }
}
