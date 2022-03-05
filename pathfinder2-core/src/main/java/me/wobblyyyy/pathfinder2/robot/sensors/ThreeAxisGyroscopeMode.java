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

public enum ThreeAxisGyroscopeMode {
    /**
     * Use ROLL for {@code getAngle()}.
     */
    USE_ROLL_AS_ANGLE,

    /**
     * Use PITCH for {@code getAngle()}.
     */
    USE_PITCH_AS_ANGLE,

    /**
     * Use YAW for {@code getAngle()}.
     */
    USE_YAW_AS_ANGLE,

    /**
     * Use YAW for {@code getAngle()}.
     */
    DEFAULT,
}
