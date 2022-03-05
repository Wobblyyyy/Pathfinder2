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
 * A very lovely and very pleasant color sensor, responsible for, among other
 * things, sensing colors.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public interface ARGBColorSensor extends Sensor<ARGB> {
    /**
     * Get the RED value from the color sensor.
     *
     * @return the RED value from the color sensor.
     */
    int getRed();

    /**
     * Get the GREEN value from the color sensor.
     *
     * @return the GREEN value from the color sensor.
     */
    int getGreen();

    /**
     * Get the BLUE value from the color sensor.
     *
     * @return the BLUE value from the color sensor.
     */
    int getBlue();

    /**
     * Get the ALPHA value from the color sensor.
     *
     * @return the ALPHA value from the color sensor.
     */
    int getAlpha();

    @Override
    default ARGB read() {
        return new ARGB(getAlpha(), getRed(), getGreen(), getBlue());
    }
}
