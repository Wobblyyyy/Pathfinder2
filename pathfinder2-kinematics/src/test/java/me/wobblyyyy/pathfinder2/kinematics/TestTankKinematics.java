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

import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTankKinematics {
    private static final double TRACK_WIDTH = 2.0;
    private static final double TURN_COEFFICIENT = 1.0 / 90.0;

    private static TankKinematics kinematics(double turnCoefficient) {
        return new TankKinematics(turnCoefficient, TRACK_WIDTH);
    }

    private static TankKinematics kinematics() {
        return kinematics(TURN_COEFFICIENT);
    }

    private static void testToTankState(
        TankState expectedState,
        Translation translation
    ) {
        Assertions.assertEquals(
            expectedState,
            kinematics().calculate(translation)
        );
    }

    private static void testFromTankState(
        Translation expectedTranslation,
        TankState state
    ) {
        Assertions.assertEquals(
            expectedTranslation,
            kinematics().toTranslation(state)
        );
    }

    @Test
    public void testForwards() {
        TankState stateA = new TankState(1, 1);
        TankState stateB = stateA.multiply(0.5);

        Translation translationA = new Translation(0, 1, 0);
        Translation translationB = translationA.multiply(0.5);

        testToTankState(stateA, translationA);
        testToTankState(stateB, translationB);

        testFromTankState(translationA, stateA);
        testFromTankState(translationB, stateB);
    }

    @Test
    public void testBackwards() {
        TankState stateA = new TankState(-1, -1);
        TankState stateB = stateA.multiply(0.5);

        Translation translationA = new Translation(0, -1, 0);
        Translation translationB = translationA.multiply(0.5);

        testToTankState(stateA, translationA);
        testToTankState(stateB, translationB);

        testFromTankState(translationA, stateA);
        testFromTankState(translationB, stateB);
    }

    @Test
    public void testRightwards() {
        TankState stateA = new TankState(-1, 1);
        TankState stateB = stateA.multiply(0.5);
        TankState stateC = stateB.multiply(0.5);
        TankState stateD = stateC.multiply(0.5);

        Translation translationA = new Translation(1, 0, 0);
        Translation translationB = translationA.multiply(0.5);
        Translation translationC = translationB.multiply(0.5);
        Translation translationD = translationC.multiply(0.5);

        testToTankState(stateA, translationA);
        testToTankState(stateB, translationB);
        testToTankState(stateC, translationC);
        testToTankState(stateD, translationD);

        testFromTankState(new Translation(0, 0, -1), stateA);
        testFromTankState(new Translation(0, 0, -0.5), stateB);
        testFromTankState(new Translation(0, 0, -0.25), stateC);
        testFromTankState(new Translation(0, 0, -0.125), stateD);
    }

    @Test
    public void testLeftwards() {
        TankState stateA = new TankState(-1, 1).multiply(-1);
        TankState stateB = stateA.multiply(0.5);
        TankState stateC = stateB.multiply(0.5);
        TankState stateD = stateC.multiply(0.5);

        Translation translationA = new Translation(1, 0, 0).multiply(-1);
        Translation translationB = translationA.multiply(0.5);
        Translation translationC = translationB.multiply(0.5);
        Translation translationD = translationC.multiply(0.5);

        testToTankState(stateA, translationA);
        testToTankState(stateB, translationB);
        testToTankState(stateC, translationC);
        testToTankState(stateD, translationD);

        testFromTankState(new Translation(0, 0, 1), stateA);
        testFromTankState(new Translation(0, 0, 0.5), stateB);
        testFromTankState(new Translation(0, 0, 0.25), stateC);
        testFromTankState(new Translation(0, 0, 0.125), stateD);
    }

    @Test
    public void testTranslationFromTankStateTurnRightwards() {
        TankState stateA = new TankState(-1, 1);
        TankState stateB = stateA.multiply(0.5);
        TankState stateC = stateB.multiply(0.5);
        TankState stateD = stateC.multiply(0.5);

        Translation translationA = new Translation(0, 0, -1);
        Translation translationB = translationA.multiply(0.5);
        Translation translationC = translationB.multiply(0.5);
        Translation translationD = translationC.multiply(0.5);

        testFromTankState(translationA, stateA);
        testFromTankState(translationB, stateB);
        testFromTankState(translationC, stateC);
        testFromTankState(translationD, stateD);
    }

    @Test
    public void testTranslationFromTankStateTurnLeftwards() {
        TankState stateA = new TankState(1, -1);
        TankState stateB = stateA.multiply(0.5);
        TankState stateC = stateB.multiply(0.5);
        TankState stateD = stateC.multiply(0.5);

        Translation translationA = new Translation(0, 0, 1);
        Translation translationB = translationA.multiply(0.5);
        Translation translationC = translationB.multiply(0.5);
        Translation translationD = translationC.multiply(0.5);

        testFromTankState(translationA, stateA);
        testFromTankState(translationB, stateB);
        testFromTankState(translationC, stateC);
        testFromTankState(translationD, stateD);
    }

    @Test
    public void testTurning() {
        TankKinematics kinematics = kinematics();
        Logger.trace(
            () -> {
                kinematics.calculate(new Translation(0, 1, 0));
                kinematics.calculate(new Translation(0, 1, 0.5));
                kinematics.calculate(new Translation(1, 1, 0));
                kinematics.calculate(new Translation(1, 1, 0.5));
                kinematics.calculate(new Translation(1, 0, 0));
                kinematics.calculate(new Translation(1, 0, 0.5));
            }
        );
    }
}
