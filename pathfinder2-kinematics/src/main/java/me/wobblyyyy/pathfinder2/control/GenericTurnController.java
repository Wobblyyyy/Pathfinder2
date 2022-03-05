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
 * Provide a simple controller used for turns. This is an extension of
 * the {@link AngleDeltaController} class.
 *
 * <p>
 * It's suggested you use one of the two static methods in this class to
 * instantiate a {@code GenericTurnController}.
 * <ul>
 *     <li>{@link #newController(double)}</li>
 *     <li>{@link #newInvertedController(double)}</li>
 * </ul>
 * If you create a controller using the first method ({@link #newController(double)})
 * and notice that your robot is turning in the opposite direction than the
 * robot should be turning in, use {@link #newInvertedController(double)}.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class GenericTurnController extends AngleDeltaController {

    /**
     * Create a new {@code GenericTurnController}.
     *
     * @param coefficient the controller's coefficient. A higher coefficient
     *                    means the robot will turn more quickly. A low
     *                    coefficient means the robot will turn more slowly.
     *                    You'll have to play around with this value until
     *                    you get it right, but generally, this number should
     *                    be around 0.01.
     */
    public GenericTurnController(double coefficient) {
        super(coefficient);
    }

    /**
     * Create a new {@code GenericTurnController}.
     *
     * @param coefficient the controller's coefficient. A higher coefficient
     *                    means the robot will turn more quickly. A low
     *                    coefficient means the robot will turn more slowly.
     *                    You'll have to play around with this value until
     *                    you get it right, but generally, this number should
     *                    be around 0.01.
     * @return a new {@code GenericTurnController}.
     */
    public static GenericTurnController newController(double coefficient) {
        return new GenericTurnController(coefficient);
    }

    /**
     * Create a new, inverted {@code GenericTurnController}. Because this
     * controller is inverted, it turns in the opposite direction a controller
     * created with {@link #newController(double)} would.
     *
     * @param coefficient the controller's coefficient. A higher coefficient
     *                    means the robot will turn more quickly. A low
     *                    coefficient means the robot will turn more slowly.
     *                    You'll have to play around with this value until
     *                    you get it right, but generally, this number should
     *                    be around 0.01.
     * @return a new {@code GenericTurnController}.
     */
    public static GenericTurnController newInvertedController(
        double coefficient
    ) {
        return new GenericTurnController(coefficient * -1);
    }
}
