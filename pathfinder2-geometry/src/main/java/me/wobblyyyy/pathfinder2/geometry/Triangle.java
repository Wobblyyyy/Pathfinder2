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

/**
 * Triangles, everyone's favorite topic. I'm sure you loved trig class, right?
 * You'll love these too. Surprisingly enough, there's not actually too much
 * trig going on with these triangles, but yeah... they're pretty cool anyways.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Triangle implements Shape {
    private final PointXY a;
    private final PointXY b;
    private final PointXY c;

    private final PointXY center;

    private final Line ab;
    private final Line bc;
    private final Line ca;
    private final List<Line> lines = new ArrayList<>(3);

    public Triangle(PointXY a,
                    PointXY b,
                    PointXY c) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.ab = new Line(a, b);
        this.bc = new Line(b, c);
        this.ca = new Line(c, a);

        this.lines.add(ab);
        this.lines.add(bc);
        this.lines.add(ca);

        Line aCentroid = new Line(a, bc.getMidPoint());
        Line bCentroid = new Line(b, ca.getMidPoint());
        Line cCentroid = new Line(c, ab.getMidPoint());

        PointXY abIntersect = Line.pointOfIntersection(aCentroid, bCentroid);
        PointXY bcIntersect = Line.pointOfIntersection(bCentroid, cCentroid);
        PointXY caIntersect = Line.pointOfIntersection(cCentroid, aCentroid);

        center = PointXY.avg(
                abIntersect,
                bcIntersect,
                caIntersect
        );
    }

    public PointXY getPointA() {
        return a;
    }

    public PointXY getPointB() {
        return b;
    }

    public PointXY getPointC() {
        return c;
    }

    public Line getLineAB() {
        return ab;
    }

    public Line getLineBC() {
        return bc;
    }

    public Line getLineCA() {
        return ca;
    }

    public Angle getAngleA() {
        return Line.angleBetweenLines(ab, ca);
    }

    public Angle getAngleB() {
        return Line.angleBetweenLines(ab, bc);
    }

    public Angle getAngleC() {
        return Line.angleBetweenLines(bc, ca);
    }

    @Override
    public boolean isPointInShape(PointXY point) {
        return Ray.intersectsOdd(
                point,
                center,
                ab.getMidPoint(),
                lines
        );
    }

    @Override
    public boolean isPointNotInShape(PointXY point) {
        return Ray.intersectsEven(
                point,
                center,
                ab.getMidPoint(),
                lines
        );
    }

    @Override
    public PointXY getClosestPoint(PointXY referencePoint) {
        PointXY abClosest = ab.getClosestPoint(referencePoint);
        PointXY bcClosest = bc.getClosestPoint(referencePoint);
        PointXY caClosest = ca.getClosestPoint(referencePoint);

        return PointXY.getClosestPoint(
                referencePoint,
                abClosest,
                bcClosest,
                caClosest
        );
    }

    @Override
    public PointXY getFurthestPoint(PointXY referencePoint) {
        PointXY abFurthest = ab.getFurthestPoint(referencePoint);
        PointXY bcFurthest = bc.getFurthestPoint(referencePoint);
        PointXY caFurthest = ca.getFurthestPoint(referencePoint);

        return PointXY.getFurthestPoint(
                referencePoint,
                abFurthest,
                bcFurthest,
                caFurthest
        );
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.getClosestPoint(center).isInside(this);
    }

    @Override
    public PointXY getCenter() {
        return center;
    }
}
