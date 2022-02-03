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

/**
 * A wrapper that encapsulates several motors. This is most useful for
 * situations where two motors should be perfectly synchronized.
 *
 * @author Colin Robertson
 * @since 0.10.7
 */
public class MultiMotor implements Motor {
    private final Motor[] motors;

    public MultiMotor(Motor... motors) {
        if (motors.length == 0)
            throw new IllegalArgumentException(
                    "Motors array may not have a length of 0!"
            );

        this.motors = motors;
    }

    public Motor[] getMotors() {
        return motors;
    }

    @Override
    public double getPower() {
        double sum = 0;
        for (Motor motor : motors) sum += motor.getPower();
        return sum / motors.length;
    }

    @Override
    public void setPower(double power) {
        for (Motor motor : motors) motor.setPower(power);
    }
}
