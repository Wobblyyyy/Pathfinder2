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

import me.wobblyyyy.pathfinder2.listening.Listener;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Toggle {
    private boolean state;

    public Toggle(Supplier<Boolean> input,
                  Consumer<Listener> listenerConsumer,
                  boolean initialState) {
        Listener toggleListener = new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                this::toggle,
                input
        );

        listenerConsumer.accept(toggleListener);
        state = initialState;
    }

    public Toggle(Supplier<Boolean> input,
                  Consumer<Listener> listenerConsumer) {
        this(input, listenerConsumer, false);
    }

    public void toggle() {
        state = !state;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
