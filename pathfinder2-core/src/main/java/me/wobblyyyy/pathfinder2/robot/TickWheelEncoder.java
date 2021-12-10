/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.kinematics.EncoderConverter;
import me.wobblyyyy.pathfinder2.kinematics.EncoderTracker;

import java.util.function.Supplier;

public class TickWheelEncoder {
    private final EncoderTracker tracker;

    public TickWheelEncoder(EncoderTracker tracker) {
        this.tracker = tracker;
    }

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

    public double getSpeed() {
        return tracker.getSpeed();
    }
}
