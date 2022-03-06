/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.follower.generators;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.exceptions.NullControllerException;
import me.wobblyyyy.pathfinder2.exceptions.NullDriveException;
import me.wobblyyyy.pathfinder2.exceptions.NullOdometryException;
import me.wobblyyyy.pathfinder2.exceptions.NullTrajectoryException;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.follower.GenericFollower;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A {@link FollowerGenerator} that generates a lovely {@link GenericFollower}.
 * For almost all use cases, a generic follower should work perfectly fine.
 * This class produces generic followers by accepting a robot and a trajectory
 * as parameters.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class GenericFollowerGenerator implements FollowerGenerator {
    /**
     * The follower's turn controller.
     */
    private final Controller turnController;

    /**
     * Create a new {@code GenericFollowerGenerator}. The purpose of this
     * class is to provide a functional interface
     * ({@link #generate(Robot, Trajectory)}) capable of generating trajectory
     * followers.
     *
     * @param turnController the generator's turn controller. This turn
     *                       controller will be used for all the generated
     *                       {@link GenericFollower}s.
     */
    public GenericFollowerGenerator(Controller turnController) {
        if (turnController == null) {
            throw new NullControllerException(
                "Can't create a generic follower generator with " +
                "a null turn controller!"
            );
        }
        this.turnController = turnController;
    }

    /**
     * Create a new {@link GenericFollower}, given a robot and a trajectory.
     *
     * @param robot      the robot the follower is acting upon.
     * @param trajectory the trajectory the follower should follow.
     * @return a new {@link GenericFollower}.
     */
    @Override
    public Follower generate(Robot robot, Trajectory trajectory) {
        if (robot.odometry() == null) {
            throw new NullOdometryException(
                "Can't generate a follower with null Odometry!"
            );
        }

        if (robot.drive() == null) {
            throw new NullDriveException(
                "Can't generate a follower with null Drive"
            );
        }

        if (trajectory == null) {
            throw new NullTrajectoryException(
                "Can't generate a follower with a null Trajectory!"
            );
        }

        return new GenericFollower(trajectory, turnController);
    }

    public Controller getTurnController() {
        return turnController;
    }

    @Override
    public String toString() {
        return StringUtils.format(
            "GenericFollowerGenerator (turn controller: <%s>)",
            turnController
        );
    }
}
