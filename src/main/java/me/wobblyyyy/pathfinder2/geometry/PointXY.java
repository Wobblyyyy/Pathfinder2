package me.wobblyyyy.pathfinder2.geometry;

public class PointXY {
    private final double x;

    private final double y;

    public PointXY(double x,
                   double y) {
        this.x = x;
        this.y = y;
    }

    public static PointXY add(PointXY a,
                              PointXY b) {
        return new PointXY(
                a.x() + b.x(),
                a.y() + b.y()
        );
    }

    public static PointXY multiply(PointXY a,
                                   PointXY b) {
        return new PointXY(
                a.x() * b.x(),
                a.y() * b.y()
        );
    }

    public static PointXY multiply(PointXY a,
                                   double b) {
        return new PointXY(
                a.x() * b,
                a.y() * b
        );
    }

    public static PointXY avg(PointXY a,
                              PointXY b) {
        return multiply(add(a, b), 0.5);
    }

    public static PointXY zero() {
        return new PointXY(0, 0);
    }

    public static double distance(PointXY a,
                                  PointXY b) {
        return Math.sqrt(
                Math.pow(b.x() - a.x(), 2) +
                        Math.pow(b.y() - a.y(), 2)
        );
    }

    public static double distanceX(PointXY a,
                                   PointXY b) {
        return b.x() - a.x();
    }

    public static double distanceY(PointXY a,
                                   PointXY b) {
        return b.y() - a.y();
    }

    public static Angle angleTo(PointXY a,
                                PointXY b) {
        return Angle.fromRad(
                Math.atan2(
                        distanceY(a, b),
                        distanceX(a, b)
                )
        );
    }

    public static PointXY inDirection(PointXY base,
                                      double distance,
                                      Angle angle) {
        return new PointXY(
                base.x() + (distance * angle.cos()),
                base.y() + (distance * angle.sin())
        );
    }

    public static boolean isNear(PointXY a,
                                 PointXY b,
                                 double tolerance) {
        return Math.abs(distance(a, b)) < Math.abs(tolerance);
    }

    public static PointXY rotate(PointXY point,
                                 PointXY center,
                                 Angle angle) {
        /*
         * 1. Calculate the angle between the center point and the target
         * point. Add the provided angle to this angle to determine
         * the new angle.
         * 2. Calculate the distance between the two points.
         * 3. Create a new point ^that^ distance away from the center at
         * whatever the new angle is.
         */
        return inDirection(
                center,
                distance(
                        center,
                        point
                ),
                angleTo(
                        center,
                        point
                ).add(angle)
        );
    }

    public static boolean areCollinear(PointXY a,
                                       PointXY b,
                                       PointXY c) {
        double dx1 = b.x() - a.x();
        double dy1 = b.y() - a.y();
        double dx2 = c.x() - a.x();
        double dy2 = c.y() - a.y();

        return Math.abs(dx1 * dy2 - dy1 * dx2) < 0.001;
    }

    public static PointXY midpoint(PointXY a,
                                   PointXY b) {
        return avg(a, b);
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public PointXY add(PointXY a) {
        return add(this, a);
    }

    public PointXY multiply(PointXY a) {
        return multiply(this, a);
    }

    public PointXY multiply(double a) {
        return multiply(this, a);
    }

    public PointXY avg(PointXY a) {
        return avg(this, a);
    }

    public PointXYZ withHeading(Angle angle) {
        return new PointXYZ(x(), y(), angle);
    }

    public double distance(PointXY a) {
        return distance(this, a);
    }

    public double distanceX(PointXY a) {
        return distanceX(this, a);
    }

    public double distanceY(PointXY a) {
        return distanceY(this, a);
    }

    public Angle angleTo(PointXY a) {
        return angleTo(this, a);
    }

    public PointXY inDirection(double distance,
                               Angle angle) {
        return inDirection(this, distance, angle);
    }

    public boolean isNear(PointXY a,
                          double tolerance) {
        return isNear(this, a, tolerance);
    }

    public PointXY rotate(PointXY center,
                          Angle angle) {
        return rotate(this, center, angle);
    }

    public boolean isCollinearWith(PointXY a,
                                   PointXY b) {
        return areCollinear(this, a, b);
    }

    public PointXY midpoint(PointXY a) {
        return midpoint(this, a);
    }
}
