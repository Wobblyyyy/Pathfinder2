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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
public class Rectangle implements Shape {
    private final PointXY aPoint;
    private final PointXY bPoint;
    private final PointXY cPoint;
    private final PointXY dPoint;

    private final PointXY center;

    private final Line abLine;
    private final Line bcLine;
    private final Line cdLine;
    private final Line daLine;
    private final List<Line> lines = new ArrayList<>(4);

    public Rectangle(PointXY... points) {
        this(
                points[0],
                points[1],
                points[2],
                points[3]
        );
    }

    public Rectangle(List<PointXY> points) {
        this(
                points.get(0),
                points.get(1),
                points.get(2),
                points.get(3)
        );
    }

    public Rectangle(PointXY aPoint,
                     PointXY bPoint,
                     PointXY cPoint,
                     PointXY dPoint) {
        if (aPoint == null || bPoint == null || cPoint == null || dPoint == null) {
            throw new IllegalArgumentException("One or more points was NULL!");
        }

        if (PointXY.areDuplicatesPresent(
                aPoint,
                bPoint,
                cPoint,
                dPoint
        )) {
            throw new IllegalArgumentException("Cannot create a rectangle with duplicate points!");
        }

        PointXY origin = PointXY.zero();

        double aDistance = origin.distance(aPoint);
        double bDistance = origin.distance(bPoint);
        double cDistance = origin.distance(cPoint);
        double dDistance = origin.distance(dPoint);

        Map<Double, PointXY> map = new HashMap<>(4) {{
            put(aDistance, aPoint);
            put(bDistance, bPoint);
            put(cDistance, cPoint);
            put(dDistance, dPoint);
        }};

        List<Double> distances = new ArrayList<>(map.keySet());
        distances.sort(Comparator.comparingDouble(Math::abs));
        distances.sort(Double::compare);

        Double key0 = distances.get(0);
        Double key1 = distances.get(1);
        Double key2 = distances.get(2);
        Double key3 = distances.get(3);

        this.aPoint = map.get(distances.get(0));
        this.bPoint = map.get(distances.get(1));
        this.cPoint = map.get(distances.get(2));
        this.dPoint = map.get(distances.get(3));

        this.center = PointXY.avg(
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );

        abLine = new Line(aPoint, bPoint);
        bcLine = new Line(bPoint, cPoint);
        cdLine = new Line(cPoint, dPoint);
        daLine = new Line(dPoint, aPoint);

        lines.add(abLine);
        lines.add(bcLine);
        lines.add(cdLine);
        lines.add(daLine);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static boolean isPointInRectangle(Rectangle rectangle,
                                             PointXY point) {
        boolean abcd = Line.isBetweenLines(
                point,
                rectangle.getLineAB(),
                rectangle.getLineCD()
        );

        boolean bcda = Line.isBetweenLines(
                point,
                rectangle.getLineBC(),
                rectangle.getLineDA()
        );

        return abcd && bcda;
    }

    public static boolean isPointNotInRectangle(Rectangle rectangle,
                                                PointXY point) {
        return !isPointInRectangle(
                rectangle,
                point
        );
    }

    public PointXY getPointA() {
        return aPoint;
    }

    public PointXY getPointB() {
        return bPoint;
    }

    public PointXY getPointC() {
        return cPoint;
    }

    public PointXY getPointD() {
        return dPoint;
    }

    public Line getLineAB() {
        return abLine;
    }

    public Line getLineBC() {
        return bcLine;
    }

    public Line getLineCD() {
        return cdLine;
    }

    public Line getLineDA() {
        return daLine;
    }

    @Override
    public boolean isPointInShape(PointXY point) {
        return isPointInRectangle(this, point);
    }

    @Override
    public boolean isPointNotInShape(PointXY point) {
        return isPointNotInRectangle(this, point);
    }

    @Override
    public PointXY getClosestPoint(PointXY referencePoint) {
        return PointXY.getClosestPoint(
                referencePoint,
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );
    }

    @Override
    public PointXY getFarthestPoint(PointXY referencePoint) {
        return PointXY.getFarthestPoint(
                referencePoint,
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.getClosestPoint(center).isInside(this);
    }
}
