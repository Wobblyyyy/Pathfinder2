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

public class Shifter {
    public Shifter(Supplier<Boolean> shiftUpInput,
                   Supplier<Boolean> shiftDownInput,
                   Consumer<Listener> listenerConsumer,
                   SimpleShifter simpleShifter) {
        Listener upListener = new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> simpleShifter.shift(ShifterDirection.UP),
                shiftUpInput
        );

        Listener downListener = new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> simpleShifter.shift(ShifterDirection.DOWN),
                shiftDownInput
        );

        listenerConsumer.accept(upListener);
        listenerConsumer.accept(downListener);
    }
}
