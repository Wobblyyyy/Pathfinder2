/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * An executor that executes followers. I know, I know - it's pretty hard to
 * interpret what "follower executor" means, but yes, it does, in fact,
 * execute followers. Lots of them. Or few of them. It really depends, you
 * know?
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class FollowerExecutor {
    /**
     * The odometry system that the executor should use to tick followers.
     */
    private final Odometry odometry;

    /**
     * The drivetrain the executor should use to move.
     */
    private final Drive drive;

    /**
     * A list of all the followers the executor is responsible for.
     */
    private final List<Follower> followers;

    /**
     * Create a new {@code FollowerExecutor}.
     *
     * @param odometry  the odometry system the executor should use.
     * @param drive     the drivetrain the executor should use.
     * @param followers a list of followers the executor should execute.
     */
    public FollowerExecutor(
        Odometry odometry,
        Drive drive,
        List<Follower> followers
    ) {
        ValidationUtils.validate(odometry, "odometry");
        ValidationUtils.validate(drive, "drive");
        ValidationUtils.validate(followers, "followers");

        for (Follower follower : followers) ValidationUtils.validate(follower);

        this.odometry = odometry;
        this.drive = drive;
        this.followers = followers;
    }

    /**
     * Create a new {@code FollowerExecutor}.
     *
     * @param odometry the odometry system the executor should use.
     * @param drive    the drivetrain the executor should use.
     * @param follower the follower to execute.
     */
    public FollowerExecutor(Odometry odometry, Drive drive, Follower follower) {
        this(
            odometry,
            drive,
            new ArrayList<Follower>() {

                {
                    add(follower);
                }
            }
        );
    }

    private boolean tickCurrentFollower() {
        return Follower.tickFollower(
            followers.get(0),
            odometry::getPosition,
            drive::setTranslation
        );
    }

    private void internalTick() {
        if (howManyFollowers() > 0) if (tickCurrentFollower()) followers.remove(
            0
        );
    }

    /**
     * Tick the follower executor. Ticking the executor will look at the
     * first {@link Follower} in the {@link #followers} list and tick it
     * using the {@link Follower#tick(PointXYZ, Consumer)} method. If the
     * tick method returns true, we know that the follower is done, so we
     * should remove it from the list. If it returns false, that follower
     * needs to continue being executed.
     *
     * @return true if the follower executor has finished executing all of its
     * followers. False if the follower executor has not finished executing
     * all of its followers.
     */
    public boolean tick() {
        internalTick();

        return followers.size() == 0;
    }

    public Follower getCurrentFollower() {
        return followers.get(0);
    }

    public int howManyFollowers() {
        return followers.size();
    }
}
