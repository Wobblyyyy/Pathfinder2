/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * A combination of two joysticks with a couple of utility methods.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class DualJoystick {
    private final Joystick joystickLeft;
    private final Joystick joystickRight;

    /**
     * Create a new {@code DualJoystick} instance.
     *
     * @param joystickLeft  the left joystick.
     * @param joystickRight the right joystick.
     */
    public DualJoystick(Joystick joystickLeft, Joystick joystickRight) {
        this.joystickLeft = joystickLeft;
        this.joystickRight = joystickRight;
    }

    /**
     * Get the left joystick.
     *
     * @return the left joystick.
     */
    public Joystick left() {
        return joystickLeft;
    }

    /**
     * Get the right joystick.
     *
     * @return the right joystick.
     */
    public Joystick right() {
        return joystickRight;
    }

    /**
     * Get the left joystick's X value.
     *
     * @return the left joystick's X value.
     */
    public double leftX() {
        return left().getX();
    }

    /**
     * Get the left joystick's Y value.
     *
     * @return the left joystick's Y value.
     */
    public double leftY() {
        return left().getY();
    }

    /**
     * Get the right joystick's X value.
     *
     * @return the right joystick's X value.
     */
    public double rightX() {
        return right().getX();
    }

    /**
     * Get the right joystick's Y value.
     *
     * @return the right joystick's Y value.
     */
    public double rightY() {
        return right().getY();
    }

    /**
     * Get the left joystick's magnitude.
     *
     * @return the left joystick's magnitude.
     */
    public double leftMagnitude() {
        return left().getMagnitude();
    }

    /**
     * Get the right joystick's magnitude.
     *
     * @return the right joystick's magnitude.
     */
    public double rightMagnitude() {
        return right().getMagnitude();
    }

    /**
     * Get the left joystick's angle.
     *
     * @return the left joystick's angle.
     */
    public Angle leftAngle() {
        return left().getAngle();
    }

    /**
     * Get the right joystick's angle.
     *
     * @return the right joystick's angle.
     */
    public Angle rightAngle() {
        return right().getAngle();
    }

    /**
     * Get the left joystick's point.
     *
     * @return the left joystick's point.
     */
    public PointXY leftPoint() {
        return left().getPoint();
    }

    /**
     * Get the right joystick's point.
     *
     * @return the right joystick's point.
     */
    public PointXY rightPoint() {
        return right().getPoint();
    }

    /**
     * Get the value of a given axis.
     *
     * @param axis the axis to get the value of.
     * @return the value of the provided axis.
     */
    public double getAxisValue(Axis axis) {
        switch (axis) {
            case LEFT_X:
                return leftX();
            case LEFT_Y:
                return leftY();
            case RIGHT_X:
                return rightX();
            case RIGHT_Y:
                return rightY();
            default:
                throw new IllegalArgumentException("Invalid axis!");
        }
    }

    /**
     * Get a translation with the following default values:
     *
     * <ul>
     *     <li>Forwards/backwards axis: left y</li>
     *     <li>Right/left axis: left x</li>
     *     <li>Turn axis: right x</li>
     * </ul>
     *
     * @return a translation formed with {@link #translation(Axis, Axis, Axis)}.
     */
    public Translation translation() {
        return translation(Axis.LEFT_Y, Axis.LEFT_X, Axis.RIGHT_X);
    }

    /**
     * Get the translation formed by three of the joystick's axes.
     *
     * @param forwardsAxis the axis responsible for controlling forwards and
     *                     backwards motion. This is typically a Y axis.
     * @param strafeAxis   the axis responsible for controlling left and right
     *                     motion. This is typically an X axis.
     * @param turnAxis     the axis responsible for controlling turning. This
     *                     is typically an X axis.
     * @return a translation formed by the values of the joystick's axes.
     */
    public Translation translation(
        Axis forwardsAxis,
        Axis strafeAxis,
        Axis turnAxis
    ) {
        double forwardsValue = getAxisValue(forwardsAxis);
        double strafeValue = getAxisValue(turnAxis);
        double turnValue = getAxisValue(turnAxis);

        return new Translation(forwardsValue, strafeValue, turnValue);
    }

    /**
     * A {@code DualJoystick} has four axes. Those axes are...
     * <ul>
     *     <li>Left X</li>
     *     <li>Left Y</li>
     *     <li>Right X</li>
     *     <li>Right Y</li>
     * </ul>
     */
    public enum Axis {
        /**
         * The left joystick's X axis.
         */
        LEFT_X,

        /**
         * The left joystick's Y axis.
         */
        LEFT_Y,

        /**
         * The right joystick's X axis.
         */
        RIGHT_X,

        /**
         * The right joystick's Y axis.
         */
        RIGHT_Y,
    }
}
