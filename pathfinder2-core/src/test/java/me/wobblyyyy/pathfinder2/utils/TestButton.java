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

import me.wobblyyyy.pathfinder2.Pathfinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestButton {
    private Pathfinder pathfinder;
    private AtomicBoolean buttonState;
    private Button button;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        buttonState = new AtomicBoolean(false);
        button = pathfinder.newButton(buttonState::get);
    }

    @Test
    public void testWhenPressed() {
        AtomicBoolean hasBeenPressed = new AtomicBoolean(false);

        button.whenPressed(() -> {
            hasBeenPressed.set(true);
        });

        pathfinder.tick();
        buttonState.set(true);
        pathfinder.tick();
        buttonState.set(false);
        pathfinder.tick();

        Assertions.assertTrue(hasBeenPressed.get());
    }

    @Test
    public void testWhilePressed() {
        AtomicInteger count = new AtomicInteger(0);

        button.whilePressed(() -> {
            count.set(count.get() + 1);
        });

        pathfinder.tick();
        buttonState.set(true);
        pathfinder.tick();
        pathfinder.tick();
        pathfinder.tick();
        buttonState.set(false);
        pathfinder.tick();

        Assertions.assertEquals(3, count.get());
    }
}
