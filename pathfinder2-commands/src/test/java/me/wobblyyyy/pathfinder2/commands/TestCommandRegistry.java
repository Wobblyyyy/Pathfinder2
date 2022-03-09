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
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCommandRegistry {
    private Pathfinder pathfinder;
    private CommandRegistry registry;

    @BeforeEach
    public void beforeEach() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        registry = CommandRegistry.createDefaultRegistry(pathfinder);
    }

    @Test
    public void testParseSingleLine() {
        Assertions.assertFalse(pathfinder.isActive());
        registry.parse("goTo 10,10,0");
        Assertions.assertTrue(pathfinder.isActive());
    }

    @Test
    public void testParseTwoLines() {
        Assertions.assertFalse(pathfinder.isActive());
        registry.parse("goTo 10,10,0", "tickUntil");
        Assertions.assertFalse(pathfinder.isActive());
    }

    @Test
    public void testParseTwoLinesWithLineBreak() {
        Assertions.assertFalse(pathfinder.isActive());
        registry.parse("goTo\\", "10,10,0", "tickUntil");
        Assertions.assertFalse(pathfinder.isActive());
    }

    @Test
    public void testParseManyLines() {
        Assertions.assertFalse(pathfinder.isActive());

        String[] lines = new String[] {
            "def a 0,0,0",
            "def b 5,10,0",
            "def c 10,20,0",
            "def d 15,25,0",
            "def e 20,30,0",
            "splineTo $a\\",
            "$b\\",
            "$c\\",
            "$d\\",
            "$e",
            "tickUntil"
        };

        registry.parse(lines);

        AssertionUtils.assertIsNear(
            new PointXYZ(20, 30, 0),
            pathfinder.getPosition(),
            2,
            Angle.fromDeg(5)
        );
    }
}
