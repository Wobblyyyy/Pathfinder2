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
 * A feedback-based controller. Controllers function by receiving some form
 * of input and returning some form of output.
 *
 * <p>
 * Most simply, a controller has a couple of components. Firstly, a controller
 * has a target value. Secondly, a controller has a stream of input values.
 * For each of these input values, the controller will output a value that
 * it "thinks" will bring the next input value closer to the target value.
 * </p>
 *
 * <p>
 * Like many other interfaces in this library, there's an abstract class that's
 * probably more convenient to work with than simply using the interface.
 * Go take a look at the {@link AbstractController}.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Controller {
    /**
     * The controller's minimum output value.
     *
     * @return the controller's minimum output value.
     */
    double getMin();

    /**
     * The controller's maximum output value.
     *
     * @return the controller's maximum output value.
     */
    double getMax();

    /**
     * Set the controller's minimum output value.
     *
     * @param min the controller's minimum output value. Any output below this
     *            value will automatically be set to this value.
     */
    void setMin(double min);

    /**
     * Set the controller's maximum output value.
     *
     * @param max the controller's maximum output value. Any output above this
     *            value will automatically be set to this value.
     */
    void setMax(double max);

    /**
     * Reset the controller. Some controllers have no use for this method,
     * but it's there anyways because my code sucks and I'm too lazy to
     * change it.
     */
    void reset();

    /**
     * Get the controller's target value.
     *
     * @return the controller's target value.
     */
    double getTarget();

    /**
     * Set the controller's target value.
     *
     * @param target the controller's target value.
     */
    void setTarget(double target);

    /**
     * Use an input reading to determine an output.
     *
     * @param value the input the controller should calculate for.
     * @return a value calculated by the controller.
     */
    double calculate(double value);

    /**
     * Use an input reading to determine an output. Also, set the target
     * point. It's a real two-in-one, you know?
     *
     * @param value  the input the controller should calculate for.
     * @param target the value the controller should attempt to reach.
     * @return a value calculated by the controller.
     */
    double calculate(double value, double target);
}
