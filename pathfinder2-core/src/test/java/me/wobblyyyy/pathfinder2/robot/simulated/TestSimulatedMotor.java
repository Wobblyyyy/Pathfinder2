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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSimulatedMotor {

    @Test
    public void testPower() {
        SimulatedMotor motor = new SimulatedMotor();

        double powerA = 0.0;
        double powerB = 1.0;
        double powerC = -1.0;
        double powerD = 0.5;
        double powerE = -0.5;

        motor.setPower(powerA);
        Assertions.assertEquals(powerA, motor.getPower());

        motor.setPower(powerB);
        Assertions.assertEquals(powerB, motor.getPower());

        motor.setPower(powerC);
        Assertions.assertEquals(powerC, motor.getPower());

        motor.setPower(powerD);
        Assertions.assertEquals(powerD, motor.getPower());

        motor.setPower(powerE);
        Assertions.assertEquals(powerE, motor.getPower());
    }

    @Test
    public void testInitialPower() {
        double initialPowerA = 0.0;
        double initialPowerB = 1.0;
        double initialPowerC = -1.0;

        Motor motorA = new SimulatedMotor(initialPowerA);
        Motor motorB = new SimulatedMotor(initialPowerB);
        Motor motorC = new SimulatedMotor(initialPowerC);

        Assertions.assertEquals(initialPowerA, motorA.getPower());
        Assertions.assertEquals(initialPowerB, motorB.getPower());
        Assertions.assertEquals(initialPowerC, motorC.getPower());
    }
}
