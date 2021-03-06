/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.commands;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTimeCommands {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testWaitCommand() {
        ElapsedTimer timer = new ElapsedTimer(true);
        registry.execute("wait", "3");
        Assertions.assertTrue(timer.elapsedMs() >= 3);
    }
}
