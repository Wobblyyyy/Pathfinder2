/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.led;

/**
 * A color, used for LEDs.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class LedColor {
    private final int red;
    private final int green;
    private final int blue;

    public LedColor(int red,
                    int green,
                    int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int red() {
        return this.red;
    }

    public int green() {
        return this.green;
    }

    public int blue() {
        return this.blue;
    }
}
