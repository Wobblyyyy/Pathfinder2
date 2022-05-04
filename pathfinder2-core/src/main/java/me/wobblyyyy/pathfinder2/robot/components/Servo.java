/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.components;

import me.wobblyyyy.pathfinder2.recording.Recordable;

/**
 * A rotary actuator that provides precise control over position, velocity,
 * and acceleration. A {@code Servo} has a minimum and maximum position of
 * 0.0 and 1.0, respectively.
 *
 * <p>
 * If the default {@link #setPosition(double)} method does not suit your needs
 * (say your servo has a range that's not just 0 to 1), you can use the
 * {@link #setPosition(double, double, double)} method instead.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public interface Servo extends Recordable<Double> {
    /**
     * Set the position of the servo. This will command the servo to move
     * to the provided position. This method should not block the program's
     * execution until the servo reaches the specified position.
     *
     * @param position the position of the servo. This position value should
     *                 be greater than or equal to 0 and less than or equal
     *                 to 1.
     */
    void setPosition(double position);

    /**
     * Get the last position that the servo was commanded to move to. Please
     * note that this will return an inaccurate value if the servo is moved
     * while not powered - this simply returns the last position the servo
     * was commanded to move to.
     *
     * @return the last position the servo was commanded to move to.
     */
    double getPosition();

    /**
     * Set the position of the servo by using the {@code min} and
     * {@code max} values to define a range of values, and then scale
     * {@code value} into the range 0 to 1.
     *
     * @param value the position to set to the servo.
     * @param min   the minimum position of the servo.
     * @param max   the maximum position of the servo.
     */
    default void setPosition(double value, double min, double max) {
        double delta = max - min;
        double position = (value - min) / delta;

        setPosition(position);
    }

    /**
     * Set the position of the servo as a percent value, from 0 to 100%.
     *
     * @param percent the percent position to set the servo to.
     */
    default void setPercent(double percent) {
        setPosition(percent / 100);
    }

    /**
     * Get the velocity of the servo. This velocity should be difference in
     * position over seconds. If this method is not implemented, it will
     * return 0. If the method is implemented, it should return the velocity
     * of the servo.
     *
     * @return the velocity of the servo, measured in difference in position
     * per second.
     */
    default double getVelocity() {
        return 0;
    }

    @Override
    default Double getRecordingValue() {
        return getPosition();
    }

    @Override
    default void setRecordingValue(Object obj) {
        if (obj instanceof Double) {
            Double d = (Double) obj;
            setPosition(d);
        }
    }
}
