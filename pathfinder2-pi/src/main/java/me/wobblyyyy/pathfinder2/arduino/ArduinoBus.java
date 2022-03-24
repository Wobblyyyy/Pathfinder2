/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.arduino;

/**
 * An interface to be implemented by any input and output channels used for
 * communicating with an Arduino.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public interface ArduinoBus {
    /**
     * Update the bus. This should use the serial communication established
     * between the Pi and the Arduino to either read or write data.
     */
    void update();

    /**
     * Get the ID of the bus. Each bus should have a unique ID. There are
     * no restrictions on the ID of the bus, but it's encouraged that you use
     * only alphanumeric characters, underscores, and hyphens.
     *
     * @return the ID of the bus.
     */
    String getId();

    /**
     * Check to see if an {@link Object} is equal to an {@link ArduinoBus} by
     * checking if they have the same {@link #getId()} value, as determined
     * by {@link String#equals(Object)}.
     *
     * @param bus one of the objects.
     * @param obj one of the objects.
     * @return true if the objects have the same ID. Otherwise, false.
     */
    static boolean equals(ArduinoBus bus, Object obj) {
        if (obj instanceof ArduinoBus) {
            ArduinoBus b = (ArduinoBus) obj;

            return bus.getId().equals(b.getId());
        }

        return false;
    }
}
