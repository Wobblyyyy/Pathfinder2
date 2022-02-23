/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import java.util.function.Supplier;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;

/**
 * A task-oriented {@code Trajectory} interface that does not require the
 * robot to move. Rather, a {@code TaskTrajectory} instructs the robot to
 * perform a task, and will block Pathfinder's queue until that task is
 * completed. This makes it easier to create simple autonomous programs.
 *
 * @author Colin Robertson
 * @since 1.1.0
 */
public class TaskTrajectory implements Trajectory {
    private final Runnable initial;
    private final Runnable during;
    private final Runnable onFinish;
    private final Supplier<Boolean> isFinished;
    private final double minTimeMs;
    private final double maxTimeMs;
    private final ElapsedTimer timer;
    private boolean hasExecuted = false;

    /**
     * Create a new {@code TaskTrajectory}.
     *
     * @param initial    code to be executed the first time the trajectory's
     *                   {@link #isDone(PointXYZ)} method is called.
     * @param during     code to be executed any time the trajectory's
     *                   {@link #isDone(PointXYZ)} method is called.
     * @param onFinish   code to be executed whenever the task is finished.
     * @param isFinished a supplier that indicates if the task is finished.
     *                   If the task is not finished, it should continue stop
     *                   its execution.
     * @param minTimeMs  the minimum time, in milliseconds, the trajectory
     *                   will be active for.
     * @param maxTimeMs  the maximum time, in milliseconds, the trajectory
     *                   will be active for.
     */
    public TaskTrajectory(Runnable initial,
                          Runnable during,
                          Runnable onFinish,
                          Supplier<Boolean> isFinished,
                          double minTimeMs,
                          double maxTimeMs) {
        this.initial = initial;
        this.during = during;
        this.onFinish = onFinish;
        this.isFinished = isFinished;
        this.minTimeMs = minTimeMs;
        this.maxTimeMs = maxTimeMs;
        this.timer = new ElapsedTimer();
    }

    public TaskTrajectory(Runnable initial,
                          Runnable during,
                          Runnable onFinish,
                          Supplier<Boolean> isFinished,
                          double maxTimeMs) {
        this(initial, during, onFinish, isFinished, 0, maxTimeMs);
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return current;
    }

    @Override
    public boolean isDone(PointXYZ current) {
        boolean isDone = isFinished.get();

        double elapsedTime = timer.elapsedMs();
        boolean validMin = elapsedTime > minTimeMs;
        boolean validMax = elapsedTime < maxTimeMs;

        if (!validMin)
            isDone = false;

        if (!validMax)
            isDone = true;

        if (!hasExecuted) {
            timer.start();
            initial.run();
            hasExecuted = true;
        }

        during.run();

        if (isDone)
            onFinish.run();

        return isDone;
    }

    @Override
    public double speed(PointXYZ current) {
        return 0;
    }
}
