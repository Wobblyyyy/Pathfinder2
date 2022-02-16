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
            default: throw new RuntimeException();
        }
        
        return this;
    }

    public double getCoefficient(PIDCoefficient coefficient) {
        switch (coefficient) {
            case PROPORTIONAL: return proportional;
            case INTEGRAL: return integral;
            case DERIVATIVE: return derivative;
            default: throw new RuntimeException();
        }
    }

    public PIDController setP(double p) {
        proportional = p;

        return this;
    }

    public PIDController setI(double i) {
        integral = i;

        return this;
    }

    public PIDController setD(double d) {
        derivative = d;

        return this;
    }

    public double getP() {
        return proportional;
    }

    public double getI() {
        return integral;
    }

    public double getD() {
        return derivative;
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

    public enum PIDCoefficient {
        PROPORTIONAL,
        INTEGRAL,
        DERIVATIVE
    }
}
