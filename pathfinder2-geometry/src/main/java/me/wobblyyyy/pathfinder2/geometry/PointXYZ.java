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
 * A 2d coordinate with an X value, a Y value, and a value "Z" representing
 * a heading angle.
 *
 * <p>
 * As an extension of the {@link PointXY} class, this class inherits many
 * of the methods. Some of them are overridden to accommodate for the extra
 * aspect (Z). Some of them aren't. You never know, right?
 * </p>
 *
 * <p>
 * Much like the aforementioned {@code PointXY} class, {@code PointXYZ}s are
 * immutable objects, meaning that after they're created, the values stored
 * inside of them cannot be changed. Many of the methods here will return
 * new {@code PointXYZ} objects instead of modifying an existing one.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class PointXYZ extends PointXY {
    /**
     * The point's angle, or heading, or whatever you want to call it.
     */
    private final Angle z;

    /**
     * Create a new {@code PointXYZ} instance.
     *
     * @param point a {@code PointXY} to base the new {@code PointXYZ} off
     *              of. See: {@link #withHeading(Angle)}
     * @param z     the heading of the new {@code PointXYZ}.
     */
    public PointXYZ(PointXY point,
                    Angle z) {
        this(point.x(), point.y(), z);
    }

    /**
     * Create a new {@code PointXYZ} instance.
     *
     * @param x the X coordinate of the point.
     * @param y the Y coordinate of the point.
     * @param z the point's heading, angle, Z - whatever you'd like to call it.
     */
    public PointXYZ(double x,
                    double y,
                    Angle z) {
        super(x, y);
        this.z = z;
    }

    /**
     * Add two points together.
     *
     * @param a one of the two points to add.
     * @param b one of the two points to add.
     * @return the sum of the two points.
     */
    public static PointXYZ add(PointXYZ a,
                               PointXYZ b) {
        return new PointXYZ(
                a.x() + b.x(),
                a.y() + b.y(),
                Angle.add(a.z(), b.z())
        );
    }

    /**
     * Multiply two points together.
     *
     * @param a one of the points to multiply.
     * @param b one of the points to multiply.
     * @return the product of the two points.
     */
    public static PointXYZ multiply(PointXYZ a,
                                    PointXYZ b) {
        return new PointXYZ(
                a.x() * b.x(),
                a.y() * b.y(),
                Angle.multiply(a.z(), b.z())
        );
    }

    /**
     * Multiply a point by a {@code double} value.
     *
     * @param a the point to multiply.
     * @param b the value to multiply the point's X, Y, and Z values by.
     * @return the newly multiplied point.
     */
    public static PointXYZ multiply(PointXYZ a,
                                    double b) {
        return new PointXYZ(
                a.x() * b,
                a.y() * b,
                Angle.multiply(a.z(), b)
        );
    }

    /**
     * Get the average between two points.
     *
     * @param a one of the two points to average.
     * @param b one of the two points to average.
     * @return the average of the two provided points.
     */
    public static PointXYZ avg(PointXYZ a,
                               PointXYZ b) {
        return multiply(add(a, b), 0.5);
    }

    /**
     * Create a new {@code PointXYZ} with 0 values for everything.
     *
     * @return a new {@code PointXYZ} instance with an X value of 0, a Y
     * value of 0, and a Z value of 0 degrees.
     */
    public static PointXYZ zero() {
        return new PointXYZ(0, 0, Angle.zero());
    }

    /**
     * Create a new {@code PointXYZ} a given distance away from a base point
     * in a specified direction.
     *
     * @param base     the base/origin point.
     * @param distance how far away the new point should be from the origin.
     * @param angle    the angle at which the new point should be drawn.
     * @return a new point, {@code distance} away from {@code base} in
     * {@code direction}.
     */
    public static PointXYZ inDirection(PointXYZ base,
                                       double distance,
                                       Angle angle) {
        return new PointXYZ(
                base.x() + (distance * angle.cos()),
                base.y() + (distance * angle.sin()),
                base.z()
        );
    }

    /**
     * Rotate a point around a center point.
     *
     * @param point  the point to rotate.
     * @param center the center point (this is the point that {@code point}
     *               will be rotated around).
     * @param angle  how far to rotate the point.
     * @return a point, rotated around {@code center} by {@code angle}.
     */
    public static PointXYZ rotate(PointXYZ point,
                                  PointXY center,
                                  Angle angle) {
        return PointXY.rotate(
                point, center, angle
        ).withHeading(point.z());
    }

    /**
     * If the provided point is null, return {@link #zero()}. Otherwise,
     * return the provided point. This method provides null safety.
     *
     * @param point the point.
     * @return either the point, or {@link #zero()}.
     */
    public static PointXYZ zeroIfNull(PointXYZ point) {
        return point == null ? new PointXYZ(0, 0, Angle.zero()) : point;
    }

    /**
     * Get the point's heading.
     *
     * @return the point's heading.
     */
    public Angle z() {
        return this.z;
    }

    /**
     * Add another point to this point.
     *
     * @param a the point to add.
     * @return the sum of the two points.
     */
    public PointXYZ add(PointXYZ a) {
        return add(this, a);
    }

    /**
     * Multiply this point by another point.
     *
     * @param a the point to multiply this point by.
     * @return the product of the two points.
     */
    public PointXYZ multiply(PointXYZ a) {
        return multiply(this, a);
    }

    /**
     * Multiply this point by a coefficient.
     *
     * @param a the coefficient to multiply the X and Y values by.
     * @return the product of the point's X, Y, and Z values when multiplied
     * by the coefficient.
     */
    public PointXYZ multiply(double a) {
        return multiply(this, a);
    }

    /**
     * Get the average of this point and another point.
     *
     * @param a the other point.
     * @return the average of the two points.
     */
    public PointXYZ avg(PointXYZ a) {
        return avg(this, a);
    }

    /**
     * Cast a {@code PointXYZ} into a {@code PointXY}.
     *
     * @return this, but as a {@code PointXY}.
     */
    public PointXY asPoint() {
        return this;
    }

    /**
     * Rotate this point around a center by a specified angle.
     *
     * @param center the center of rotation.
     * @param angle  how far to rotate this point.
     * @return the newly-rotated point.
     */
    public PointXYZ rotate(PointXY center,
                           Angle angle) {
        return rotate(this, center, angle);
    }

    /**
     * Create a new point a specified distance away from this point.
     *
     * @param distance the distance the new point should be created at.
     * @param angle    the angle the new point should be created at.
     * @return a new point, {@code distance} away from this point, drawn
     * at {@code angle}.
     */
    @Override
    public PointXYZ inDirection(double distance,
                                Angle angle) {
        return PointXYZ.inDirection(this, distance, angle);
    }

    /**
     * Create a new point with the same Y value as this point and whatever
     * X value you provide.
     *
     * @param x the X value the new point should have.
     * @return a new point with the X value you specified.
     */
    public PointXYZ withX(double x) {
        return new PointXYZ(x, y(), this.z);
    }

    /**
     * Create a new point with the same X value as this point and whatever
     * Y value you provide.
     *
     * @param y the Y value the new point should have.
     * @return a new point with the Y value you specified.
     */
    public PointXYZ withY(double y) {
        return new PointXYZ(x(), y, this.z);
    }
}
