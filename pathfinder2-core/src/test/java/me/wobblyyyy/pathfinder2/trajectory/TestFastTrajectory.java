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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;

public class TestFastTrajectory {
    private static void setPos(SimulatedOdometry odometry,
                               PointXYZ position) {
        odometry.setRawPosition(position);
    }

    @Test
    public void testSimpleFastTrajectory() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        Trajectory trajectory = new FastTrajectory(
                pathfinder.getPosition(), 
                new PointXYZ(10, 10, 45), 
                0.5
        );
        pathfinder.followTrajectory(trajectory);
        SimulatedOdometry odometry = (SimulatedOdometry) pathfinder.getOdometry();

        setPos(odometry, new PointXYZ(0, 0, 0));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        setPos(odometry, new PointXYZ(5, 5, 15));
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());

        setPos(odometry, new PointXYZ(10, 10, 35));
        pathfinder.tick();
        Assertions.assertFalse(pathfinder.isActive());
    }
}
