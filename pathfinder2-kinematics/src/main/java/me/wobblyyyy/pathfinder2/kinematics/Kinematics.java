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

import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * Based on a translation, calculate a value of type E. Forwards kinematics
 * convert a {@code Translation} into some form (of type {@code E}) that the
 * robot can then use to interact with the drive train in a matter that
 * moves the robot according to the {@code Translation}.
 *
 * @param <E>
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Kinematics<E> {
    /**
     * Calculate a value of type E based on a translation.
     *
     * @param translation the translation to calculate a value of type E
     *                    based on.
     * @return a calculated value based on the provided translation.
     */
    E calculate(Translation translation);

    /**
     * Calculate a translation based on a state.
     *
     * @param state convert a state into a translation.
     * @return the state, as a translation.
     */
    Translation toTranslation(E state);
}
