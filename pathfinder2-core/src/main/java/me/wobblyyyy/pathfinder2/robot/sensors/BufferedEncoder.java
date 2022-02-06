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

import me.wobblyyyy.pathfinder2.math.RollingAverage;

/**
 * An encoder wrapper that utilizes a moving average to smoothen values.
 *
 * @author Colin Robertson
 * @since 0.8.0
 */
public class BufferedEncoder implements Encoder {
    private final Encoder encoder;
    private final RollingAverage average;

    /**
     * Create a new {@code BufferedEncoder}.
     *
     * @param encoder    the encoder that will be wrapped.
     * @param bufferSize the size of the buffer. A larger buffer means it will
     *                   take longer for the velocity to change but makes the
     *                   change smoother, while a smaller buffer means change
     *                   will occur more quickly but be more rough.
     */
    public BufferedEncoder(Encoder encoder,
                           int bufferSize) {
        this.encoder = encoder;
        this.average = new RollingAverage(bufferSize);
    }

    @Override
    public int getTicks() {
        return encoder.getTicks();
    }

    @Override
    public int getOffset() {
        return encoder.getOffset();
    }

    @Override
    public void setOffset(int offset) {
        encoder.setOffset(offset);
    }

    @Override
    public void offsetSoPositionIs(int target) {
        encoder.offsetSoPositionIs(target);
    }

    @Override
    public int getMultiplier() {
        return encoder.getMultiplier();
    }

    @Override
    public void setMultiplier(int multiplier) {
        encoder.setMultiplier(multiplier);
    }

    @Override
    public double getVelocity() {
        return average.add(encoder.getVelocity());
    }
}
