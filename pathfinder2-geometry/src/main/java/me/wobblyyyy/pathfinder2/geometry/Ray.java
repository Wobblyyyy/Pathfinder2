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

public class Ray {
    private final PointXY point;
    private final Angle direction;

    public Ray(PointXY point,
               Angle direction) {
        this.point = point;
        this.direction = direction;
    }

    public Line toLine(double length) {
        return new Line(
                point,
                point.inDirection(
                        length,
                        direction
                )
        );
    }

    public Line toLine(double endPointX,
                       double endPointY) {
        return new Line(
                point,
                new PointXY(
                        endPointX,
                        endPointY
                )
        );
    }

    public Line toLine(double minimumX,
                       double minimumY,
                       double maximumX,
                       double maximumY) {
        PointXY minimumPoint = new PointXY(minimumX, minimumY);
        PointXY maximumPoint = new PointXY(maximumX, maximumY);

        double distanceToMin = point.distance(minimumPoint);
        double distanceToMax = point.distance(maximumPoint);

        Angle angleToMin = point.angleTo(minimumPoint);
        Angle angleToMax = point.angleTo(maximumPoint);

        return new Line(
                point.inDirection(
                        distanceToMin,
                        angleToMin
                ),
                point.inDirection(
                        distanceToMax,
                        angleToMax
                )
        );
    }

    public static boolean doesIntersect(Ray ray,
                                        Line test) {
        Line rayAsLine = ray.toLine(
                test.minimumX(),
                test.minimumY(),
                test.maximumX(),
                test.maximumY()
        );

        return test.doesIntersectWith(rayAsLine);
    }

    public static boolean doesIntersect(Ray ray,
                                        Ray test) {
        Line rayAsLine = ray.toLine(1);
        Line testAsLine = ray.toLine(1);

        PointXY intersection = Line.unboundedPointOfIntersection(
                rayAsLine,
                testAsLine
        );

        boolean xValid = !(intersection.x() >= Double.MAX_VALUE) &&
                !(intersection.x() <= Double.MIN_VALUE) &&
                !Double.isNaN(intersection.x());
        boolean yValid = !(intersection.y() >= Double.MAX_VALUE) &&
                !(intersection.y() <= Double.MIN_VALUE) &&
                !Double.isNaN(intersection.y());

        return xValid && yValid;
    }

    public static List<Line> getIntersectingLines(Ray ray,
                                                  List<Line> allLines) {
        List<Line> intersectingLines = new ArrayList<>();

        for (Line line : allLines) {
            if (ray.doesIntersect(line)) intersectingLines.add(line);
        }

        return intersectingLines;
    }

    public static int howManyIntersections(Ray ray,
                                           List<Line> allLines) {
        return getIntersectingLines(
                ray,
                allLines
        ).size();
    }

    public boolean doesIntersect(Line test) {
        return doesIntersect(this, test);
    }

    public boolean doesIntersect(Ray test) {
        return doesIntersect(this, test);
    }
}
