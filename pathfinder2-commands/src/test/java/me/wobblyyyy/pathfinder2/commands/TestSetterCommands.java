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
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSetterCommands {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testSetSpeed() {
        registry.execute("setSpeed", "0.1");
        AssertionUtils.assertSoftEquals(0.1, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.2");
        AssertionUtils.assertSoftEquals(0.2, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.3");
        AssertionUtils.assertSoftEquals(0.3, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.5");
        AssertionUtils.assertSoftEquals(0.5, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.6");
        AssertionUtils.assertSoftEquals(0.6, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.7");
        AssertionUtils.assertSoftEquals(0.7, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.8");
        AssertionUtils.assertSoftEquals(0.8, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "0.9");
        AssertionUtils.assertSoftEquals(0.9, pathfinder.getSpeed(), 0.01);

        registry.execute("setSpeed", "1.0");
        AssertionUtils.assertSoftEquals(1.0, pathfinder.getSpeed(), 0.01);
    }

    @Test
    public void testSetTolerance() {
        registry.execute("setTolerance", "0.1");
        AssertionUtils.assertSoftEquals(0.1, pathfinder.getTolerance(), 0.01);

        registry.execute("setTolerance", "0.2");
        AssertionUtils.assertSoftEquals(0.2, pathfinder.getTolerance(), 0.01);

        registry.execute("setTolerance", "0.3");
        AssertionUtils.assertSoftEquals(0.3, pathfinder.getTolerance(), 0.01);
    }

    @Test
    public void testSetAngleTolerance() {
        registry.execute("setAngleTolerance", "5 deg");
        AssertionUtils.assertEquals(
            Angle.fromDeg(5),
            pathfinder.getAngleTolerance()
        );

        registry.execute("setAngleTolerance", "5 rad");
        AssertionUtils.assertEquals(
            Angle.fromRad(5),
            pathfinder.getAngleTolerance()
        );
    }
}
