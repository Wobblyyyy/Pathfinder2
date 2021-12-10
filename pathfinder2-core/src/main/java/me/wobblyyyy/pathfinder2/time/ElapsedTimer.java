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

    public double elapsedSeconds() {
        return getElapsed() / 1_000d;
    }

    /**
     * Is the elapsed time greater than a provided value?
     *
     * @param time the time value to compare.
     * @return whether or not the elapsed time is greater than the provided
     * time.
     */
    public boolean isElapsedMoreThan(long time) {
        return time >= getElapsed();
    }

    /**
     * Is the elapsed time less than a provided value?
     *
     * @param time the time value to compare.
     * @return whether or not the elapsed time is less than the provided
     * time.
     */
    public boolean isElapsedLessThan(long time) {
        return time <= getElapsed();
    }

    /**
     * Is the elapsed time greater than a provided value?
     *
     * @param time the time value to compare.
     * @return whether or not the elapsed time is greater than the provided
     * time.
     */
    public boolean isElapsedMoreThan(double time) {
        return time >= getElapsed();
    }

    /**
     * Is the elapsed time less than a provided value?
     *
     * @param time the time value to compare.
     * @return whether or not the elapsed time is less than the provided
     * time.
     */
    public boolean isElapsedLessThan(double time) {
        return time <= getElapsed();
    }
}
