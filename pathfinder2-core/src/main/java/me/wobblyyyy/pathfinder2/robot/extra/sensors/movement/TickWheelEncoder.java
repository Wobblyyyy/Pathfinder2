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

import me.wobblyyyy.pathfinder2.kinematics.EncoderConverter;
import me.wobblyyyy.pathfinder2.kinematics.EncoderTracker;

import java.util.function.Supplier;

/**
 * Used in tracking the speed of an encoder that outputs ticks.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class TickWheelEncoder {
    private final EncoderTracker tracker;

    /**
     * Create a new {@code TickWheelEncoder} by using an
     * {@link EncoderTracker}.
     *
     * @param tracker the tracker this {@code TickWheelEncoder} will be
     *                based on.
     */
    public TickWheelEncoder(EncoderTracker tracker) {
        this.tracker = tracker;
    }

    /**
     * Create a new {@code TickWheelEncoder}.
     *
     * @param getTicks                   a supplier that returns how many
     *                                   ticks the encoder is at.
     * @param encoderCountsPerRevolution how many ticks the encoder will
     *                                   output for every complete revolution.
     * @param wheelCircumference         the circumference of the wheel.
     */
    public TickWheelEncoder(Supplier<Integer> getTicks,
                            double encoderCountsPerRevolution,
                            double wheelCircumference) {
        this(
                new EncoderTracker(
                        new EncoderConverter(
                                encoderCountsPerRevolution,
                                wheelCircumference
                        ),
                        getTicks
                )
        );
    }

    /**
     * Get the speed of the encoder. This can be positive or negative. I know,
     * I know, all of the physics nerds out there aren't going to like the
     * fact that speed can be negative... but... deal with it.
     *
     * @return the speed of the encoder.
     */
    public double getSpeed() {
        return tracker.getSpeed();
    }
}
