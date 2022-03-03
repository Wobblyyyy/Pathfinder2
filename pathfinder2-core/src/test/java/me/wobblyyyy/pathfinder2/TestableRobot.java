/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedRobot;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

@TestInstance(Lifecycle.PER_CLASS)
public class TestableRobot {
    public SimulatedRobot robot = new SimulatedRobot();
    public double tolerance = 2;
    public Angle angleTolerance = Angle.fromDeg(5);
    public double speed = 0.5;
    public double turnCoefficient = -0.05;
    public Pathfinder pathfinder;

    @BeforeAll
    public void beforeAll() {
        robot = new SimulatedRobot();
    }

    @BeforeEach
    public void beforeEach() {
        pathfinder = new Pathfinder(robot, turnCoefficient);
    }

    @AfterEach
    public void afterEach() {
        robot.setPosition(new PointXYZ());
    }

    public void testTrajectory(Trajectory trajectory,
                               PointXYZ target) {
        ValidationUtils.validate(trajectory, "trajectory");
        ValidationUtils.validate(target, "target");

        pathfinder.followTrajectory(trajectory);

        pathfinder.tickUntil();

        AssertionUtils.assertIsNear(
                target,
                pathfinder.getPosition(),
                tolerance,
                angleTolerance
        );
    }
}
