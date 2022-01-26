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
 * Update: as of January 24th, 2022, the maximum lazy milliseconds value is
 * set to infinity, negating its purpose.
 * </p>
 *
 * <p>
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
     * Should power values be inverted when being set?
     */
    private final boolean isSetInverted;

    /**
     * Should power values be inverted when being retrieved?
     */
    private final boolean isGetInverted;

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
     * Maximum power value gap for lazy mode. By default, this is 0.01.
     */
    private double maxLazyPowerGap = 0.01;

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
        this(
                setPower,
                getPower,
                false,
                false
        );
    }

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
     * @param isInverted if this is true, all {@code setPower} and
     *                   {@code getPower} operations will multiply any
     *                   inputted/outputted power values by -1.
     */
    public AbstractMotor(Consumer<Double> setPower,
                         Supplier<Double> getPower,
                         boolean isInverted) {
        this(
                setPower,
                getPower,
                isInverted,
                isInverted
        );
    }

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
     * @param isSetInverted if this is true, all {@code setPower} operations
     *                      will multiply the inputted power by -1.
     * @param isGetInverted if this is true, all {@code getPower} operations
     *                      will multiply the outputted power by -1.
     */
    public AbstractMotor(Consumer<Double> setPower,
                         Supplier<Double> getPower,
                         boolean isSetInverted,
                         boolean isGetInverted) {
        this.setPower = setPower;
        this.getPower = getPower;
        this.isSetInverted = isSetInverted;
        this.isGetInverted = isGetInverted;
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
     * @return {@code this}, used for method chaining.
     */
    public AbstractMotor setMinPower(double minPower) {
        this.minPower = minPower;

        return this;
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
     * @return {@code this}, used for method chaining.
     */
    public AbstractMotor setMaxPower(double maxPower) {
        this.maxPower = maxPower;

        return this;
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
     * @return {@code this}, used for method chaining.
     */
    public AbstractMotor setLazy(boolean isLazy) {
        this.isLazy = isLazy;

        return this;
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
     * @return {@code this}, used for method chaining.
     */
    public AbstractMotor setMaxPowerGap(double maxPowerGap) {
        this.maxLazyPowerGap = maxPowerGap;

        return this;
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
        double power = this.getPower.get();

        if (isGetInverted) {
            power *= -1;
        }

        return power;
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

        if (isSetInverted) {
            power = power * -1;
        }

        if (isLazy) {
            if (Math.abs(lastPower - power) >= maxLazyPowerGap)
                accept(power);
        } else {
            accept(power);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractMotor) {
            AbstractMotor motor = (AbstractMotor) obj;

            boolean sameSetPower = motor.setPower == this.setPower;
            boolean sameGetPower = motor.getPower == this.getPower;
            boolean sameSetInvert = motor.isSetInverted == this.isSetInverted;
            boolean sameGetInvert = motor.isGetInverted == this.isGetInverted;

            return sameSetPower &&
                    sameGetPower &&
                    sameSetInvert &&
                    sameGetInvert;
        }

        return false;
    }

    @SuppressWarnings("ALL")
    @Override
    protected Object clone() {
        return new AbstractMotor(
                this.setPower,
                this.getPower,
                this.isSetInverted,
                this.isGetInverted
        );
    }
}
