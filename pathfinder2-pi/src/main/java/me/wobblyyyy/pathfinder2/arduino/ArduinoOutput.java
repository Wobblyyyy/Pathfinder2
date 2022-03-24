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
 * An output (Pi to Arduino) for communicating with an Arduino.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class ArduinoOutput implements ArduinoBus {
    private final ArduinoInterface arduino;
    private final String id;
    private double previousValue = 0.0;
    private double value = 0.0;

    public ArduinoOutput(ArduinoInterface arduino, String id) {
        this.arduino = arduino;
        this.id = id;

        arduino.addBus(this);
    }

    private void realUpdate() {}

    @Override
    public void update() {
        if (value != previousValue) {
            realUpdate();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Write information to the {@code Arduino}. This will not actually
     * transmit any data over the serial bus - rather, it'll update the
     * internal {@link #value} value. In order for this value to actually
     * be updated, the {@link #update()} method will need to be called
     * first. This <em>should</em> be done via the
     * {@link ArduinoInterface#update} method, but that's not required.
     *
     * @param value the value to write.
     */
    public void write(double value) {
        this.value = value;
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
