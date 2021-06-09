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
}
