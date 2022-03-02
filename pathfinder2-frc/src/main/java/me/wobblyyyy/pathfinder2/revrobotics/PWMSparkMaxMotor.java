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

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

/**
 * An implementation of {@link PWMSparkMax}.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public class PWMSparkMaxMotor extends AbstractMotor implements AutoCloseable {
    private final PWMSparkMax spark;

    /**
     * Create a new {@code PWMSparkMaxMotor}.
     *
     * @param spark the spark max.
     */
    public PWMSparkMaxMotor(PWMSparkMax spark) {
        this(spark, false);
    }

    /**
     * Create a new {@code PWMSparkMaxMotor}.
     *
     * @param spark      the spark max.
     * @param isInverted is it inverted?
     */
    public PWMSparkMaxMotor(PWMSparkMax spark,
                            boolean isInverted) {
        super(spark::set, spark::get, isInverted);

        this.spark = spark;
    }

    /**
     * Create a new {@code PWMSparkMaxMotor}.
     *
     * @param channel the motor's channel.
     */
    public PWMSparkMaxMotor(int channel) {
        this(channel, false);
    }

    /**
     * Create a new {@code PWMSparkMaxMotor}.
     *
     * @param channel    the motor's channel.
     * @param isInverted is the motor inverted?
     */
    public PWMSparkMaxMotor(int channel,
                            boolean isInverted) {
        this(new PWMSparkMax(channel), isInverted);
    }

    /**
     * Get the internal spark max.
     *
     * @return the internal spark max.
     */
    public PWMSparkMax getSpark() {
        return spark;
    }

    @Override
    public void close() {
        spark.close();
    }
}
