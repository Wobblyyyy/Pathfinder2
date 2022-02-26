/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.components;

import me.wobblyyyy.pathfinder2.utils.ArrayUtils;

import java.util.List;

/**
 * A wrapper that encapsulates several motors. This is most useful for
 * situations where two motors should be perfectly synchronized.
 *
 * @author Colin Robertson
 * @since 0.10.7
 */
public class MultiMotor implements Motor {
    private final List<Motor> motors;

    public MultiMotor(Motor... motors) {
        if (motors.length == 0)
            throw new IllegalArgumentException(
                    "Motors array may not have a length of 0!"
            );

        this.motors = ArrayUtils.toList(motors);
    }

    public void addMotor(Motor motor) {
        if (motor == null)
            throw new NullPointerException("Cannot add a null motor!");

        motors.add(motor);
    }

    public void addMotor(Motor motor,
                         boolean isInverted) {
        addMotor(motor.applyInversions(isInverted, isInverted));
    }

    public List<Motor> getMotors() {
        return motors;
    }

    @Override
    public double getPower() {
        double sum = 0;
        for (Motor motor : motors) sum += motor.getPower();
        return sum / motors.size();
    }

    @Override
    public void setPower(double power) {
        for (Motor motor : motors) motor.setPower(power);
    }
}
