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

/**
 * An abstract implementation of {@link Motor}.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public abstract class BaseMotor implements Motor {
    /**
     * Should power values be inverted when being set?
     */
    private boolean isSetInverted;

    /**
     * Should power values be inverted when being retrieved?
     */
    private boolean isGetInverted;

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
     * The motor's deadband. Any power value set to the motor that has an
     * absolute value less than this value will actually set the motor's power
     * to 0 to prevent motor slippage.
     */
    private double deadband;

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
    public BaseMotor setMinPower(double minPower) {
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
    public BaseMotor setMaxPower(double maxPower) {
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
    public BaseMotor setLazy(boolean isLazy) {
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
     * Get the deadband.
     *
     * @return the deadband.
     */
    public double getDeadband() {
        return this.deadband;
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
    public BaseMotor setMaxPowerGap(double maxPowerGap) {
        this.maxLazyPowerGap = maxPowerGap;

        return this;
    }

    /**
     * Set if the motor's set method is inverted.
     *
     * @param isSetInverted should the motor's set method be inverted?
     * @return {@code this}, used for method chaining.
     */
    public BaseMotor setIsSetInverted(boolean isSetInverted) {
        this.isSetInverted = isSetInverted;

        return this;
    }

    /**
     * Set if the motor's get method is inverted.
     *
     * @param isGetInverted should the motor's get method be inverted?
     * @return {@code this}, used for method chaining.
     */
    public BaseMotor setIsGetInverted(boolean isGetInverted) {
        this.isGetInverted = isGetInverted;

        return this;
    }

    /**
     * Set the motor's set and get inversion states.
     *
     * @param isInverted should the motor be inverted?
     * @return {@code this}, used for method chaining.
     */
    public BaseMotor setIsInverted(boolean isInverted) {
        this.isSetInverted = isInverted;
        this.isGetInverted = isInverted;

        return this;
    }

    /**
     * Set the deadband.
     *
     * @param deadband the deadband.
     * @return {@code this}, used for method chaining.
     */
    public BaseMotor setDeadband(double deadband) {
        this.deadband = deadband;

        return this;
    }

    public abstract void abstractSetPower(double power);

    public double abstractGetPower() {
        return lastPower;
    }

    /**
     * Accept a power value.
     *
     * @param power the power value to accept.
     */
    private void accept(double power) {
        abstractSetPower(power);
        lastPower = power;
    }

    /**
     * Get the motor's power.
     *
     * @return the motor's power.
     */
    @Override
    public double getPower() {
        double power = abstractGetPower();

        if (isGetInverted) power *= -1;

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
        power = Math.max(minPower, Math.min(power, maxPower));

        if (Math.abs(power) < deadband) power = 0;

        if (isSetInverted) power = power * -1;

        if (isLazy) if (Math.abs(lastPower - power) >= maxLazyPowerGap) accept(
            power
        ); else accept(power);
    }
}
