/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.plugin.bundled;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestHeadingLock {
    @Test
    public void testHeadingLockAlongLine() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        SimulatedOdometry odometry = (SimulatedOdometry) pathfinder.getOdometry();
        pathfinder.loadBundledPlugins();
        pathfinder.lockHeading(new PointXY(10, 10));
        pathfinder.followTrajectory(new LinearTrajectory(
                new PointXYZ(0, 10, 0),
                0.5,
                2,
                Angle.fromDeg(5)
        ));

        odometry.setRawPosition(new PointXYZ(0, 0, 0));
        pathfinder.tick();
        Assertions.assertEquals(
                -0.45,
                pathfinder.getTranslation().vz()
        );

        odometry.setRawPosition(new PointXYZ(0, 0, 45));
        pathfinder.tick();
        Assertions.assertEquals(
                0,
                pathfinder.getTranslation().vz()
        );

        odometry.setRawPosition(new PointXYZ(0, 0, 90));
        pathfinder.tick();
        Assertions.assertEquals(
                0.45,
                pathfinder.getTranslation().vz()
        );

        odometry.setRawPosition(0, 5, 0);
        pathfinder.tick();
        Assertions.assertTrue(Equals.soft(-0.265, 
                    pathfinder.getTranslation().vz(), 0.01));

        odometry.setRawPosition(5, 5, 0);
        pathfinder.tick();
        Assertions.assertTrue(Equals.soft(-0.45, 
                    pathfinder.getTranslation().vz(), 0.01));
    }
}
