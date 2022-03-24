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
 * motor should be capable of receiving an input via {@link #setPower(double)}
 * and then spinning according to that input.
 *
 * <p>
 * A "power value" is defined such that the motor will free spin at the same
 * velocity if given the same power. For example, if a given motor were to
 * spin at 100 RPM when given a power value of 1.0, it should always spin at
 * (or at least very close to) 100 RPM when given that same power value.
 * Furthermore, an ideal motor should spin at a speed directly proportional
 * to the power value inputted. A power value of -1.0 should cause the motor
 * to spin at -100 RPM. Likewise:
 * <ul>
 *     <li>-0.5 power/speed: -50 RPM</li>
 *     <li>0.0 power/speed: 0 RPM</li>
 *     <li>0.5 power/speed: RPM</li>
 * </ul>
 * </p>
 *
 * <p>
 * Unless the underlying motor implementation is capable of reading a power
 * (or speed) value from the motor hardware itself, the {@link #getPower()}
 * method should return the last power value that was set to the motor.
 * Note that this method SHOULD NOT be used for positional tracking purposes.
 * </p>
 *
 * @author Colin Robertson
 * @see AbstractMotor
 * @see BaseMotor
 * @since 0.0.0
 */
public interface Motor {
    /**
     * Get a power value from the motor. This method should return whatever
     * power the motor is currently operating at. In most cases, this should
     * return the last power value that was set to the motor.
     *
     * <p>
     * This method SHOULD NOT be used for positional tracking purposes. The
     * accuracy of using motor power for tracking a robot's position is so
     * limited that you'd likely be better off guessing.
     * </p>
     *
     * @return the motor's current power. Frequently, this will be the last
     * power value that was set to the motor.
     */
    double getPower();

    /**
     * Set power to the motor.
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
     * calls {@link #applyInversions(boolean, boolean)} with both parameters
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
     * calls {@link #applyInversions(boolean, boolean)} with both parameters
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
            power -> setPower(power * setPowerMultiplier),
            () -> getPower() * getPowerMultiplier
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
