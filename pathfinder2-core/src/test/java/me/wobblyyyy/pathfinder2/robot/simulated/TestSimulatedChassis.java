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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.plugin.bundled.StatTracker;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.ArcTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.builder.LinearTrajectoryBuilder;
import me.wobblyyyy.pathfinder2.trajectory.multi.segment.MultiSegmentTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.AdvancedSplineTrajectoryBuilder;
import me.wobblyyyy.pathfinder2.trajectory.spline.InterpolationMode;
import me.wobblyyyy.pathfinder2.trajectory.spline.MultiSplineBuilder;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.List;

@TestInstance(Lifecycle.PER_CLASS)
public class TestSimulatedChassis {
    private SimulatedOdometry odometry;
    private SimulatedWrapper wrapper;
    private Robot robot;
    private Controller turnController;
    private Pathfinder pathfinder;
    private SplineBuilderFactory factory;

    @BeforeEach
    public void beforeEach() {
        wrapper = new SimulatedWrapper(new SimulatedDrive(), new SimulatedOdometry());
        odometry = wrapper.getOdometry();
        robot = wrapper.getRobot();
        turnController = new ProportionalController(-0.05);
        pathfinder = new Pathfinder(robot, turnController)
                .setSpeed(0.5)
                .setTolerance(0.6)
                .setAngleTolerance(Angle.fromDeg(5));
        factory = new SplineBuilderFactory()
                .setSpeed(0.5)
                .setStep(0.1)
                .setTolerance(0.5)
                .setAngleTolerance(Angle.fromDeg(5));
    }

    @BeforeAll
    public void beforeAll() {
        Translation.DEFAULT_EQUALITY_TOLERANCE = 0.1;
        StatTracker.SECOND_MS_DURATION = 1_000_000;
    }

