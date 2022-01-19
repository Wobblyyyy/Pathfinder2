/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.follower;

import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A functional interface used for generating followers. Pathfinder operates
 * based on a couple of core ideas - firstly, trajectories, and secondly,
 * followers. Pathfinder's movement is controlled via an executor manager,
 * which, in turn, controls several follower executors. Each of these follower
 * executors (you guessed it) executes a set of followers. Each of these
 * followers instructs the robot on how to follow a certain trajectory.
 *
 * <p>
 * Pathfinder can be commanded to follow a path using some of the methods
 * available in the {@link me.wobblyyyy.pathfinder2.Pathfinder} class.
 * However, Pathfinder can only actually interpret followers - not just
 * trajectories. A {@code FollowerGenerator} provides a way for Pathfinder
 * to interpret trajectories as followers, and thus, interpret trajectories
 * and move the robot accordingly.
 * </p>
 *
 * <p>
 * Included in the "see" tag of this class JavaDoc is a link to the "generic
 * follower generator." This is the simplest (and probably best) option when
 * it comes to generating followers. Instead of creating your own method of
 * creating new followers, the generic follower generator will produce generic
 * followers - pretty simple, right? Unless you have a very specific use case,
 * generic followers should work more than well enough.
 * </p>
 *
 * @author Colin Robertson
 * @see me.wobblyyyy.pathfinder2.follower.generators.GenericFollowerGenerator
 * @since 0.0.0
 */
@FunctionalInterface
public interface FollowerGenerator {
    /**
     * Generate a new follower, based on the robot the follower will be
     * acting upon and the trajectory that robot should follow.
     *
     * @param robot      the robot that this follower is acting upon. This
     *                   robot should be capable of providing positioning
     *                   using the {@link Robot#odometry()} method, as well
     *                   as be capable o providing movement options with the
     *                   {@link Robot#drive()} method.
     * @param trajectory the trajectory this follower should be following.
     *                   Each {@link Follower} should be responsible for
     *                   one single {@link Trajectory}, so a {@link Follower}
     *                   will be created for this {@link Trajectory}.
     * @return a newly-generated {@link Follower} based on the provided robot
     * and the provided trajectory. This {@link Follower} is responsible for
     * the execution of the inputted trajectory.
     */
    Follower generate(Robot robot,
                      Trajectory trajectory);
}
