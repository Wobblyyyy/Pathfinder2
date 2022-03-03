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
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

import java.util.function.Function;
import java.util.function.Supplier;

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
 * <p>
 * Some of the most common forms of odometry include...
 * <ul>
 *     <li>Vision-based odometry</li>
 *     <li>Three-wheel odometry</li>
 *     <li>Two-wheel odometry</li>
 * </ul>
 * Optimally, you should use as many odometry systems as you can to maintain
 * the best positional accuracy. However, that's not really very realistic,
 * is it? An odometry system's only job is to provide information on the
 * position of the robot it belongs to.
 * </p>
 *
 * <p>
 * Another one of the neat features of the {@code Odometry} interface is
 * the ability to offset the position. This allows you to manipulate the
 * reported position at will. Say you'd like to relocalize/recalibrate your
 * robot's position during operation - offsets to the rescue!
 * </p>
 *
 * <p>
 * The unit you choose for odometry does not matter. Odometry should always
 * report the center of the robot's position.
 * </p>
 *
 * @author Colin Robertson
 * @see AbstractOdometry
 * @since 0.0.0
 */
public interface Odometry {
    /**
     * Get the raw position reported by the odometry system. This position
     * should not be modified by any user code (unless you really want it
     * to be, but for the sake of simplicity, I'd suggest you DON'T).
     *
     * @return the raw position reported by the odometry system. The unit of
     * this position is not specified. This should always return the very
     * center of the robot's position.
     */
    PointXYZ getRawPosition();

    /**
     * Get the position produced by {@link #getRawPosition()} combined with
     * the offset produced by {@link #getOffset()}.
     *
     * @return the odometry system's position, with an offset. The unit of
     * this position is not specified. This should always return the very
     * center of the robot's position.
     */
    default PointXYZ getPosition() {
        PointXYZ position = ValidationUtils.validate(
                getRawPosition(),
                "getRawPosition()",
                "your getRawPosition() method implementation returned null, " +
                        "which it should never do!"
        );

        PointXYZ offset = ValidationUtils.validate(
                getOffset(),
                "getOffset()",
                "your getOffset() method implementation returned null, " +
                        "which it should never do!"
        );

        return position.add(offset);
    }

    /**
     * Get the odometry system's offset.
     *
     * @return the odometry system's offset.
     */
    default PointXYZ getOffset() {
        throw new UnsupportedOperationException("The implementation of " +
                "the Odometry interface you're using does not support " +
                "offsets! Maybe use AbstractOdometry?");
    }

    /**
     * Set the odometry system's offset. This will overwrite any existing
     * offset that's been set to the odometry system.
     *
     * @param offset the new offset. This offset value will replace whatever
     *               the old offset value was. An offset value is used by
     *               the {@link #getPosition()} method, which returns the result
     *               of {@link #getRawPosition()} added to the offset using
     *               {@link PointXYZ#add(PointXYZ)}.
     */
    default void setOffset(PointXYZ offset) {
        throw new UnsupportedOperationException("The implementation of " +
                "the Odometry interface you're using does not support " +
                "offsets! Maybe use AbstractOdometry?");
    }

    /**
     * Set a component of the odometry's system offset.
     *
     * @param offset the offset to apply.
     * @return {@code this}, used for method chaining.
     */
    default Odometry setOffsetX(double offset) {
        setOffset(getOffset().withX(offset));
        return this;
    }

    /**
     * Set a component of the odometry's system offset.
     *
     * @param offset the offset to apply.
     * @return {@code this}, used for method chaining.
     */
    default Odometry setOffsetY(double offset) {
        setOffset(getOffset().withY(offset));
        return this;
    }

    /**
     * Set a component of the odometry's system offset.
     *
     * @param offset the offset to apply.
     * @return {@code this}, used for method chaining.
     */
    default Odometry setOffsetZ(Angle offset) {
        setOffset(getOffset().withHeading(offset));
        return this;
    }

    /**
     * Get the X value of the offset.
     *
     * @return the X value of the offset.
     */
    default double getOffsetX() {
        return getOffset().x();
    }

