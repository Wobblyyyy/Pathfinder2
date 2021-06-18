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
 * Different units of time. Very cool, right?
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public enum TimeUnit {
    /**
     * Milliseconds.
     */
    MS(1),

    /**
     * Seconds.
     */
    S(1000),

    /**
     * Minutes.
     */
    M(60000),

    /**
     * Hours.
     */
    H(3600000);

    /**
     * How many milliseconds is this time unit worth?
     */
    private final double timeInMs;

    /**
     * Create a new time unit.
     *
     * @param timeInMs the amount of time, in milliseconds, that one unit
     *                 of this time represents.
     */
    TimeUnit(double timeInMs) {
        this.timeInMs = timeInMs;
    }

    /**
     * Get the amount of milliseconds one unit of this time represents.
     *
     * @return the unit represented in milliseconds.
     */
    public double getTimeInMs() {
        return timeInMs;
    }
}
