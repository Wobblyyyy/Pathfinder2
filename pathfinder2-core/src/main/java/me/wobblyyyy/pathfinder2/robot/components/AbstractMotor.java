/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.components;

import me.wobblyyyy.pathfinder2.time.Time;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An abstract implementation of the {@link Motor} interface. This
 * implementation does a few things - namely, it adds minimum and maximum power
 * values, as well as a feature called "lazy mode."
 *
 * <p>
 * <h3>Lazy Mode</h3>
 * Lazy mode is a feature I saw in another robotics library. Unfortunately,
 * I can't, for the life of me, remember the name of the team that wrote
 * the library or the name of the library - if you happen to know what
 * I'm talking about, do me a favor and insert that name right here. Anyways.
 * Lazy mode conditionally calls the motor's actual set power method only
 * if one of two conditions are met.
 * <ul>
 *     <li>
 *         The amount of time that's elapsed since power was last actually
 *         set to the motor and the current time is greater than a minimum
 *         elapsed milliseconds value.
 *     </li>
 *     <li>
 *         The difference between the current power value and the target
 *         power value exceeds the maximum power value delta.
 *     </li>
 * </ul>
 * Lazy mode is designed to decrease CPU load and free up some busses by not
 * requiring the device running the robot code to set power to the motor every
 * single time the set power method is called. If there's such little
 * difference between the power values that would be set to the motor, there's
 * no point in wasting resources trying to do so.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class AbstractMotor implements Motor {
    /**
     * A {@code Consumer} that accepts a double value, representing the
     * motor's target power.
     */
    private final Consumer<Double> setPower;

    /**
     * A {@code Supplier} that returns the motor's current power.
     */
    private final Supplier<Double> getPower;

    /**
     * The motor's minimum power.
     */
    private double minPower = -1.0;

    /**
     * The motor's maximum power.
     */
    private double maxPower = 1.0;

    /**
     * Is the motor operating in lazy mode? By default, this is true.
     */
    private boolean isLazy = true;

    /**
     * Maximum time delta for lazy mode. By default, this is 50 ms.
     */
    private double lazyMs = 50;

    /**
     * Maximum power value gap for lazy mode. By default, this is 0.01.
     */
    private double maxLazyPowerGap = 0.01;

    /**
     * The last recorded timestamp.
     */
    private double lastTimestamp = Time.ms();

    /**
     * The last set power value.
     */
    private double lastPower = 0.0;

    /**
     * Create a new {@code AbstractMotor} using a {@link Supplier} and a
     * {@link Consumer}.
     *
     * @param setPower a {@code Consumer} that accepts a double value. This
     *                 consumer should perform some set of actions that
     *                 actually sets power to the motor and makes it spin.
     * @param getPower a {@code Supplier} that returns the motor's current
     *                 power. This method typically calls a provided method
     *                 that queries the power from the physical motor. If such
     *                 a method is not provided, you should go about making
     *                 it function in some other way. Sorry!
     */
    public AbstractMotor(Consumer<Double> setPower,
                         Supplier<Double> getPower) {
        this.setPower = setPower;
        this.getPower = getPower;
    }

    /**
     * Get the motor's minimum power value.
     *
     * @return the motor's minimum power value.
     */
    public double getMinPower() {
        return this.minPower;
    }

    /**
     * Set the motor's minimum power value.
     *
     * @param minPower the motor's minimum power value.
     */
    public void setMinPower(double minPower) {
        this.minPower = minPower;
    }

    /**
     * Get the motor's maximum power value.
     *
     * @return the motor's maximum power value.
     */
    public double getMaxPower() {
        return this.maxPower;
    }

    /**
     * Set the motor's maximum power value.
     *
     * @param maxPower the motor's maximum power value.
     */
    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * Get the motor's minimum power value.
     *
     * @return the motor's minimum power value.
     */
    public boolean isLazy() {
        return this.isLazy;
    }

    /**
     * Set whether lazy mode is enabled or disabled. By default, lazy mode
     * is enabled.
     *
     * @param isLazy should lazy mode be enabled? If true, lazy mode will be
     *               enabled. If false, it will not.
     */
    public void setLazy(boolean isLazy) {
        this.isLazy = isLazy;
    }

    /**
     * Get the maximum amount of elapsed milliseconds between setting power
     * to the motor.
     *
     * @return the maximum amount of elapsed milliseconds before setting power
     * to the motor if operating in lazy mode.
     */
    public double getMaxElapsedMs() {
        return this.lazyMs;
    }

    /**
     * Set the maximum amount of milliseconds elapsed before power will be
     * physically set to the motor.
     *
     * @param maxElapsedMs the maximum amount of milliseconds that can be
     *                     elapsed before power is set to the motor.
     */
    public void setMaxElapsedMs(double maxElapsedMs) {
        this.lazyMs = maxElapsedMs;
    }

    /**
     * Get the maximum power gap.
     *
     * @return the maximum power gap.
     */
    public double getMaxPowerGap() {
        return this.maxLazyPowerGap;
    }

    /**
     * Set the maximum power gap. If the motor is operating in lazy mode,
     * power will only physically be set to the motor if the difference between
     * the motor's current power value and the new power value is greater than
     * this value.
     *
     * @param maxPowerGap the maximum power gap.
     */
    public void setMaxPowerGap(double maxPowerGap) {
        this.maxLazyPowerGap = maxPowerGap;
    }

    /**
     * Accept a power value.
     *
     * @param power the power value to accept.
     */
    private void accept(double power) {
        this.setPower.accept(power);
        lastPower = power;
    }

    /**
     * Get the motor's power.
     *
     * @return the motor's power.
     */
    @Override
    public double getPower() {
        return this.getPower.get();
    }

    /**
     * Set power to the motor.
     *
     * <p>
     * This method doesn't just set power to the motor. It also does a couple
     * of other pretty cool things.
     * <ul>
     *     <li>
     *         Ensure the motor's power value fits between the minimum and
     *         maximum power values. If the motor's power value is less than
     *         the minimum power value, the power will be set to the minimum
     *         power value. If the motor's power is greater than the maximum
     *         power value, the power will be set to the maximum power value.
     *     </li>
     *     <li>
     *         Do some cool stuff related to "lazy mode." Go read the
     *         {@link AbstractMotor} class JavaDoc if you're confused about
     *         what this is.
     *     </li>
     * </ul>
     * </p>
     *
     * @param power the power value to set to the motor.
     */
    @Override
    public void setPower(double power) {
        // Ensure the power value is between the minimum and maximum
        // power values. By default, these are -1.0 and 1.0, respectively.
        power = Math.max(minPower, Math.min(power, maxPower));

        // Check to see if the motor is operating in "lazy mode."
        if (isLazy) {
            // Get the current timestamp.
            double currentTimestamp = Time.ms();

            // Determine how many ms have elapsed since power was last set.
            double timeDelta = currentTimestamp - lastTimestamp;

            // Determine the difference between the motor's current power
            // value and the motor's target power value.
            double powerDelta = Math.abs(lastPower - power);

            // If the difference between the motor's current and target power
            // values is greater than the maximum power gap, or if the amount
            // of time (in ms) elapsed since power was last set to the motor
            // is greater than the maximum allowable lazy ms, set power to the
            // motor. Otherwise, do nothing.
            if (powerDelta >= maxLazyPowerGap && timeDelta >= lazyMs) {
                accept(power);
                lastTimestamp = currentTimestamp;
            }
        } else {
            // If the motor's not lazy, we set the power without doing any
            // other checks. Sick!
            accept(power);
        }
    }
}