    /**
     * Get the Y value of the offset.
     *
     * @return the Y value of the offset.
     */
    default double getOffsetY() {
        return getOffset().y();
    }

    /**
     * Get the Z value of the offset.
     *
     * @return the Z value of the offset.
     */
    default Angle getOffsetZ() {
        return getOffset().z();
    }

    /**
     * Modify the existing offset by whatever offset you provide.
     *
     * @param offset the offset to add to the existing offset.
     */
    default void offsetBy(PointXYZ offset) {
        ValidationUtils.validate(offset, "offset");

        setOffset(PointXYZ.add(getOffset(), offset));
    }

    /**
     * Remove the current offset. This will set the offset to (0, 0, 0).
     */
    default void removeOffset() {
        setOffset(new PointXYZ());
    }

    /**
     * Create an offset that makes the odometry's current position the
     * provided target position.
     *
     * @param targetPosition the position you'd like the odometry system
     *                       to be offset to.
     */
    default void offsetSoPositionIs(PointXYZ targetPosition) {
        ValidationUtils.validate(targetPosition, "targetPosition");

        setOffset(getRawPosition().multiply(-1).add(targetPosition));
    }

    /**
     * Find an offset that makes the odometry's current position equal to
     * (0, 0) and apply it.
     */
    default void zeroOdometry() {
        offsetSoPositionIs(new PointXYZ(0, 0, Angle.zero()));
    }

    /**
     * Get the robot's X position.
     *
     * @return {@link #getPosition()}.
     */
    default double getX() {
        return getPosition().x();
    }

    /**
     * Get the robot's Y position.
     *
     * @return {@link #getPosition()}.
     */
    default double getY() {
        return getPosition().y();
    }

    /**
     * Get the robot's Z position.
     *
     * @return {@link #getPosition()}.
     */
    default Angle getZ() {
        return getPosition().z();
    }

    /**
     * Get the robot's current heading in radians.
     *
     * @return the robot's current heading in radians.
     */
    default double getRad() {
        return getPosition().z().rad();
    }

    /**
     * Get the robot's current heading in degrees.
     *
     * @return the robot's current heading in degrees.
     */
    default double getDeg() {
        return getPosition().z().deg();
    }

    /**
     * Get the raw X value from the robot.
     *
     * @return the robot's raw X value.
     * @see #getRawPosition()
     */
    default double getRawX() {
        return getRawPosition().x();
    }

    /**
     * Get the raw Y value from the robot.
     *
     * @return the robot's raw Y value.
     * @see #getRawPosition()
     */
    default double getRawY() {
        return getRawPosition().y();
    }

    /**
     * Get the raw Z value from the robot.
     *
     * @return the robot's raw Z value.
     * @see #getRawPosition()
     */
    default Angle getRawZ() {
        return getRawPosition().z();
    }

    /**
     * Get the robot's raw heading in radians.
     *
     * @return the robot's raw heading in radians.
     */
    default double getRawRad() {
        return getRawPosition().z().rad();
    }

    /**
     * Get the robot's raw heading in degrees.
     *
     * @return the robot's raw heading in degrees.
     */
    default double getRawDeg() {
        return getRawPosition().z().deg();
    }

    /**
     * Convert {@code this} to an instance of {@link AbstractOdometry}.
     *
     * @return {@code this}, as an {@link AbstractOdometry}.
     */
    default AbstractOdometry toAbstractOdometry() {
        Supplier<PointXYZ> getPos = this::getRawPosition;

        return new AbstractOdometry() {
            @Override
            public PointXYZ getRawPosition() {
                return getPos.get();
            }
        };
    }

    default Function<PointXYZ, PointXYZ> getOdometryModifier() {
        throw new UnsupportedOperationException("The implementation of " +
                "the Odometry interface you're using does not support " +
                "modifiers! Maybe use AbstractOdometry?");
    }

    default void setOdometryModifier(Function<PointXYZ, PointXYZ> modifier) {
        throw new UnsupportedOperationException("The implementation of " +
                "the Odometry interface you're using does not support " +
                "modifiers! Maybe use AbstractOdometry?");
    }
}
