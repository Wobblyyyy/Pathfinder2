/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.components;

/**
 * A (very simple) motor interface. For almost all purposes, it's generally
 * a better idea to use a more abstract implementation of this interface. In
 * Pathfinder, motors should only have power values between -1 and 1. A
 * motor should be capable of recieving an input via {@link #setPower(double)}
 * and then spinning according to that input.
 *
 * @author Colin Robertson
 * @see AbstractMotor
 * @since 0.0.0
 */
public interface Motor {
    /**
     * Get a power value from the motor. This method should return whatever
     * power the motor is currently operating at.
     *
     * <p>
     * In some cases, it may be preferable to store a value inside of the
     * program instead of polling the motor to determine the power it's
     * operating at. This can preserve some CPU cycles, but to be honest,
     * the difference is minimal.
     * </p>
     *
     * @return the motor's current power.
     */
    double getPower();

    /**
     * Set power to the motor.
     *
     * <p>
     * Typically, this power value should fit within the range of -1.0 to
     * +1.0. This isn't required, but it's common enough I feel I should make
     * a note of it.
     * </p>
     *
     * @param power the power value to set to the motor. Conventionally, this
     *              value should be between -1 and 1, representing full speed
     *              reverse and full speed forwards, respectively.
     */
    void setPower(double power);

    /**
     * Create a new motor with inversion options added.
     *
     * @param isSetPowerInverted should all power values inputted to the
     *                           motor be multiplied by -1?
     * @param isGetPowerInverted should all power values outputted by the
     *                           motor be multiplied by -1?
     * @return a new {@code Motor}.
     */
    default Motor applyInversions(
        boolean isSetPowerInverted,
        boolean isGetPowerInverted
    ) {
        return new AbstractMotor(
            this::setPower,
            this::getPower,
            isSetPowerInverted,
            isGetPowerInverted
        );
    }

    /**
     * Invert the motor by applying an inversion to both the
     * {@link #setPower(double)} and {@link #getPower()} methods. This method
     * calls {@link applyInversions(boolean, boolean)} with both parameters
     * set to true.
     *
     * @return a new {@code Motor}.
     */
    default Motor invert() {
        return applyInversions(true, true);
    }

    /**
     * Invert the motor by applying an inversion to both the
     * {@link #setPower(double)} and {@link #getPower()} methods. This method
     * calls {@link applyInversions(boolean, boolean)} with both parameters
     * set to {@code isInverted}.
     *
     * @param isInverted should the motor be inverted?
     * @return a new {@code Motor}.
     */
    default Motor invert(boolean isInverted) {
        return applyInversions(isInverted, isInverted);
    }

    default Motor invertSetPower() {
        return applyInversions(true, false);
    }

    default Motor invertGetPower() {
        return applyInversions(false, true);
    }

    default Motor applyMultipliers(
        double setPowerMultiplier,
        double getPowerMultiplier
    ) {
        return new AbstractMotor(
            power -> {
                setPower(power * setPowerMultiplier);
            },
            () -> {
                return getPower() * getPowerMultiplier;
            }
        );
    }

    default Motor applySetPowerMultiplier(double setPowerMultiplier) {
        return applyMultipliers(setPowerMultiplier, 1);
    }

    default Motor applyGetPowerMultiplier(double getPowerMultiplier) {
        return applyMultipliers(1, getPowerMultiplier);
    }

    default Motor applyMultiplier(double multiplier) {
        return applyMultipliers(multiplier, 1 / multiplier);
    }

    /**
     * Convert this {@code Motor} to a {@code AbstractMotor}. If {@code this}
     * motor is an instance of {@code AbstractMotor}, this method will simply
     * cast {@code this} to {@code AbstractMotor}. If {@code this} is not
     * an instance of {@code AbstractMotor}, this will create a new
     * {@code AbstractMotor}.
     *
     * @return {@code this}, represented as an abstract motor. If
     * {@code this} is already an {@code AbstractMotor}, this will simply
     * cast this to an {@code AbstractMotor}.
     */
    default AbstractMotor toAbstractMotor() {
        if (this instanceof AbstractMotor) return (AbstractMotor) this;

        return new AbstractMotor(this::setPower, this::getPower);
    }

    /**
     * Convert this {@code Motor} into a {@code AbstractMotor} and apply
     * a deadband to the new motor.
     *
     * @param deadband the deadband to apply to the motor.
     * @return {@code this}, represented as an abstract motor. This will
     * also have a deadband applied to it.
     */
    default BaseMotor deadband(double deadband) {
        return toAbstractMotor().deadband(deadband);
    }

    /**
     * Convert {@code this} to an {@link AbstractMotor} and call
     * {@link AbstractMotor#setMin(double)}.
     *
     * @param min the value to apply.
     * @return {@code this}, as an {@link AbstractMotor}.
     */
    default BaseMotor setMin(double min) {
        return toAbstractMotor().setMin(min);
    }

    /**
     * Convert {@code this} to an {@link AbstractMotor} and call
     * {@link AbstractMotor#setMax(double)}.
     *
     * @param max the value to apply.
     * @return {@code this}, as an {@link AbstractMotor}.
     */
    default BaseMotor setMax(double max) {
        return toAbstractMotor().setMax(max);
    }
}
