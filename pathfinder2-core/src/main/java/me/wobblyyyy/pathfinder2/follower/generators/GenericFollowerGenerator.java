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
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.follower.GenericFollower;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

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
     *                       controller will be used for all of the generated
     *                       {@link GenericFollower}s.
     */
    public GenericFollowerGenerator(Controller turnController) {
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
    public Follower generate(Robot robot,
                             Trajectory trajectory) {
        return new GenericFollower(
                trajectory,
                turnController
        );
    }
}
