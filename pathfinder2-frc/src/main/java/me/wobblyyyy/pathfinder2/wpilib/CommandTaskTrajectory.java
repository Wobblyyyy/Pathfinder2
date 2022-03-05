/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.wpilib;

import edu.wpi.first.wpilibj2.command.Command;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.TaskTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.TaskTrajectoryBuilder;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * Wrapper for an instance of {@link TaskTrajectory} that is linked to a
 * {@link Command}. Executing this trajectory will execute the command until
 * it's completed, or until the "end" method is called.
 *
 * @author Colin Robertson
 * @since 2.0.0
 * @see TaskTrajectory
 * @see TaskTrajectoryBuilder
 * @see Command
 */
public class CommandTaskTrajectory implements Trajectory {
    private final TaskTrajectory trajectory;

    /**
     * Create a new {@code CommandTaskTrajectory}.
     *
     * @param command the command to base the trajectory on. This
     *                {@code Trajectory}'s state is directly linked to the
     *                state of the provided {@code Command}.
     */
    public CommandTaskTrajectory(Command command) {
        trajectory = new TaskTrajectoryBuilder()
            .setInitial(command::initialize)
            .setDuring(command::execute)
            .setOnFinish(() -> command.end(false))
            .setIsFinished(command::isFinished)
            .build();
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return trajectory.nextMarker(current);
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return trajectory.isDone(current);
    }

    @Override
    public double speed(PointXYZ current) {
        return trajectory.speed(current);
    }

    /**
     * End the trajectory/command immediately.
     */
    public void end() {
        trajectory.end();
    }
}
