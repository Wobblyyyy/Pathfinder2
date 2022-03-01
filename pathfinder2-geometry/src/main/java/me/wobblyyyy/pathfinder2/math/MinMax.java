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

/**
 * Utility class for clipping values.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class MinMax {
    private final double minimumX;
    private final double maximumX;

    public MinMax(double minimumX,
                  double maximumX) {
        this.minimumX = minimumX;
        this.maximumX = maximumX;
    }

    public static MinMax doNotClip() {
        return new MinMax(0, 0);
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
    public static double clip(double value,
                              double min,
                              double max) {
        if (min >= max) {
            throw new IllegalArgumentException(
                    "Cannot perform a clip operation with a higher minimum " +
                            "than maximum!"
            );
        }

        return Math.max(Math.min(value, max), min);
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
        return clip(value, minimumX, maximumX);
    }
}
