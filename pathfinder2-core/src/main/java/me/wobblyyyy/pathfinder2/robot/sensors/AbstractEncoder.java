/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

import me.wobblyyyy.pathfinder2.time.Time;

/**
 * An abstract implementation of the {@code Encoder} interface. In order to
 * make use of this class, override the {@link #getRawTicks()} method, which
 * should return a raw tick reading from the encoder. You shouldn't modify
 * this value yourself - you should use this class to apply any modifications
 * you'd like to use to the encoder.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public abstract class AbstractEncoder implements Encoder {
    private int offset = 0;
    private int multiplier = 1;
    private int lastTicks = 0;
    private double lastMs;

    public abstract int getRawTicks();

    @Override
    public int getTicks() {
        lastTicks = (getRawTicks() + offset) * multiplier;
        return lastTicks;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void offsetSoPositionIs(int target) {
        this.offset = target - getRawTicks();
    }

    @Override
    public int getMultiplier() {
        return multiplier;
    }

    @Override
    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public double getVelocity() {
        double currentMs = Time.ms();
        if (lastMs == 0) lastMs = currentMs;
        double elapsedSeconds = (currentMs - lastMs) / 1_000;
        int previousTicks = lastTicks;
        int currentTicks = getTicks();
        int elapsedTicks = currentTicks - previousTicks;
        return elapsedTicks / elapsedSeconds;
    }
}
