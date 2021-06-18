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

public class PointXYZ extends PointXY {
    private final Angle z;

    public PointXYZ(PointXY point,
                    Angle z) {
        this(point.x(), point.y(), z);
    }

    public PointXYZ(double x,
                    double y,
                    Angle z) {
        super(x, y);
        this.z = z;
    }

    public static PointXYZ add(PointXYZ a,
                               PointXYZ b) {
        return new PointXYZ(
                a.x() + b.x(),
                a.y() + b.y(),
                Angle.add(a.z(), b.z())
        );
    }

    public static PointXYZ multiply(PointXYZ a,
                                    PointXYZ b) {
        return new PointXYZ(
                a.x() * b.x(),
                a.y() * b.y(),
                Angle.multiply(a.z(), b.z())
        );
    }

    public static PointXYZ multiply(PointXYZ a,
                                    double b) {
        return new PointXYZ(
                a.x() * b,
                a.y() * b,
                Angle.multiply(a.z(), b)
        );
    }

    public static PointXYZ avg(PointXYZ a,
                               PointXYZ b) {
        return multiply(add(a, b), 0.5);
    }

    public static PointXYZ zero() {
        return new PointXYZ(0, 0, Angle.zero());
    }

    public static PointXYZ inDirection(PointXYZ base,
                                       double distance,
                                       Angle angle) {
        return new PointXYZ(
                base.x() + (distance * angle.cos()),
                base.y() + (distance * angle.sin()),
                base.z()
        );
    }

    public static PointXYZ rotate(PointXYZ point,
                                  PointXY center,
                                  Angle angle) {
        return PointXY.rotate(
                point, center, angle
        ).withHeading(point.z());
    }

    public static PointXYZ zeroIfNull(PointXYZ point) {
        return point == null ? new PointXYZ(0, 0, Angle.zero()) : point;
    }

    public Angle z() {
        return this.z;
    }

    public PointXYZ add(PointXYZ a) {
        return add(this, a);
    }

    public PointXYZ multiply(PointXYZ a) {
        return multiply(this, a);
    }

    public PointXYZ multiply(double a) {
        return multiply(this, a);
    }

    public PointXYZ avg(PointXYZ a) {
        return avg(this, a);
    }

    public PointXY asPoint() {
        return this;
    }

    public PointXYZ rotate(PointXY center,
                           Angle angle) {
        return rotate(this, center, angle);
    }

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
