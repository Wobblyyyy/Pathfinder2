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
     * @param power the power value to set to the motor.
     */
    void setPower(double power);
}