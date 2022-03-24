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
 * An input (Arduino to Pi) for communicating with an Arduino.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class ArduinoInput implements ArduinoBus {
    private final ArduinoInterface arduino;
    private final String id;
    private double value = 0.0;

    public ArduinoInput(ArduinoInterface arduino, String id) {
        this.arduino = arduino;
        this.id = id;
    }

    @Override
    public void update() {}

    @Override
    public String getId() {
        return id;
    }

    public double read() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return ArduinoBus.equals(this, obj);
    }

    @Override
    public String toString() {
        return id;
    }
}
