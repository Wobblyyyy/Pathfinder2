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

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An abstract implementation of the {@link Motor} interface. This
 * implementation does a few things - namely, it adds minimum and maximum power
 * values, as well as a feature called "lazy mode."
 *
 * <p>
 * Lazy mode is a feature I saw in another robotics library. Unfortunately,
 * I can't, for the life of me, remember the name of the team that wrote
 * the library or the name of the library - if you happen to know what
 * I'm talking about, do me a favor and insert that name right here. Anyways.
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
public class AbstractMotor extends BaseMotor {
    private final Consumer<Double> setPower;
    private final Supplier<Double> getPower;

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
    public AbstractMotor(Consumer<Double> setPower, Supplier<Double> getPower) {
        this(setPower, getPower, false, false);
    }

    /**
     * Create a new {@code AbstractMotor} using a {@link Supplier} and a
     * {@link Consumer}.
     *
     * @param setPower   a {@code Consumer} that accepts a double value. This
     *                   consumer should perform some set of actions that
     *                   actually sets power to the motor and makes it spin.
     * @param getPower   a {@code Supplier} that returns the motor's current
     *                   power. This method typically calls a provided method
     *                   that queries the power from the physical motor. If such
     *                   a method is not provided, you should go about making
     *                   it function in some other way. Sorry!
     * @param isInverted if this is true, all {@code setPower} and
     *                   {@code getPower} operations will multiply any
     *                   inputted/outputted power values by -1.
     */
    public AbstractMotor(
        Consumer<Double> setPower,
        Supplier<Double> getPower,
        boolean isInverted
    ) {
        this(setPower, getPower, isInverted, isInverted);
    }

    /**
     * Create a new {@code AbstractMotor} using a {@link Supplier} and a
     * {@link Consumer}.
     *
     * @param setPower      a {@code Consumer} that accepts a double value. This
     *                      consumer should perform some set of actions that
     *                      actually sets power to the motor and makes it spin.
     * @param getPower      a {@code Supplier} that returns the motor's current
     *                      power. This method typically calls a provided method
     *                      that queries the power from the physical motor. If such
     *                      a method is not provided, you should go about making
     *                      it function in some other way. Sorry!
     * @param isSetInverted if this is true, all {@code setPower} operations
     *                      will multiply the inputted power by -1.
     * @param isGetInverted if this is true, all {@code getPower} operations
     *                      will multiply the outputted power by -1.
     */
    public AbstractMotor(
        Consumer<Double> setPower,
        Supplier<Double> getPower,
        boolean isSetInverted,
        boolean isGetInverted
    ) {
        this(setPower, getPower, isSetInverted, isGetInverted, 0.0);
    }

    /**
     * Create a new {@code AbstractMotor} using a {@link Supplier} and a
     * {@link Consumer}.
     *
     * @param setPower      a {@code Consumer} that accepts a double value. This
     *                      consumer should perform some set of actions that
     *                      actually sets power to the motor and makes it spin.
     * @param getPower      a {@code Supplier} that returns the motor's current
     *                      power. This method typically calls a provided method
     *                      that queries the power from the physical motor. If such
     *                      a method is not provided, you should go about making
     *                      it function in some other way. Sorry!
     * @param isSetInverted if this is true, all {@code setPower} operations
     *                      will multiply the inputted power by -1.
     * @param isGetInverted if this is true, all {@code getPower} operations
     *                      will multiply the outputted power by -1.
     * @param deadband      the motor's deadband. If a power value is set to
     *                      a motor such that the absolute value of the
     *                      motor's power is less than this value, the motor's
     *                      power will simply be set to 0. This is an option
     *                      in case you'd like to try to prevent motor
     *                      slippage, leading to encoder inaccuracies.
     */
    public AbstractMotor(
        Consumer<Double> setPower,
        Supplier<Double> getPower,
        boolean isSetInverted,
        boolean isGetInverted,
        double deadband
    ) {
        this.setPower = setPower;
        this.getPower = getPower;

        setIsSetInverted(isSetInverted);
        setIsGetInverted(isGetInverted);
        setDeadband(deadband);
    }

    @Override
    public void abstractSetPower(double power) {
        setPower.accept(power);
    }

    @Override
    public double abstractGetPower() {
        return getPower.get();
    }
}
