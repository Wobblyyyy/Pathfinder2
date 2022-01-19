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

import java.util.function.Supplier;

/**
 * Listener based on a boolean state: take, for example, a button, a
 * limit switch, or... uhh... what else... I don't know. You get what I mean,
 * right? Yeah.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class DigitalListener extends Listener {
    /**
     * Create a new {@code DigitalListener}.
     *
     * @param whenTriggered functionality to be executed whenever the listener
     *                      is triggered.
     * @param input         the input that's being listened to.
     */
    public DigitalListener(Runnable whenTriggered,
                           Supplier<Boolean> input) {
        super(
                ListenerMode.CONDITION_NEWLY_MET,
                whenTriggered,
                input
        );
    }
}
