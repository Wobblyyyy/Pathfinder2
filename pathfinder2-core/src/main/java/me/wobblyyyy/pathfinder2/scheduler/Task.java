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

import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

public class Task {
    private Trajectory trajectory;

    private double minTimeMilliseconds;

    private double maxTimeMilliseconds;

    private double startTimeMilliseconds = 0;

    public Task(Trajectory trajectory,
                double minTimeMilliseconds,
                double maxTimeMilliseconds) {

    }

    public static Task newTask(Trajectory trajectory,
                               double minTimeMilliseconds,
                               double maxTimeMilliseconds) {
        return new Task(
                trajectory,
                minTimeMilliseconds,
                maxTimeMilliseconds
        );
    }

    public static Task newUntimedTask(Trajectory trajectory) {
        return new Task(
                trajectory,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY
        );
    }

    public static Task newTaskWithMinimumTime(Trajectory trajectory,
                                              double minTimeMilliseconds) {
        return new Task(
                trajectory,
                minTimeMilliseconds,
                Double.POSITIVE_INFINITY
        );
    }

    public static Task newTaskWithMaximumTime(Trajectory trajectory,
                                              double maxTimeMilliseconds) {
        return new Task(
                trajectory,
                Double.NEGATIVE_INFINITY,
                maxTimeMilliseconds
        );
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    public double getMinTimeMilliseconds() {
        return this.minTimeMilliseconds;
    }

    public double getMaxTimeMilliseconds() {
        return this.maxTimeMilliseconds;
    }

    public double getStartTimeMilliseconds() {
        return startTimeMilliseconds;
    }

    public void start(double startTimeMilliseconds) {
        this.startTimeMilliseconds = startTimeMilliseconds;
    }

    private double calculateTimeDelta(double currentTimeMilliseconds) {
        return currentTimeMilliseconds - this.startTimeMilliseconds;
    }

    public boolean isMinimumTimeLimitValid(double currentTimeMilliseconds) {
        return calculateTimeDelta(currentTimeMilliseconds) >= minTimeMilliseconds;
    }

    public boolean isMaximumTimeLimitValid(double currentTimeMilliseconds) {
        return calculateTimeDelta(currentTimeMilliseconds) <= maxTimeMilliseconds;
    }

    public boolean areTimeLimitsValid(double currentTimeMilliseconds) {
        return isMaximumTimeLimitValid(currentTimeMilliseconds) &&
                isMaximumTimeLimitValid(currentTimeMilliseconds);
    }

    public boolean hasStarted() {
        return this.startTimeMilliseconds > 0;
    }
}
