/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.extra.sensors;

/**
 * A physical device used to read information from an outside environment.
 *
 * @param <T> the type of data the sensor reads. Color sensors return ARGB
 *            values. Distance sensors return distance values (doubles).
 *            Digital inputs (such as buttons or limit switches) return
 *            boolean values (true = pressed, false = not pressed).
 * @author Colin Robertson
 * @since 0.7.1
 */
public interface Sensor<T> {
    /**
     * Read data from the sensor.
     *
     * @return data, read from the sensor.
     */
    T read();
}
