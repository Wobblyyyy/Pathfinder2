/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.GenericTurnController;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.follower.generators.GenericFollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.prebuilt.TrajectoryFactory;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.builder.LinearTrajectoryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Very simple example implementation demonstrating the basic usage of
 * Pathfinder. Note that is just a surface level implementation - there are
 * a ton of features not present here. There are more examples for those -
 * this is just designed to be a simple implementation.
 *
 * Also, there aren't very many comments in this class. This is because most
 * of the class is named pretty well, so it shouldn't be that challenging to
 * understand. If you're curious about what something's doing, you can
 * control click on the class (or 'gd' for my fellow Vim users) to go to
 * the class' or method's declaration and learn more.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
@SuppressWarnings("FieldCanBeLocal")
public class ExamplePathfinder {
    private final Controller turnController = new GenericTurnController(0.1);
    private final FollowerGenerator followerGenerator = new GenericFollowerGenerator(turnController);
    private final Drive drive = new SimulatedDrive();
    private final Odometry odometry = new SimulatedOdometry();
    private final Robot robot = new Robot(drive, odometry);
    private final Pathfinder pathfinder = new Pathfinder(robot, followerGenerator);

    private static final double SPEED = 0.5;
    private static final double TOLERANCE = 1.0;
    private static final Angle ANGLE_TOLERANCE = Angle.fromDeg(15);

    // We'll pretend these are controller inputs. In a real implementation,
    // there would be a Gamepad object, and we'd get input states from there.
    // However, for the sake of simplicity, these are being used in place of
    // that Gamepad object.
    private static final boolean gamepadA = false;
    private static final boolean gamepadB = false;
    private static final boolean gamepadX = false;
    private static final boolean gamepadY = false;
    private static final boolean gamepadStart = false;
    private static final double joystick1x = 0.0;
    private static final double joystick2x = 0.0;
    private static final double joystick1y = 0.0;
    private static final double joystick2y = 0.0;

    private static final PointXYZ TARGET_A = new PointXYZ(0, 0, 0);
    private static final PointXYZ TARGET_B = new PointXYZ(10, 10, 0);
    private static final PointXYZ TARGET_X = new PointXYZ(10, 15, 45);
    private static final PointXYZ TARGET_Y = new PointXYZ(30, 20, 90);

    public ExamplePathfinder() {
        pathfinder.setSpeed(SPEED);
        pathfinder.setTolerance(TOLERANCE);
        pathfinder.setAngleTolerance(ANGLE_TOLERANCE);
    }

    private void goToPoint(PointXYZ target) {
        pathfinder.clear();

        pathfinder.goTo(target);

        while (pathfinder.isActive()) {
            // commented out to support jdk8... :(
            // Thread.onSpinWait();

            pathfinder.tick();
        }
    }

    /**
     * Go to a series of points. This method is blocking, meaning its execution
     * takes a variable amount of time to execute, and, in some cases,
     * nearly impossible. A method like this would have good use in the
     * Autonomous period of FTC/FRC games, because it's the only thing the robot
     * has to do, but wouldn't be so great for the teleop periods.
     */
    @SuppressWarnings("DuplicatedCode")
    public void goToSomePoints() {
        // this is a pretty bad way of going to the points...
        List<PointXYZ> points = new ArrayList<PointXYZ>() {{
            add(new PointXYZ( 0,  0, 0));
            add(new PointXYZ(10,  0, 0));
            add(new PointXYZ(10, 10, 0));
            add(new PointXYZ( 0, 10, 0));
            add(new PointXYZ( 0,  0, 0));
        }};

        for (PointXYZ point : points) {
            goToPoint(point);
        }
    }

    public void betterGoToSomePoints() {
        // generate a list of trajectories w/ LinearTrajectoryBuilder
        // the setSpeed method is used to dynamically adjust the speed of
        // the robot - for example, the speed is halved, then the speed
        // is reset, then the speed is doubled
        List<Trajectory> trajectories = new LinearTrajectoryBuilder(
                SPEED,
                TOLERANCE,
                ANGLE_TOLERANCE,
                PointXYZ.ZERO
        )
                .goTo(new PointXYZ(0, 0, 0))
                .goTo(new PointXYZ(10, 0, 0))
                .setSpeed(SPEED / 2)
                .goTo(new PointXYZ(10, 10, 0))
                .setSpeed(SPEED)
                .goTo(new PointXYZ(0, 10, 0))
                .setSpeed(SPEED * 2)
                .goTo(new PointXYZ(0, 0, 0))
                .getTrajectories();

        // follow the trajectories with a timeout of 10 seconds. if more than
        // 10 seconds pass and the trajectories haven't finished yet, stop
        // following the trajectories
        pathfinder.followTrajectories(trajectories)
                .tickUntil(10_000);
    }

    /**
     * This is a traditional loop method - it's meant to be run dozens of times
     * per second, over and over and over again. The general premise for this
     * loop is as follows: if Pathfinder is NOT active (meaning it isn't
     * following any trajectories), it'll check for user input using the
     * A, B, X, and Y gamepad buttons. If any of those buttons are pressed,
     * the robot will begin automatically navigating to an associated position.
     *
     * Let's say Pathfinder IS active... what happens then? There will be times
     * when Pathfinder is attempting to control your robot while you'd like to
     * be the one who's in control of it. In this case, you should clear
     * Pathfinder, meaning it will no longer try to follow any paths, meaning
     * you have control over the robot.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public void loop() {
        if (!pathfinder.isActive()) {
            // If Pathfinder isn't active, let's take a look at our
            // controller inputs.
            PointXYZ targetPoint = null;

            // Based on the input, set the target point to be one of
            // four targets. Real life example:
            // A: High Goal
            // B: Power Shot 1
            // X: Power Shot 2
            // Y: Power Shot 3
            // The robot will automatically move to the associated positions.
            // Pretty cool, right?
            if (gamepadA)
                targetPoint = TARGET_A;
            else if (gamepadB)
                targetPoint = TARGET_B;
            else if (gamepadX)
                targetPoint = TARGET_X;
            else if (gamepadY)
                targetPoint = TARGET_Y;

            if (targetPoint != null) {
                pathfinder.goTo(targetPoint);
            } else {
                // Based on some joysticks, generate a translation.
                // This translation will then be used to drive the robot.
                double moveForwards = joystick1y;
                double moveStrafe = joystick1x;
                double moveRotate = joystick2x;

                Translation translation = new Translation(
                        moveForwards,
                        moveStrafe,
                        moveRotate
                );

                pathfinder.getRobot().drive().setTranslation(translation);
            }
        }

        if (gamepadStart) {
            // Let's say we want to manually override Pathfinder and regain
            // control of the robot. All we'd have to do:
            pathfinder.clear();
        }

        // ... any other code that you would need in your main loop
        // ex. sensor updates, other motors, you know the deal

        // Tick or update Pathfinder once. Remember, this is absolutely
        // essential - if you don't tick Pathfinder, nothing can happen.
        pathfinder.tick();
    }
}
