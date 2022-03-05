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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBaseMotor {
    private Motor motor;

    @BeforeEach
    public void beforeEach() {
        motor =
            new BaseMotor() {
                private double power;

                @Override
                public void abstractSetPower(double power) {
                    this.power = power;
                }

                @Override
                public double abstractGetPower() {
                    return power;
                }
            };
    }

    @Test
    public void testBaseMotor() {
        motor.setPower(0.0);
        Assertions.assertEquals(0.0, motor.getPower());

        motor.setPower(0.5);
        Assertions.assertEquals(0.5, motor.getPower());

        motor.setPower(0.75);
        Assertions.assertEquals(0.75, motor.getPower());

        motor.setPower(1.0);
        Assertions.assertEquals(1.0, motor.getPower());
    }

    @Test
    public void testMaxPower() {
        motor.setPower(100);
        Assertions.assertEquals(1.0, motor.getPower());

        motor.setMax(10.0);
        motor.setPower(100);
        Assertions.assertEquals(10.0, motor.getPower());
    }

    @Test
    public void testMinPower() {
        motor.setPower(-100);
        Assertions.assertEquals(-1.0, motor.getPower());

        motor.setMin(-10.0);
        motor.setPower(-100);
        Assertions.assertEquals(-10.0, motor.getPower());
    }
}
