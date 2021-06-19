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
 * An abstract implementation of the {@link Controller} interface, designed
 * to simplify most of the tedious methods.
 *
 * <p>
 * The only method you have to implement is {@link #calculate(double)}. If
 * you need to get the target, you can use {@link #getTarget()}. Of course,
 * minimum and maximum are {@link #getMin()} and {@link #getMax()}.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public abstract class AbstractController implements Controller {
    private double min = Double.NEGATIVE_INFINITY;
    private double max = Double.POSITIVE_INFINITY;
    private double target = 0.0;

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public void setMin(double min) {
        this.min = min;
    }

    @Override
    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public void reset() {

    }

    @Override
    public double getTarget() {
        return target;
    }

    @Override
    public void setTarget(double target) {
        this.target = target;
    }

    @Override
    public double calculate(double value, double target) {
        setTarget(target);
        return calculate(value);
    }
}
