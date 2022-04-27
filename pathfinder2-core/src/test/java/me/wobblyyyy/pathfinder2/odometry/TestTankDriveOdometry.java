/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.odometry;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.utils.AtomicAngle;
import me.wobblyyyy.pathfinder2.utils.AtomicDouble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTankDriveOdometry {
    private final AtomicDouble rightDistance = new AtomicDouble();
    private final AtomicDouble leftDistance = new AtomicDouble();
    private final AtomicAngle gyroAngle = new AtomicAngle();
    private TankDriveOdometry odometry;

    @BeforeEach
    public void beforeEach() {
        rightDistance.set(0.0);
        leftDistance.set(0.0);
        gyroAngle.set(Angle.ZERO);
        odometry =
            new TankDriveOdometry(
                rightDistance::get,
                leftDistance::get,
                gyroAngle::get
            );
    }

    private void testDistances(
        PointXYZ expected,
        double newRightDistance,
        double newLeftDistance,
        Angle newGyroAngle
    ) {
        rightDistance.set(newRightDistance);
        leftDistance.set(newLeftDistance);
        gyroAngle.set(newGyroAngle);
        Assertions.assertEquals(expected, odometry.getPosition());
    }

    @Test
    public void testDriveForward() {
        testDistances(new PointXYZ(0, 0, 0), 0, 0, Angle.ZERO);
        testDistances(new PointXYZ(0, 1, 0), 1, 1, Angle.ZERO);
        testDistances(new PointXYZ(0, 2, 0), 2, 2, Angle.ZERO);
    }

    @Test
    public void testDriveBackward() {
        testDistances(new PointXYZ(0, 0, 0), 0, 0, Angle.ZERO);
        testDistances(new PointXYZ(0, -1, 0), -1, -1, Angle.ZERO);
        testDistances(new PointXYZ(0, -2, 0), -2, -2, Angle.ZERO);
    }
}
