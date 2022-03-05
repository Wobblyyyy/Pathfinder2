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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;

/**
 * Thin wrapper for {@link Pathfinder} that utilizes the {@code Subsystem}
 * functionality of wpilib. Very simply, this just calls Pathfinder's
 * {@link Pathfinder#tick()} method in the {@link #periodic()} method.
 *
 * <p>
 * You can access Pathfinder via the {@link #getPathfinder()} method,
 * essentially allowing you to never have to worry about instantiation of
 * Pathfinder if you use the {@link PathfinderSubsystem#PathfinderSubsystem(Drive, Odometry, double)}
 * constructor.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class PathfinderSubsystem extends SubsystemBase {
    private final Pathfinder pathfinder;

    /**
     * Create a new {@code PathfinderSubsystem}. This will also initialize
     * an instance of Pathfinder that can be accessed with
     * {@link #getPathfinder()}.
     *
     * @param drive           the robot's drive system.
     * @param odometry        the robot's odometry system.
     * @param turnCoefficient the robot's turn coefficient.
     */
    public PathfinderSubsystem(
        Drive drive,
        Odometry odometry,
        double turnCoefficient
    ) {
        this(new Pathfinder(new Robot(drive, odometry), turnCoefficient));
    }

    /**
     * Create a new {@code PathfinderSubsystem}.
     *
     * @param pathfinder the instance of Pathfinder the subsystem uses.
     */
    public PathfinderSubsystem(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    /**
     * Called periodically (shocking, I know). This simply ticks Pathfinder
     * once.
     */
    @Override
    public void periodic() {
        pathfinder.tick();
    }

    /**
     * Get the instance of Pathfinder being used by the
     * {@code PathfinderSubsystem}.
     *
     * @return the instance of Pathfinder being used by the
     * {@code PathfinderSubsystem}.
     */
    public Pathfinder getPathfinder() {
        return pathfinder;
    }
}
