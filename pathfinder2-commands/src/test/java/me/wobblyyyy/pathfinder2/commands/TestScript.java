/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.commands;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestScript {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testGoToScript() {
        Script script = Script.load(
            registry,
            "me/wobblyyyy/pathfinder2/commands/testGoTo.pf"
        );

        Assertions.assertFalse(pathfinder.isActive());
        script.execute();
        Assertions.assertTrue(pathfinder.isActive());
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        pathfinder.tickUntil();
        Assertions.assertFalse(pathfinder.isActive());
        AssertionUtils.assertIsNear(
            new PointXYZ(0, 0, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testSplineToScript() {
        Script script = Script.load(
            registry,
            "me/wobblyyyy/pathfinder2/commands/testSplineTo.pf"
        );

        script.execute();

        AssertionUtils.assertIsNear(
            new PointXYZ(20, 45, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testSetValuesAndSplineToScript() {
        pathfinder.setSpeed(0.2);
        pathfinder.setTolerance(0.1);
        pathfinder.setAngleTolerance(Angle.fromDeg(45));

        Script script = Script.load(
            registry,
            "me/wobblyyyy/pathfinder2/commands/testSetValuesAndSplineTo.pf"
        );

        script.execute();

        Assertions.assertEquals(0.5, pathfinder.getSpeed());
        Assertions.assertEquals(2, pathfinder.getTolerance());
        Assertions.assertEquals(
            Angle.fromDeg(5),
            pathfinder.getAngleTolerance()
        );

        AssertionUtils.assertIsNear(
            new PointXYZ(0, 0, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }
}
