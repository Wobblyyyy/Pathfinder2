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

public class DoubleQueue {
    private final double[] data;
    private int maxIndex = 0;

    public DoubleQueue(double[] data) {
        this.data = data;
    }

    public DoubleQueue(int size) {
        this(new double[size]);
    }

    public double get(int index) {
        return data[index];
    }

    public double[] getData() {
        double[] d = new double[maxIndex];
        System.arraycopy(data, 0, d, 0, d.length);
        return d;
    }

    public DoubleQueue add(double value) {
        System.arraycopy(data, 0, data, 1, data.length - 1);
        data[0] = value;

        if (maxIndex < data.length) {
            maxIndex++;
        }

        return this;
    }

    public void set(int index, double value) {
        data[index] = value;
    }

    public DoubleQueue clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }

        maxIndex = 0;

        return this;
    }
}
