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

public class EncoderTracker {
    private final EncoderConverter converter;
    private final Supplier<Integer> getTicks;
    private final boolean isInverted;
    private int lastTicks = 0;
    private boolean hasUpdated = false;
    private long lastMs = 0;

    public EncoderTracker(EncoderConverter converter,
                          Supplier<Integer> getTicks,
                          boolean isInverted) {
        this.converter = converter;
        this.getTicks = getTicks;
        this.isInverted = isInverted;
    }

    public EncoderTracker(EncoderConverter converter,
                          Supplier<Integer> getTicks) {
        this(
                converter,
                getTicks,
                false
        );
    }

    public EncoderConverter getSpeedConverter() {
        return converter;
    }

    public double getSpeedWithTime(double elapsedSeconds) {
        int currentTicks = isInverted ? -getTicks.get() : getTicks.get();
        if (!hasUpdated) {
            hasUpdated = true;
            lastTicks = currentTicks;
        }
        int elapsedTicks = currentTicks - lastTicks;
        double elapsedDistance = converter.distanceFromTicks(elapsedTicks);
        return elapsedDistance / elapsedSeconds;
    }

    public double getSpeed() {
        long currentMs = System.currentTimeMillis();
        if (lastMs == 0) lastMs = currentMs;
        long elapsedMs = currentMs - lastMs;
        lastMs = currentMs;
        double elapsedSeconds = elapsedMs / 1_000d;
        return getSpeedWithTime(elapsedSeconds);
    }
}
