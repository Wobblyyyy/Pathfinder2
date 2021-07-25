/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.scheduler;

import me.wobblyyyy.pathfinder2.trajectory.EmptyTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * An instruction or {@code Task} that can be used in conjunction with
 * a {@link Scheduler} to automate Pathfinder's operation.
 *
 * <p>
 * If you have an autonomous routine where carefully-timed execution is
 * important, you can make use of a scheduler to manage that time for you,
 * all from within Pathfinder.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Task {
    /**
     * The trajectory the task is responsible for executing.
     */
    private final Trajectory trajectory;

    /**
     * The task's minimum time.
     */
    private final double minTimeMilliseconds;

    /**
     * The task's maximum time.
     */
    private final double maxTimeMilliseconds;

    /**
     * The time at which the task was started. If the task has not
     * been started, this value is 0.
     */
    private double startTimeMilliseconds = 0;

    /**
     * Create a new {@link Task}.
     *
     * @param trajectory          the trajectory the task is responsible for
     *                            following. this can be a non-functional
     *                            trajectory if you'd like.
     * @param minTimeMilliseconds the minimum amount of time (in milliseconds)
     *                            that the task must take to execute.
     * @param maxTimeMilliseconds the maximum amount of time (in milliseconds)
     *                            that the task is allowed to execute.
     * @see #newTask(Trajectory, double, double)
     * @see #newUntimedTask(Trajectory)
     * @see #newTaskWithMinimumTime(Trajectory, double)
     * @see #newTaskWithMaximumTime(Trajectory, double)
     * @see #newWaitTask(double)
     */
    public Task(Trajectory trajectory,
                double minTimeMilliseconds,
                double maxTimeMilliseconds) {
        this.trajectory = trajectory;
        this.minTimeMilliseconds = minTimeMilliseconds;
        this.maxTimeMilliseconds = maxTimeMilliseconds;
    }

    /**
     * Create a new {@link Task}.
     *
     * @param trajectory          the trajectory the task is responsible for
     *                            following. this can be a non-functional
     *                            trajectory if you'd like.
     * @param minTimeMilliseconds the minimum amount of time (in milliseconds)
     *                            that the task must take to execute.
     * @param maxTimeMilliseconds the maximum amount of time (in milliseconds)
     *                            that the task is allowed to execute.
     * @see #newTask(Trajectory, double, double)
     * @see #newUntimedTask(Trajectory)
     * @see #newTaskWithMinimumTime(Trajectory, double)
     * @see #newTaskWithMaximumTime(Trajectory, double)
     * @see #newWaitTask(double)
     */
    public static Task newTask(Trajectory trajectory,
                               double minTimeMilliseconds,
                               double maxTimeMilliseconds) {
        return new Task(
                trajectory,
                minTimeMilliseconds,
                maxTimeMilliseconds
        );
    }

    /**
     * Create a new task.
     *
     * @param trajectory the trajectory the task is responsible for
     *                   executing.
     * @return a new untimed task.
     * @see #newTask(Trajectory, double, double)
     * @see #newUntimedTask(Trajectory)
     * @see #newTaskWithMinimumTime(Trajectory, double)
     * @see #newTaskWithMaximumTime(Trajectory, double)
     * @see #newWaitTask(double)
     */
    public static Task newUntimedTask(Trajectory trajectory) {
        return new Task(
                trajectory,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY
        );
    }

    /**
     * Create a new task.
     *
     * @param trajectory          the trajectory the task is responsible
     *                            for executing.
     * @param minTimeMilliseconds the minimum amount of time (in milliseconds)
     *                            that the task is REQUIRED to take before
     *                            its execution finishes.
     * @return a new task, with a minimum time.
     */
    public static Task newTaskWithMinimumTime(Trajectory trajectory,
                                              double minTimeMilliseconds) {
        return new Task(
                trajectory,
                minTimeMilliseconds,
                Double.POSITIVE_INFINITY
        );
    }

    /**
     * Create a new task.
     *
     * @param trajectory          the trajectory the task is responsible
     *                            for executing.
     * @param maxTimeMilliseconds the maximum amount of time (in milliseconds)
     *                            that the task is ALLOWED to take before it
     *                            is forcibly marked as finished.
     * @return a new task, with a maximum time.
     */
    public static Task newTaskWithMaximumTime(Trajectory trajectory,
                                              double maxTimeMilliseconds) {
        return new Task(
                trajectory,
                Double.NEGATIVE_INFINITY,
                maxTimeMilliseconds
        );
    }

    /**
     * Create a new task. This task does not move the robot - rather, it
     * waits for a certain amount of time before being marked as done.
     * This can be used like the {@link Thread#sleep(long)} method.
     *
     * @param howLongToWaitMilliseconds how long the wait task should be.
     * @return a new task with a specified wait length.
     */
    public static Task newWaitTask(double howLongToWaitMilliseconds) {
        return new Task(
                new EmptyTrajectory(),
                howLongToWaitMilliseconds,
                howLongToWaitMilliseconds + 1
        );
    }

    /**
     * Get the task's {@code Trajectory}.
     *
     * @return the task's {@code Trajectory}
     */
    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    /**
     * Get the minimum amount of time the task is allowed to use.
     *
     * @return the minimum amount of time the task is allowed to use.
     */
    public double getMinTimeMilliseconds() {
        return this.minTimeMilliseconds;
    }

    /**
     * Get the maximum amount of time the task is allowed to use.
     *
     * @return the maximum amount of time the task is allowed to use.
     */
    public double getMaxTimeMilliseconds() {
        return this.maxTimeMilliseconds;
    }

    /**
     * Get the task's start time, in milliseconds.
     *
     * @return the task's start time, in milliseconds. If the task has not
     * yet been started, this method will return 0.
     */
    public double getStartTimeMilliseconds() {
        return startTimeMilliseconds;
    }

    /**
     * "Start" the task. This should almost always be handled by a
     * {@link Scheduler}, not manually.
     *
     * @param startTimeMilliseconds the time the task is starting at.
     */
    public void start(double startTimeMilliseconds) {
        this.startTimeMilliseconds = startTimeMilliseconds;
    }

    /**
     * Calculate the difference (in milliseconds) between the current time
     * and the start time.
     *
     * @param currentTimeMilliseconds the current time, in milliseconds.
     * @return the difference (in milliseconds) between the current time
     * and the task's start time.
     */
    private double calculateTimeDelta(double currentTimeMilliseconds) {
        return currentTimeMilliseconds - this.startTimeMilliseconds;
    }

    /**
     * Is the minimum time limit valid?
     *
     * @param currentTimeMilliseconds the current time, in milliseconds.
     * @return whether or not the minimum time limit is valid.
     */
    public boolean isMinimumTimeLimitValid(double currentTimeMilliseconds) {
        return calculateTimeDelta(currentTimeMilliseconds) >= minTimeMilliseconds;
    }

    /**
     * Is the maximum time limit valid?
     *
     * @param currentTimeMilliseconds the current time, in milliseconds.
     * @return whether or not the maximum time limit is valid.
     */
    public boolean isMaximumTimeLimitValid(double currentTimeMilliseconds) {
        return calculateTimeDelta(currentTimeMilliseconds) <= maxTimeMilliseconds;
    }

    /**
     * Are both time limits valid?
     *
     * @param currentTimeMilliseconds the current time, in milliseconds.
     * @return whether or not both time limits are valid.
     * @see #isMinimumTimeLimitValid(double)
     * @see #isMaximumTimeLimitValid(double)
     */
    public boolean areTimeLimitsValid(double currentTimeMilliseconds) {
        return isMaximumTimeLimitValid(currentTimeMilliseconds) &&
                isMaximumTimeLimitValid(currentTimeMilliseconds);
    }

    /**
     * Has the task started yet?
     *
     * @return whether or not the task has started yet.
     */
    public boolean hasStarted() {
        return this.startTimeMilliseconds > 0;
    }
}
