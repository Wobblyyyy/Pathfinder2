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
import com.revrobotics.RelativeEncoder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.robot.sensors.AbstractAngleEncoder;
import me.wobblyyyy.pathfinder2.robot.sensors.Encoder;

/**
 * A lovely encoder for a lovely {@link CANSparkMax}'s lovely encoder.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class SparkMaxEncoder extends AbstractAngleEncoder implements Encoder {
    private final RelativeEncoder encoder;
    private final int cpr;
    private int offset;
    private int multiplier;

    /**
     * Create a new {@code SparkMaxEncoder}.
     *
     * @param encoder the encoder to use.
     */
    public SparkMaxEncoder(RelativeEncoder encoder) {
        this.encoder = encoder;
        this.cpr = encoder.getCountsPerRevolution();
    }

    /**
     * Create a new {@code SparkMaxEncoder}.
     *
     * @param spark the spark max that has the encoder you'd like to use.
     */
    public SparkMaxEncoder(CANSparkMax spark) {
        this(spark.getEncoder());
    }

    @Override
    public Angle getRawAngle() {
        return Angle.fromDeg(encoder.getPosition() * 360);
    }

    @Override
    public int getTicks() {
        return (int) (((encoder.getPosition() * cpr) * multiplier) + offset);
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void offsetSoPositionIs(int target) {
        setOffset((getTicks() - offset) - target);
    }

    @Override
    public int getMultiplier() {
        return multiplier;
    }

    @Override
    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }
}
