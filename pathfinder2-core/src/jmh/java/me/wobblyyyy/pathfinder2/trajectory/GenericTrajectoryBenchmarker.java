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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedRobot;
import org.openjdk.jmh.infra.Blackhole;

public class GenericTrajectoryBenchmarker {

    public void followTrajectories(
        Blackhole blackhole,
        Trajectory... trajectories
    ) {
        SimulatedRobot robot = new SimulatedRobot();
        Pathfinder pathfinder = new Pathfinder(robot, -0.05);
        PointXYZ lastPosition = PointXYZ.ZERO;
        double distance = 0;

        for (Trajectory trajectory : trajectories) {
            robot.setPosition(PointXYZ.ZERO);
            pathfinder.followTrajectory(trajectory);

            while (distance < 10) {
                pathfinder.tick();
                distance += pathfinder.getPosition().absDistance(lastPosition);
            }
        }
    }
}
