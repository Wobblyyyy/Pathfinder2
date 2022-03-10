/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kauailabs;

/**
 * Modes the {@link AHRSGyro} can operate in. Unless you have a reason not
 * to, you should use {@link AHRSGyroMode#NORMAL}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public enum AHRSGyroMode {
    /**
     * The gyroscope's normal mode of operation. This is the same as YAW.
     */
    NORMAL,

    /**
     * Use the gyroscope's yaw for angle values.
     */
    YAW,

    /**
     * Use the gyroscope's pitch for angle values.
     */
    PITCH,

    /**
     * Use the gyroscope's roll for angle values.
     */
    ROLL
}
