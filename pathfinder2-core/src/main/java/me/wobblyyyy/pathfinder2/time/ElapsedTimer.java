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
 * A very simple timer that keeps track of how much time has been elapsed.
 * This can be used to create time-based loops. As of Pathfinder v0.10.7,
 * the functionality of {@code ElapsedTimer} has been effectively
 * encapsulated in the {@link Time} class: {@link Time#runFor(double, Runnable)}
 *
 * <p>
 * As of Pathfinder v2.1.0, there are some new methods here:
 * <ul>
 *     <li>{@link #wait(double)}</li>
 *     <li>{@link #waitThenRun(Runnable, double)}</li>
 *     <li>{@link #runThenWait(Runnable, double)}</li>
 *     <li>{@link #runFor(Runnable, double)}</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.2.4
 */
public class ElapsedTimer {
    private long start = 0;

    /**
     * Create a new {@code ElapsedTimer}. The timer will NOT start
     * automatically.
     */
    public ElapsedTimer() {
        this(false);
    }

    /**
     * Create a new {@code ElapsedTimer}, and based on a boolean, potentially
     * start the timer.
     *
     * @param shouldStart should the timer start automatically? If this is
     *                    true, the timer will start automatically. If this
     *                    is false, the timer will not start automatically.
     */
    public ElapsedTimer(boolean shouldStart) {
        if (shouldStart) {
            start();
        }
    }

    /**
     * Wait for a certain amount of time then run a {@code Runnable}.
     *
     * @param runnable the runnable to run.
     * @param timeMs   how long to wait for, in milliseconds.
     */
    public static void waitThenRun(Runnable runnable, double timeMs) {
        wait(timeMs);
        runnable.run();
    }

    /**
     * Wait for a certain amount of time.
     *
     * @param timeMs   how long to wait for, in milliseconds.
     */
    public static void wait(double timeMs) {
        ElapsedTimer timer = new ElapsedTimer(true);
        while (timer.isElapsedLessThan(timeMs)) ;
    }

    /**
     * Run a {@code Runnable} then wait for a certain amount of time.
     *
     * @param runnable the runnable to run.
     * @param timeMs   how long to wait for, in milliseconds.
     */
    public static void runThenWait(Runnable runnable, double timeMs) {
        runnable.run();
        wait(timeMs);
    }

    /**
     * Run a {@code Runnable} for a certain amount of time (in milliseconds).
     * This will execute the {@code Runnable} repeatedly until the specified
     * amount of time has elapsed.
     *
     * @param runnable the runnable to run.
     * @param timeMs   how long to run the runnable for.
     */
    public static void runFor(Runnable runnable, double timeMs) {
        ElapsedTimer timer = new ElapsedTimer(true);
        while (timer.isElapsedLessThan(timeMs)) runnable.run();
    }

    /**
     * Start the timer.
     */
    public void start() {
        start = Time.longMs();
    }

    /**
     * Get the elapsed time, in milliseconds.
     *
     * @return the elapsed time, in milliseconds.
     */
    public long getElapsed() {
        long current = Time.longMs();

        return current - start;
    }

    /**
     * Get the elapsed time, in milliseconds.
     *
     * @return the elapsed time, in milliseconds.
     */
    public long elapsedMilliseconds() {
        return getElapsed();
    }

    /**
     * Get the elapsed time, in milliseconds.
     *
     * @return the elapsed time, in milliseconds.
     */
    public long elapsedMs() {
        return getElapsed();
    }

    public double elapsedSeconds() {
        return getElapsed() / 1_000d;
    }

    /**
     * Is the elapsed time greater than a provided value?
     *
     * <p>
     * This method is based on milliseconds.
     * </p>
     *
     * @param time the time value to compare (milliseconds).
     * @return whether or not the elapsed time is greater than the provided
     * time.
     */
    public boolean isElapsedMoreThan(long time) {
        return time >= getElapsed();
    }

    /**
     * Is the elapsed time less than a provided value?
     *
     * <p>
     * This method is based on milliseconds.
     * </p>
     *
     * @param time the time value to compare (milliseconds).
     * @return whether or not the elapsed time is less than the provided
     * time.
     */
    public boolean isElapsedLessThan(long time) {
        return time <= getElapsed();
    }

    /**
     * Is the elapsed time greater than a provided value?
     *
     * <p>
     * This method is based on milliseconds.
     * </p>
     *
     * @param time the time value to compare (milliseconds).
     * @return whether or not the elapsed time is greater than the provided
     * time.
     */
    public boolean isElapsedMoreThan(double time) {
        return time >= getElapsed();
    }

    /**
     * Is the elapsed time less than a provided value?
     *
     * <p>
     * This method is based on milliseconds.
     * </p>
     *
     * @param time the time value to compare (milliseconds).
     * @return whether or not the elapsed time is less than the provided
     * time.
     */
    public boolean isElapsedLessThan(double time) {
        return time <= getElapsed();
    }

    public boolean hasStarted() {
        return start != 0;
    }
}
