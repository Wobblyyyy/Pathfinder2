/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import me.wobblyyyy.pathfinder2.math.Max;

/**
 * A state for a meccanum chassis.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class MeccanumState {
    /**
     * The front-left motor state.
     */
    private final double fl;

    /**
     * The front-right motor state.
     */
    private final double fr;

    /**
     * The back-left motor state.
     */
    private final double bl;

    /**
     * The back-right motor state.
     */
    private final double br;

    /**
     * The magnitude of the translation.
     */
    private final double magnitude;

    /**
     * Create a new meccanum state.
     *
     * @param fl the front-left motor.
     * @param fr the front-right motor.
     * @param bl the back-left motor.
     * @param br the back-right motor.
     */
    public MeccanumState(double fl,
                         double fr,
                         double bl,
                         double br) {
        this(
                fl,
                fr,
                bl,
                br,
                0
        );
    }

    /**
     * Create a new meccanum state.
     *
     * @param fl        the front-left motor.
     * @param fr        the front-right motor.
     * @param bl        the back-left motor.
     * @param br        the back-right motor.
     * @param magnitude the magnitude of the translation.
     */
    public MeccanumState(double fl,
                         double fr,
                         double bl,
                         double br,
                         double magnitude) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
        this.magnitude = magnitude;
    }

    /**
     * Get the front-left motor's value.
     *
     * @return the front-left motor's value.
     */
    public double fl() {
        return this.fl;
    }

    /**
     * Get the front-right motor's value.
     *
     * @return the front-right motor's value.
     */
    public double fr() {
        return this.fr;
    }

    /**
     * Get the back-left motor's value.
     *
     * @return the back-left motor's value.
     */
    public double bl() {
        return this.bl;
    }

    /**
     * Get the back-right motor's value.
     *
     * @return the back-right motor's value.
     */
    public double br() {
        return this.br;
    }

    /**
     * Get the magnitude of the translation.
     *
     * @return the magnitude of the translation.
     */
    public double magnitude() {
        return this.magnitude;
    }

    /**
     * Get the max power value out of all the motor power values.
     *
     * @return the maximum power value.
     */
    public double maxPower() {
        return Max.absoluteOf(
                fl,
                fr,
                bl,
                br
        );
    }

    /**
     * Create a new (normalized) meccanum state.
     *
     * @param max the maximum power value. This is the highest value the
     *            normalized state can have for any individual power value.
     * @return a normalized meccanum state.
     */
    public MeccanumState normalize(double max) {
        max = Math.abs(max);
        double realMax = maxPower();

        double _fl;
        double _fr;
        double _bl;
        double _br;

        if (realMax > max) {
            _fl = (fl / realMax) * max;
            _fr = (fr / realMax) * max;
            _bl = (bl / realMax) * max;
            _br = (br / realMax) * max;
        } else {
            _fl = fl;
            _fr = fr;
            _bl = bl;
            _br = br;
        }

        return new MeccanumState(_fl, _fr, _bl, _br);
    }

    /**
     * Normalize the meccanum state from the state's maximum power value.
     * If the state's maximum power value is greater than 1.0, 1.0 will
     * be used in place of the maximum power value.
     *
     * @return a normalized meccanum state.
     */
    public MeccanumState normalizeFromMaxUnderOne() {
        double max = maxPower();

        return normalize(max > 1 ? 1.0 : max);
    }

    /**
     * Add two meccanum states together. I have absolutely no idea why anybody
     * would literally ever use this method ever.
     *
     * @param state the state to add.
     * @return the sum of the two states.
     */
    public MeccanumState add(MeccanumState state) {
        return new MeccanumState(
                fl + state.fl,
                fr + state.fr,
                bl + state.bl,
                br + state.br
        );
    }

    /**
     * Multiply two meccanum states together.
     *
     * @param state the state to multiply by.
     * @return the product of the two states.
     */
    public MeccanumState multiply(MeccanumState state) {
        return new MeccanumState(
                fl * state.fl,
                fr * state.fr,
                bl * state.bl,
                br * state.br
        );
    }

    /**
     * Multiply a meccanum state's four motor values based on a multiplier.
     *
     * @param multiplier the multiplier to multiply each of the power values
     *                   by.
     * @return the multiplied meccanum state.
     */
    public MeccanumState multiply(double multiplier) {
        return multiply(new MeccanumState(
                multiplier,
                multiplier,
                multiplier,
                multiplier
        ));
    }
}
