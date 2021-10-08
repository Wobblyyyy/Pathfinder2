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

import java.util.ArrayList;
import java.util.List;

/**
 * A coordinate is basically a {@link me.wobblyyyy.pathfinder2.geometry.PointXY},
 * but with integers instead of doubles.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Coord {
    private final int x;
    private final int y;

    /**
     * Create a new coordinate.
     *
     * @param x the coordinate's X value.
     * @param y the coordinate's Y value.
     */
    public Coord(int x,
                 int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Node node) {
        this(node.getX(), node.getY());
    }

    public static List<Coord> convertNodes(List<Node> nodes) {
        List<Coord> coords = new ArrayList<>(nodes.size());

        for (Node node : nodes) {
            coords.add(new Coord(node));
        }

        return coords;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public int hashCode() {
        return (x * 100_000) + (y * 5_000);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coord) {
            Coord c = (Coord) obj;
            return x == c.x() && y == c.y();
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "(%s, %s)",
                x,
                y
        );
    }
}
