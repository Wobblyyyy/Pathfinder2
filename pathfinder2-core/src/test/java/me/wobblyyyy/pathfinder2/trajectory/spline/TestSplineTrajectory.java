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

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.GenericTrajectoryTester;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.ArrayUtils;
import org.junit.jupiter.api.Test;

public class TestSplineTrajectory extends GenericTrajectoryTester {
    private static final double DEGREES = 360;
    private static final int TOTAL_STEPS = 8;
    private static final Angle STEP = Angle.fromDeg(DEGREES / TOTAL_STEPS);
    private static final PointXY CENTER = PointXY.ZERO;

    private static final PointXYZ[] BASE_POINTS = new PointXYZ[] {
        new PointXYZ(0, 0, 0),
        new PointXYZ(5, 6, 0),
        new PointXYZ(10, 18, 0),
        new PointXYZ(15, 30, 0),
    };

    private static final List<PointXYZ[]> ALL_POINTS = new ArrayList<PointXYZ[]>() {

        {
            for (int i = 0; i < TOTAL_STEPS; i++) {
                Angle angle = STEP.multiply(i);
                PointXYZ[] rotated = PointXYZ.rotatePoints(
                    BASE_POINTS,
                    CENTER,
                    angle
                );
                add(rotated);
            }
        }
    };

    private void runTest(PointXYZ[] points) {
        Spline spline = MonotoneCubicSpline.fromPoints(points);
        PointXYZ[] reversedPoints = new PointXYZ[points.length];
        System.arraycopy(points, 0, reversedPoints, 0, points.length);
        ArrayUtils.reverse(reversedPoints);
        Spline reverseSpline = MonotoneCubicSpline.fromPoints(reversedPoints);
        Trajectory trajectory = new SplineTrajectory(
            spline,
            Angle.DEG_45,
            0.5,
            0.1,
            2,
            Angle.fromDeg(5)
        );
        Trajectory reverseTrajectory = new SplineTrajectory(
            reverseSpline,
            Angle.DEG_0,
            0.5,
            -0.1,
            2,
            Angle.fromDeg(5)
        );
        follow(trajectory, points[points.length - 1].withZ(Angle.DEG_45));
        follow(
            reverseTrajectory,
            reversedPoints[points.length - 1].withZ(Angle.ZERO)
        );
    }

    @Test
    public void testSplineTrajectories() {
        for (PointXYZ[] points : ALL_POINTS) runTest(points);
    }
}
