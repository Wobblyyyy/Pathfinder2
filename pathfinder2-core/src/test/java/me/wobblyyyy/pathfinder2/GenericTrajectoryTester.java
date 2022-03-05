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

import java.util.concurrent.atomic.AtomicBoolean;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.*;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import me.wobblyyyy.pathfinder2.trajectory.TestFastTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.TestSequentialLinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;
import me.wobblyyyy.pathfinder2.trajectory.spline.TestAdvancedSplineTrajectory;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import org.junit.jupiter.api.*;

/**
 * Base class to be used by classes that test a trajectory. This is designed
 * to abstract away all of the movement-related code, so that the test can
 * focus JUST on the trajectory.
 *
 * <p>
 * Example implementations:
 * <ul>
 *     <li>{@link TestAdvancedSplineTrajectory}</li>
 *     <li>{@link TestFastTrajectory}</li>
 *     <li>{@link TestSequentialLinearTrajectory}</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 1.4.1
 */
@SuppressWarnings("BusyWait")
public class GenericTrajectoryTester {
    public final double speed = 0.5;
    public final double tolerance = 2;
    public final Angle angleTolerance = Angle.fromDeg(5);
    public final double step = 0.25;
    public final double turnCoefficient = -0.25;
    public final double maximumExecutionTimeMs = 1_000;

    public SimulatedOdometry odometry;
    public SimulatedWrapper wrapper;
    public Robot robot;
    public Controller turnController;
    public Pathfinder pathfinder;
    public SplineBuilderFactory factory;

    @BeforeEach
    public void beforeEach() {
        wrapper =
            new SimulatedWrapper(new SimulatedDrive(), new SimulatedOdometry());
        odometry = wrapper.getOdometry();
        robot = wrapper.getRobot();
        turnController = new ProportionalController(turnCoefficient);
        pathfinder =
            new Pathfinder(robot, turnController)
                .setSpeed(speed)
                .setTolerance(tolerance)
                .setAngleTolerance(angleTolerance);
        factory =
            new SplineBuilderFactory()
                .setSpeed(speed)
                .setStep(step)
                .setTolerance(tolerance)
                .setAngleTolerance(angleTolerance);
    }

    public void assertPositionIs(PointXYZ target) {
        AssertionUtils.assertIsNear(
            target,
            pathfinder.getPosition(),
            tolerance,
            angleTolerance
        );
    }

    public void follow(Trajectory trajectory, PointXYZ point) {
        // this is pretty disgusting code, but it gets the job done
        // basically just spawn a new monitor thread to ensure that it doesn't
        // take over a certain amount of time to execute the trajectory. if it
        // does, something's broken, so the test should fail
        pathfinder.followTrajectory(trajectory);

        ElapsedTimer timer = new ElapsedTimer();

        AtomicBoolean hasNotExpired = new AtomicBoolean(true);
        AtomicBoolean hasExecuted = new AtomicBoolean(false);

        Thread monitor = new Thread(
            () -> {
                timer.start();

                try {
                    while (!hasExecuted.get()) Thread.sleep(1);

                    while (timer.elapsedMs() < maximumExecutionTimeMs) {
                        Thread.sleep(1);

                        if (!pathfinder.isActive()) break;
                    }

                    if (pathfinder.isActive()) hasNotExpired.set(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        );

        monitor.start();
        pathfinder.tickUntil(hasNotExpired::get, pf -> hasExecuted.set(true));

        if (!hasNotExpired.get()) throw new RuntimeException(
            StringUtils.format(
                "Trajectory <%s> (target <%s>) took more than %s " +
                "milliseconds to execute, meaning something " +
                "went wrong with following the trajectory!",
                trajectory,
                point,
                maximumExecutionTimeMs
            )
        );

        assertPositionIs(point);
    }
}
