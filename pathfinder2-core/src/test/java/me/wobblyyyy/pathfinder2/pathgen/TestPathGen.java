/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.pathgen;

import java.util.List;
import org.junit.jupiter.api.Test;

public class TestPathGen {

    @Test
    public void testUnobstructedRoute() {
        Grid grid = Grid.generateGrid(10, 10);
        Node start = new Node(0, 0);
        Node end = new Node(9, 9);
        PathGen gen = new PathGen(grid, start, end);
        gen.findPath();
        List<Node> path = gen.getPath();
    }

    @Test
    public void testEntirelyObstructedRoute() {
        Grid grid = Grid.generateGrid(10, 10);
        Node start = new Node(0, 0);
        Node end = new Node(9, 9);
        for (int i = 0; i < 10; i++) {
            grid.findNode(i, 2).setValid(false);
        }
        PathGen gen = new PathGen(grid, start, end);
        gen.findPath();
        List<Node> path = gen.getPath();
    }

    @Test
    public void testPartiallyObstructedPath() {
        Grid grid = Grid.generateGrid(10, 10);
        Node start = new Node(0, 0);
        Node end = new Node(9, 9);
        for (int i = 0; i < 9; i++) {
            grid.findNode(i, 2).setValid(false);
        }
        PathGen gen = new PathGen(grid, start, end);
        gen.findPath();
        List<Node> path = gen.getPath();
    }
}
