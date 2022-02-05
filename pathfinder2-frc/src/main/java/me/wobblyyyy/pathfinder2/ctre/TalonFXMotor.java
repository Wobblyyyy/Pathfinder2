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

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

/**
 * Wrapper for {@link TalonFX}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class TalonFXMotor extends AbstractMotor {
    private final TalonFX talon;

    /**
     * Create a new {@code TalonFXMotor}.
     *
     * @param talon the {@link TalonFX} to use.
     */
    public TalonFXMotor(TalonFX talon) {
        super(
                (power) -> talon.set(TalonFXControlMode.PercentOutput, power),
                talon::getMotorOutputPercent
        );

        this.talon = talon;
    }

    /**
     * Create a new {@code TalonFXMotor}.
     *
     * @param deviceNumber the Talon's device number.
     */
    public TalonFXMotor(int deviceNumber) {
        this(new TalonFX(deviceNumber));
    }

    /**
     * Get the internal TalonFX.
     *
     * @return the internal TalonFX.
     */
    public TalonFX getTalon() {
        return talon;
    }
}
