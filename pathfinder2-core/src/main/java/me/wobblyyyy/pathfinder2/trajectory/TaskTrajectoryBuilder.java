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

/**
 * A builder for {@link TaskTrajectory}.
 *
 * @author Colin Robertson
 * @since 1.1.0
 */
public class TaskTrajectoryBuilder {
    private Runnable initial = () -> {};
    private Runnable during = () -> {};
    private Runnable onFinish = () -> {};
    private Supplier<Boolean> isFinished = null;
    private double minTimeMs = 0;
    private double maxTimeMs = Double.MAX_VALUE;

    public TaskTrajectoryBuilder setInitial(Runnable initial) {
        this.initial = initial;

        return this;
    }

    public TaskTrajectoryBuilder setDuring(Runnable during) {
        this.during = during;

        return this;
    }

    public TaskTrajectoryBuilder setOnFinish(Runnable onFinish) {
        this.onFinish = onFinish;

        return this;
    }

    public TaskTrajectoryBuilder setIsFinished(Supplier<Boolean> isFinished) {
        this.isFinished = isFinished;

        return this;
    }

    public TaskTrajectoryBuilder setMinTimeMs(double minTimeMs) {
        this.minTimeMs = minTimeMs;

        return this;
    }

    public TaskTrajectoryBuilder setMaxTimeMs(double maxTimeMs) {
        this.maxTimeMs = maxTimeMs;

        return this;
    }

    public TaskTrajectory build() {
        if (isFinished == null) throw new NullPointerException(
            "Cannot build a task trajectory " +
            "without first setting isFinished Supplier<Boolean>!"
        );

        return new TaskTrajectory(
            initial,
            during,
            onFinish,
            isFinished,
            minTimeMs,
            maxTimeMs
        );
    }
}
