/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.extra.sensors.movement;

/**
 * A basic interface for an accelerometer.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public interface Accelerometer {
    /**
     * Get the velocity of the {@code Accelerometer} in units per second.
     *
     * @return the velocity on the X axis, in units per second.
     */
    double getVxPerSecond();

    /**
     * Get the velocity of the {@code Accelerometer} in units per second.
     *
     * @return the velocity on the Y axis, in units per second.
     */
    double getVyPerSecond();

    /**
     * Get the velocity of the {@code Accelerometer} in rotations
     * per second.
     *
     * @return how many times the robot is rotating, per second.
     */
    double getRotationsPerSecond();
}
