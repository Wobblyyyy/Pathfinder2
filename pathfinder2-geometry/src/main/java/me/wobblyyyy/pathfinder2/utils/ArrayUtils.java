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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Various utilities for arrays.
 *
 * @author Colin Robertson
 * @since 1.0.1
 */
public class ArrayUtils {

    private ArrayUtils() {}

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param <T> the type of object contained in the array.
     * @param arr the array to reverse.
     */
    public static <T> void reverse(T[] arr) {
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            T temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(double[] arr) {
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

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
        for (double[] arr : arrs) reverse(arr);
    }

    /**
     * Reverse an array of objects. This will reverse the array in place.
     *
     * @param arr the array to reverse.
     */
    public static void reverse(int[] arr) {
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

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
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

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
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

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
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

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
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

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
        if (arr == null) throw new NullPointerException(
            "Cannot reverse a null array!"
        );

        if (arr.length < 1) throw new IllegalArgumentException(
            "Cannot reverse an array " + "with a length less than 1!"
        );

        for (int i = 0; i < arr.length / 2; i++) {
            int idx = arr.length - 1 - i;

            long temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    /**
     * Convert an array into a list.
     *
     * @param <T> the type of elements in the array.
     * @param arr the array.j
     * @return the array, represented as a list.
     */
    public static <T> List<T> toList(T[] arr) {
        List<T> list = new ArrayList<>(arr.length);

        Collections.addAll(list, arr);

        return list;
    }

    public static List<Double> toList(double[] arr) {
        List<Double> list = new ArrayList<>(arr.length);

        for (double d : arr) list.add(d);

        return list;
    }

    public static List<Float> toList(float[] arr) {
        List<Float> list = new ArrayList<>(arr.length);

        for (float d : arr) list.add(d);

        return list;
    }

    public static List<Integer> toList(int[] arr) {
        List<Integer> list = new ArrayList<>(arr.length);

        for (int d : arr) list.add(d);

        return list;
    }

    public static double[] toDoubleArray(
        Collection<? extends Number> collection
    ) {
        double[] arr = new double[collection.size()];

        int i = 0;
        for (Number n : collection) arr[i++] = n.doubleValue();

        return arr;
    }

    public static PointXY[] toPointXYArray(
        Collection<? extends PointXY> collection
    ) {
        PointXY[] arr = new PointXY[collection.size()];

        int i = 0;
        for (PointXY p : collection) arr[i++] = p;

        return arr;
    }

    public static PointXYZ[] toPointXYZArray(
        Collection<? extends PointXYZ> collection
    ) {
        PointXYZ[] arr = new PointXYZ[collection.size()];

        int i = 0;
        for (PointXYZ p : collection) arr[i++] = p;

        return arr;
    }

    public static Angle[] toAngleArray(Collection<? extends Angle> collection) {
        Angle[] arr = new Angle[collection.size()];

        int i = 0;
        for (Angle a : collection) arr[i++] = a;

        return arr;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param a the first of the two arrays.
     * @param b the second of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(double[] a, double[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;

        return true;
    }

    public static boolean arrayEquals(double[]... arrays) {
        if (arrays.length == 0) return true;

        for (int i = 0; i < arrays.length - 1; i++) {
            double[] a = arrays[i];
            double[] b = arrays[i + 1];

            if (!arrayEquals(a, b)) return false;
        }

        return true;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param a the first of the two arrays.
     * @param b the second of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(float[] a, float[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;

        return true;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param a the first of the two arrays.
     * @param b the second of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(int[] a, int[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;

        return true;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param a the first of the two arrays.
     * @param b the second of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;

        return true;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param a the first of the two arrays.
     * @param b the second of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(char[] a, char[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;

        return true;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param a the first of the two arrays.
     * @param b the second of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(Object[] a, Object[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) if (!a[i].equals(b[i])) return false;

        return true;
    }

    /**
     * Compare two arrays to see if they're equal.
     *
     * @param arrays the first of the two arrays.
     * @return if each of the elements in the arrays are equal, return true.
     * Otherwise, return false.
     */
    public static boolean arrayEquals(Object[]... arrays) {
        if (arrays.length == 0) return true;

        for (int i = 0; i < arrays.length - 1; i++) {
            Object[] a = arrays[i];
            Object[] b = arrays[i + 1];

            if (!arrayEquals(a, b)) return false;
        }

        return true;
    }
}
