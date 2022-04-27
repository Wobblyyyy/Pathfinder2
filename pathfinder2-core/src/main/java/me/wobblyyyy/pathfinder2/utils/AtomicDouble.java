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

import java.util.concurrent.atomic.AtomicReference;

/**
 * Atomic reference for a double value.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public class AtomicDouble {
    private double value = 0.0;

    /**
     * Create a new {@code AtomicDouble} with a default value of 0.
     */
    public AtomicDouble() {
        this(0.0);
    }

    /**
     * Create a new {@code AtomicDouble} with a provided value.
     *
     * @param value the initial value of the {@code AtomicDouble}.
     */
    public AtomicDouble(double value) {
        this.value = value;
    }

    /**
     * Get the {@code AtomicDouble}'s value.
     *
     * @return the value.
     */
    public double get() {
        return value;
    }

    /**
     * Set the {@code AtomicDouble}'s value.
     *
     * @param value the value.
     */
    public void set(double value) {
        this.value = value;
    }

    /**
     * Convert {@code this} to an {@link AtomicReference} with the same value.
     *
     * @return {@code this} as an {@link AtomicReference}.
     */
    public AtomicReference<Double> toAtomicReference() {
        return new AtomicReference<>(value);
    }
}
