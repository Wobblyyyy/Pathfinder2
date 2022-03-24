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
    void update();

    String getId();

    static boolean equals(ArduinoBus bus, Object obj) {
        if (obj instanceof ArduinoBus) {
            ArduinoBus b = (ArduinoBus) obj;

            return bus.getId().equals(b.getId());
        }

        return false;
    }
}
