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
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrajectoryCommands {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testLinearTrajectory() {
        registry.execute("linearTrajectory", "10,10,45deg", "0.5", "2", "5deg");
        registry.execute("tickUntil");
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 10, 45),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }

    @Test
    public void testFastTrajectoryWithoutAutoInitial() {
        registry.execute("fastTrajectory", "0,0,0", "10,10,45deg", "0.5");
        registry.execute("tickUntil");
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 10, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(360)
        );
    }

    @Test
    public void testFastTrajectoryWithAutoInitial() {
        registry.execute("fastTrajectory", "10,10,45deg", "0.5");
        registry.execute("tickUntil");
        AssertionUtils.assertIsNear(
            new PointXYZ(10, 10, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(360)
        );
    }
}
