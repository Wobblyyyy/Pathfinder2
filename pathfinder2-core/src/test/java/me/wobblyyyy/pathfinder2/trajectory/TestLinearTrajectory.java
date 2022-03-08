/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedRobot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLinearTrajectory {
    private static final PointXYZ target = new PointXYZ(10, 10, 0);
    private static final double speed = 1.0;
    private static final double tolerance = 0.1;
    private static final Angle angleTolerance = Angle.fromDeg(5);
    private static final LinearTrajectory trajectory = new LinearTrajectory(
        target,
        speed,
        tolerance,
        angleTolerance
    );

    private static void setPos(SimulatedRobot odometry, PointXYZ pos) {
        odometry.setPosition(pos);
    }

    @Test
    public void testGetNextMarker() {
        double nextX = 10;
        double nextY = 10;
        double nextZ = 0;

        Assertions.assertEquals(nextX, trajectory.nextMarker(target).x());
        Assertions.assertEquals(nextY, trajectory.nextMarker(target).y());
        Assertions.assertEquals(nextZ, trajectory.nextMarker(target).z().deg());
    }

    @Test
    public void testIsDoneExact() {
        Assertions.assertTrue(trajectory.isDone(target));
    }

    @Test
    public void testIsDoneInexact() {
        PointXYZ position = new PointXYZ(9.95, 9.95, 4.9);

        Assertions.assertTrue(trajectory.isDone(position));
    }

    @Test
    public void testSimulation() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        Trajectory a = new LinearTrajectory(
            new PointXYZ(10, 10, 0),
            0.5,
            2,
            Angle.fromDeg(5)
        );
        SimulatedRobot odometry = (SimulatedRobot) pathfinder.getOdometry();

        pathfinder.followTrajectory(a);

        odometry.setPosition(new PointXYZ(0, 0, 0));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.353, 0.353, 0.0),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(0, 0, 15));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(10, 10, 45));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, 0.45),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(10, 10, 40));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, 0.4),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(10, 10, 3));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, 0.0),
            pathfinder.getTranslation()
        );
        Assertions.assertFalse(pathfinder.isActive());

        Trajectory b = new LinearTrajectory(
            new PointXYZ(10, 10, 45),
            0.5,
            2,
            Angle.fromDeg(5)
        );
        pathfinder.followTrajectory(b);

        odometry.setPosition(new PointXYZ(10, 10, 0));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, -0.45),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(10, 10, 10));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, -0.35),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(10, 10, 20));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, -0.25),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(12, 12, 0));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(-0.353, -0.353, -0.45),
            pathfinder.getTranslation()
        );
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(12, 12, 45));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(new PointXYZ(10, 10, 45));
        pathfinder.tick();
        Assertions.assertEquals(
            new Translation(0.0, 0.0, 0.0),
            pathfinder.getTranslation()
        );
        Assertions.assertFalse(pathfinder.isActive());
    }

    @Test
    public void testZeroPointTurn() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        Trajectory trajectory = new LinearTrajectory(
            pathfinder.getPosition().withHeadingDegrees(45),
            0.5,
            2,
            Angle.fromDeg(2)
        );
        SimulatedRobot odometry = (SimulatedRobot) pathfinder.getOdometry();
        pathfinder.followTrajectory(trajectory);

        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(
            odometry.getPosition().withHeading(Angle.fromDeg(15))
        );
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(
            odometry.getPosition().withHeading(Angle.fromDeg(30))
        );
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(
            odometry.getPosition().withHeading(Angle.fromDeg(42))
        );
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(
            odometry.getPosition().withHeading(Angle.fromDeg(48))
        );
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        odometry.setPosition(
            odometry.getPosition().withHeading(Angle.fromDeg(45))
        );
        pathfinder.tick();
        Assertions.assertFalse(pathfinder.isActive());
    }

    @Test
    public void testMultipleTrajectories() {
        // look... nobody said tests have to be written WELL...
        // i am sincerely sorry for whatever this mess is

        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

        Trajectory a = new LinearTrajectory(
            new PointXYZ(0, 0, 0),
            0.5,
            2,
            Angle.fromDeg(5)
        );
        Trajectory b = new LinearTrajectory(
            new PointXYZ(10, 0, 0),
            0.5,
            2,
            Angle.fromDeg(5)
        );
        Trajectory c = new LinearTrajectory(
            new PointXYZ(10, 10, 0),
            0.5,
            2,
            Angle.fromDeg(5)
        );
        Trajectory d = new LinearTrajectory(
            new PointXYZ(0, 10, 0),
            0.5,
            2,
            Angle.fromDeg(5)
        );
        Trajectory e = new LinearTrajectory(
            new PointXYZ(0, 0, 0),
            0.5,
            2,
            Angle.fromDeg(5)
        );

        SimulatedRobot o = (SimulatedRobot) pathfinder.getOdometry();

        pathfinder.followTrajectories(a, b, c, d, e);

        int lastFollowers = pathfinder.getExecutorManager().howManyFollowers();
        int followers;

        setPos(o, new PointXYZ(0, 0, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers - 1, followers);
        lastFollowers = followers;

        setPos(o, new PointXYZ(5, 5, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers, followers);
        lastFollowers = followers;

        setPos(o, new PointXYZ(10, 0, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers - 1, followers);
        lastFollowers = followers;

        setPos(o, new PointXYZ(10, 5, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers, followers);
        lastFollowers = followers;

        setPos(o, new PointXYZ(10, 10, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers - 1, followers);
        lastFollowers = followers;

        setPos(o, new PointXYZ(0, 10, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers - 1, followers);
        lastFollowers = followers;

        setPos(o, new PointXYZ(0, 0, 0));
        pathfinder.tick();
        Assertions.assertFalse(pathfinder.isActive());
        followers = pathfinder.getExecutorManager().howManyFollowers();
        Assertions.assertEquals(lastFollowers - 1, followers);
    }
}
