/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.control;

/**
 * A proportional, integral, derivative controller. This is like the
 * {@link ProportionalController}'s cool older cousin. Not only does it
 * factor in proportion, it even brings in the integral and derivative
 * components as well! And you thought you'd never use calculus...
 *
 * @author Colin Robertson
 * @since 0.15.0
 */
public class PIDController extends AbstractController {
    private double proportional;
    private double integral;
    private double derivative;

    private boolean firstRun = true;
    private double lastActual;
    private double errorSum;

    public PIDController() {
        this(0, 0, 0);
    }

    /**
     * Create a new {@code PIDController}.
     *
     * @param proportional the controller's proportional coefficient.
     * @param integral     the controller's integral coefficient.
     * @param derivative   the controller's derivative coefficient.
     */
    public PIDController(double proportional,
                         double integral,
                         double derivative) {
        this.proportional = proportional;
        this.integral = integral;
        this.derivative = derivative;
    }

    /**
     * Create a new {@code PIDController}.
     *
     * @param controller the controller to copy.
     */
    public PIDController(PIDController controller) {
        this(
                controller.proportional,
                controller.integral,
                controller.derivative
        );
    }

    /**
     * Create a new {@code PIDController}.
     *
     * @param controller the controller to copy.
     */
    public PIDController(ProportionalController controller) {
        this(
                controller.getCoefficient(),
                0,
                0
        );
    }

    /**
     * Set a single coefficient of the PID controller.
     *
     * @param coefficient the coefficient to set: either proportional,
     *                    integral, or derivative.
     * @param value       the value to set to the coefficient.
     * @return {@code this}, used for method chaining.
     */
    public PIDController setCoefficient(PIDCoefficient coefficient,
                                        double value) {
        switch (coefficient) {
            case PROPORTIONAL:
                proportional = value;
                break;
            case INTEGRAL:
                integral = value;
                break;
            case DERIVATIVE:
                derivative = value;
                break;
            default:
                throw new RuntimeException();
        }

        return this;
    }

    /**
     * Set the controller's coefficients.
     *
     * @param proportional the controller's proportional coefficient.
     * @param integral     the controller's integral coefficient.
     * @param derivative   the controller's derivative coefficient.
     * @return {@code this}, used for method chaining.
     */
    public PIDController setCoefficients(double proportional,
                                         double integral,
                                         double derivative) {
        this.proportional = proportional;
        this.integral = integral;
        this.derivative = derivative;

        return this;
    }

    public double getCoefficient(PIDCoefficient coefficient) {
        switch (coefficient) {
            case PROPORTIONAL:
                return proportional;
            case INTEGRAL:
                return integral;
            case DERIVATIVE:
                return derivative;
            default:
                throw new RuntimeException();
        }
    }

    public double getP() {
        return proportional;
    }

    public PIDController setP(double p) {
        proportional = p;

        return this;
    }

    public double getI() {
        return integral;
    }

    public PIDController setI(double i) {
        integral = i;

        return this;
    }

    public double getD() {
        return derivative;
    }

    public PIDController setD(double d) {
        derivative = d;

        return this;
    }

    @Override
    public double calculate(double actual) {
        double error = getTarget() - actual;

        double pOutput = proportional * error;

        if (firstRun) {
            lastActual = actual;
            firstRun = false;
        }

        double iOutput = integral * errorSum;
        double dOutput = -derivative * (actual - lastActual);
        double output = pOutput + iOutput + dOutput;

        lastActual = actual;

        return output;
    }

    @Override
    public void reset() {
        errorSum = 0.0;
    }

    /**
     * A type of coefficient: either proportional, integral, derivative.
     */
    public enum PIDCoefficient {
        PROPORTIONAL,
        INTEGRAL,
        DERIVATIVE
    }
}
