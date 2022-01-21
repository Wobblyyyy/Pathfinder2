/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

/**
 * A {@code Toggle} is a rather basic concept - it can be in one of two states:
 * on and off. Whenever the toggle is toggled, it'll follow the following
 * pattern:
 *
 * <ul>
 *     <li>If it's off, the toggle will turn to on.</li>
 *     <li>If it's on, the toggle will turn to off.</li>
 * </ul>
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class Toggle {
    private boolean state;

    /**
     * Create a new {@code Toggle}.
     *
     * @param initialState the state the toggle is initially in.
     */
    public Toggle(boolean initialState) {
        this.state = initialState;
    }

    /**
     * Create a new {@code Toggle} with an initial state of FALSE.
     */
    public Toggle() {
        this(false);
    }

    /**
     * Toggle the toggle. If it's on, turn it off. If it's off, turn it on.
     */
    public void toggle() {
        state = !state;
    }

    /**
     * Get the toggle's current state.
     *
     * @return the toggle's current state.
     */
    public boolean getState() {
        return state;
    }

    /**
     * Override/set the toggle's state.
     *
     * @param state the toggle's new state.
     */
    public void setState(boolean state) {
        this.state = state;
    }
}
