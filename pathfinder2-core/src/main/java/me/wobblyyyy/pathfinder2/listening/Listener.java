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

import me.wobblyyyy.pathfinder2.Pathfinder;

import java.util.function.Supplier;

/**
 * Responsible for listening for a condition and executing some functionality
 * if that condition is met. Listeners allow code to be executed whenever
 * a certain condition is met: for example, your robot could automatically
 * slow down if it's approaching a wall, or it could automatically activate
 * some servo if some condition is met, etc.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class Listener implements Tickable {
    private final ListenerMode mode;
    private final Runnable whenTriggered;
    private final Supplier<Boolean>[] input;
    private boolean previousInput;

    /**
     * Create a new {@code Listener}.
     *
     * @param mode          the mode the listener should operate in.
     *                      See the {@link ListenerMode} documentation to
     *                      learn more.
     * @param whenTriggered functionality to be executed whenever the
     *                      condition is triggered.
     * @param input         a variable argument parameter: a set of
     *                      {@link Supplier}s that are used for determining
     *                      the listener's input state. For example, if all
     *                      of these suppliers return true, meaning all of the
     *                      required conditions are met, then the whenTriggered
     *                      {@link Runnable} will be invoked. If any of the
     *                      suppliers return false, then the input state is
     *                      false. The only way to trigger the listener
     *                      being "true" is by having all of the suppliers
     *                      provided in this parameter also return true.
     */
    public Listener(ListenerMode mode,
                    Runnable whenTriggered,
                    Supplier<Boolean>... input) {
        this.mode = mode;
        this.whenTriggered = whenTriggered;
        this.input = input;
    }

    @Override
    public boolean tick(Pathfinder pathfinder) {
        boolean input = true;

        for (Supplier<Boolean> supplier : this.input)
            if (!supplier.get()) {
                input = false;
                break;
            }

        switch (mode) {
            case CONDITION_IS_MET:
                if (input) whenTriggered.run();
                break;
            case CONDITION_IS_NOT_MET:
                if (!input) whenTriggered.run();
                break;
            case CONDITION_NEWLY_MET:
                if (!previousInput && input) whenTriggered.run();
                break;
            case CONDITION_NEWLY_NOT_MET:
                if (previousInput && !input) whenTriggered.run();
                break;
            case CONDITION_NEWLY_CHANGED:
                if ((!previousInput && input) || (previousInput && !input)) whenTriggered.run();
                break;
        }

        previousInput = input;

        return true;
    }
}
