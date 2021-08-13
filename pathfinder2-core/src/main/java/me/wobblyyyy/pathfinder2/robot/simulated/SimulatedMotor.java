/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.robot.components.Motor;

/**
 * A virtual motor - this doesn't actually do anything with a real motor,
 * it just provides a way to play around with motors.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SimulatedMotor implements Motor {
    /**
     * The motor's power - defaults to 0.0.
     */
    private double power = 0.0;

    /**
     * Create a new SimulatedMotor.
     */
    public SimulatedMotor() {

    }

    /**
     * Create a new SimulatedMotor with an initial power value.
     *
     * @param initialPower the motor's initial power.
     */
    public SimulatedMotor(double initialPower) {
        this.power = initialPower;
    }

    /**
     * Get the motor's power.
     *
     * @return the motor's current power.
     */
    @Override
    public double getPower() {
        return this.power;
    }

    /**
     * Set the motor's power.
     *
     * @param power the power value to set to the motor.
     */
    @Override
    public void setPower(double power) {
        this.power = power;
    }
}
