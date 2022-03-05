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

import java.util.function.Supplier;

/**
 * Filters used for boolean conditions. This allows more complex button
 * requirements to be created: for example, you MUST hold A, but can not
 * hold B or X.
 *
 * @author Colin Robertson
 * @since 0.8.0
 * @deprecated Use {@link SupplierFactory} instead!
 */
@Deprecated
public class SupplierFilter {

    private SupplierFilter() {}

    /**
     * Are any of the suppliers true?
     *
     * @param suppliers the suppliers.
     * @return true if any of the suppliers are true - otherwise, false.
     */
    public static boolean anyTrue(Supplier<Boolean>... suppliers) {
        for (Supplier<Boolean> supplier : suppliers) if (
            supplier.get()
        ) return true;

        return false;
    }

    /**
     * Are any of the suppliers false?
     *
     * @param suppliers the suppliers.
     * @return true if any of the suppliers are false - otherwise, false.
     */
    public static boolean anyFalse(Supplier<Boolean>... suppliers) {
        for (Supplier<Boolean> supplier : suppliers) if (
            !supplier.get()
        ) return true;

        return false;
    }

    /**
     * Are all of the suppliers true?
     *
     * @param suppliers the suppliers.
     * @return true if all of the suppliers are true - otherwise, false.
     */
    public static boolean allTrue(Supplier<Boolean>... suppliers) {
        boolean t = true;

        for (Supplier<Boolean> supplier : suppliers) if (!supplier.get()) {
            t = false;
            break;
        }

        return t;
    }

    /**
     * Are all of the suppliers false?
     *
     * @param suppliers the suppliers.
     * @return true if all of the suppliers are false - otherwise, false.
     */
    public static boolean allFalse(Supplier<Boolean>... suppliers) {
        boolean t = true;

        for (Supplier<Boolean> supplier : suppliers) if (supplier.get()) {
            t = false;
            break;
        }

        return t;
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns true and all of the other
     * suppliers also return true.
     */
    public static boolean trueThenAllTrue(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return supplier.get() && allTrue(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns true and all of the other
     * suppliers return false.
     */
    public static boolean trueThenAllFalse(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return supplier.get() && allFalse(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns false and all of the other
     * suppliers also return true.
     */
    public static boolean falseThenAllTrue(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return !supplier.get() && allTrue(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns false and all of the other
     * suppliers return false.
     */
    public static boolean falseThenAllFalse(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return !supplier.get() && allFalse(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns true and any of the other
     * suppliers also return true.
     */
    public static boolean trueThenAnyTrue(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return supplier.get() && anyTrue(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns true and any of the other
     * suppliers return false.
     */
    public static boolean trueThenAnyFalse(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return supplier.get() && anyFalse(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns false and any of the other
     * suppliers also return true.
     */
    public static boolean falseThenAnyTrue(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return !supplier.get() && anyTrue(inhibitors);
    }

    /**
     * @param supplier   the first supplier.
     * @param inhibitors the rest of the suppliers.
     * @return true if the first supplier returns false and any of the other
     * suppliers return false.
     */
    public static boolean falseThenAnyFalse(
        Supplier<Boolean> supplier,
        Supplier<Boolean>... inhibitors
    ) {
        return !supplier.get() && anyFalse(inhibitors);
    }

    public static int trueCount(Supplier<Boolean>... suppliers) {
        int count = 0;

        for (Supplier<Boolean> supplier : suppliers) if (
            supplier.get()
        ) count++;

        return count;
    }

    public static int falseCount(Supplier<Boolean>... suppliers) {
        int count = 0;

        for (Supplier<Boolean> supplier : suppliers) if (
            !supplier.get()
        ) count++;

        return count;
    }

    /**
     * Is the amount of true suppliers greater than the minimum?
     *
     * @param minimum   the minimum value.
     * @param suppliers the suppliers.
     * @return true if the amount is greater, otherwise, false.
     */
    public static boolean trueCountGreaterThan(
        int minimum,
        Supplier<Boolean>... suppliers
    ) {
        return trueCount(suppliers) > minimum;
    }

    /**
     * Is the amount of true suppliers less than the maximum?
     *
     * @param maximum   the maximum value.
     * @param suppliers the suppliers.
     * @return true if the amount is greater, otherwise, false.
     */
    public static boolean trueCountLessThan(
        int maximum,
        Supplier<Boolean>... suppliers
    ) {
        return trueCount(suppliers) < maximum;
    }

    /**
     * Is the amount of false suppliers greater than the minimum?
     *
     * @param minimum   the minimum value.
     * @param suppliers the suppliers.
     * @return false if the amount is greater, otherwise, true.
     */
    public static boolean falseCountGreaterThan(
        int minimum,
        Supplier<Boolean>... suppliers
    ) {
        return falseCount(suppliers) > minimum;
    }

    /**
     * Is the amount of false suppliers less than the maximum?
     *
     * @param maximum   the maximum value.
     * @param suppliers the suppliers.
     * @return false if the amount is greater, otherwise, true.
     */
    public static boolean falseCountLessThan(
        int maximum,
        Supplier<Boolean>... suppliers
    ) {
        return falseCount(suppliers) < maximum;
    }

    /**
     * Invert a {@link Supplier}.
     *
     * @param supplier the supplier to invert.
     * @return an inverted supplier.
     */
    public static Supplier<Boolean> invertSupplier(Supplier<Boolean> supplier) {
        return () -> !supplier.get();
    }
}
