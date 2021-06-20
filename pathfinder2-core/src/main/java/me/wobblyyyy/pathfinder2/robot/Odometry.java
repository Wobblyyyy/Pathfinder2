/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.modifiers.Modifiable;

/**
 * A system capable of reporting the position of a robot. Several odometry
 * systems available for use are provided with Pathfinder. If you'd like to
 * create your own implementation of the {@code Odometry} interface, it's
 * suggested you make use of the {@link AbstractOdometry} class instead - it
 * abstracts away many of the tedious methods.
 *
 * <p>
 * It's strongly suggested that you don't perform any offsetting on the
 * odometry's reported positioning outside of the methods provided in this
 * class. This helps to ensure that positions are only modified from a single
 * source, making debugging a lot easier.
 * </p>
 *
 * @author Colin Robertson
 * @see AbstractOdometry
 * @since 0.0.0
 */
public interface Odometry extends Modifiable<PointXYZ> {
    /**
     * Get the raw position reported by the odometry system.
     *
     * <p>
     * I'd also like to suggest that you make use of the method
     * {@link PointXYZ#zeroIfNull(PointXYZ)}. Doing so will ensure your
     * odometry never reports a null position. I personally have had some
     * issues with exactly that, so I'm putting this warning here so I
     * can possibly save someone a bit of a headache.
     * </p>
     *
     * @return the raw position reported by the odometry system.
     */
    PointXYZ getRawPosition();

    /**
     * Get the position produced by {@link #getRawPosition()} combined with
     * the offset produced by {@link #getOffset()}.
     *
     * @return the odometry system's position, with an offset.
     */
    PointXYZ getPosition();

    /**
     * Get the odometry system's offset.
     *
     * @return the odometry system's offset.
     */
    PointXYZ getOffset();

    /**
     * Set the odometry system's offset.
     *
     * @param offset the new offset. This offset value will replace whatever
     *               the old offset value was.
     */
    void setOffset(PointXYZ offset);

    /**
     * Get the X value of the offset.
     *
     * @return the X value of the offset.
     */
    double getOffsetX();

    /**
     * Get the Y value of the offset.
     *
     * @return the Y value of the offset.
     */
    double getOffsetY();

    /**
     * Get the Z value of the offset.
     *
     * @return the Z value of the offset.
     */
    Angle getOffsetZ();

    /**
     * Modify the existing offset by whatever offset you provide.
     *
     * @param offset the offset to add to the existing offset.
     */
    void offsetBy(PointXYZ offset);

    /**
     * Remove the current offset. This will set the offset to (0, 0, 0).
     */
    void removeOffset();

    /**
     * Create an offset that makes the odometry's current position the
     * provided target position.
     *
     * @param targetPosition the position you'd like the odometry system
     *                       to be offset to.
     */
    void offsetSoPositionIs(PointXYZ targetPosition);

    /**
     * Find an offset that makes the odometry's current position equal to
     * (0, 0) and apply it.
     */
    void zeroOdometry();

    /**
     * Get the robot's X position.
     *
     * @return {@link #getPosition()}.
     */
    double getX();

    /**
     * Get the robot's Y position.
     *
     * @return {@link #getPosition()}.
     */
    double getY();

    /**
     * Get the robot's Z position.
     *
     * @return {@link #getPosition()}.
     */
    Angle getZ();

    /**
     * Get the robot's current heading in radians.
     *
     * @return the robot's current heading in radians.
     */
    double getRad();

    /**
     * Get the robot's current heading in degrees.
     *
     * @return the robot's current heading in degrees.
     */
    double getDeg();

    /**
     * Get the raw X value from the robot.
     *
     * @return the robot's raw X value.
     * @see #getRawPosition()
     */
    double getRawX();

    /**
     * Get the raw Y value from the robot.
     *
     * @return the robot's raw Y value.
     * @see #getRawPosition()
     */
    double getRawY();

    /**
     * Get the raw Z value from the robot.
     *
     * @return the robot's raw Z value.
     * @see #getRawPosition()
     */
    Angle getRawZ();

    /**
     * Get the robot's raw heading in radians.
     *
     * @return the robot's raw heading in radians.
     */
    double getRawRad();

    /**
     * Get the robot's raw heading in degrees.
     *
     * @return the robot's raw heading in degrees.
     */
    double getRawDeg();
}
