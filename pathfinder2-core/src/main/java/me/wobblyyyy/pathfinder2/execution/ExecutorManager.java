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
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;

import java.util.ArrayList;
import java.util.List;

/**
 * A manager, responsible for... well, managing executors. This manager
 * operates on a first-in, first-out basis. Basically, you can add a follower
 * executor to the queue of executors. The manager will continue executing
 * the executor that was added first until it's finished, and then it'll move
 * onto the next executor.
 *
 * <p>
 * There are only two ways you can interact with the queue of executors here.
 * <ul>
 *     <li>Add an executor by adding a list of followers.</li>
 *     <li>Clear the entire list of executors.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Executor managers are a relatively simple and limited method of managing
 * autonomous movement - this is completely intentional, as to not
 * overcomplicate usage of the library.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 * @see FollowerExecutor
 */
public class ExecutorManager {
    /**
     * The manager's odometry.
     */
    private final Odometry odometry;

    /**
     * The manager's drive.
     */
    private final Drive drive;

    /**
     * A list of follower executors. This list can be added to and cleared,
     * but otherwise, cannot be modified externally.
     */
    private final List<FollowerExecutor> executors = new ArrayList<>();

    /**
     * Create a new {@code ExecutorManager}. By default, new managers will
     * not have any executors.
     *
     * @param odometry the odometry system the executor should use.
     * @param drive    the drive system the executor should use.
     */
    public ExecutorManager(Odometry odometry,
                           Drive drive) {
        this.odometry = odometry;
        this.drive = drive;
    }

    /**
     * Add an executor to the manager. Because this manager operates on a
     * first-in, first-out model, the follower list you've added will be
     * executed after any other executors have finished.
     *
     * @param followers a list of followers. These followers will be used to
     *                  create a new {@link FollowerExecutor}.
     */
    public void addExecutor(List<Follower> followers) {
        executors.add(
                new FollowerExecutor(
                        odometry,
                        drive,
                        followers
                )
        );
    }

    /**
     * Add an executor to the manager. Because this manager operates on a
     * first-in, first-out model, the follower you've added will be executed
     * after any other executors have finished.
     *
     * @param follower a single follower to add.
     */
    public void addExecutor(Follower follower) {
        executors.add(
                new FollowerExecutor(
                        odometry,
                        drive,
                        follower
                )
        );
    }

    /**
     * Clear the list of {@link FollowerExecutor}s.
     *
     * <p>
     * This will, in effect, stop the robot. Because there's no longer any
     * followers to execute, Pathfinder will do... just about nothing. It's
     * still important that you actually tick Pathfinder after clearing the
     * executors - this will stop the robot from moving.
     * </p>
     */
    public void clearExecutors() {
        executors.clear();
    }

    /**
     * Is the executor manager active?
     *
     * @return true if the executor manager has at least 1 executor in queue.
     * False if not.
     */
    public boolean isActive() {
        return executors.size() > 0;
    }

    /**
     * The opposite of the {@link #isActive()} method.
     *
     * @return the opposite of the {@link #isActive()} method.
     */
    public boolean isInactive() {
        return !(executors.size() > 0);
    }

    /**
     * Tick the {@link ExecutorManager} once. The tick method returns a boolean
     * value indicating the status of the {@code ExecutorManager}. If the
     * method returns true, the {@code ExecutorManager} has finished its
     * execution - this means all the {@link FollowerExecutor}s have finished
     * their execution. If the method returns false, the {@code ExecutorManager}
     * hasn't yet finished its execution.
     *
     * @return true if there are no remaining executors in the list. False if
     * there are still remaining executors.
     */
    public boolean tick() {
        // If there's at least 1 executor in the queue...
        if (howManyExecutors() > 0) {
            FollowerExecutor executor = executors.get(0);

            // Tick the executor. The tick method returns a boolean result.
            // If true, the executor is finished. If false, it's not finished.
            if (executor.tick()) {
                // If the executor's finished, we no longer need it.
                executors.remove(executor);
            }
        }

        // Return true if there's 0 remaining executors (meaning we're entirely
        // finished) and false if there's 1 or more.
        return howManyExecutors() == 0;
    }

    /**
     * Get a count of how many executors there are.
     *
     * @return how many executors there are.
     */
    public int howManyExecutors() {
        return executors.size();
    }
}
