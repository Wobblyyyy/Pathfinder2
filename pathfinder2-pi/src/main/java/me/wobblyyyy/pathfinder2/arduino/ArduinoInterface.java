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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Interface for interacting with an Arduino, assuming that Arduino is
 * connected via USB and is also running compatible code. Because the Pi,
 * although a wonderful piece of technology, is a bit too slow to control
 * motors and read sensors as precisely as needed, using an Arduino and
 * communicating with it via serial (hopefully) circumvents that issue.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class ArduinoInterface {
    private final Collection<ArduinoBus> buses = new ArrayList<>();

    public ArduinoInterface() {}

    public Collection<ArduinoBus> getBuses() {
        return buses;
    }

    public ArduinoInterface addBus(ArduinoBus bus) {
        buses.add(bus);

        return this;
    }

    public void update() {
        for (ArduinoBus bus : buses) {
            bus.update();
        }
    }
}
