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
 * Generate a path! Or several paths. Or something like that.
 *
 * <p>
 * This isn't intended for external use, and I'm too lazy to document everything,
 * so... you're on your own here. You should use a localized path generator
 * instead, unless you have a very specific reason for using a non-localized one.
 * </p>
 *
 * <p>
 * I'll be honest, my understanding of algorithms is limited to... well, let's
 * just say... I don't know.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class PathGen {
    private final Grid grid;
    private List<Node> path;

    private final Node start;
    private final Node end;

    private List<Node> openList;

    /**
     * Create a new path generator.
     *
     * @param grid  the generator's grid.
     * @param start the path's start point.
     * @param end   the path's end point.
     */
    public PathGen(Grid grid,
                   Node start,
                   Node end) {
        this.grid = grid;
        this.start = start;
        this.end = end;
    }

    public List<Coord> findCoordPath() {
        return Coord.convertNodes(findPath());
    }

    /**
     * Find a path. Wow!
     *
     * @return a path. Or something.
     */
    public List<Node> findPath() {
        if (start == null)
            throw new IllegalArgumentException("Start node may not be null!");
        if (end == null)
            throw new IllegalArgumentException("End node may not be null!");

        start.calculateNeighbours(grid);
        end.calculateNeighbours(grid);

        this.path = new ArrayList<>();

        if (start.equals(end)) return new ArrayList<Node>() {{
            add(start);
            add(end);
        }};

        this.openList = new ArrayList<Node>() {{
            add(start);
        }};
        List<Node> closedList = new ArrayList<>();
        while (!this.openList.isEmpty()) {
            Node current = getLowestF();

            if (current.equals(end)) {
                retracePath(current);
                break;
            }

            openList.remove(current);
            closedList.add(current);

            for (Node node : current.getNeighbours()) {
                if (closedList.contains(node) || !node.isValid()) continue;
                double score = current.getCost() + current.distanceTo(node);
                if (openList.contains(node)) {
                    if (score < node.getCost()) {
                        node.setCost(score);
                        node.setParent(current);
                    }
                } else {
                    node.setCost(score);
                    openList.add(node);
                    node.setParent(current);
                }

                node.setHeuristic(node.heuristic(end));
                node.setFunction(node.getCost() + node.getHeuristic());
            }
        }

        return getPath();
    }

    private void retracePath(Node current) {
        this.path.add(current);

        while (current.getParent() != null) {
            Node parent = current.getParent();
            if (!path.contains(parent)) path.add(parent);
            current = current.getParent();
        }

        if (!path.contains(start)) path.add(start);
    }

    private Node getLowestF() {
        Node lowest = openList.get(0);

        for (Node n : openList) {
            if (n.getFunction() < lowest.getFunction()) {
                lowest = n;
            }
        }

        return lowest;
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Node> getPath() {
        List<Node> newPath = new ArrayList<>();

        for (int i = path.size() - 1; i != -1; i--) {
            newPath.add(path.get(i));
        }

        int size = newPath.size();
        if (size == 0) return newPath;

        if (!newPath.get(size - 1).equals(end)) newPath.add(end);

        return newPath;
    }

    /**
     * Get the start node.
     *
     * @return the start node.
     */
    public Node getStart() {
        return start;
    }

    /**
     * Get the end node.
     *
     * @return the end node.
     */
    public Node getEnd() {
        return end;
    }
}
