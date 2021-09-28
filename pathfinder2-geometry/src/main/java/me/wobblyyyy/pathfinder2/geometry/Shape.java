/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

import java.util.ArrayList;
import java.util.List;

public interface Shape {
    PointXY getClosestPoint(PointXY reference);

    boolean isPointInShape(PointXY reference);

    boolean doesCollideWith(Shape shape);

    PointXY getCenter();

    static List<Line> getIntersections(Line line,
                                       List<Line> allLines) {
        List<Line> intersectingLines = new ArrayList<>();

        for (Line l : allLines)
            if (line.doesIntersectWith(l))
                intersectingLines.add(l);

        return intersectingLines;
    }

    static int getIntersectionsCount(Line line,
                                     List<Line> allLines) {
        int i = 0;

        for (Line l : allLines)
            if (line.doesIntersectWith(l))
                i++;

        return i;
    }

    static boolean doesIntersectOdd(Line line,
                                    List<Line> allLines) {
        return getIntersectionsCount(line, allLines) % 2 == 1;
    }

    static boolean doesIntersectEven(Line line,
                                     List<Line> allLines) {
        return getIntersectionsCount(line, allLines) % 2 == 0;
    }
}
