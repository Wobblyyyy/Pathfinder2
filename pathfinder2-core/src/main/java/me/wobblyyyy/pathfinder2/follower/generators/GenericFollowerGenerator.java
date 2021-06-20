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

public class GenericFollowerGenerator implements FollowerGenerator {
    private final Controller turnController;

    public GenericFollowerGenerator(Controller turnController) {
        this.turnController = turnController;
    }

    @Override
    public Follower generate(Robot robot, Trajectory trajectory) {
        return new GenericFollower(
                trajectory,
                turnController
        );
    }
}
