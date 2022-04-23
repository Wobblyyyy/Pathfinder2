/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import java.util.function.Supplier;

/**
 * An {@code EncoderTracker} tracks the position of a tick-based encoder by
 * storing the current and last tick values, as well as the elapsed time,
 * in milliseconds. This allows for the speed of the encoder to be calculated.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class EncoderTracker {
    private final EncoderConverter converter;
    private final Supplier<Integer> getTicks;
    private final boolean isInverted;
    private int lastTicks = 0;
    private boolean hasUpdated = false;
    private long lastMs = 0;

    /**
     * Create a new {@code EncoderTracker}.
     *
     * @param converter  an {@link EncoderConverter} that will convert ticks
     *                   to rotations and distance.
     * @param getTicks   a supplier that returns the encoder's outputted
     *                   tick value.
     * @param isInverted is the encoder inverted? An inverted encoder will
     *                   function exactly the same as a non-inverted encoder:
     *                   the only difference is that an inverted encoder has
     *                   all of its values multiplied by -1.
     */
    public EncoderTracker(
        EncoderConverter converter,
        Supplier<Integer> getTicks,
        boolean isInverted
    ) {
        this.converter = converter;
        this.getTicks = getTicks;
        this.isInverted = isInverted;
    }

    /**
     * Create a new {@code EncoderTracker}.
     *
     * @param converter  an {@link EncoderConverter} that will convert ticks
     *                   to rotations and distance.
     * @param getTicks   a supplier that returns the encoder's outputted
     *                   tick value.
     */
    public EncoderTracker(
        EncoderConverter converter,
        Supplier<Integer> getTicks
    ) {
        this(converter, getTicks, false);
    }

    public EncoderConverter getSpeedConverter() {
        return converter;
    }

    /**
     * Get the speed of the encoder based on an amount of elapsed milliseconds.
     * It's strongly encouraged that you use the {@link #getSpeed()} method
     * instead.
     *
     * @param elapsedSeconds how many seconds have elapsed since the last
     *                       time this method was called.
     * @return the speed of the encoder.
     */
    public double getSpeedWithTime(double elapsedSeconds) {
        int currentTicks = isInverted ? -getTicks.get() : getTicks.get();
        int elapsedTicks = currentTicks - lastTicks;
        lastTicks = currentTicks;
        double elapsedDistance = converter.distanceFromTicks(elapsedTicks);
        return elapsedDistance / elapsedSeconds;
    }

    /**
     * Get the speed of the encoder.
     *
     * @return the speed of the encoder.
     */
    public double getSpeed() {
        long currentMs = System.currentTimeMillis();
        if (lastMs == 0) lastMs = currentMs;
        long elapsedMs = currentMs - lastMs;
        lastMs = currentMs;
        double elapsedSeconds = elapsedMs / 1_000d;
        return getSpeedWithTime(elapsedSeconds);
    }
}
