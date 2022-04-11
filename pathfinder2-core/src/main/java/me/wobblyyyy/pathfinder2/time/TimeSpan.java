/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.time;

import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A period of elapsed time.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class TimeSpan {
    private final double startTimeMs;
    private final double stopTimeMs;
    private final double totalTimeMs;

    /**
     * Create a new {@code TimeSpan}.
     *
     * @param startTimeMs the {@code TimeSpan}'s start time, in milliseconds.
     * @param stopTimeMs  the {@code TimeSpan}'s stop time, in milliseconds.
     */
    public TimeSpan(double startTimeMs, double stopTimeMs) {
        ValidationUtils.validate(startTimeMs, "startTimeMs");
        ValidationUtils.validate(stopTimeMs, "stopTimeMs");

        this.startTimeMs = startTimeMs;
        this.stopTimeMs = stopTimeMs;
        this.totalTimeMs = stopTimeMs - startTimeMs;

        if (totalTimeMs < 0) {
            throw new IllegalArgumentException(
                "Can not create a TimeSpan with a negative duration!"
            );
        }
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public TimeSpan(TimeSpan timeSpan) {
       this(timeSpan.startTimeMs, timeSpan.stopTimeMs);
    }

    public static TimeSpan elapsed(double startTimeMs, double elapsedTimeMs) {
        return new TimeSpan(startTimeMs, startTimeMs + elapsedTimeMs);
    }

    /**
     * Get the time at which the {@code TimeSpan} starts.
     *
     * @return the time at which the {@code TimeSpan} starts.
     */
    public double getStartTimeMs() {
        return startTimeMs;
    }

    /**
     * Get the time at which the {@code TimeSpan} stops.
     *
     * @return the time at which the {@code TimeSpan} stops.
     */
    public double getStopTimeMs() {
        return startTimeMs;
    }

    /**
     * Get the total amount of time represented by {@code this} {@code TimeSpan}, in milliseconds.
     *
     * @return the total amount of time represented by {@code this} {@code TimeSpan}, in milliseconds.
     */
    public double getTotalTimeMs() {
        return totalTimeMs;
    }

    /**
     * Add a {@code double} value to {@code this} by setting the
     * {@code stopTimeMs} value to its current value plus the provided
     * {@code timeToAddMs} parameter.
     *
     * @param timeToAddMs the time to add, in milliseconds.
     * @return an added {@code TimeSpan}.
     */
    public TimeSpan add(double timeToAddMs) {
        return new TimeSpan(
            startTimeMs,
            stopTimeMs + timeToAddMs
        );
    }

    /**
     * Add a {@code TimeSpan} to {@code this} by adding the provided
     * {@code TimeSpan}'s {@code totalTimeMs} value as the amount of time (in
     * milliseconds) to add to {@code this} {@code TimeSpan}'s end time.
     *
     * @param timeToAdd the {@code TimeSpan} to add.
     * @return the added {@code TimeSpan}s.
     */
    public TimeSpan add(TimeSpan timeToAdd) {
        return add(timeToAdd.totalTimeMs);
    }

    @Override
    public int hashCode() {
        return (int) ((startTimeMs - totalTimeMs) / 1_000);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeSpan) {
            TimeSpan t = (TimeSpan) obj;

            boolean sameStartTimeMs = Equals.soft(
                this.startTimeMs,
                t.startTimeMs,
                0.01
            );
            boolean sameStopTimeMs = Equals.soft(
                    this.stopTimeMs,
                    t.stopTimeMs,
                    0.01
            );

            return sameStartTimeMs && sameStopTimeMs;
        }

        return false;
    }

    @Override
    public String toString() {
        return StringUtils.format("%dms", totalTimeMs);
    }
}
