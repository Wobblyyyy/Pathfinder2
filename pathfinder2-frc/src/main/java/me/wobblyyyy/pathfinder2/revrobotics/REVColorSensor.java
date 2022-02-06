/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.revrobotics;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import me.wobblyyyy.pathfinder2.robot.sensors.ARGBColorSensor;

/**
 * A wrapper for {@link ColorSensorV3}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class REVColorSensor implements ARGBColorSensor {
    private final ColorSensorV3 sensor;

    /**
     * Create a new {@code REVColorSensor}.
     *
     * @param sensor the color sensor.
     */
    public REVColorSensor(ColorSensorV3 sensor) {
        this.sensor = sensor;
    }

    /**
     * Create a new {@code REVColorSensor}.
     *
     * @param port the port the color sensor is in.
     */
    public REVColorSensor(I2C.Port port) {
        this(new ColorSensorV3(port));
    }

    @Override
    public int getRed() {
        return sensor.getRed();
    }

    @Override
    public int getGreen() {
        return sensor.getGreen();
    }

    @Override
    public int getBlue() {
        return sensor.getBlue();
    }

    @Override
    public int getAlpha() {
        return sensor.getIR();
    }
}
