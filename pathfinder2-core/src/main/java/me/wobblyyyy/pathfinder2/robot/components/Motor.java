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
 * a better idea to use a more abstract implementation of this interface.
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
    default Motor applyInversions(boolean isSetPowerInverted,
                                  boolean isGetPowerInverted) {
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

    default Motor invertSetPower() {
        return applyInversions(true, false);
    }

    default Motor invertGetPower() {
        return applyInversions(false, true);
    }

    default Motor applyMultipliers(double setPowerMultiplier,
                                   double getPowerMultiplier) {
        return new AbstractMotor(
                (power) -> {
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

    default AbstractMotor toAbstractMotor() {
        return new AbstractMotor(this::setPower, this::getPower);
    }
}
