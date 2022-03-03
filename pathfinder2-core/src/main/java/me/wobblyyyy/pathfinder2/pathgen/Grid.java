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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A grid is pretty much a two-dimensional array.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Grid {
    private final int width;
    private final int height;
    private final Map<Coord, Node> map = new HashMap<>();
    private List<Node> nodes;

    private Grid(int width,
                 int height) {
        this(width, height, null);
    }

    public Grid(int width,
                int height,
                List<Node> nodes) {
        this.width = width;
        this.height = height;
        setNodes(nodes);
        this.nodes = nodes;
    }

    public static Grid generateGrid(int width,
                                    int height) {
        Grid grid = new Grid(width, height);

        List<Node> nodes = Node.getNodes(width, height, grid);

        grid.setNodes(nodes);

        Node.calculateNeighborsFor(grid.getNodes(), grid);

        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Node findNode(Coord coord) {
        return findNode(coord.x(), coord.y());
    }

    public Node findNode(int x,
                         int y) {
        Coord coord = new Coord(x, y);

        if (map.containsKey(coord)) return map.get(coord);

        for (Node node : nodes) {
            int targetX = node.getX();
            int targetY = node.getY();

            boolean sameX = x == targetX;
            boolean sameY = y == targetY;

            if (sameX && sameY) return node;
        }

        return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;

        if (nodes != null) {
            for (Node node : nodes) {
                map.put(
                        new Coord(node),
                        node
                );
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                builder.append(findNode(j, i).isValid() ? "." : "#");
            }
            builder.append(StringUtils.format(
                    " (row %s)\n",
                    i
            ));
        }

        return builder.toString();
    }
}
