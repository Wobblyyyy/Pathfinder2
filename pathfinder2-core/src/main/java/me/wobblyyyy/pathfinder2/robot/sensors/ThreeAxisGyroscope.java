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
 * An interface for a gyroscope with three axes.
 *
 * @author Colin Robertson
 * @since 1.3.0
 */
public interface ThreeAxisGyroscope extends Gyroscope {
    /**
     * Get the gyroscope's roll value.
     *
     * @return the gyroscope's roll value.
     */
    Angle getRoll();

    /**
     * Set the roll value's offset.
     *
     * @param rollOffset the offset for the roll value.
     * @return {@code this}, used for method chaining.
     */
    ThreeAxisGyroscope setRollOffset(Angle rollOffset);

    /**
     * Get the roll value's offset.
     *
     * @return the roll value's offset.
     */
    Angle getRollOffset();

    /**
     * Get the gyroscope's pitch value.
     *
     * @return the gyroscope's pitch value.
     */
    Angle getPitch();

    /**
     * Set the pitch value's offset.
     *
     * @param pitchOffset the offset for the pitch value.
     * @return {@code this}, used for method chaining.
     */
    ThreeAxisGyroscope setPitchOffset(Angle pitchOffset);

    /**
     * Get the pitch value's offset.
     *
     * @return the pitch value's offset.
     */
    Angle getPitchOffset();

    /**
     * Get the gyroscope's yaw.
     *
     * @return the gyroscope's yaw value.
     */
    Angle getYaw();

    /**
     * Set the roll value's offset.
     *
     * @param rollOffset the offset for the roll value.
     * @return {@code this}, used for method chaining.
     */
    ThreeAxisGyroscope setYawOffset(Angle yawOffset);

    /**
     * Get the yaw value's offset.
     *
     * @return the yaw value's offset.
     */
    Angle getYawOffset();

    @Override
    default Angle getAngle() {
        return getYaw();
    }
}
