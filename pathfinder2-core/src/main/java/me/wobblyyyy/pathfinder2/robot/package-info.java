/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

/**
 * Anything related to the actual robot Pathfinder is responsible for.
 *
 * <p>
 * Pathfinder contains many hardware components that are not used anywhere
 * in Pathfinder's source code. This is done for standardization purposes -
 * a generic sensor, such as {@link me.wobblyyyy.pathfinder2.robot.sensors.Accelerometer},
 * can now be used across many different projects without relying on
 * vendor-specific classes.
 * </p>
 */
package me.wobblyyyy.pathfinder2.robot;
