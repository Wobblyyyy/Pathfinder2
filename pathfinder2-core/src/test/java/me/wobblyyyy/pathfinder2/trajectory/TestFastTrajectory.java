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

import me.wobblyyyy.pathfinder2.GenericTrajectoryTester;
import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestFastTrajectory extends GenericTrajectoryTester {
    private void testFastTrajectory(PointXYZ target) {
        Trajectory trajectory = new FastTrajectory(pathfinder.getPosition(),
                target, speed);
        pathfinder.followTrajectory(trajectory);
        pathfinder.tickUntil(1_000);
        assertPositionIs(target);
    }

    private void testFastTrajectories(PointXYZ... targets) {
        for (PointXYZ target : targets)
            testFastTrajectory(target);
    }

    @Test
    public void testForwardsFastTrajectory() {
        testFastTrajectory(new PointXYZ(0, 10, 0));
    }

    @Test
    public void testRightwardsFastTrajectory() {
        testFastTrajectory(new PointXYZ(10, 0, 0));
    }

    @Test
    public void testBackwardsFastTrajectory() {
        testFastTrajectory(new PointXYZ(0, 10, 0));
    }

    @Test
    public void testLeftwardsFastTrajectory() {
        testFastTrajectory(new PointXYZ(-10, 0, 0));
    }

    @Test
    public void testAngledTrajectories() {
        testFastTrajectories(
                new PointXYZ(10, 10, 0),
                new PointXYZ(-10, 10, 0),
                new PointXYZ(-10, -10, 0),
                new PointXYZ(10, -10, 0),
                new PointXYZ(0, 0, 0)
        );
    }

    @Test
    public void testThrowsExceptions() {
        Assertions.assertThrows(NullPointException.class, () -> {
            new FastTrajectory(null, new PointXYZ(), 0.5);
        });

        Assertions.assertThrows(NullPointException.class, () -> {
            new FastTrajectory(new PointXYZ(), null, 0.5);
        });

        Assertions.assertThrows(InvalidSpeedException.class, () -> {
            new FastTrajectory(new PointXYZ(), new PointXYZ(), -100);
        });

        Assertions.assertThrows(InvalidSpeedException.class, () -> {
            new FastTrajectory(new PointXYZ(), new PointXYZ(), 200);
        });
    }
}
