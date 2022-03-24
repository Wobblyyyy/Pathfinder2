/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.pi;

import me.wobblyyyy.pathfinder2.arduino.ArduinoInput;
import me.wobblyyyy.pathfinder2.arduino.ArduinoInterface;
import me.wobblyyyy.pathfinder2.robot.sensors.AbstractEncoder;
import me.wobblyyyy.pathfinder2.robot.sensors.Encoder;

/**
 * Pi-specific {@code Encoder} implementation, reliant on using an Arduino
 * to recieve encoder ticks.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class PiEncoder extends ArduinoInput implements Encoder {
    private final AbstractEncoder encoder = new AbstractEncoder() {

        @Override
        public int getRawTicks() {
            return (int) read();
        }
    };

    public PiEncoder(ArduinoInterface arduino, String id) {
        super(arduino, id);
    }

    public int getTicks() {
        return encoder.getTicks();
    }

    public int getOffset() {
        return encoder.getOffset();
    }

    public void setOffset(int offset) {
        encoder.setOffset(offset);
    }

    public void offsetSoPositionIs(int target) {
        encoder.offsetSoPositionIs(target);
    }

    public int getMultiplier() {
        return encoder.getMultiplier();
    }

    public void setMultiplier(int multiplier) {
        encoder.setMultiplier(multiplier);
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }
}
