/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

public class TestSimulatedChassis {
    private SimulatedDrive drive;
    private SimulatedOdometry odometry;
    private SimulatedWrapper wrapper;
    private Robot robot;
    private Controller turnController;
    private Pathfinder pathfinder;

    @BeforeEach
    public void beforeEach() {
        wrapper = new SimulatedWrapper(new SimulatedDrive(), new SimulatedOdometry());
        drive = wrapper.getDrive();
        odometry = wrapper.getOdometry();
        robot = wrapper.getRobot();
        turnController = new ProportionalController(-0.05);
        pathfinder = new Pathfinder(robot, turnController);
    }

    @Test
    public void testStraightMovement() {
        pathfinder.followTrajectory(new LinearTrajectory(
                new PointXYZ(10, 10, 0),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        pathfinder.tickUntil();
    }

    @Test
    public void testZeroPointTurn() {
        pathfinder.followTrajectory(new LinearTrajectory(
                new PointXYZ(0, 0, 10),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        pathfinder.tickUntil();
    }

    @Test
    public void testMovingTurn() {
        pathfinder.followTrajectory(new LinearTrajectory(
                new PointXYZ(10, 10, 10),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        pathfinder.tickUntil();
    }

    @Test
    public void testCompleteRotation() {
        pathfinder.followTrajectory(new LinearTrajectory(
                new PointXYZ(10, 10, 270),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        pathfinder.tickUntil();
    }

    @Test
    public void testBackwardsMovement() {
        pathfinder.followTrajectory(new LinearTrajectory(
                new PointXYZ(-10, -10, 0),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        pathfinder.tickUntil();
    }

    @Test
    public void testTurningRectangle() {
        pathfinder.followTrajectories(
                new LinearTrajectory(
                        new PointXYZ(0, 0, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(10, 0, 90),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(10, 10, 180),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(0, 10, 270),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(0, 0, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                )
        );

        pathfinder.tickUntil();
    }

    @Test
    public void testRectangle() {
        pathfinder.followTrajectories(
                new LinearTrajectory(
                        new PointXYZ(0, 0, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(10, 0, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(10, 10, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(0, 10, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(0, 0, 0),
                        0.5,
                        2,
                        Angle.fromDeg(5)
                )
        );

        pathfinder.tickUntil();
    }

    @Test
    @Disabled
    public void testMultipleTrajectories() {
        pathfinder.followTrajectories(new LinearTrajectory(
                new PointXYZ(10, 10, 270),
                0.5,
                2,
                Angle.fromDeg(5)
        ), new LinearTrajectory(
                new PointXYZ(20, 20, 360),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        pathfinder.tickUntil();
    }
}
