/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.follower;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.GenericTurnController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGenericFollower {
    private static final PointXYZ target = new PointXYZ(10, 10, 90);

    private static final Trajectory trajectory = new LinearTrajectory(
        target,
        1.0,
        1.0,
        Angle.fromDeg(0.5)
    );

    private static final Controller turnController = new GenericTurnController(
        0.1
    );

    private void testTranslationConsumer(Translation translation) {
        Assertions.assertTrue(Equals.soft(45, translation.angle().deg(), 0.1));

        Assertions.assertTrue(Equals.soft(0.7, translation.vx(), 0.1));

        Assertions.assertTrue(Equals.soft(0.7, translation.vy(), 0.1));
    }

    @Test
    public void testTick() {
        Follower follower = new GenericFollower(trajectory, turnController);

        follower.tick(PointXYZ.zero(), this::testTranslationConsumer);
    }

    @Test
    public void testGetTrajectory() {
        Assertions.assertEquals(
            trajectory,
            new GenericFollower(trajectory, turnController).getTrajectory()
        );
    }
}
