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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;

public class TestSimulatedChassis {
    private SimulatedDrive drive;
    private SimulatedOdometry odometry;
    private SimulatedWrapper wrapper;
    private Robot robot;
    private Controller turnController;
    private Pathfinder pathfinder;
    private SplineBuilderFactory factory;

    @BeforeEach
    public void beforeEach() {
        wrapper = new SimulatedWrapper(new SimulatedDrive(), new SimulatedOdometry());
        drive = wrapper.getDrive();
        odometry = wrapper.getOdometry();
        robot = wrapper.getRobot();
        turnController = new ProportionalController(-0.05);
        pathfinder = new Pathfinder(robot, turnController)
            .setSpeed(0.5)
            .setTolerance(2)
            .setAngleTolerance(Angle.fromDeg(5));
        factory = new SplineBuilderFactory()
                .setSpeed(0.5)
                .setStep(0.1)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5));
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

    @Test
    public void testArcSpline() {
        Trajectory trajectory = factory.builder()
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(10, 0, 0).inDirection(10, Angle.fixedDeg(135)))
                .add(new PointXYZ(10, 10, 0))
                .build();

        pathfinder.followTrajectory(trajectory);

        pathfinder.tickUntil();
    }

    @Test
    public void testBackwardsArcSpline() {
        Trajectory trajectory = factory.builder()
                .setStep(-0.1)
                .add(new PointXYZ(10, 10, 0))
                .add(new PointXYZ(10, 0, 0).inDirection(10, Angle.fixedDeg(135)))
                .add(new PointXYZ(0, 0, 0))
                .build();

        odometry.setRawPosition(new PointXYZ(10, 10, 0));
        pathfinder.followTrajectory(trajectory);

        pathfinder.tickUntil();

        Assertions.assertTrue(PointXY.ZERO.distance(pathfinder.getPosition()) <= 2);
    }

    @Test
    public void testSplineTo() {
        pathfinder.splineTo(
                new PointXYZ(0, 0, 0),
                new PointXYZ(2, 4, 90),
                new PointXYZ(4, 8, 100),
                new PointXYZ(6, 12, 120)
        );

        pathfinder.tickUntil();
    }

    @Test
    public void testNegativePath() {
        pathfinder.goTo(new PointXYZ(-10, 0, 0))
                  .goTo(new PointXYZ(-10, -10, 0))
                  .goTo(new PointXYZ(0, -10, 0))
                  .goTo(new PointXYZ(0, 0, 0));

        pathfinder.tickUntil();
    }

    @Test
    public void testMultipleSplines() {
        Trajectory[] splines = new Trajectory[] {
            factory.builder()
                    .add(new PointXYZ(0, 0, 0))
                    .add(new PointXYZ(4, 10, 0))
                    .add(new PointXYZ(8, 12, 0))
                    .build(),
            factory.builder()
                    .add(new PointXYZ(8, 12, 0))
                    .add(new PointXYZ(16, 10, 0))
                    .add(new PointXYZ(18, 5, 0))
                    .build(),
            factory.builder()
                    .add(new PointXYZ(18, 5, 0))
                    .add(new PointXYZ(20, 10, 0))
                    .add(new PointXYZ(22, 12, 0))
                    .build()
        };

        for (Trajectory spline : splines)
            pathfinder.followTrajectory(spline);

        pathfinder.tickUntil();
    }
}
