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

import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * Utility class for clipping values.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class MinMax {
    private final double minimum;
    private final double maximum;

    /**
     * Create a new {@code MinMax}.
     *
     * @param minimum the minimum value.
     * @param maximum the maximum value.
     */
    public MinMax(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static MinMax doNotClip() {
        return new MinMax(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Clip a value by ensuring it's between the minimum and maximum values.
     *
     * @param value the value to clip.
     * @param min   the minimum value.
     * @param max   the maximum value.
     * @return the clipped value. If the value is less than the minimum, the
     * minimum will be returned. If the value is greater than the maximum,
     * the maximum will be returned. Otherwise, the value will be returned.
     */
    public static double clip(double value, double min, double max) {
        ValidationUtils.validate(value, "value");

        if (min >= max) {
            throw new IllegalArgumentException(
                StringUtils.format(
                    "Cannot perform a clip operation with a higher minimum " +
                    "than maximum! Min: <%s>, Max: <%s>",
                    min,
                    max
                )
            );
        }

        return Math.max(Math.min(value, max), min);
    }

    /**
     * Clip a value by ensuring the value is at least {@code min}, is at
     * most {@code max}, and the value's absolute value is at least
     * {@code minMagnitude} and the value's absolute value is at most
     * {@code maxMagnitude}.
     *
     * @param value        the value to clip.
     * @param min          the minimum value.
     * @param minMagnitude the value's minimum magnitude.
     * @param max          the maximum value.
     * @param maxMagnitude the value's maximum magnitude.
     * @return
     * @return the clipped value. If the value is less than the minimum, the
     * minimum will be returned. If the value is greater than the maximum,
     * the maximum will be returned. If the value's absolute value is less
     * than the minimum magnitude, the minimum magnitude will be returned (it
     * will be negative if {@code value} was negative). If the value's
     * absolute value is greater than the maximum magnitude, the maximum
     * magnitude will be returned (it will be negative if {@code value} was
     * negative).
     */
    public static double clip(
        double value,
        double min,
        double minMagnitude,
        double max,
        double maxMagnitude
    ) {
        double abs = Math.abs(value);
        boolean isNegative = value < 0;

        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else if (abs < minMagnitude) {
            return isNegative ? minMagnitude * -1 : minMagnitude;
        } else if (abs > maxMagnitude) {
            return isNegative ? maxMagnitude * -1 : maxMagnitude;
        } else {
            return value;
        }
    }

    /**
     * Clip a value by ensuring it's between the minimum and maximum values.
     *
     * @param value the value to clip.
     * @return the clipped value. If the value is less than the minimum, the
     * minimum will be returned. If the value is greater than the maximum,
     * the maximum will be returned. Otherwise, the value will be returned.
     */
    public double clip(double value) {
        return clip(value, minimum, maximum);
    }
}
