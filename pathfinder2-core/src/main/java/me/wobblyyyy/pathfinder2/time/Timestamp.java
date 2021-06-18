/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.time;

/**
 * A single point in time. A time can either be provided, or the static method
 * {@link #now()} can be used to create a timestamp at the current point
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Timestamp {
    /**
     * The timestamp's value.
     *
     * <p>
     * Values reported by the {@link System#currentTimeMillis()} are provided
     * as longs, but we convert them to doubles because those are cooler.
     * </p>
     */
    private final double value;

    /**
     * Create a new {@code Timestamp} instance.
     *
     * @param value the timestamp's value. Typically, this should be the
     *              current time, but it can really be whatever you want.
     */
    public Timestamp(double value) {
        this.value = value;
    }

    /**
     * Create a new {@code Timestamp} based on the current time in ms.
     *
     * @return a new {@code Timestamp} based on the current time.
     */
    public static Timestamp now() {
        return new Timestamp(Time.ms());
    }

    /**
     * Get the difference between two timestamps.
     *
     * <p>
     * This value is calculated by subtracting the value of timestamp A from
     * the value of timestamp B.
     * </p>
     *
     * @param a the initial timestamp.
     * @param b the final timestamp.
     * @return the difference between the two timestamps.
     */
    public static double difference(Timestamp a,
                                    Timestamp b) {
        return b.value() - a.value();
    }

    /**
     * Get the {@code Timestamp}'s value as a double.
     *
     * @return the {@code Timestamp}'s value as a double.
     * @see #longValue()
     */
    public double value() {
        return value;
    }

    /**
     * Get the {@code Timestamp}'s value as a long.
     *
     * @return the {@code Timestamp}'s value as a long.
     * @see #value()
     */
    public long longValue() {
        return (long) value;
    }

    /**
     * Convert the {@code Timestamp} to a string. It's crazy, isn't it?
     *
     * @return literally just the timestamp's value but as a string.
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
