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

/**
 * A circle. I'm not really sure how many else I can describe it. It's
 * a pretty simple circe.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Circle implements Shape {
    private final PointXY center;
    private final double radius;

    /**
     * Create a new circle.
     *
     * @param center the center of the circle.
     * @param radius the circle's radius.
     */
    public Circle(PointXY center,
                  double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Get the circle's center.
     *
     * @return the center of the circle.
     */
    @Override
    public PointXY getCenter() {
        return center;
    }

    @Override
    public PointXY getClosestPoint(PointXY referencePoint) {
        Angle circleToReference = center.angleTo(referencePoint);

        return center.inDirection(radius, circleToReference);
    }

    @Override
    public PointXY getFurthestPoint(PointXY referencePoint) {
        Angle referenceToCircle = referencePoint.angleTo(center);

        return center.inDirection(radius, referenceToCircle);
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.getClosestPoint(center).isInside(this);
    }

    @Override
    public boolean isPointInShape(PointXY point) {
        return center.distance(point) <= radius;
    }

    @Override
    public boolean isPointNotInShape(PointXY point) {
        return !isPointInShape(point);
    }

    /**
     * Get the circle's radius.
     *
     * @return the circle's radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Get the circle's diameter.
     *
     * @return the circle's diameter.
     */
    public double getDiameter() {
        return radius * 2;
    }

    /**
     * Get the circle's area.
     *
     * @return the circle's area.
     */
    public double getArea() {
        return getDiameter() * Math.PI;
    }
}
