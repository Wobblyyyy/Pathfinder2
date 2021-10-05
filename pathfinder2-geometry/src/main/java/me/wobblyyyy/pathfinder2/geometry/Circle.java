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
 * Among everyone's favorite things in the world, is, of course, the circle!
 * What's not to love about a classic circle? I mean, come on, they're
 * pretty cool, you have to admit.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Circle implements Shape<Circle> {
    private final PointXY center;
    private final double radius;

    /**
     * Create a new {@code Circle}.
     *
     * @param center the circle's center point.
     * @param radius the circle's radius.
     */
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
    public boolean doesCollideWith(Shape<?> shape) {
        return shape.getClosestPoint(center).isInside(this);
    }

    @Override
    public PointXY getCenter() {
        return center;
    }

    @Override
    public Circle rotate(Angle rotation) {
        return rotate(rotation, center);
    }

    @Override
    public Circle rotate(Angle rotation, PointXY centerOfRotation) {
        return new Circle(
                center.rotate(centerOfRotation, rotation),
                radius
        );
    }

    @Override
    public Circle shift(double shiftX, double shiftY) {
        return new Circle(
                center.shift(shiftX, shiftY),
                radius
        );
    }

    @Override
    public Circle moveTo(PointXY newCenter) {
        return new Circle(newCenter, radius);
    }

    @Override
    public Circle scale(double scale) {
        return new Circle(center, radius * scale);
    }

    @Override
    public Circle growBy(double growth) {
        return new Circle(center, radius + growth);
    }
}
