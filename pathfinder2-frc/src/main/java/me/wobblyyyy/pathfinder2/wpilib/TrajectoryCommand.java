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

import edu.wpi.first.wpilibj2.command.CommandBase;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A simple command-based wrapper for Pathfinder's ticking system. This command
 * follows a {@link Trajectory} injected via the constructor when the
 * {@link #initialize()} method is called. The {@link #execute()} method will
 * simply use {@link Pathfinder#tick()}. Calling {@link #end(boolean)} will
 * clear Pathfinder, tick it once, and set its translation to (0, 0, 0). This
 * command is considered to be finished if Pathfinder is NOT active and the
 * {@link #initialize()} method has already been called.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class TrajectoryCommand extends CommandBase {
    private final Pathfinder pathfinder;
    private final Trajectory trajectory;
    private boolean hasStarted = false;

    /**
     * Create a new {@code TrajectoryCommand}.
     *
     * @param pathfinderSubsystem the Pathfinder subsystem.
     * @param trajectory          the trajectory to follow.
     */
    public TrajectoryCommand(PathfinderSubsystem pathfinderSubsystem,
                             Trajectory trajectory) {
        this.pathfinder = pathfinderSubsystem.getPathfinder();
        this.trajectory = trajectory;

        addRequirements(pathfinderSubsystem);
    }

    @Override
    public void initialize() {
        pathfinder.followTrajectory(trajectory);
        hasStarted = true;
    }

    @Override
    public void end(boolean interrupted) {
        pathfinder.clear();
        pathfinder.tick();
        pathfinder.setTranslation(Translation.ZERO);
    }

    @Override
    public boolean isFinished() {
        return hasStarted && !pathfinder.isActive();
    }
}
