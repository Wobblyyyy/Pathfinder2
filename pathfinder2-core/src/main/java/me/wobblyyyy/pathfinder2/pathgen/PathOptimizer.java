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

import me.wobblyyyy.pathfinder2.geometry.Line;
import me.wobblyyyy.pathfinder2.geometry.PointXY;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities used for optimizing paths.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class PathOptimizer {
    /**
     * Determine the length of a set of points/path.
     *
     * @param path the path to determine the total length of.
     * @return the total length of the path.
     */
    public static double determineLength(List<PointXY> path) {
        List<Line> lines = new ArrayList<>(path.size() * 2);

        for (int i = 0; i < path.size() - 1; i++) {
            PointXY current = path.get(i);
            PointXY next = path.get(i + 1);

            Line line = new Line(current, next);
            lines.add(line);
        }

        double length = 0;

        for (Line line : lines) {
            length += Math.abs(line.length());
        }

        return length;
    }

    /**
     * Optimize a path by removing collinear points. This reduces the size
     * of the path, and, in some cases, reduces the length of the path by
     * allowing your robot to move in ways it wouldn't be able to have
     * previously.
     *
     * @param path the path to optimize.
     * @return an optimized path.
     */
    public static List<PointXY> optimize(List<PointXY> path) {
        List<PointXY> optimized = new ArrayList<>(path.size());

        PointXY start = path.get(0);
        PointXY end = path.get(path.size() - 1);

        // remove collinear points
        PointXY lastCollinear = null;
        for (int i = 1; i < path.size() - 1; i++) {
            PointXY previous = path.get(i - 1);
            PointXY current = path.get(i);
            PointXY next = path.get(i + 1);

            if (PointXY.areCollinear(previous, current, next)) {
                lastCollinear = current;
            } else {
                if (lastCollinear == null) {
                    optimized.add(current);
                } else {
                    optimized.add(lastCollinear);
                    lastCollinear = null;
                }
            }
        }

        // if there's still a collinear point to add, add it
        if (lastCollinear != null) optimized.add(lastCollinear);

        if (optimized.size() == 0) return new ArrayList<>();

        // make sure the optimized path includes the start and end points
        if (!optimized.get(0).equals(start)) optimized.add(0, start);
        if (!optimized.get(optimized.size() - 1).equals(end)) optimized.add(end);

        // edge case - if the path is entirely linear, return a 2-point
        // path
        int size = optimized.size();
        if (size == 3) {
            PointXY a = optimized.get(0);
            PointXY b = optimized.get(1);
            PointXY c = optimized.get(2);

            if (PointXY.areCollinear(a, b, c)) optimized.remove(b);

            return optimized;
        }

        // do a second pass to further optimize the path
        List<PointXY> toRemove = new ArrayList<>(optimized.size());
        for (int i = 1; i < size - 1; i++) {
            PointXY previous = path.get(i - 1);
            PointXY current = path.get(i);
            PointXY next = path.get(i + 1);

            if (PointXY.areCollinear(previous, current, next)) {
                toRemove.add(current);
            }
        }

        for (PointXY point : toRemove) {
            optimized.remove(point);
        }

        return optimized;
    }
}
