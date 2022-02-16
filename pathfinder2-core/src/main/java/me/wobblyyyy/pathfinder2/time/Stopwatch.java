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

import java.util.ArrayList;
import java.util.List;

/**
 * A stopwatch, capable of stopping AND watching. It's absolutely mind
 * bending how incredibly complex such a thing is.
 *
 * <p>
 * To be honest, this code kind of sucks, so... yeah. Not my problem, right?
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Stopwatch {
    /**
     * A list of timestamps. The first of these timestamps should always be the
     * start time. Every other timestamp is technically considered to be a lap.
     */
    private final List<Timestamp> timestamps = new ArrayList<>();

    /**
     * Is the {@code Stopwatch} stopped? If it's stopped, users can no longer
     * add any more laps.
     */
    private boolean isStopped = false;

    /**
     * Create a new {@code Stopwatch}.
     *
     * @see #now()
     * @see #start()
     */
    public Stopwatch() {

    }

    /**
     * Create a new {@code Stopwatch} instance and start it. This is a chain
     * of constructing a new {@code Stopwatch} and then calling the
     * {@link #start()} method.
     *
     * @return a stopwatch started at the current time.
     */
    public static Stopwatch now() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        return stopwatch;
    }

    /**
     * Start the stopwatch. This will clear the list of laps as well as ensure
     * the stopwatch isn't stopped. Calling this method essentially resets
     * the stopwatch, meaning it can be used again for other purposes.
     */
    public void start() {
        timestamps.clear();
        isStopped = false;
        timestamps.add(Timestamp.now());
    }

    /**
     * Record a lap using the current time.
     */
    public void lap() {
        if (!isStopped) {
            timestamps.add(Timestamp.now());
        }
    }

    /**
     * Add one final lap to the stopwatch and set the {@code isStopped} flag
     * to true, meaning no more timestamps can be added without first calling
     * the {@link #start()} method again.
     */
    public void stop() {
        lap();
        isStopped = true;
    }

    /**
     * Get a {@code String} representation of a specified lap.
     *
     * @param index the index of the lap. (0 = start)
     * @return a string representation of the specified lap.
     */
    public String lap(int index) {
        return timestamps.get(index).toString();
    }

    /**
     * Get the amount of time elapsed between a specified lap number and the
     * start of the timestamp.
     *
     * @param index a number greater than 1, representing the index of the
     *              desired lap to calculate.
     * @return the amount of time elapsed between the start and the target
     * lap.
     */
    public double elapsedSince(int index) {
        if (index > 1) {
            return Timestamp.difference(
                    timestamps.get(0),
                    timestamps.get(index)
            );
        } else {
            return 0;
        }
    }

    /**
     * Get the total amount of laps.
     *
     * <p>
     * Please note that the START timestamp is included in this count.
     * </p>
     *
     * @return the total amount of timestamps stored in the stopwatch.
     */
    public int laps() {
        return timestamps.size();
    }

    /**
     * Get the total amount of time elapsed between the first timestamp and
     * the last timestamp. Note that this method doesn't have any checks to
     * see whether there's more than a single timestamp - if you only
     * have one timestamp (say you just called the start method and nothing
     * else) this method will then return 0.
     *
     * @return the total amount of time elapsed between the {@code Stopwatch}'s
     * first and last recorded timestamps. These CAN both be the same.
     */
    public double elapsed() {
        if (timestamps.size() < 1)
            throw new RuntimeException("you need to start the stopwatch " +
                    "before getting the elapsed time!");

        return Timestamp.difference(
                timestamps.get(0),
                timestamps.get(laps() - 1)
        );
    }

    public List<Timestamp> getTimestamps() {
        return this.timestamps;
    }

    public List<Double> getTimes() {
        List<Double> list = new ArrayList<>();

        for (Timestamp timestamp : timestamps) {
            list.add(timestamp.value());
        }

        return list;
    }

    /**
     * Convert the timestamp to a string.
     *
     * <p>
     * The general format is as follows:
     * <pre>
     * 0: (start time)
     * 1: (lap 1 time)
     * 2: (lap 2 time)
     * 3: (lap 3 / stop time)
     * Elapsed: (elapsed time)
     * </pre>
     * </p>
     *
     * @return a string representation of the {@code Stopwatch}.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < laps(); i++) {
            builder.append(i);
            builder.append(": ");
            builder.append(lap(i));
            builder.append("\n");
        }

        builder.append("Elapsed: ");
        builder.append(elapsed());

        return builder.toString();
    }
}
