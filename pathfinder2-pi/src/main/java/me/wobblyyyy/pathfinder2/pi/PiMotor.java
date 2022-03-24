/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.pi;

import me.wobblyyyy.pathfinder2.arduino.ArduinoInterface;
import me.wobblyyyy.pathfinder2.arduino.ArduinoOutput;
import me.wobblyyyy.pathfinder2.robot.components.Motor;

public class PiMotor extends ArduinoOutput implements Motor {
    private double power;

    public PiMotor(ArduinoInterface arduino, String id) {
        super(arduino, id);
    }

    @Override
    public void setPower(double power) {
        super.write(power);

        this.power = power;
    }

    @Override
    public double getPower() {
        return this.power;
    }
}
