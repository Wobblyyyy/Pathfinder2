/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.commands;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.FastTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

public class TrajectoryCommands {

    private TrajectoryCommands() {}

    public static Command LINEAR_TRAJECTORY_COMMAND = new Command(
        "linearTrajectory",
        (pathfinder, args) -> {
            PointXYZ target = PointXYZ.parse(args[0]);
            double speed = Double.parseDouble(args[1]);
            double tolerance = Double.parseDouble(args[2]);
            Angle angleTolerance = Angle.parse(args[3]);

            Trajectory trajectory = new LinearTrajectory(
                target,
                speed,
                tolerance,
                angleTolerance
            );

            pathfinder.followTrajectory(trajectory);
        },
        4
    );

    public static Command FAST_TRAJECTORY_COMMAND = new Command(
        "fastTrajectory",
        (pathfinder, args) -> {
            PointXYZ initial;
            int startIdx = 0;
            if (args.length == 2) {
                initial = pathfinder.getPosition();
            } else {
                initial = PointXYZ.parse(args[0]);
                startIdx = 1;
            }
            PointXYZ target = PointXYZ.parse(args[startIdx]);
            double speed = Double.parseDouble(args[startIdx + 1]);

            Trajectory trajectory = new FastTrajectory(initial, target, speed);

            pathfinder.followTrajectory(trajectory);
        },
        2,
        3
    );

    public static void addTrajectoryCommands(CommandRegistry registry) {
        registry.unsafeAdd(LINEAR_TRAJECTORY_COMMAND);
        registry.unsafeAdd(FAST_TRAJECTORY_COMMAND);
    }
}
