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

import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.GenericTrajectoryTester;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class TestSequentialLinearTrajectory extends GenericTrajectoryTester {
    private void testTrajectoryTo(PointXYZ target) {
        Trajectory trajectory = new LinearTrajectory(target, speed,
                tolerance, angleTolerance);
        follow(trajectory, target);
    }

    @Test
    public void testSingleLinearTrajectoryForwards() {
        testTrajectoryTo(new PointXYZ(0, 10, 0));
    }

    @Test
    public void testSingleLinearTrajectoryBackwards() {
        testTrajectoryTo(new PointXYZ(0, -10, 0));
    }

    @Test
    public void testSingleLinearTrajectoryRightwards() {
        testTrajectoryTo(new PointXYZ(10, 0, 0));
    }

    @Test
    public void testSingleLinearTrajectoryLeftwards() {
        testTrajectoryTo(new PointXYZ(-10, 0, 0));
    }
}
