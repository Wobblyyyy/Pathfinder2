/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.trajectory.FastTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building a series of points.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class PathBuilder {
    private final List<PointXYZ> targets;

    public PathBuilder() {
        this(10);
    }

    public PathBuilder(int initialSize) {
        targets = new ArrayList<>(initialSize);
    }

    public static List<Follower> followersFromTrajectories(Robot robot,
                                                           FollowerGenerator followerGenerator,
                                                           List<Trajectory> trajectories) {
        List<Follower> followers = new ArrayList<>(trajectories.size());

        for (Trajectory trajectory : trajectories) {
            followers.add(
                    followerGenerator.generate(
                            robot,
                            trajectory
                    )
            );
        }

        return followers;
    }

    public List<PointXYZ> getTargets() {
        return targets;
    }

    public List<Trajectory> linearPath(double speed,
                                       double tolerance,
                                       Angle angleTolerance) {
        List<Trajectory> trajectories = new ArrayList<>(targets.size());

        for (PointXYZ target : targets) {
            trajectories.add(
                    new LinearTrajectory(
                            target,
                            speed,
                            tolerance,
                            angleTolerance
                    )
            );
        }

        return trajectories;
    }

    public List<Trajectory> fastPath(PointXYZ start,
                                     double speed) {
        List<Trajectory> trajectories = new ArrayList<>(targets.size());
        PointXYZ previousPoint = start;

        for (PointXYZ target : targets) {
            trajectories.add(
                    new FastTrajectory(
                            previousPoint,
                            target,
                            speed
                    )
            );

            previousPoint = target;
        }

        return trajectories;
    }

    public List<Follower> linearPathFollowers(Robot robot,
                                              FollowerGenerator followerGenerator,
                                              double speed,
                                              double tolerance,
                                              Angle angleTolerance) {
        List<Trajectory> trajectories = linearPath(
                speed,
                tolerance,
                angleTolerance
        );

        return followersFromTrajectories(
                robot,
                followerGenerator,
                trajectories
        );
    }

    public List<Follower> fastPathFollowers(Robot robot,
                                            FollowerGenerator followerGenerator,
                                            double speed) {
        List<Trajectory> trajectories = fastPath(
                robot.odometry().getPosition(),
                speed
        );

        return followersFromTrajectories(
                robot,
                followerGenerator,
                trajectories
        );
    }

    public void addTarget(PointXYZ target) {
        targets.add(target);
    }

    public void addTarget(Angle angleToMoveAt,
                          double distanceToMove) {
        addTarget(
                targets.get(targets.size() - 1).inDirection(
                        distanceToMove,
                        angleToMoveAt
                )
        );
    }
}
