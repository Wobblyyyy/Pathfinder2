/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.spline;

import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.GenericTrajectoryTester;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class TestAdvancedSplineTrajectory extends GenericTrajectoryTester {
    private void testSplineTo(PointXYZ... points) {
        if (points.length < 1)
            throw new IllegalArgumentException();

        pathfinder.splineTo(points);
        pathfinder.tickUntil(1_000);
        assertPositionIs(points[points.length - 1]);
    }

    @Test
    public void testLinearSpline() {
        testSplineTo(new PointXYZ(5, 5), new PointXYZ(10, 10),
                new PointXYZ(20, 20));
    }

    @Test
    public void testBackwardsLinearSpline() {
        testSplineTo(new PointXYZ(-5, -5), new PointXYZ(-10, -10),
                new PointXYZ(-20, -20));
    }

    @Test
    public void testTurningLinearSpline() {
        testSplineTo(new PointXYZ(5, 5), new PointXYZ(10, 10, 45),
                new PointXYZ(20, 20, 90));
    }

    @Test
    public void testBackwardsTurningLinearSpline() {
        testSplineTo(new PointXYZ(-5, -5), new PointXYZ(-10, -10, 45),
                new PointXYZ(-20, -20, 90));
    }

    @Test
    public void testNonTurningNonLinearSpline() {
        testSplineTo(new PointXYZ(0, 0), new PointXYZ(5, 5),
                new PointXYZ(10, 15), new PointXYZ(15, 30));
    }

    @Test
    public void testTurningNonLinearSpline() {
        testSplineTo(new PointXYZ(0, 0, 45), new PointXYZ(5, 5, 65),
                new PointXYZ(10, 15, 90), new PointXYZ(15, 30, 180));
    }
}
