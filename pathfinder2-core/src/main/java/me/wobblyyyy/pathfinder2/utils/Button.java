/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import java.util.function.Supplier;

/**
 * A very simple boolean button.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Button {
    private final Supplier<Boolean> stateSupplier;
    private boolean lastState;

    /**
     * Create a new {@code Button}.
     *
     * @param stateSupplier a method that returns the button's state.
     */
    public Button(Supplier<Boolean> stateSupplier) {
        this.stateSupplier = stateSupplier;
    }

    /**
     * Is the button currently active?
     *
     * @return true if the button is pressed, false if the button is not
     * pressed.
     */
    public boolean isPressed() {
        boolean state = stateSupplier.get();
        lastState = state;
        return state;
    }

    /**
     * Is the button currently NOT active?
     *
     * @return false if the button is pressed, true if the button is
     * pressed.
     */
    public boolean isNotPressed() {
        return !stateSupplier.get();
    }

    /**
     * Was the button just pressed? When a button is pressed, this method will
     * return true one time, until the button is released and pressed again.
     *
     * @return was the button just pressed? This method will only return true
     * once per button press.
     */
    public boolean wasPressed() {
        return (!lastState) && isPressed();
    }

    /**
     * Was the button just released? When a button is released, this method will
     * return true one time, until the button is pressed and released again.
     *
     * @return was the button just released? This method will only return true
     * once per button press.
     */
    public boolean wasReleased() {
        return (lastState) && (!isPressed());
    }
}
