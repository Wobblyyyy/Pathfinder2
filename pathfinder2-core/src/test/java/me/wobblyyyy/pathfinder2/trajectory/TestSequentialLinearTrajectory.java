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

import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.GenericTrajectoryTester;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class TestSequentialLinearTrajectory extends GenericTrajectoryTester {
    private void testTrajectoryTo(PointXYZ target) {
        Trajectory trajectory = new LinearTrajectory(target, speed,
                tolerance, angleTolerance);
        follow(trajectory, target);
    }

    @Test
    public void testSingleLinearTrajectoryForwards() {
        testTrajectoryTo(new PointXYZ(0, 10, 0));
    }

    @Test
    public void testSingleLinearTrajectoryBackwards() {
        testTrajectoryTo(new PointXYZ(0, -10, 0));
    }

    @Test
    public void testSingleLinearTrajectoryRightwards() {
        testTrajectoryTo(new PointXYZ(10, 0, 0));
    }

    @Test
    public void testSingleLinearTrajectoryLeftwards() {
        testTrajectoryTo(new PointXYZ(-10, 0, 0));
    }

    private void testTurnTo(Angle... angles) {
        for (Angle angle : angles) {
            PointXYZ point = new PointXYZ().addZ(angle);
            testTrajectoryTo(point);
        }
    }

    private void testTurnTo(double... angles) {
        Angle[] a = new Angle[angles.length];

        for (int i = 0; i < angles.length; i++)
            a[i] = Angle.fixedDeg(angles[i]);

        testTurnTo(a);
    }

    @Test
    public void testRightwardsTurns() {
        testTurnTo(0, 45, 90, 135, 180, 225, 270, 315, 360);
    }

    @Test
    public void testLeftwardsTurns() {
        testTurnTo(0, -45, -90, -135, -180, -225, -270, -315, -360);
    }

    @Test
    public void testMovingTurns() {
        testTrajectoryTo(new PointXYZ(10, 10, Angle.fixedDeg(45)));
        testTrajectoryTo(new PointXYZ(-10, -10, Angle.fixedDeg(-45)));
        testTrajectoryTo(new PointXYZ(10, 10, Angle.fixedDeg(90)));
        testTrajectoryTo(new PointXYZ(-10, -10, Angle.fixedDeg(-90)));

        testTrajectoryTo(new PointXYZ(15, 15, Angle.fixedDeg(450)));
        testTrajectoryTo(new PointXYZ(-15, -15, Angle.fixedDeg(-450)));
        testTrajectoryTo(new PointXYZ(15, 15, Angle.fixedDeg(900)));
        testTrajectoryTo(new PointXYZ(-15, -15, Angle.fixedDeg(-900)));
    }
}
