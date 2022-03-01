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

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.*;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

import org.junit.jupiter.api.*;

public class GenericTrajectoryTester {
    public double speed = 0.5;
    public double tolerance = 2;
    public Angle angleTolerance = Angle.fromDeg(5);
    public double step = 0.25;
    public double turnCoefficient = -0.25;

    public SimulatedOdometry odometry;
    public SimulatedWrapper wrapper;
    public Robot robot;
    public Controller turnController;
    public Pathfinder pathfinder;
    public SplineBuilderFactory factory;

    @BeforeEach
    public void beforeEach() {
        wrapper = new SimulatedWrapper(new SimulatedDrive(), new SimulatedOdometry());
        odometry = wrapper.getOdometry();
        robot = wrapper.getRobot();
        turnController = new ProportionalController(turnCoefficient);
        pathfinder = new Pathfinder(robot, turnController)
                .setSpeed(speed)
                .setTolerance(tolerance)
                .setAngleTolerance(angleTolerance);
        factory = new SplineBuilderFactory()
                .setSpeed(speed)
                .setStep(step)
                .setTolerance(tolerance)
                .setAngleTolerance(angleTolerance);
    }

    public void assertPositionIs(PointXYZ target) {
        PointXYZ position = pathfinder.getPosition();
        double distance = position.distance(target);
        Angle angleDistance = Angle.fromDeg(Math.abs(
                Angle.minimumDelta(position.z(), target.z())));

        Assertions.assertTrue(
                distance <= tolerance,
                StringUtils.format(
                        "Could not assert position! Expected <%s> but got " +
                                "<%s> instead. Distance <%s> was greater " +
                                "than maximum distance of <%s>!",
                        target,
                        position,
                        distance,
                        tolerance
                )
        );
        Assertions.assertTrue(
                angleDistance.deg() <= angleTolerance.deg(),
                StringUtils.format(
                        "Could not assert angle! Expected <%s> but got " +
                                "<%s> instead. Distance <%s> was greater " +
                                "than maximum distance of <%s>!",
                        target.z(),
                        position.z(),
                        angleDistance,
                        angleTolerance.formatAsDegShort()
                )
        );
    }

    public void follow(Trajectory trajectory,
                       PointXYZ point) {
        pathfinder.followTrajectory(trajectory);
        pathfinder.tickUntil(1_000);
        assertPositionIs(point);
    }
}
