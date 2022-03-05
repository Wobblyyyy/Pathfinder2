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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedRobot;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import me.wobblyyyy.pathfinder2.trajectory.EmptyTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class TestableRobot {
    public double maxTimeMs = 1_000;
    public SimulatedRobot robot = new SimulatedRobot();
    public double tolerance = 2;
    public Angle angleTolerance = Angle.fromDeg(5);
    public double speed = 0.5;
    public double turnCoefficient = -0.05;
    public Pathfinder pathfinder;
    private boolean hasBeforeEachBeenCalled = false;

    @BeforeAll
    public void beforeAll() {
        robot = new SimulatedRobot();
    }

    @BeforeEach
    public void beforeEach() {
        ValidationUtils.validate(maxTimeMs, "maxTimeMs");
        ValidationUtils.validate(robot, "robot");
        ValidationUtils.validate(tolerance, "tolerance");
        ValidationUtils.validate(angleTolerance, "angleTolerance");
        ValidationUtils.validate(speed, "speed");
        ValidationUtils.validate(turnCoefficient, "turnCoefficient");

        pathfinder = new Pathfinder(robot, turnCoefficient);

        ValidationUtils.validate(pathfinder, "pathfinder");

        hasBeforeEachBeenCalled = true;
    }

    @AfterEach
    public void afterEach() {
        robot.setPosition(new PointXYZ());
        
        hasBeforeEachBeenCalled = false;
    }

    public void testTrajectory(
        Trajectory trajectory,
        PointXYZ target,
        double maxTimeMs
    ) {
        if (!hasBeforeEachBeenCalled) throw new RuntimeException(
            "Did not call beforeEach method! Try super.beforeEach()?"
        );

        ValidationUtils.validate(trajectory, "trajectory");
        ValidationUtils.validate(target, "target");

        pathfinder.followTrajectory(trajectory);

        ElapsedTimer timer = new ElapsedTimer(true);

        pathfinder.tickUntil(maxTimeMs);

        if (timer.elapsedMs() >= maxTimeMs) throw new RuntimeException(
            StringUtils.format(
                "Trajectory <%s> to target <%s> took <%s> milliseconds " +
                "to execute indiciating there was an issue " +
                "following the trajectory.",
                trajectory,
                target,
                timer.elapsedMs()
            )
        );

        AssertionUtils.assertIsNear(
            target,
            pathfinder.getPosition(),
            tolerance,
            angleTolerance
        );
    }

    public void testTrajectory(Trajectory trajectory, PointXYZ target) {
        testTrajectory(trajectory, target, maxTimeMs);
    }

    @Test
    public void testTestTrajectory() {
        Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                Trajectory trajectory = new EmptyTrajectory();
                PointXYZ target = PointXYZ.ZERO;
                double maxTimeMs = 0;

                testTrajectory(trajectory, target, maxTimeMs);
            }
        );
    }
}
