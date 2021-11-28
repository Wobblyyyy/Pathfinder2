/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.multi.target;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMultiTargetTrajectory {
    @Test
    public void testMultiTargetTrajectory() {
        TrajectoryTarget[] targets = new MultiTargetBuilder()
                .setPrecision(TargetPrecision.FAST)
                .setSpeed(0.5)
                .setTolerance(2.0)
                .setAngleTolerance(Angle.fromDeg(5))
                .addTargetPoint(new PointXYZ(10, 10, 0))
                .addTargetPoint(new PointXYZ(10, 10, 10))
                .addTargetPoint(new PointXYZ(-10, -10, 0))
                .build();

        MultiTargetTrajectory trajectory = new MultiTargetTrajectory(targets);

        PointXYZ a = trajectory.nextMarker(new PointXYZ(0, 0, 0));
        PointXYZ b = trajectory.nextMarker(new PointXYZ(10, 0, 0));
        PointXYZ c = trajectory.nextMarker(new PointXYZ(10, 5, 0));
        Assertions.assertEquals(a, b);
        Assertions.assertEquals(b, c);
        PointXYZ d = trajectory.nextMarker(new PointXYZ(10, 10, 0));
        Assertions.assertEquals(d, new PointXYZ(10, 10, 10));
        PointXYZ e = trajectory.nextMarker(new PointXYZ(10, 10, 10));
        PointXYZ f = trajectory.nextMarker(new PointXYZ(0, 0, 10));
        PointXYZ g = trajectory.nextMarker(new PointXYZ(-5, -5, 5));
        Assertions.assertEquals(e, f);
        Assertions.assertEquals(f, g);
        PointXYZ h = trajectory.nextMarker(new PointXYZ(-10, -10, 0));
        Assertions.assertTrue(trajectory.isDone(new PointXYZ(-10, -10, 0)));
    }
}
