/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * A layer of abstraction for {@link AngleEncoder}, providing offset and
 * multiplier functionality.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public abstract class AbstractAngleEncoder implements AngleEncoder {
    private Angle offset;
    private double multiplier;

    public AbstractAngleEncoder() {
        this(Angle.fromDeg(0), 0);
    }

    public AbstractAngleEncoder(Angle offset) {
        this(offset, 1);
    }

    public AbstractAngleEncoder(double multiplier) {
        this(Angle.fromDeg(0), multiplier);
    }

    public AbstractAngleEncoder(Angle offset, double multiplier) {
        this.offset = offset;
        this.multiplier = multiplier;
    }

    /**
     * Set the encoder's offset.
     *
     * @param offset the encoder's offset.
     * @return {@code this}, used for method chaining.
     */
    public AbstractAngleEncoder setOffset(Angle offset) {
        this.offset = offset;

        return this;
    }

    /**
     * Set the encoder's multiplier.
     *
     * @param multiplier the encoder's multiplier.
     * @return {@code this}, used for method chaining.
     */
    public AbstractAngleEncoder setMultiplier(double multiplier) {
        this.multiplier = multiplier;

        return this;
    }

    /**
     * Get the angle from the encoder.
     *
     * @return the encoder's angle.
     */
    public abstract Angle getRawAngle();

    @Override
    public Angle getAngle() {
        return getRawAngle().multiply(multiplier).add(offset);
    }
}
