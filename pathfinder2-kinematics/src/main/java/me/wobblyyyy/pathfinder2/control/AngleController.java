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

import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * A feedback-based controller. Controllers function by receiving some form
 * of input and returning some form of output. This type of controller
 * works with {@code Angle}.
 *
 * @author Colin Robertson
 * @since 1.3.0
 */
public interface AngleController {
    /**
     * The controller's minimum output value.
     *
     * @return the controller's minimum output value.
     */
    Angle getMin();

    /**
     * Set the controller's minimum output value.
     *
     * @param min the controller's minimum output value. Any output below this
     *            value will automatically be set to this value.
     */
    void setMin(Angle min);

    /**
     * The controller's maximum output value.
     *
     * @return the controller's maximum output value.
     */
    Angle getMax();

    /**
     * Set the controller's maximum output value.
     *
     * @param max the controller's maximum output value. Any output above this
     *            value will automatically be set to this value.
     */
    void setMax(Angle max);

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
    Angle getTarget();

    /**
     * Set the controller's target value.
     *
     * @param target the controller's target value.
     */
    void setTarget(Angle target);

    /**
     * Use an input reading to determine an output.
     *
     * @param value the input the controller should calculate for.
     * @return a value calculated by the controller.
     */
    Angle calculate(Angle value);

    /**
     * Use an input reading to determine an output. Also, set the target
     * point. It's a real two-in-one, you know?
     *
     * @param value  the input the controller should calculate for.
     * @param target the value the controller should attempt to reach.
     * @return a value calculated by the controller.
     */
    Angle calculate(Angle value, Angle target);
}
