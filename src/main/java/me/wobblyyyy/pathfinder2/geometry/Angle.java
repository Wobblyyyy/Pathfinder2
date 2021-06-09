package me.wobblyyyy.pathfinder2.geometry;

import java.io.Serializable;

public class Angle implements Comparable<Angle>, Serializable {
    public static final String FORMAT_RAD_SHORT = "rad";

    public static final String FORMAT_DEG_SHORT = "deg";

    public static final String FORMAT_RAD_LONG = "radians";

    public static final String FORMAT_DEG_LONG = "degrees";

    public static final double MIN_RAD = 0;

    public static final double MAX_RAD = 6.28318531;

    public static final double MIN_DEG = 0;

    public static final double MAX_DEG = 360;

    public static final Angle DEG_0 = Angle.fromDeg(0);

    public static final Angle DEG_45 = Angle.fromDeg(45);

    public static final Angle DEG_90 = Angle.fromDeg(90);

    public static final Angle DEG_135 = Angle.fromDeg(135);

    public static final Angle DEG_180 = Angle.fromDeg(180);

    public static final Angle DEG_225 = Angle.fromDeg(225);

    public static final Angle DEG_270 = Angle.fromDeg(270);

    public static final Angle DEG_315 = Angle.fromDeg(315);

    public static final Angle DEG_360 = Angle.fromDeg(360);

    public static final Angle PI_OVER_4 = Angle.fromDeg(45);

    public static final Angle PI_OVER_2 = Angle.fromDeg(90);

    public static final Angle THREE_PI_OVER_4 = Angle.fromDeg(135);

    public static final Angle PI = Angle.fromDeg(180);

    public static final Angle FIVE_PI_OVER_4 = Angle.fromDeg(225);

    public static final Angle THREE_PI_OVER_2 = Angle.fromDeg(270);

    public static final Angle SEVEN_PI_OVER_4 = Angle.fromDeg(315);

    public static final Angle TWO_PI = Angle.fromDeg(360);


    private final double rad;

    private final double deg;

    private Angle(double rad,
                  double deg) {
        this.rad = rad;
        this.deg = deg;
    }

    public static double fixRad(double rad) {
        while (rad < MIN_RAD) rad += MAX_RAD;
        while (rad > MAX_RAD) rad -= MAX_RAD;
        return rad;
    }

    public static double fixDeg(double deg) {
        while (deg < MIN_DEG) deg += MAX_DEG;
        while (deg > MAX_DEG) deg -= MAX_DEG;
        return deg;
    }

    public static Angle fromRad(double rad) {
        return new Angle(rad, Math.toDegrees(rad));
    }

    public static Angle fromDeg(double deg) {
        return new Angle(Math.toRadians(deg), deg);
    }

    public static Angle fixedRad(double rad) {
        return fromRad(fixRad(rad));
    }

    public static Angle fixedDeg(double deg) {
        return fromDeg(fixDeg(deg));
    }

    public static Angle add(Angle a,
                            Angle b) {
        return Angle.fromRad(a.rad() + b.rad());
    }

    public static Angle subtract(Angle a,
                                 Angle b) {
        return add(a, multiply(b, -1));
    }

    public static Angle multiply(Angle a,
                                 Angle b) {
        return Angle.fromRad(a.rad() * b.rad());
    }

    public static Angle multiply(Angle a,
                                 double b) {
        return Angle.fromRad(a.rad() * b);
    }

    public static Angle zero() {
        return new Angle(0, 0);
    }

    public static boolean equals(Angle a,
                                 Angle b) {
        return a.rad() == b.rad() || a.deg() == b.deg();
    }

    public static boolean isCloseRad(Angle a,
                                     Angle b,
                                     double tolerance) {
        return Math.abs(a.rad() - b.rad()) < tolerance;
    }

    public static boolean isCloseDeg(Angle a,
                                     Angle b,
                                     double tolerance) {
        return Math.abs(a.deg() - b.deg()) < tolerance;
    }

    public static Angle acos(double a) {
        return Angle.fromRad(Math.acos(a));
    }

    public static Angle asin(double a) {
        return Angle.fromRad(Math.asin(a));
    }

    public static Angle atan(double a) {
        return Angle.fromRad(Math.atan(a));
    }

    public static Angle atan2(double y, double x) {
        return Angle.fromRad(Math.atan2(y, x));
    }

    public static Angle rotateRad(Angle a,
                                  double rad) {
        return Angle.fromRad(a.rad() + rad);
    }

    public static Angle rotateDeg(Angle a,
                                  double deg) {
        return Angle.fromDeg(a.deg() + deg);
    }

    public static Angle fixedRotateRad(Angle a,
                                       double rad) {
        return Angle.fromRad(fixRad(a.rad() + rad));
    }

    public static Angle fixedRotateDeg(Angle a,
                                       double deg) {
        return Angle.fromDeg(fixDeg(a.deg() + deg));
    }

    public static Angle flip(Angle a) {
        return rotateDeg(a, 180);
    }

    public static Angle fixedFlip(Angle a) {
        return fixedRotateDeg(a, 180);
    }

    public double rad() {
        return this.rad;
    }

    public double deg() {
        return this.deg;
    }

    public double sin() {
        return Math.sin(rad);
    }

    public double cos() {
        return Math.cos(rad);
    }

    public double tan() {
        return Math.tan(rad);
    }

    public double csc() {
        return 1 / sin();
    }

    public double sec() {
        return 1 / cos();
    }

    public double cot() {
        return 1 / tan();
    }

    public Angle add(Angle a) {
        return add(this, a);
    }

    public Angle subtract(Angle a) {
        return subtract(this, a);
    }

    public Angle multiply(Angle a) {
        return multiply(this, a);
    }

    public Angle multiply(double a) {
        return multiply(this, a);
    }

    public boolean equals(Angle a) {
        return equals(this, a);
    }

    public boolean isCloseRad(Angle a,
                              double tolerance) {
        return isCloseRad(this, a, tolerance);
    }

    public boolean isCloseDeg(Angle a,
                              double tolerance) {
        return isCloseDeg(this, a, tolerance);
    }

    public Angle rotateRad(double rad) {
        return rotateRad(this, rad);
    }

    public Angle rotateDeg(double deg) {
        return rotateDeg(this, deg);
    }

    public Angle fixedRotateRad(double rad) {
        return fixedRotateRad(this, rad);
    }

    public Angle fixedRotateDeg(double deg) {
        return fixedRotateDeg(this, deg);
    }

    public Angle flip() {
        return flip(this);
    }

    public Angle fixedFlip() {
        return fixedFlip(this);
    }

    public String formatAsRadShort() {
        return rad() + " " + FORMAT_RAD_SHORT;
    }

    public String formatAsDegShort() {
        return deg() + " " + FORMAT_DEG_SHORT;
    }

    public String formatAsRadLong() {
        return rad() + " " + FORMAT_RAD_LONG;
    }

    public String formatAsDegLong() {
        return deg() + " " + FORMAT_DEG_LONG;
    }

    @Override
    public int compareTo(Angle o) {
        return Double.compare(this.rad(), o.rad());
    }

    @Override
    public String toString() {
        return deg() + " " + FORMAT_DEG_SHORT;
    }
}
