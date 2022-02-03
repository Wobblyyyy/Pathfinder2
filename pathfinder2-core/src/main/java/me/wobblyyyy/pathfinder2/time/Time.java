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
 * Utilities related to time. This includes convenience methods to get
 * the system's current time, as well as convenience methods to calculate
 * a timestamp based on the current timestamp - for example, check out
 * {@link #secondsFromNow(double)}.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Time {
    private Time() {

    }

    /**
     * Get the system's current time in milliseconds.
     *
     * @return the system's current time in milliseconds.
     */
    public static double ms() {
        return (double) System.currentTimeMillis();
    }

    /**
     * Get the system's current time in milliseconds.
     *
     * @return the system's current time in milliseconds.
     */
    public static long longMs() {
        return System.currentTimeMillis();
    }

    /**
     * Get the system's current time, in seconds.
     *
     * @return the system's current time, in seconds.
     */
    public static double seconds() {
        return ms() / 1_000;
    }

    /**
     * Get the system's current time, in seconds.
     *
     * @return the system's current time, in seconds.
     */
    public static long longSeconds() {
        return longMs() / 1_000;
    }

    /**
     * Convert between two different units of time.
     *
     * @param value    the value represented in original units.
     * @param original the original unit (to convert from).
     * @param target   the target unit (to convert to).
     * @return a converted measurement of time measured in target units.
     */
    public static double convert(double value,
                                 TimeUnit original,
                                 TimeUnit target) {
        double originalMs = original.getTimeInMs();
        double targetMs = target.getTimeInMs();

        return (value / originalMs) * value;
    }

    /**
     * Get a time, in milliseconds, that's TIME units away from now.
     *
     * @param time the amount of time in units.
     * @param unit the unit the time parameter is measured in.
     * @return a time, in milliseconds, that's TIME units away from now.
     */
    public static double fromNow(double time,
                                 TimeUnit unit) {
        double targetMs = unit.getTimeInMs() * time;

        return Time.ms() + time;
    }

    /**
     * @param time milliseconds.
     * @return time from now, milliseconds.
     * @see Time#fromNow(double, TimeUnit)
     */
    public static double millisecondsFromNow(double time) {
        return fromNow(time, TimeUnit.MS);
    }

    /**
     * @param time seconds.
     * @return time from now, milliseconds.
     * @see Time#fromNow(double, TimeUnit)
     */
    public static double secondsFromNow(double time) {
        return fromNow(time, TimeUnit.S);
    }

    /**
     * @param time minutes.
     * @return time from now, milliseconds.
     * @see Time#fromNow(double, TimeUnit)
     */
    public static double minutesFromNow(double time) {
        return fromNow(time, TimeUnit.M);
    }

    /**
     * @param time hours.
     * @return time from now, milliseconds.
     * @see Time#fromNow(double, TimeUnit)
     */
    public static double hoursFromNow(double time) {
        return fromNow(time, TimeUnit.H);
    }

    /**
     * Execute a certain piece of functionality repeatedly as long as the
     * elapsed time (in milliseconds) is less than the provided maximum time
     * (also in milliseconds). This is a blocking method and will call the
     * {@code Runnable}'s {@link Runnable#run()} method as often as possible.
     *
     * @param durationMs how long, in milliseconds, the provided
     *                   {@code Runnable} should run for.
     * @param runnable   functionality that will be executed repeatedly for
     *                   {@code durationMs}.
     */
    public static void runFor(double durationMs,
                              Runnable runnable) {
        ElapsedTimer timer = new ElapsedTimer(true);

        while (timer.elapsedMs() <= durationMs)
            runnable.run();
    }
}
