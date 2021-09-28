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
 * A rolling average/moving average is a type of average that takes the
 * average of a predetermined amount of previous numbers. Every time a new
 * number is added to that average, the oldest number in the rolling average
 * (whatever number has an index of size - 1) will be removed from the array,
 * all the other numbers will be shifted over by 1, and the array's 0th
 * number will be the new number.
 *
 * <p>
 * This uses {@link System#arraycopy(Object, int, Object, int, int)} to rotate
 * an array of primitive double values for the sake of speed.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class RollingAverage {
    /**
     * The numbers to average.
     */
    private final double[] data;

    /**
     * The size of the data array.
     */
    private final int size;

    /**
     * The maximum index that's been used.
     */
    private int maxIndex = 0;

    /**
     * The last-calculated average.
     */
    private double lastAverage = 0.0;

    /**
     * Has any data changed since the last time the average was determined?
     */
    private boolean hasChanged = true;

    /**
     * Create a new {@code RollingAverage}. Having a larger size means the
     * average will be more well-rounded, at the cost of being more
     * computationally expensive. Any time a number is added, the average
     * is recalculated, meaning it has to find the sum of SIZE number of
     * numbers.
     *
     * @param size how many numbers to store in the rolling average.
     */
    public RollingAverage(int size) {
        data = new double[size];
        this.size = size;
    }

    /**
     * Get the average of the data stored in the {@code RollingAverage}
     * object.
     *
     * @return the average of all the numbers stored in the
     * {@code RollingAverage} object.
     */
    public double average() {
        if (hasChanged) {
            double sum = 0;

            for (int i = 0; i < maxIndex; i++) {
                sum += data[i];
            }

            if (maxIndex == size - 1) {
                lastAverage = sum / size;
            } else {
                lastAverage = sum / maxIndex;
            }
        }

        return lastAverage;
    }

    /**
     * Rotate the array.
     *
     * @param newValue the new value to add.
     */
    private void rotate(double newValue) {
        System.arraycopy(data, 0, data, 1, data.length - 1);
        data[0] = newValue;
    }

    /**
     * Add a number to the rolling average. This will also force the average
     * to be re-calculated.
     *
     * @param value the number to add to the average.
     * @return the new average.
     */
    public double add(double value) {
        if (maxIndex != size - 1) maxIndex++;
        rotate(value);
        hasChanged = true;
        return average();
    }
}
