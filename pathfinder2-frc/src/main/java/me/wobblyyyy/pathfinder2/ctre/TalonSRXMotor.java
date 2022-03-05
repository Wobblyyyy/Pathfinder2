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

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

/**
 * Wrapper for {@link TalonSRX}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class TalonSRXMotor extends AbstractMotor {
    private final TalonSRX talon;

    /**
     * Create a new {@code TalonSRXMotor}.
     *
     * @param talon the {@link TalonSRX} to use.
     */
    public TalonSRXMotor(TalonSRX talon) {
        super(
            power -> talon.set(TalonSRXControlMode.PercentOutput, power),
            talon::getMotorOutputPercent
        );
        this.talon = talon;
    }

    /**
     * Create a new {@code TalonSRXMotor}.
     *
     * @param deviceNumber the Talon's device number.
     */
    public TalonSRXMotor(int deviceNumber) {
        this(new TalonSRX(deviceNumber));
    }

    /**
     * Get the internal Talon SRX.
     *
     * @return the internal Talon SRX.
     */
    public TalonSRX getTalon() {
        return talon;
    }
}