    @AfterAll
    public void afterAll() {
        Translation.DEFAULT_EQUALITY_TOLERANCE = 0.01;
        StatTracker.SECOND_MS_DURATION = 1_000;
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
    public void testHalfRotation() {
        pathfinder.followTrajectory(new LinearTrajectory(
                pathfinder.getPosition().withHeading(Angle.fromDeg(180)),
                0.5,
                0.1,
                Angle.fromDeg(1)
        ));

        pathfinder.tickUntil();

        odometry.setTranslation(new Translation(-1, -1, 0));
        odometry.updatePositionBasedOnVelocity(1000);

        odometry.setTranslation(new Translation(1, 1, 0));
        odometry.updatePositionBasedOnVelocity(1000);
    }

    @Test
    public void testManyTargets() {
        pathfinder
                .goTo(new PointXYZ(0, 0, 0))
                .goTo(new PointXYZ(10, 10, 90))
                .goTo(new PointXYZ(0, -10, 45))
                .goTo(new PointXYZ(3, 3, 33))
                .goTo(new PointXYZ(-3, 6, 88))
                .goTo(new PointXYZ(2, -2, 87))
                .goTo(new PointXYZ(3, -6, 22))
                .goTo(new PointXYZ(-3, 6, 1))
                .goTo(new PointXYZ(0, 0, 0))
                .tickUntil();
    }

    @Test
    public void testRightTurn() {
        pathfinder.goTo(new PointXYZ(5, 5, 90));
        pathfinder.tickUntil();

        odometry.setTranslation(new Translation(1, 0, 0));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(-1, 0, 0));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(1, 1, 0));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(-1, -1, 0));
        odometry.updatePositionBasedOnVelocity(1_000);

        Assertions.assertTrue(pathfinder.getPosition().absDistance(new PointXYZ(5, 5, 90)) < 2);
    }

    @Test
    public void testTurningRectangle() {
        pathfinder.followTrajectories(
                new LinearTrajectory(
                        new PointXYZ(0, 0, 0),
                        0.5,
                        0.1,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(10, 0, 90),
                        0.5,
                        0.1,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(10, 10, 180),
                        0.5,
                        0.1,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(0, 10, 270),
                        0.5,
                        0.1,
                        Angle.fromDeg(5)
                ),
                new LinearTrajectory(
                        new PointXYZ(0, 0, 0),
                        0.5,
                        0.1,
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
        Trajectory[] splines = new Trajectory[]{
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

    @Test
    public void testAkimaSplineInterpolation() {
        Trajectory trajectory = new AdvancedSplineTrajectoryBuilder()
                .setStep(0.1)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(1))
                .setInterpolationMode(InterpolationMode.AKIMA)
                .setSpeed(0.5)
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(4, 10, 0))
                .add(new PointXYZ(8, 12, 0))
                .add(new PointXYZ(10, 13, 0))
                .add(new PointXYZ(12, 14, 0))
                .add(new PointXYZ(14, 15, 0))
                .build();

        pathfinder.followTrajectory(trajectory);

        pathfinder.tickUntil(1_000);

        Assertions.assertTrue(pathfinder.getPosition().distance(new PointXYZ(14, 15, 0)) <= 2);
    }

    @Test
    public void testCubicSplineInterpolation() {
        Trajectory trajectory = new AdvancedSplineTrajectoryBuilder()
                .setStep(0.1)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(1))
                .setInterpolationMode(InterpolationMode.CUBIC)
                .setSpeed(0.5)
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(4, 10, 0))
                .add(new PointXYZ(8, 12, 0))
                .add(new PointXYZ(10, 13, 0))
                .add(new PointXYZ(12, 14, 0))
                .add(new PointXYZ(14, 15, 0))
                .build();

        pathfinder.followTrajectory(trajectory);

        pathfinder.tickUntil(1_000);

        Assertions.assertTrue(pathfinder.getPosition().distance(new PointXYZ(14, 15, 0)) <= 2);
    }

    public void testArcTrajectory() {
        ArcTrajectory arc = new ArcTrajectory(
                new PointXYZ(5, 5, 0),
                5,
                0.5,
                Angle.fromDeg(-5),
                Angle.fromDeg(0),
                Angle.fromDeg(180),
                Angle.fromDeg(90)
        );

        pathfinder.followTrajectory(arc);

        pathfinder.tickUntil(1_000);

        Assertions.assertTrue(pathfinder.getPosition().distance(new PointXYZ(5, 5, 0)) <= 2);
    }

    private void testRectangle(double vx,
                               double vy,
                               double vz) {
        odometry.setRawPosition(new PointXYZ(0, 0, 0));

        // first group of 4
        odometry.setTranslation(new Translation(vx, 0, vz));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(0, vy, vz));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(-vx, 0, vz));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(0, -vy, vz));
        odometry.updatePositionBasedOnVelocity(1_000);

        Assertions.assertEquals(
                new PointXYZ(0, 0, 0),
                odometry.getPosition()
        );

        Translation a = new Translation(vx * vx, vy * vy, vz * vz).multiply(4);

        // second group of 4
        odometry.setTranslation(new Translation(vx, 0, vz).multiply(a));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(0, vy, vz).multiply(a));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(-vx, 0, vz).multiply(a));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(0, -vy, vz).multiply(a));
        odometry.updatePositionBasedOnVelocity(1_000);

        Assertions.assertEquals(
                new PointXYZ(0, 0, 0),
                odometry.getPosition()
        );

        Translation b = new Translation(3, 3, 3);

        // ... and of course, the third group of 4
        odometry.setTranslation(new Translation(vx, 0, vz).multiply(b));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(0, vy, vz).multiply(b));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(-vx, 0, vz).multiply(b));
        odometry.updatePositionBasedOnVelocity(1_000);

        odometry.setTranslation(new Translation(0, -vy, vz).multiply(b));
        odometry.updatePositionBasedOnVelocity(1_000);

        Assertions.assertEquals(
                new PointXYZ(0, 0, 0),
                odometry.getPosition()
        );
    }

    @Test
    public void testRectangleTranslations() {
        testRectangle(1, 0, 0);
        testRectangle(0, 1, 0);

        testRectangle(1, 0.5, 0);
        testRectangle(0.5, 1, 0);

        testRectangle(1, 0, 1);
        testRectangle(0, 1, 1);

        testRectangle(10, 5, 1);
        testRectangle(5, 10, -1);
    }

    @Test
    public void testMonotonicMultiSplineTrajectory() {
        MultiSplineBuilder builder = new MultiSplineBuilder()
            .setDefaultStep(0.05)
            .setDefaultSpeed(0.5)
            .setDefaultTolerance(0.5)
            .setDefaultAngleTolerance(Angle.fromDeg(5))
            .add(0, 0, Angle.fromDeg(0), 0.1)
            .add(2, 3, Angle.fromDeg(0), 0.1)
            .add(4, 6, Angle.fromDeg(0), 0.1)
            .add(6, 9, Angle.fromDeg(0), 0.5, 0.1, 2, Angle.fromDeg(5));

        pathfinder.followTrajectory(builder.build());

        pathfinder.tickUntil();
    }

    @Test
    public void testNonMonotonicMultiSplineTrajectory() {
        MultiSplineBuilder builder = new MultiSplineBuilder()
            .setDefaultStep(0.05)
            .setDefaultSpeed(0.5)
            .setDefaultTolerance(0.5)
            .setDefaultAngleTolerance(Angle.fromDeg(5))
            .add(0, 0, Angle.fromDeg(0), 0.1)
            .add(2, 3, Angle.fromDeg(0), 0.1)
            .add(4, 6, Angle.fromDeg(0), 0.1)
            .add(6, 9, Angle.fromDeg(0), 0.1)
            .add(8, 6, Angle.fromDeg(0), 0.1)
            .add(10, 3, Angle.fromDeg(0), 0.1)
            .add(12, 6, Angle.fromDeg(0), 0.1)
            .add(14, 9, Angle.fromDeg(0), 0.1)
            .add(16, 6, Angle.fromDeg(0), 0.1)
            .add(18, 3, Angle.fromDeg(0), 0.1)
            .add(20, 0, Angle.fromDeg(0), 0.5, 0.1, 2, Angle.fromDeg(5));

        pathfinder.followTrajectory(builder.build());

        pathfinder.tickUntil();
    }

    @Test
    public void testMultiSegmentTrajectory() {
        List<Trajectory> trajectories = new LinearTrajectoryBuilder()
            .setSpeed(0.5)
            .setTolerance(0.1)
            .setAngleTolerance(Angle.fromDeg(1))
            .goTo(new PointXYZ(0, 0, 0))
            .goTo(new PointXYZ(0, 10, 0))
            .goTo(new PointXYZ(10, 10, 0))
            .goTo(new PointXYZ(10, 0, 0))
            .goTo(new PointXYZ(0, 0, 0))
            .getTrajectories();

        Trajectory trajectory = new MultiSegmentTrajectory(trajectories);

        pathfinder.followTrajectory(trajectory);
        
        pathfinder.tickUntil();
    }

    @Test
    public void testSplineTicksPerSecond() {
        // if your computer is fast enough, this should be 1_000 ticks
        // per second. if it's not... oh well.

        // gotta load the StatTracker plugin in order to view tps
        pathfinder.loadBundledPlugins();

        MultiSplineBuilder builder = new MultiSplineBuilder()
            .setDefaultStep(0.5)
            .setDefaultSpeed(0.5)
            .setDefaultTolerance(0.5)
            .setDefaultInterpolationMode(InterpolationMode.DEFAULT)
            .setDefaultAngleTolerance(Angle.fromDeg(5));

        for (int i = 0; i < 10; i++) {
            builder.add(i * 2, Math.pow(i, 2), Angle.fromDeg(0), 
                    Math.pow((i + 1) / 10d, 2), 0.5, 2, Angle.fromDeg(5));
        }

        pathfinder.followTrajectory(builder.build());

        pathfinder.tickUntil();
    }
}
