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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
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
    public void testGetSpeed() {
        Assertions.assertEquals(1.0, trajectory.speed(target));
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
}
