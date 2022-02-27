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
 * An abstract implementation of {@link ThreeAxisGyroscope} that handles
 * offsets for you.
 *
 * @author Colin Robertson
 * @since 1.3.0
 */
public abstract class AbstractThreeAxisGyroscope implements ThreeAxisGyroscope {
    private ThreeAxisGyroscopeMode mode = ThreeAxisGyroscopeMode.DEFAULT;

    private Angle rollOffset = Angle.DEG_0;
    private Angle pitchOffset = Angle.DEG_0;
    private Angle yawOffset = Angle.DEG_0;

    public abstract Angle getRawRoll();
    public abstract Angle getRawPitch();
    public abstract Angle getRawYaw();

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getRoll() {
        return getRawRoll().add(rollOffset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreeAxisGyroscope setRollOffset(Angle rollOffset) {
        this.rollOffset = rollOffset;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getRollOffset() {
        return rollOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getPitch() {
        return getRawPitch().add(pitchOffset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreeAxisGyroscope setPitchOffset(Angle pitchOffset) {
        this.pitchOffset = pitchOffset;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getPitchOffset() {
        return pitchOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getYaw() {
        return getRawYaw().add(yawOffset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreeAxisGyroscope setYawOffset(Angle yawOffset) {
        this.yawOffset = yawOffset;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getYawOffset() {
        return yawOffset;
    }
    
    @Override
    public Angle getAngle() {
        switch (mode) {
            case USE_ROLL_AS_ANGLE:
                return getRoll();
            case USE_PITCH_AS_ANGLE:
                return getPitch();
            case DEFAULT:
            case USE_YAW_AS_ANGLE:
                return getPitch();
            default:
                throw new RuntimeException("how did you even get here?");
        }
    }

    public ThreeAxisGyroscope setMode(ThreeAxisGyroscopeMode mode) {
        this.mode = mode;
        return this;
    }

    public ThreeAxisGyroscopeMode getMode() {
        return mode;
    }
}
