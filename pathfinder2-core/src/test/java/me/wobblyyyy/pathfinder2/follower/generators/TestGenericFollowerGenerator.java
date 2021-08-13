/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.follower.generators;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.GenericTurnController;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGenericFollowerGenerator {
    @Test
    public void testGeneration() {
        Robot robot = new Robot(
                new SimulatedDrive(),
                new SimulatedOdometry()
        );

        Controller controller = new GenericTurnController(0.1);

        Trajectory trajectory = new LinearTrajectory(
                new PointXYZ(10, 10, 10),
                0.5,
                0.1,
                Angle.fromDeg(3)
        );

        GenericFollowerGenerator generator = new GenericFollowerGenerator(controller);

        Follower follower = generator.generate(robot, trajectory);

        Assertions.assertEquals(
                trajectory,
                follower.getTrajectory()
        );
    }
}
