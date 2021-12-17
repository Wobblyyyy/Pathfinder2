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
 * An interface for controlling an LED.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public interface LedController {
    /**
     * Set the color of the LEDs.
     *
     * @param color the color to set to the LEDs.
     */
    void setColor(LedColor color);

    /**
     * Set the pattern of the LEDs.
     *
     * @param pattern the pattern to set to the LEDs.
     */
    void setPattern(LedPattern pattern);
}
