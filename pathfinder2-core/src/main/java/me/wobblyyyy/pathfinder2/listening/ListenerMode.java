/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.listening;

/**
 * Modes a listener can operate in.
 *
 * @author Colin Robertson
 * @since 0.7.1
 * @see Listener
 */
public enum ListenerMode {
    /**
     * Activated whenever the condition is met. It doesn't matter the history
     * of whether the condition has been met.
     */
    CONDITION_IS_MET,

    /**
     * Activated whenever the condition is not met. It doesn't matter the
     * history of whether the condition has been met.
     */
    CONDITION_IS_NOT_MET,

    /**
     * Activated the first time the condition is met. If the condition is
     * met twice in a row, it's only activated once. Activation will not
     * occur again until the condition has been not met and then met once
     * again.
     */
    CONDITION_NEWLY_MET,

    /**
     * Activated the first time the condition is not met. If the condition is
     * not met twice in a row, it's only activated once. Activation will not
     * occur again until the condition has been met and then not met once
     * again.
     */
    CONDITION_NEWLY_NOT_MET,

    /**
     * Activated whenever the return value of the {@code Supplier} changes
     * from true to false or false to true.
     */
    CONDITION_NEWLY_CHANGED,

    /**
     * Only when the condition has never been met. If the condition has
     * been met even once before, the listener will automatically remove
     * itself from the listener manager so it can't be executed again.
     */
    CONDITION_NEVER_MET,

    /**
     * Only when the condition has always been met. If the condition has
     * been not met even once before, the listener will automatically remove
     * itself from the listener manager so it can't be executed again.
     */
    CONDITION_ALWAYS_MET,
}
