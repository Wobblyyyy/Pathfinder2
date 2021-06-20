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

import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;

import java.util.List;
import java.util.function.Consumer;

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
     * A list of all of the followers the executor is responsible for.
     */
    private final List<Follower> followers;

    /**
     * Create a new {@code FollowerExecutor}.
     *
     * @param odometry  the odometry system the executor should use.
     * @param drive     the drivetrain the executor should use.
     * @param followers a list of followers the executor should execute.
     */
    public FollowerExecutor(Odometry odometry,
                            Drive drive,
                            List<Follower> followers) {
        this.odometry = odometry;
        this.drive = drive;
        this.followers = followers;
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
        // Before anything else, check to see if the followers list isn't
        // completely empty. If it IS empty, ignore the if statement and
        // return false.
        if (followers.size() > 0) {
            // Otherwise, get the current follower so we can do more stuff
            // with said follower.
            Follower current = followers.get(0);

            // noinspection StatementWithEmptyBody
            if (current.tick(odometry.getPosition(), drive::setTranslation)) {
                // If the tick method returns true, the follower has finished
                // its execution. We need to remove the follower from the list.
                followers.remove(current);
            } else {
                // If the tick method returns false, the follower has not
                // finished its execution. So we do... well, nothing.
                // Please note that there's no reason to have this else
                // statement here, other than telling the compiler that
                // it better execute this "else" statement.
            }

            // Returning FALSE indicates that the follower executor has NOT
            // finished executing all of its followers.
            return false;
        }

        // Returning TRUE indicates that the follower executor has finished
        // executing all of its followers.
        return true;
    }
}
