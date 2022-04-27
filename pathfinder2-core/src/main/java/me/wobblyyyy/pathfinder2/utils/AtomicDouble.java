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

public class AtomicDouble {
    private double value = 0.0;

    public AtomicDouble() {
        this(0.0);
    }

    public AtomicDouble(double value) {
        this.value = value;
    }

    public double get() {
        return value;
    }

    public void set(double value) {
        this.value = value;
    }
}
