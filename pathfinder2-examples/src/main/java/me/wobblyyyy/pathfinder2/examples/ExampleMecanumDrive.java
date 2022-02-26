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
import me.wobblyyyy.pathfinder2.drive.MecanumDrive;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.follower.generators.GenericFollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedMotor;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.utils.DualJoystick;
import me.wobblyyyy.pathfinder2.utils.Gamepad;
import me.wobblyyyy.pathfinder2.utils.Joystick;

import java.util.ArrayList;
import java.util.List;

public class ExampleMecanumDrive {
    private final SimulatedMotor frontLeft = new SimulatedMotor();
    private final SimulatedMotor frontRight = new SimulatedMotor();
    private final SimulatedMotor backLeft = new SimulatedMotor();
    private final SimulatedMotor backRight = new SimulatedMotor();

    private final Drive drive = new MecanumDrive(
            frontRight,
            frontLeft,
            backRight,
            backLeft
    );
    private final Robot robot = new Robot(drive, odometry);
    private final Odometry odometry = new SimulatedOdometry();
    private final Controller turnController = new GenericTurnController(0.05);
    private final FollowerGenerator followerGenerator = new GenericFollowerGenerator(turnController);
    private final Pathfinder pathfinder = new Pathfinder(robot, followerGenerator);

    private final Joystick rightJoystick = new Joystick(this::rightStickX, this::rightStickY);
    private final Joystick leftJoystick = new Joystick(this::leftStickX, this::leftStickY);
    private final DualJoystick joysticks = new DualJoystick(leftJoystick, rightJoystick);
    private final Gamepad gamepad = new Gamepad();

    public ExampleMecanumDrive() {

    }

    public double leftStickX() {
        return 0.0;
    }

    public double leftStickY() {
        return 0.0;
    }

    public double rightStickX() {
        return 0.0;
    }

    public double rightStickY() {
        return 0.0;
    }

    /**
     * Drive in autonomous mode! Very cool.
     * <p>
     * You don't have to do any looping - this method will execute until
     * Pathfinder has finished following its path. There's a variety of
     * ways to improve upon this that we can explore later.
     */
    @SuppressWarnings("DuplicatedCode")
    public void autonomousDrive() {
        // go in a big rectangle
        List<PointXYZ> path = new ArrayList<PointXYZ>() {{
            add(new PointXYZ(0, 0, 0));
            add(new PointXYZ(10, 0, 0));
            add(new PointXYZ(10, 10, 0));
            add(new PointXYZ(0, 10, 0));
            add(new PointXYZ(0, 0, 0));
        }};

        for (PointXYZ point : path) {
            pathfinder.goTo(point);

            while (pathfinder.isActive()) {
                // commented out to support jdk8... :(
                // Thread.onSpinWait();

                pathfinder.tick();
            }
        }
    }

    public void loopDrive() {
        if (!pathfinder.isActive()) {
            if (gamepad.areAnyButtonsPressed()) {
                PointXYZ target = null;

                if (gamepad.a()) {
                    target = new PointXYZ(10, 10, 0);
                } else if (gamepad.b()) {
                    target = new PointXYZ(10, 10, 0);
                } else if (gamepad.x()) {
                    target = new PointXYZ(10, 10, 0);
                } else if (gamepad.y()) {
                    target = new PointXYZ(10, 10, 0);
                }

                if (target != null) {
                    pathfinder.goTo(target);
                }
            }

            Translation translation = joysticks.translation(
                    DualJoystick.Axis.LEFT_Y,
                    DualJoystick.Axis.LEFT_X,
                    DualJoystick.Axis.RIGHT_X
            );

            pathfinder.getDrive().setTranslation(translation);
        } else {
            if (gamepad.start()) {
                pathfinder.clear();
            }
        }

        pathfinder.tick();
    }
}
