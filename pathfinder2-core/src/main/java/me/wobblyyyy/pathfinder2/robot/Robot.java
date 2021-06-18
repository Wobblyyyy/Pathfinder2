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

public class Robot {
    private final Drive drive;

    private final Odometry odometry;

    public Robot(Drive drive,
                 Odometry odometry) {
        this.drive = drive;
        this.odometry = odometry;
    }

    public Drive drive() {
        return this.drive;
    }

    public Odometry odometry() {
        return this.odometry;
    }
}
