/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.exceptions.NullDriveException;
import me.wobblyyyy.pathfinder2.exceptions.NullOdometryException;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A representation of a physical robot. Robots are defined by their drive
 * and odometry systems - the odometry system should report the robot's
 * position, and the drive system should respond to commands given to it.
 *
 * @author Colin Robertson
 * @see #drive()
 * @see #odometry()
 * @since 0.0.0
 */
public class Robot {
    /**
     * The robot's drive system.
     */
    private Drive drive;

    /**
     * The robot's odometry system.
     */
    private Odometry odometry;

    /**
     * Create a new {@code Robot}.
     */
    public Robot() {}

    /**
     * Create a new {@code Robot}.
     *
     * @param drive    the robot's drive system.
     * @param odometry the robot's odometry system.
     */
    public Robot(Drive drive, Odometry odometry) {
        if (drive == null) {
            throw new NullDriveException(
                "Attempted to create a robot with a null instance of the " +
                "Drive interface, please ensure you're passing " +
                "a non-null object to the Robot constructor!"
            );
        }

        if (odometry == null) {
            throw new NullOdometryException(
                "Attempted to create a robot with a null instance of the " +
                "Odometry interface, please ensure you're passing " +
                "a non-null object to the Robot constructor!"
            );
        }

        this.drive = drive;
        this.odometry = odometry;
    }

    /**
     * @return the robot's drive system.
     */
    public Drive drive() {
        return this.drive;
    }

    /**
     * @return the robot's odometry system.
     */
    public Odometry odometry() {
        return this.odometry;
    }

    public Robot drive(Drive drive) {
        ValidationUtils.validate(drive, "drive");
        this.drive = drive;
        return this;
    }

    public Robot odometry(Odometry odometry) {
        ValidationUtils.validate(odometry, "odometry");
        this.odometry = odometry;
        return this;
    }
}
