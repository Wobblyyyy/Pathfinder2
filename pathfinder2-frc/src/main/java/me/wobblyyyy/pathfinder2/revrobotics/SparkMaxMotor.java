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

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushed;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;

/**
 * Wrapper for {@link CANSparkMax}. Uses {@link AbstractMotor} internally to
 * take advantage of lazy mode.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class SparkMaxMotor extends AbstractMotor implements AutoCloseable {
    private final CANSparkMax spark;

    /**
     * Create a new {@code SparkMaxMotor}.
     *
     * @param spark the spark max.
     */
    public SparkMaxMotor(CANSparkMax spark) {
        super(spark::set, spark::get);

        this.spark = spark;
    }

    public SparkMaxMotor(CANSparkMax spark,
                         boolean isInverted) {
        super(spark::set, spark::get, isInverted);

        this.spark = spark;
    }

    /**
     * Create a new {@code SparkMaxMotor}.
     *
     * @param deviceId the spark's device ID.
     * @param type     the spark's type - either brushless or brushed.
     */
    public SparkMaxMotor(int deviceId,
                         MotorType type) {
        this(new CANSparkMax(deviceId, type));
    }

    public SparkMaxMotor(int deviceId,
                         MotorType type,
                         boolean isInverted) {
        this(new CANSparkMax(deviceId, type), isInverted);
    }

    /**
     * Create a new brushed {@code SparkMaxMotor}.
     *
     * @param deviceId the spark's device ID.
     * @return a new brushed {@code SparkMaxMotor}.
     */
    public static SparkMaxMotor brushed(int deviceId) {
        return new SparkMaxMotor(deviceId, kBrushed);
    }

    /**
     * Create a new brushed {@code SparkMaxMotor}.
     *
     * @param deviceId   the spark's device ID.
     * @param isInverted is the motor inverted?
     * @return a new brushed {@code SparkMaxMotor}.
     */
    public static SparkMaxMotor brushed(int deviceId,
                                        boolean isInverted) {
        return new SparkMaxMotor(deviceId, kBrushed, isInverted);
    }

    /**
     * Create a new brushless {@code SparkMaxMotor}.
     *
     * @param deviceId the spark's device ID.
     * @return a new brushless {@code SparkMaxMotor}.
     */
    public static SparkMaxMotor brushless(int deviceId) {
        return new SparkMaxMotor(deviceId, kBrushless);
    }

    /**
     * Create a new brushless {@code SparkMaxMotor}.
     *
     * @param deviceId   the spark's device ID.
     * @param isInverted is the motor inverted?
     * @return a new brushless {@code SparkMaxMotor}.
     */
    public static SparkMaxMotor brushless(int deviceId,
                                          boolean isInverted) {
        return new SparkMaxMotor(deviceId, kBrushless, isInverted);
    }

    /**
     * Get the internal spark max.
     *
     * @return the internal spark max.
     */
    public CANSparkMax getSpark() {
        return spark;
    }

    @Override
    public void close() {
        spark.close();
    }
}
