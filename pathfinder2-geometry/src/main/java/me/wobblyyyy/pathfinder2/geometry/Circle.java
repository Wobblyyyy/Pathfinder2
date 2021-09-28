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

public class Circle implements Shape {
    private final PointXY center;
    private final double radius;

    public Circle(PointXY center,
                  double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public PointXY getClosestPoint(PointXY reference) {
        if (isPointInShape(reference)) {
            return reference;
        }

        Angle centerToReference = center.angleTo(reference);

        return center.inDirection(radius, centerToReference);
    }

    @Override
    public boolean isPointInShape(PointXY reference) {
        return reference.absDistance(center) <= radius;
    }

    @Override
    public boolean doesCollideWith(Shape shape) {
        return shape.getClosestPoint(center).isInside(this);
    }

    @Override
    public PointXY getCenter() {
        return center;
    }
}
