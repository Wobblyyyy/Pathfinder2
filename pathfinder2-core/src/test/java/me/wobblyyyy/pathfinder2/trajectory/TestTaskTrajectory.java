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

import java.util.concurrent.atomic.AtomicBoolean;
import me.wobblyyyy.pathfinder2.TestableRobot;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTaskTrajectory extends TestableRobot {

    @Test
    public void testSimpleTaskTrajectory() {
        AtomicBoolean b = new AtomicBoolean(false);

        Trajectory trajectory = new TaskTrajectoryBuilder()
            .setInitial(() -> b.set(true))
            .setIsFinished(b::get)
            .build();

        testTrajectory(trajectory, pathfinder.getPosition(), 10);
    }

    @Test
    public void testMinTimeTaskTrajectory() {
        ElapsedTimer timer = new ElapsedTimer(true);
        Trajectory trajectory = new TaskTrajectoryBuilder()
            .setMinTimeMs(10)
            .setIsFinished(() -> timer.elapsedMs() > 10)
            .build();

        testTrajectory(trajectory, pathfinder.getPosition(), 100);
        Assertions.assertTrue(timer.elapsedMs() > 10);
    }

    @Test
    public void testMaxTimeTaskTrajectory() {
        ElapsedTimer timer = new ElapsedTimer(true);
        Trajectory trajectory = new TaskTrajectoryBuilder()
            .setMaxTimeMs(10)
            .setIsFinished(() -> false)
            .build();

        testTrajectory(trajectory, pathfinder.getPosition(), 100);
        Assertions.assertTrue(timer.elapsedMs() < 10);
    }
}
