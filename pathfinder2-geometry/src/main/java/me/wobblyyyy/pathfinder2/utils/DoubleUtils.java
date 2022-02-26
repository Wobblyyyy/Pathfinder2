/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

/**
 * Various utilities for {@code Double} and {@code double}.
 *
 * @author Colin Robertson
 * @since 1.0.1
 */
public class DoubleUtils {
    private DoubleUtils() {

    }

    public static Double[] box(double[] array) {
        Double[] boxed = new Double[array.length];

        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];

        return boxed;
    }

    public static double[] unbox(Double[] array) {
        double[] unboxed = new double[array.length];

        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];

        return unboxed;
    }

    public static double[] unbox(Iterable<Double> collection) {
        int size = 0;
        for (Double d : collection)
            size++;

        double[] unboxed = new double[size];
        int i = 0;
        for (Double d : collection)
            unboxed[i++] = d;

        return unboxed;
    }
}
