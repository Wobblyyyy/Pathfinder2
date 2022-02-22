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
 * Various utilities for arrays.
 *
 * @since 1.0.1
 * @author Colin Robertson
 */
public class ArrayUtils {
    private ArrayUtils() {

    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param <T> the type of object contained in the array.
     * @param arr the array to reverse.
     */
    public static <T> void reverse(T[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            T temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse several arrays (in place).
     *
     * @param arrs the arrays to reverse.
     */
    public static <T> void reverse(T[]... arrs) {
        for (T[] arr : arrs)
            reverse(arr);
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(double[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            double temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse several arrays (in place).
     *
     * @param arrs the arrays to reverse.
     */
    public static void reverse(double[]... arrs) {
        for (double[] arr : arrs)
            reverse(arr);
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(int[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            int temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(float[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            float temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(boolean[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            boolean temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(byte[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            byte temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(char[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            char temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(long[] arr) {
        if (arr == null)
            throw new NullPointerException("Cannot reverse a null array!");

        if (arr.length < 1)
            throw new IllegalArgumentException("Cannot reverse an array " +
                    "with a length less than 1!");

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            long temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }
}
