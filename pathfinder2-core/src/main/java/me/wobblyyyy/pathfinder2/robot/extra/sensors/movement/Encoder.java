/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.extra.sensors.movement;

/**
 * An interface for encoders.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public interface Encoder {
    /**
     * Get the current ticks reading of the encoder.
     *
     * @return the current ticks reading of the encoder.
     */
    int getTicks();

    /**
     * Get the encoder's offset.
     *
     * @return the encoder's offset.
     */
    int getOffset();

    /**
     * Set an offset to the encoder.
     *
     * @param offset an offset for the encoder.
     */
    void setOffset(int offset);

    /**
     * Apply an offset so that the current tick count is equal to the
     * provided target tick count.
     *
     * @param target the target tick count.
     */
    void offsetSoPositionIs(int target);

    /**
     * Get the encoder's multiplier.
     *
     * @return the encoder's multiplier.
     */
    int getMultiplier();

    /**
     * Set a multiplier for the encoder. All of the outputted tick values
     * should be multiplied by this value.
     *
     * @param multiplier a multiplier for the encoder.
     */
    void setMultiplier(int multiplier);

    /**
     * Get the velocity of the encoder, measured in ticks per second.
     *
     * @return the velocity of the encoder, measured in ticks per second.
     */
    double getVelocity();
}
