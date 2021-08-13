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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSimulatedOdometry {
    @Test
    public void testRawPosition() {
        SimulatedOdometry odometry = new SimulatedOdometry();

        double xA = 0;
        double yA = 0;
        Angle zA = Angle.fromDeg(0);
        double xB = 10;
        double yB = 10;
        Angle zB = Angle.fromDeg(0);

        PointXYZ positionA = new PointXYZ(xA, yA, zA);
        PointXYZ positionB = new PointXYZ(xB, yB, zB);

        odometry.setRawPosition(positionA);
        Assertions.assertEquals(xA, odometry.getRawPosition().x());
        Assertions.assertEquals(xA, odometry.getPosition().x());
        Assertions.assertEquals(yA, odometry.getRawPosition().y());
        Assertions.assertEquals(yA, odometry.getPosition().y());
        Assertions.assertEquals(zA.deg(), odometry.getRawPosition().z().deg());
        Assertions.assertEquals(zA.deg(), odometry.getPosition().z().deg());

        odometry.setRawPosition(positionB);
        Assertions.assertEquals(xB, odometry.getRawPosition().x());
        Assertions.assertEquals(xB, odometry.getPosition().x());
        Assertions.assertEquals(yB, odometry.getRawPosition().y());
        Assertions.assertEquals(yB, odometry.getPosition().y());
        Assertions.assertEquals(zB.deg(), odometry.getRawPosition().z().deg());
        Assertions.assertEquals(zB.deg(), odometry.getPosition().z().deg());
    }

    @Test
    public void testVelocity() {
        SimulatedOdometry odometry = new SimulatedOdometry();
        odometry.setShouldAutomaticallyCalculateElapsedTime(false);

        double velocity = 10;
        double elapsedTimeMs = 10_000;
        Angle movementAngle = Angle.fromDeg(0);

        PointXYZ expected = new PointXYZ(100, 0, Angle.fromDeg(0));

        odometry.setVelocity(movementAngle, velocity);
        odometry.updatePositionBasedOnVelocity(elapsedTimeMs);

        Assertions.assertEquals(expected.x(), odometry.getX());
        Assertions.assertEquals(expected.y(), odometry.getY());
        Assertions.assertEquals(expected.z().deg(), odometry.getZ().deg());
    }
}
