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

public class TestTankState {

    private static void testTankStateConstructor(double right, double left) {
        TankState state = new TankState(right, left);

        Assertions.assertEquals(right, state.right());
        Assertions.assertEquals(left, state.left());
    }

    @Test
    public void testGetRightAndLeftValuesFromTankState() {
        testTankStateConstructor(-1.0, -1.0);
        testTankStateConstructor(0.0, 0.0);
        testTankStateConstructor(1.0, 1.0);
        testTankStateConstructor(-1.0, 0.0);
        testTankStateConstructor(0.0, -1.0);
        testTankStateConstructor(-1.0, 1.0);
        testTankStateConstructor(1.0, -1.0);
    }

    private static void testAddTankStates(TankState a, TankState b) {
        double aRight = a.right();
        double aLeft = a.left();

        double bRight = b.right();
        double bLeft = b.left();

        TankState sum = new TankState(aRight + bRight, aLeft + bLeft);

        Assertions.assertEquals(sum, a.add(b));
    }

    private static void testAddTankStates(
        double aRight,
        double aLeft,
        double bRight,
        double bLeft
    ) {
        testAddTankStates(
            new TankState(aRight, aLeft),
            new TankState(bRight, bLeft)
        );
    }

    @Test
    public void testAddTankStates() {
        testAddTankStates(0, 0, 0, 0);
        testAddTankStates(1, 0, 1, 0);
        testAddTankStates(1, 0, 0, 0);
        testAddTankStates(0, 1, 0, 0);
        testAddTankStates(0, 0, 1, 0);
        testAddTankStates(0, 0, 0, 1);
        testAddTankStates(-1.0, -1.0, 1.0, 1.0);
    }
}
