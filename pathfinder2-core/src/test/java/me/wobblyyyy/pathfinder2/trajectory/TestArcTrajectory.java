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

import me.wobblyyyy.pathfinder2.GenericTrajectoryTester;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestArcTrajectory extends GenericTrajectoryTester {
    private static final double RADIUS = 10;
    private static final Angle ANGLE_STEP = Angle.fromDeg(5);
    private static final Angle[] ANGLES = new Angle[24];

    static {
        for (int i = 0; i < ANGLES.length; i++)
            ANGLES[i] = Angle.fromDeg(i * 15);
    }

    private void testSingleArcTrajectory(Angle startAngle,
                                         Angle size,
                                         Angle targetHeading) {
        Trajectory trajectory = new ArcTrajectory(new PointXYZ(), RADIUS,
                speed, ANGLE_STEP, targetHeading, startAngle,
                startAngle.add(size));

        pathfinder.followTrajectory(trajectory);

        PointXYZ end = new PointXY().inDirection(RADIUS,
                startAngle.add(size)).withHeading(targetHeading);

        pathfinder.tickUntil(1_000);
        assertPositionIs(end);
    }

    @Test
    private void testNonTurningArcTrajectories() {
        for (Angle startAngle : ANGLES)
            for (Angle size : ANGLES)
                for (Angle targetHeading : ANGLES)
                    testSingleArcTrajectory(startAngle, size, targetHeading);
    }
}
