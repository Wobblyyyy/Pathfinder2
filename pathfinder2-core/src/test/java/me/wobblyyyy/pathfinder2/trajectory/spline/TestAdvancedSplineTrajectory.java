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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.TestableRobot;
import me.wobblyyyy.pathfinder2.exceptions.SplineException;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

public class TestAdvancedSplineTrajectory extends TestableRobot {
    private InterpolationMode mode = InterpolationMode.DEFAULT;

    private void testSplineTo(PointXYZ... points) {
        if (points.length < 1)
            throw new IllegalArgumentException();

        AdvancedSplineTrajectoryBuilder builder =
                new AdvancedSplineTrajectoryBuilder()
                        .setStep(0.1)
                        .setTolerance(tolerance)
                        .setSpeed(speed)
                        .setAngleTolerance(angleTolerance);

        builder.setInterpolationMode(mode);

        for (PointXYZ point : points)
            builder.add(point);

        builder.setInterpolationMode(InterpolationMode.DEFAULT);
        Trajectory trajectory = builder.build();
        PointXYZ target = points[points.length - 1];
        testTrajectory(trajectory, target);
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

    @Test
    public void testInvalidSplines() {
        Assertions.assertThrows(SplineException.class, () -> {
            new MonotoneCubicSpline(
                    new double[] { 0 },
                    new double[] { 0 }
            );
        });

        Assertions.assertThrows(SplineException.class, () -> {
            new MonotoneCubicSpline(
                    new double[] { 0 },
                    new double[] { 0, 1 }
            );
        });

        Assertions.assertThrows(SplineException.class, () -> {
            new MonotoneCubicSpline(
                    new double[] { 0 },
                    null
            );
        });
    }

    @Test
    public void testCubicSplineInterpolation() {
        mode = InterpolationMode.CUBIC;
        testSplineTo(new PointXYZ(0, 0), new PointXYZ(5, 5),
                new PointXYZ(10, 15), new PointXYZ(15, 30));
        testSplineTo(new PointXYZ(0, 0, 45), new PointXYZ(5, 5, 65),
                new PointXYZ(10, 15, 90), new PointXYZ(15, 30, 180));
        mode = InterpolationMode.DEFAULT;
    }

    @Test
    public void testInvalidAkimaSpline() {
        mode = InterpolationMode.AKIMA;

        Assertions.assertThrows(SplineException.class, () -> {
                testSplineTo(new PointXYZ(0, 0));
        });

        mode = InterpolationMode.DEFAULT;
    }

    @Test
    public void testAkimaSplineInterpolation() {
        mode = InterpolationMode.AKIMA;
        testSplineTo(new PointXYZ(0, 0), new PointXYZ(5, 5),
                new PointXYZ(10, 15), new PointXYZ(15, 30));
        testSplineTo(new PointXYZ(0, 0, 45), new PointXYZ(5, 5, 65),
                new PointXYZ(10, 15, 90), new PointXYZ(15, 30, 180));
        mode = InterpolationMode.DEFAULT;
    }

    @Test
    public void testLongLinearSpline() {
        testSplineTo(
                new PointXYZ(10, 0, 0),
                new PointXYZ(20, 10, 0),
                new PointXYZ(30, 20, 0),
                new PointXYZ(40, 30, 0),
                new PointXYZ(50, 40, 0),
                new PointXYZ(60, 50, 0),
                new PointXYZ(70, 60, 0),
                new PointXYZ(80, 70, 0),
                new PointXYZ(90, 80, 0),
                new PointXYZ(100, 90, 0),
                new PointXYZ(110, 100, 0),
                new PointXYZ(120, 110, 0),
                new PointXYZ(130, 120, 0)
        );
    }

    @Test
    public void testBackwardsLongLinearSpline() {
        testSplineTo(
                new PointXYZ(10, 0, 0).multiply(-1),
                new PointXYZ(20, 10, 0).multiply(-1),
                new PointXYZ(30, 20, 0).multiply(-1),
                new PointXYZ(40, 30, 0).multiply(-1),
                new PointXYZ(50, 40, 0).multiply(-1),
                new PointXYZ(60, 50, 0).multiply(-1),
                new PointXYZ(70, 60, 0).multiply(-1),
                new PointXYZ(80, 70, 0).multiply(-1),
                new PointXYZ(90, 80, 0).multiply(-1),
                new PointXYZ(100, 90, 0).multiply(-1),
                new PointXYZ(110, 100, 0).multiply(-1),
                new PointXYZ(120, 110, 0).multiply(-1),
                new PointXYZ(130, 120, 0).multiply(-1)
        );
    }
}
