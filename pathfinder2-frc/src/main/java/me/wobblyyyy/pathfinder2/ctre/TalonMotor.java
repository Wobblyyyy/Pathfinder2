/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.ctre;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

/**
 * Wrapper for {@link BaseTalon}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class TalonMotor extends AbstractMotor {
    private final BaseTalon talon;

    /**
     * Create a new {@code TalonMotor}.
     *
     * @param talon the {@link BaseTalon} to use.
     */
    public TalonMotor(BaseTalon talon) {
        super(
            power -> talon.set(ControlMode.PercentOutput, power),
            talon::getMotorOutputPercent
        );
        this.talon = talon;
    }

    /**
     * Create a new {@code TalonMotor}.
     *
     * @param deviceNumber the Talon's device number.
     * @param model        honestly, no idea. I just know that the
     *                     {@link BaseTalon} constructor requires it as a
     *                     parameter, so... yeah...
     */
    public TalonMotor(int deviceNumber, String model) {
        this(new BaseTalon(deviceNumber, model));
    }

    /**
     * Get the internal base talon.
     *
     * @return the internal base talon.
     */
    public BaseTalon getTalon() {
        return talon;
    }
}
