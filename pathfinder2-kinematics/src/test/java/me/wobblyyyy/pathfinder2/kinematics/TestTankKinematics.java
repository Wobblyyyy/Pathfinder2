/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.geometry.Translation;

public class TestTankKinematics {
    private static final double TRACK_WIDTH = 2.0;
    private static final double TURN_COEFFICIENT = 1.0 / 90.0;

    private static TankKinematics kinematics(double turnCoefficient) {
        return new TankKinematics(turnCoefficient, TRACK_WIDTH);
    }

    private static TankKinematics kinematics() {
        return kinematics(TURN_COEFFICIENT);
    }

    private static TankState getTankState(Translation translation) {
        return kinematics().calculate(translation);
    }

    private static void testToTankState(
        TankState expectedState,
        Translation translation
    ) {
        Assertions.assertEquals(
            expectedState,
            getTankState(translation)
        );
    }

    @Test
    public void testForwards() {
        testToTankState(new TankState(1, 1), new Translation(0, 1, 0));
        testToTankState(new TankState(0.5, 0.5), new Translation(0, 0.5, 0));
    }

    @Test
    public void testBackwards() {
        testToTankState(new TankState(-1, -1), new Translation(0, -1, 0));
        testToTankState(new TankState(-0.5, -0.5), new Translation(0, -0.5, 0));
    }

    @Test
    public void testRightwards() {
        testToTankState(new TankState(-1, 1), new Translation(1, 0, 0));
        testToTankState(new TankState(-0.5, 0.5), new Translation(0.5, 0, 0));
    }

    @Test
    public void testLeftwards() {
        testToTankState(new TankState(1, -1), new Translation(-1, 0, 0));
        testToTankState(new TankState(0.5, -0.5), new Translation(-0.5, 0, 0));
    }
}
