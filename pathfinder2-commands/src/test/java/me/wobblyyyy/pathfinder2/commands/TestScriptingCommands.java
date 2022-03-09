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
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestScriptingCommands {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testDefAndGoTo() {
        registry.execute("def", "abc", "10,10,0");
        registry.execute("goTo", "$abc");
        registry.execute("tickUntil");
    }

    @Test
    public void testDefAndSplineTo() {
        registry.execute("def", "a", "0,0,0");
        registry.execute("def", "b", "5,10,0");
        registry.execute("def", "c", "10,15,0");
        registry.execute("def", "d", "15,25,0");
        registry.execute("splineTo", "$a", "$b", "$c", "$d");
        registry.execute("tickUntil");
    }
}
