/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.execution;

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.GenericTurnController;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.generators.GenericFollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestExecutorManager {

    @Test
    public void testExecutorManager() {
        SimulatedOdometry odometry = new SimulatedOdometry();
        SimulatedDrive drive = new SimulatedDrive();
        Robot robot = new Robot(drive, odometry);
        Trajectory trajectory = new LinearTrajectory(
            new PointXYZ(10, 10, 0),
            1.0,
            0.1,
            Angle.fromDeg(3)
        );
        Controller controller = new GenericTurnController(0.1);
        GenericFollowerGenerator generator = new GenericFollowerGenerator(
            controller
        );
        Follower follower = generator.generate(robot, trajectory);
        List<Follower> list = new ArrayList<Follower>() {

            {
                add(follower);
            }
        };

        FollowerExecutor executor = new FollowerExecutor(odometry, drive, list);

        Assertions.assertFalse(executor.tick());

        ExecutorManager manager = new ExecutorManager(robot);

        Assertions.assertFalse(manager.isActive());

        manager.addExecutor(list);

        Assertions.assertTrue(manager.isActive());
        Assertions.assertFalse(manager.isInactive());

        Assertions.assertFalse(manager.tick());

        odometry.setRawPosition(new PointXYZ(10, 10, 0));

        Assertions.assertEquals(1, manager.howManyExecutors());

        Assertions.assertTrue(manager.tick());

        Assertions.assertFalse(manager.isActive());
        Assertions.assertTrue(manager.isInactive());
    }

    @Test
    public void testExecutorManager2() {
        SimulatedOdometry odometry = new SimulatedOdometry();
        SimulatedDrive drive = new SimulatedDrive();
        Robot robot = new Robot(drive, odometry);
        Trajectory trajectory1 = new LinearTrajectory(
            new PointXYZ(10, 10, 0),
            1.0,
            0.1,
            Angle.fromDeg(3)
        );
        Trajectory trajectory2 = new LinearTrajectory(
            new PointXYZ(20, 20, 45),
            1.0,
            0.1,
            Angle.fromDeg(3)
        );
        Trajectory trajectory3 = new LinearTrajectory(
            new PointXYZ(30, 30, 90),
            1.0,
            0.1,
            Angle.fromDeg(3)
        );
        Controller controller = new GenericTurnController(0.1);
        GenericFollowerGenerator generator = new GenericFollowerGenerator(
            controller
        );
        Follower follower1 = generator.generate(robot, trajectory1);
        Follower follower2 = generator.generate(robot, trajectory2);
        Follower follower3 = generator.generate(robot, trajectory3);
        List<Follower> followers = new ArrayList<Follower>() {

            {
                add(follower1);
                add(follower2);
                add(follower3);
            }
        };

        ExecutorManager manager = new ExecutorManager(robot);

        manager.addExecutor(followers);

        manager.tick();
        Assertions.assertTrue(manager.isActive());

        odometry.setRawPosition(new PointXYZ(10, 10, 0));
        manager.tick();
        Assertions.assertTrue(manager.isActive());

        odometry.setRawPosition(new PointXYZ(20, 20, 30));
        manager.tick();
        Assertions.assertTrue(manager.isActive());

        odometry.setRawPosition(new PointXYZ(20, 20, 45));
        manager.tick();
        Assertions.assertTrue(manager.isActive());

        odometry.setRawPosition(new PointXYZ(30, 30, 45));
        manager.tick();
        Assertions.assertTrue(manager.isActive());

        odometry.setRawPosition(new PointXYZ(30, 30, 90));
        manager.tick();
        Assertions.assertFalse(manager.isActive());
    }
}
