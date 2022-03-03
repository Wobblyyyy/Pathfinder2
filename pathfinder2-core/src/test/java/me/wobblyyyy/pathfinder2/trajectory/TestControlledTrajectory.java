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
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.PIDController;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class TestControlledTrajectory extends GenericTrajectoryTester {
    private final Controller controllerA = new ProportionalController(1.0);
    private final Controller controllerB = new PIDController(1.0, 0, 0);
    private final Controller controllerC = new PIDController(0, 0.5, 0.5);
    private final Controller controllerD = new PIDController(0.5, 0.5, 2.0);

    private final Controller[] controllers = new Controller[] {
            controllerA,
            controllerB,
            controllerC,
            controllerD
    };

    private void testSingleController(Controller controller,
                                      PointXYZ target,
                                      double tolerance,
                                      Angle angleTolerance) {
        PointXYZ origin = pathfinder.getPosition();

        Trajectory toTarget = new ControlledTrajectory(target, controller,
                tolerance, angleTolerance);
        Trajectory toOrigin = new ControlledTrajectory(origin, controller,
                tolerance, angleTolerance);

        follow(toTarget, target);
        follow(toOrigin, origin);
    }

    private void testPoint(PointXYZ target) {
        testSingleController(controllerA, target,
                tolerance, angleTolerance);
        testSingleController(controllerB, target,
                tolerance, angleTolerance);
        testSingleController(controllerC, target,
                tolerance, angleTolerance);
        testSingleController(controllerD, target,
                tolerance, angleTolerance);
    }

    @Test
    public void testForwardsNonTurningControlledTrajectory() {
        testPoint(new PointXYZ(0, 10, 0));
    }

    @Test
    public void testRightwardsNonTurningControlledTrajectory() {
        testPoint(new PointXYZ(10, 0, 0));
    }

    @Test
    public void testBackwardsNonTurningControlledTrajectory() {
        testPoint(new PointXYZ(0, -10, 0));
    }

    @Test
    public void testLeftwardsNonTurningControlledTrajectory() {
        testPoint(new PointXYZ(-10, 0, 0));
    }

    @Test
    public void testForwardsTurningControlledTrajectory() {
        testPoint(new PointXYZ(0, 10, 45));
        testPoint(new PointXYZ(0, 10, 90));
    }

    @Test
    public void testRightwardsTurningControlledTrajectory() {
        testPoint(new PointXYZ(10, 0, 45));
        testPoint(new PointXYZ(10, 0, 90));
    }

    @Test
    public void testBackwardsTurningControlledTrajectory() {
        testPoint(new PointXYZ(0, -10, 45));
        testPoint(new PointXYZ(0, -10, 90));
    }

    @Test
    public void testLeftwardsTurningControlledTrajectory() {
        testPoint(new PointXYZ(-10, 0, 45));
        testPoint(new PointXYZ(-10, 0, 90));
    }
}
