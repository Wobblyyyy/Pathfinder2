/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

/**
 * A lovely rotational encoder. Quite fancy, I know.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public interface RotationalEncoder {
    /**
     * Get the amount of rotations the encoder has made.
     *
     * @return the amount of rotations the encoder has made.
     */
    double getRotations();
}
