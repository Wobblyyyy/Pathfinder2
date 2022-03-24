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

/**
 * An interface for encoders. If you'd like to track the angle an encoder
 * is facing, check out {@link AngleEncoder} instead. An encoder should report
 * a quantity (named "ticks") that represents how far the motor has spun since
 * tracking the position of the motor has begun. If a motor were to spin
 * forwards for 10 seconds, and then spin backwards for 10 seconds, at the same
 * speed (in both directions), assuming the motor and encoder are both
 * completely accurate, the encoder should have a position of 0. If it were to
 * spin forwards for 10 seconds, it should have a position double that of if it
 * were to spin forwards for 5 seconds.
 *
 * <p>
 * For most use cases, it's advisable to use {@link AbstractEncoder} instead.
 * </p>
 *
 * @author Colin Robertson
 * @see AngleEncoder
 * @see AbstractEncoder
 * @since 0.5.0
 */
public interface Encoder {
    /**
     * Get the current ticks reading of the encoder. This should factor in
     * the encoder's offset, as specified by {@link #setOffset(int)}.
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
