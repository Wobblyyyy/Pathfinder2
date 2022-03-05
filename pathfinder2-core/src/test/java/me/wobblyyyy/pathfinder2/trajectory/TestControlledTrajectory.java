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
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.PIDController;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Test;

public class TestControlledTrajectory extends GenericTrajectoryTester {
    private final Controller controllerA = new ProportionalController(1.0);
    private final Controller controllerB = new PIDController(1.0, 0, 0);
    private final Controller controllerC = new PIDController(0, 0.5, 0.5);
    private final Controller controllerD = new PIDController(0.5, 0.5, 2.0);
    private final Controller controllerE = new ProportionalController(0.1);
    private final Controller controllerF = new ProportionalController(3.0);

    private void testSingleController(
        Controller controller,
        PointXYZ target,
        double tolerance,
        Angle angleTolerance
    ) {
        PointXYZ origin = pathfinder.getPosition();

        Trajectory toTarget = new ControlledTrajectory(
            target,
            controller,
            tolerance,
            angleTolerance
        );
        Trajectory toOrigin = new ControlledTrajectory(
            origin,
            controller,
            tolerance,
            angleTolerance
        );

        follow(toTarget, target);
        follow(toOrigin, origin);
    }

    private void testPoint(PointXYZ target) {
        testSingleController(controllerA, target, tolerance, angleTolerance);
        testSingleController(controllerB, target, tolerance, angleTolerance);
        testSingleController(controllerC, target, tolerance, angleTolerance);
        testSingleController(controllerD, target, tolerance, angleTolerance);
        testSingleController(controllerE, target, tolerance, angleTolerance);
        testSingleController(controllerF, target, tolerance, angleTolerance);
    }

    private void testMultiples(double x, double y, double... zs) {
        for (double z : zs) for (int i = 0; i < 10; i++) testPoint(
            new PointXYZ(x * i, y * i, z * i)
        );
    }

    @Test
    public void testForwardsNonTurningControlledTrajectory() {
        testMultiples(0, 1, 0);
    }

    @Test
    public void testRightwardsNonTurningControlledTrajectory() {
        testMultiples(1, 0, 0);
    }

    @Test
    public void testBackwardsNonTurningControlledTrajectory() {
        testMultiples(0, -1, 0);
    }

    @Test
    public void testLeftwardsNonTurningControlledTrajectory() {
        testMultiples(-1, 0, 0);
    }

    @Test
    public void testForwardsTurningControlledTrajectory() {
        testMultiples(0, 1, 15, 5, 30);
    }

    @Test
    public void testRightwardsTurningControlledTrajectory() {
        testMultiples(1, 0, 15, 5, 30);
    }

    @Test
    public void testBackwardsTurningControlledTrajectory() {
        testMultiples(0, -1, 15, 5, 30);
    }

    @Test
    public void testLeftwardsTurningControlledTrajectory() {
        testMultiples(-1, 0, 15, 5, 30);
    }
}
