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

/**
 * Convert encoder ticks into rotations and distance.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class EncoderConverter {
    private final double encoderCpr;
    private final double wheelCircumference;

    /**
     * Create a new {@code EncoderConverter}.
     *
     * @param encoderCountsPerRevolution the encoder's counts per revolution.
     * @param wheelCircumference         the circumference of the wheel that's
     *                                   attached to the encoder.
     */
    public EncoderConverter(double encoderCountsPerRevolution,
                            double wheelCircumference) {
        this.encoderCpr = encoderCountsPerRevolution;
        this.wheelCircumference = wheelCircumference;
    }

    public double getEncoderCpr() {
        return encoderCpr;
    }

    public double getWheelCircumference() {
        return wheelCircumference;
    }

    public double rotationsFromTicks(double ticks) {
        return ticks / encoderCpr;
    }

    public double distanceFromRotations(double rotations) {
        return rotations * wheelCircumference;
    }

    public double distanceFromTicks(double ticks) {
        return (ticks / encoderCpr) * wheelCircumference;
    }
}
