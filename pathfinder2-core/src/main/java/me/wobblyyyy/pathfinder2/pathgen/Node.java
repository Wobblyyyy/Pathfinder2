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

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * "I don't even know to be honest."
 *
 * <p>
 * Nodes are the basis for which the pathfinding grid operates on.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Node {
    private Node parent;
    private List<Node> neighbours;
    private double cost;
    private double heuristic;
    private double function;
    private boolean valid = true;

    private final int x;
    private final int y;

    public Node(int x,
                int y) {
        this.x = x;
        this.y = y;
    }

    public static void calculateNeighborsFor(List<Node> nodes,
                                             Grid grid) {
        for (Node node : nodes) {
            node.calculateNeighbours(grid);
        }
    }

    public static List<Node> getNodes(int x,
                                      int y,
                                      Grid grid) {
        List<Node> nodes = new ArrayList<>(x * y);

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                nodes.add(new Node(i, j));
            }
        }

        return nodes;
    }

    private static void tryAddNode(boolean condition,
                                   int x,
                                   int y,
                                   Function<Coord, Node> getNode,
                                   List<Node> nodes) {
        if (condition) {
            Node node = getNode.apply(new Coord(x, y));
            nodes.add(node);
        }
    }

    private static void tryAddNodes(Map<Coord, Boolean> map,
                                    Function<Coord, Node> getNode,
                                    List<Node> nodes) {
        for (Map.Entry<Coord, Boolean> entry : map.entrySet()) {
            Coord coord = entry.getKey();
            boolean condition = entry.getValue();

            tryAddNode(
                    condition,
                    coord.x(),
                    coord.y(),
                    getNode,
                    nodes
            );
        }
    }

    public static List<Node> getValidNodes(List<Node> nodes) {
        return nodes.stream().filter(Node::isValid).collect(Collectors.toList());
    }

    public static List<Node> getInvalidNodes(List<Node> nodes) {
        return nodes.stream().filter(Node::isInvalid).collect(Collectors.toList());
    }

    public void calculateNeighbours(Grid grid) {
        List<Node> nodes = new ArrayList<>();

        int x = getX();
        int y = getY();

        Coord coord = new Coord(x, y);

        int minX = 0;
        int minY = 0;
        int maxX = grid.getWidth() - 1;
        int maxY = grid.getHeight() - 1;

        Map<Coord, Boolean> map = new HashMap<>();

        boolean validMinX = x > minX; // x - 1
        boolean validMaxX = x < maxX; // x + 1
        boolean validMinY = y > minY; // y - 1
        boolean validMaxY = y < maxY; // y + 1

        boolean validDiagNorthwest = validMinX && validMinY; // x - 1, y - 1
        boolean validDiagSoutheast = validMaxX && validMaxY; // x + 1, y + 1
        boolean validDiagNortheast = validMaxX && validMinY; // x + 1, y - 1
        boolean validDiagSouthwest = validMinX && validMaxY; // x - 1, y + 1

        map.put(new Coord(x - 1, y), validMinX);
        map.put(new Coord(x + 1, y), validMaxX);
        map.put(new Coord(x, y - 1), validMinY);
        map.put(new Coord(x, y + 1), validMaxY);

        map.put(new Coord(x - 1, y - 1), validDiagNorthwest);
        map.put(new Coord(x + 1, y + 1), validDiagSoutheast);
        map.put(new Coord(x + 1, y - 1), validDiagNorthwest);
        map.put(new Coord(x - 1, y + 1), validDiagSouthwest);

        tryAddNodes(
                map,
                grid::findNode,
                nodes
        );

        setNeighbours(nodes);
    }

    public double heuristic(Node target) {
        return distanceTo(target);
    }

    public double distanceTo(Node target) {
        PointXY currentPoint = new PointXY(getX(), getY());
        PointXY targetPoint = new PointXY(target.getX(), target.getY());

        return currentPoint.distance(targetPoint);
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public double getFunction() {
        return function;
    }

    public void setFunction(double function) {
        this.function = function;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Node> neighbours) {
        List<Node> noNull = new ArrayList<>(neighbours.size());
        neighbours.forEach((n) -> {
            if (n != null) noNull.add(n);
        });
        this.neighbours = noNull;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isInvalid() {
        return !valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void reverseValidation() {
        valid = !valid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return (x * 100_000) + (y * 5_000);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node c = (Node) obj;
            return x == c.getX() && y == c.getY();
        }

        return false;
    }

    @Override
    public String toString() {
        return StringUtils.format(
                "(%s, %s, valid: %s)",
                x,
                y,
                valid
        );
    }
}

