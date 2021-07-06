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

/**
 * A representation of a physical robot. Robots are defined by their drive
 * and odometry systems - the odometry system should report the robot's
 * position, and the drive system should respond to commands given to it.
 *
 * @author Colin Robertson
 * @since 0.0.0
 * @see #drive()
 * @see #odometry()
 */
public class Robot {
    /**
     * The robot's drive system.
     */
    private final Drive drive;

    /**
     * The robot's odometry system.
     */
    private final Odometry odometry;

    /**
     * Create a new {@code Robot}.
     *
     * @param drive    the robot's drive system.
     * @param odometry the robot's odometry system.
     */
    public Robot(Drive drive,
                 Odometry odometry) {
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
}
