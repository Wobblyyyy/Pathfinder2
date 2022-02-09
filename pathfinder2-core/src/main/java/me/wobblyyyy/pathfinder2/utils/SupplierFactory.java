package me.wobblyyyy.pathfinder2.utils;

import java.util.function.Supplier;

/**
 * Used for creating more complex suppliers. Similiar to (but better) than
 * {@link SupplierFilter}. There are some warnings about possible heap
 * pollution, but to be honest, I wouldn't worry about them. If something
 * goes wrong, sucks! Who cares if the heap gets polluted? What did the heap
 * ever do to NOT deserve it?
 *
 * @author Colin Robertson
 * @since 0.10.9
 */
public class SupplierFactory {
    private SupplierFactory() {

    }

    public static Supplier<Boolean> anyTrue(Supplier<Boolean>... suppliers) {
        return () -> {
            for (Supplier<Boolean> supplier : suppliers)
                if (supplier.get()) return true;

            return false;
        };
    }

    public static Supplier<Boolean> anyFalse(Supplier<Boolean>... suppliers) {
        return () -> {
            for (Supplier<Boolean> supplier : suppliers)
                if (!supplier.get()) return true;

            return false;
        };
    }

    public static Supplier<Boolean> allTrue(Supplier<Boolean>... suppliers) {
        return () -> {
            boolean allTrue = true;

            for (Supplier<Boolean> supplier : suppliers)
                if (!supplier.get()) {
                    allTrue = false;
                    break;
                }

            return allTrue;
        };
    }

    public static Supplier<Boolean> allFalse(Supplier<Boolean>... suppliers) {
        return () -> {
            boolean allFalse = true;

            for (Supplier<Boolean> supplier : suppliers)
                if (supplier.get()) {
                    allFalse = false;
                    break;
                }

            return allFalse;
        };
    }

    public static Supplier<Boolean> trueThenAllFalse(Supplier<Boolean> mustBeTrue,
                                                     Supplier<Boolean>... suppliers) {
        Supplier<Boolean> allFalse = allFalse(suppliers);

        return () -> mustBeTrue.get() && allFalse.get();
    }

    public static Supplier<Boolean> falseThenAllTrue(Supplier<Boolean> mustBeFalse,
                                                     Supplier<Boolean>... suppliers) {
        Supplier<Boolean> allTrue = allFalse(suppliers);

        return () -> !mustBeFalse.get() && allTrue.get();
    }
}
