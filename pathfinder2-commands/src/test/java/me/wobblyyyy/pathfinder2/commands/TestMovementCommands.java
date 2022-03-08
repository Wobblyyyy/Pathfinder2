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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMovementCommands {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testGoToWithPointXY() {
        registry.execute("goTo", "10, ", "0");
        pathfinder.tickUntil(1_000);
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 0, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );

        registry.execute("goTo", "0, 10");
        pathfinder.tickUntil(1_000);
        AssertionUtils.assertIsNear(
            new PointXYZ(0, 10, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );

        registry.execute("goTo", "10, 10");
        pathfinder.tickUntil(1_000);
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 10, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testGoToWithPointXYZ() {
        registry.execute("goTo", "10, ", "0,", "45 deg");
        pathfinder.tickUntil(100);
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 0, 45),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );

        registry.execute("goTo", "0, 10, 90");
        pathfinder.tickUntil(100);
        AssertionUtils.assertIsNear(
            new PointXYZ(0, 10, 90),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );

        registry.execute("goTo", "10, 10, 10 deg");
        pathfinder.tickUntil(100);
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 10, 10),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testNonTurningSplineTo() {
        registry.execute("splineTo", "0,0,0", "5,10,0", "10,15,0", "15,25,0");
        pathfinder.tickUntil(100);
        AssertionUtils.assertIsNear(
            new PointXYZ(15, 25, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testTurningSplineTo() {
        registry.execute(
            "splineTo",
            "0,0,15",
            "5,10,30",
            "10,15,45",
            "15,25,60"
        );
        pathfinder.tickUntil(100);
        AssertionUtils.assertIsNear(
            new PointXYZ(15, 25, 60),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testSplineToWithParameters() {
        Logger.debug(
            () -> {
                registry.execute(
                    "splineTo",
                    "0.5",
                    "2",
                    "5 deg",
                    "0,0,15",
                    "5,10,30",
                    "10,15,45",
                    "15,25,60"
                );
                pathfinder.tickUntil(100);
                AssertionUtils.assertIsNear(
                    new PointXYZ(15, 25, 60),
                    pathfinder.getPosition(),
                    2,
                    Angle.fromDeg(5)
                );
            }
        );
    }
}
