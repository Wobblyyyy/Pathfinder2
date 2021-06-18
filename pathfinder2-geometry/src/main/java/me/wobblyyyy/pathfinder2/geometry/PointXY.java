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

import java.io.Serializable;

/**
 * A 2d coordinate with an X and Y value.
 *
 * <p>
 * A point is defined as a pair of X and Y values. These values are stored
 * as doubles. Points are immutable objects, meaning they can not be modified
 * once created. Many methods in the {@code PointXY} class will return a new
 * {@code PointXY} based on whatever modifications you will have made.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class PointXY implements Serializable {
    /*
     * Another non-JavaDoc comment - if you're only browsing the API, you
     * can skip over all of this, as it contains nothing that's in any way,
     * shape nor form, useful to you.
     *
     * Using PointXY can make code rather confusing. Because PointXYZ extends
     * it and can be used in place of it, there's very few applications in
     * which it's ever useful. Not only that, but it makes maintenance a
     * total nightmare - if you add a method to the PointXY class, you have
     * to add an analogous method to the PointXYZ class, otherwise things
     * can go wrong pretty quickly.
     *
     * I guess what I'm trying to say here is that you should keep the
     * PointXY and PointXYZ classes on the same page. If you make any
     * modifications to the PointXY class, go check the PointXYZ class and
     * see if there's anything you need to modify there.
     */

    /**
     * The point's X value.
     */
    private final double x;

    /**
     * The point's Y value.
     */
    private final double y;

    /**
     * Create a new {@code PointXY}.
     *
     * @param x the point's X value.
     * @param y the point's Y value.
     */
    public PointXY(double x,
                   double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Add two points together by creating another point with an X value
     * of the sum of A and B's X values and a Y value of the sum of A and
     * B's Y values.
     *
     * @param a one of the two points to add.
     * @param b one of the two points to add.
     * @return a new point, created by adding the component X and Y values of
     * each of the points together.
     */
    public static PointXY add(PointXY a,
                              PointXY b) {
        return new PointXY(
                a.x() + b.x(),
                a.y() + b.y()
        );
    }

    /**
     * Multiply two points together.
     *
     * <p>
     * The new point's X value is equal to {@code a.x() * b.x()}. Likewise, the
     * new point's Y value is equal to {@code a.y() * b.y()}.
     * </p>
     *
     * @param a one of the two points to multiply.
     * @param b one of the two points to multiply.
     * @return the product of the two point's component X and Y values.
     */
    public static PointXY multiply(PointXY a,
                                   PointXY b) {
        return new PointXY(
                a.x() * b.x(),
                a.y() * b.y()
        );
    }

    /**
     * Multiply a point's X and Y values by a single multiplier.
     *
     * @param a the point that will be multiplied by {@code b}.
     * @param b the multiplier to multiply {@code a}'s X and Y values.
     * @return a new point, the result of multiplying {@code a}'s component
     * X and Y values by {@code b}.
     */
    public static PointXY multiply(PointXY a,
                                   double b) {
        return new PointXY(
                a.x() * b,
                a.y() * b
        );
    }

    /**
     * Get the average between two points.
     *
     * @param a one of the two points to average.
     * @param b one of the two points to average.
     * @return the average (midpoint, if you will) of the two points.
     */
    public static PointXY avg(PointXY a,
                              PointXY b) {
        return multiply(add(a, b), 0.5);
    }

    /**
     * Create a new {@code PointXY} instance with an X value of 0 and a
     * Y value of 0.
     *
     * @return a new {@code PointXY} instance with an X value of 0 and a
     * Y value of 0.
     */
    public static PointXY zero() {
        return new PointXY(0, 0);
    }

    /**
     * Get the distance between two points.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the distance between the two points.
     * @see <a href="https://en.wikipedia.org/wiki/Distance">Distance</a>
     */
    public static double distance(PointXY a,
                                  PointXY b) {
        // The distance formula is essentially as follows:
        // sqrt((Bx-Ax)^2+(By-Ay)^2)
        // Obviously, there's some minor formatting issues there, but you
        // get the general idea.
        return Math.sqrt(
                Math.pow(b.x() - a.x(), 2) +
                        Math.pow(b.y() - a.y(), 2)
        );
    }

    /**
     * Get the difference in X values between the two points.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return {@code b}'s X value minus {@code a}'s X value.
     */
    public static double distanceX(PointXY a,
                                   PointXY b) {
        return b.x() - a.x();
    }

    /**
     * Get the difference in Y values between the two points.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return {@code b}'s Y value minus {@code a}'s Y value.
     */
    public static double distanceY(PointXY a,
                                   PointXY b) {
        return b.y() - a.y();
    }

    /**
     * Calculate the angle from {@code a} to {@code b}.
     *
     * @param a the origin point.
     * @param b the target point.
     * @return the angle from {@code a} to {@code b}.
     * @see <a href="https://en.wikipedia.org/wiki/Atan2">atan2</a>
     */
    public static Angle angleTo(PointXY a,
                                PointXY b) {
        // We used to use a bit of a messier implementation, but since
        // the Angle class has gotten the atan2 static method, there's no
        // point. I'll preserve the old code just in case it's useful some day.
        // return Angle.fromRad(Math.atan2(distanceY(a,b), distanceX(a, b)));
        return Angle.atan2(distanceY(a, b), distanceX(a, b));
    }

    /**
     * Create a new point a given distance and angle away from a base point.
     *
     * @param base     the base point. This serves as the center of whatever
     *                 transformation the distance and angle values will cause.
     * @param distance the distance the new point should be away from the
     *                 center point.
     * @param angle    the angle at which the new point should be created,
     *                 at a given distance away from the base.
     * @return a new point, a given distance away from {@code base}, drawn
     * at {@code angle}.
     */
    public static PointXY inDirection(PointXY base,
                                      double distance,
                                      Angle angle) {
        // Look at that! Making use of those sexy angle trig functions.
        // On a more serious note, remember:
        // COS corresponds with X
        // SIN corresponds with Y
        return new PointXY(
                base.x() + (distance * angle.cos()),
                base.y() + (distance * angle.sin())
        );
    }

    /**
     * Are two points close to each other?
     *
     * @param a         one of the two points.
     * @param b         one of the two points.
     * @param tolerance the maximum distance value.
     * @return if the `distance(a, b)` method call returns a value less than
     * or equal to the {@code tolerance}, this method will return true.
     * Else, this method will return false.
     */
    public static boolean isNear(PointXY a,
                                 PointXY b,
                                 double tolerance) {
        // Get the distance between the two points, make sure it's absolute,
        // and check to see if it's less than the tolerance value.
        return Math.abs(distance(a, b)) <= Math.abs(tolerance);
    }

    /**
     * Rotate a point around a center point.
     *
     * @param point  the point that should be rotated.
     * @param center the center point (center of rotation).
     * @param angle  how far the point should be rotated.
     * @return a new point, rotated around {@code center} by {@code angle}.
     */
    public static PointXY rotate(PointXY point,
                                 PointXY center,
                                 Angle angle) {
        // Okay. I know for sure this is a bit confusing-looking at the very
        // Thankfully, it's actually pretty simple.
        // Firstly, we need to calculate the angle between the center point
        // and the target point. We'll call this our base angle.
        // Secondly, we need to determine how far away the target point is
        // from the center point - kind of like a magnitude.
        // Thirdly, we'll need to create another angle by adding the base
        // angle with our rotation angle.
        // And finally, we'll create a new point that's the right distance
        // away from the center point using the new angle.
        return inDirection(
                center,
                distance(center, point),
                angleTo(center, point).add(angle)
        );
    }

    /**
     * Determine if three points are collinear.
     *
     * @param a one of the three points.
     * @param b one of the three points.
     * @param c one of the three points.
     * @return true if the points are collinear, false if not.
     */
    public static boolean areCollinear(PointXY a,
                                       PointXY b,
                                       PointXY c) {
        // I'll be 100% honest here - I think I copied this code from
        // a stackoverflow answer somewhere. So I can't offer too much insight
        // into how it works. Sorry!
        double dx1 = b.x() - a.x();
        double dy1 = b.y() - a.y();
        double dx2 = c.x() - a.x();
        double dy2 = c.y() - a.y();

        return Math.abs(dx1 * dy2 - dy1 * dx2) < 0.001;
    }

    /**
     * Get the midpoint between two points.
     *
     * @param a one of the two points.
     * @param b one of the two points.
     * @return the midpoint between the two points.
     */
    public static PointXY midpoint(PointXY a,
                                   PointXY b) {
        return avg(a, b);
    }

    /**
     * If the provided point is null, return {@link #zero()}. Otherwise,
     * return the provided point.
     *
     * @param point the point that may be returned.
     * @return either the provided point or {@link #zero()}.
     */
    public static PointXY zeroIfNull(PointXY point) {
        return point == null ? zero() : point;
    }

    /**
     * Get the point's X value.
     *
     * @return the point's X value.
     */
    public double x() {
        return this.x;
    }

    /**
     * Get the point's Y value.
     *
     * @return the point's Y value.
     */
    public double y() {
        return this.y;
    }

    /**
     * Add this point with another point.
     *
     * @param a the point to add to this point.
     * @return the sum of this point and {@code a}.
     */
    public PointXY add(PointXY a) {
        return add(this, a);
    }

    /**
     * Multiply this point with another point.
     *
     * @param a the point to multiply this point by.
     * @return the product of this point and {@code a}.
     */
    public PointXY multiply(PointXY a) {
        return multiply(this, a);
    }

    /**
     * Multiply the X and Y values of this point by a single coefficient.
     *
     * @param a the coefficient to multiply the X and Y values by.
     * @return the product point.
     */
    public PointXY multiply(double a) {
        return multiply(this, a);
    }

    /**
     * Get the average between this point and another point.
     *
     * @param a the point that will be averaged with this point.
     * @return the average of the two points.
     */
    public PointXY avg(PointXY a) {
        return avg(this, a);
    }

    /**
     * Create a {@link PointXYZ} by adding a heading value to this point.
     *
     * @param angle the heading that should be added to the new point.
     * @return a new {@code PointXYZ}.
     */
    public PointXYZ withHeading(Angle angle) {
        return new PointXYZ(x(), y(), angle);
    }

    /**
     * Get the distance between this point and {@code a}.
     *
     * @param a the point to calculate the distance between.
     * @return the distance between this point and {@code a}.
     */
    public double distance(PointXY a) {
        return distance(this, a);
    }

    /**
     * Get the X difference between this point and {@code a}.
     *
     * @param a the point to get the X difference from.
     * @return the X distance between the two points.
     */
    public double distanceX(PointXY a) {
        return distanceX(this, a);
    }

    /**
     * Get the Y difference between this point and {@code a}.
     *
     * @param a the point to get the Y difference from.
     * @return the Y distance between the two points.
     */
    public double distanceY(PointXY a) {
        return distanceY(this, a);
    }

    /**
     * Determine the angle to {@code a} from this point.
     *
     * @param a the point to determine the angle to.
     * @return the angle from this point to {@code a}.
     */
    public Angle angleTo(PointXY a) {
        return angleTo(this, a);
    }

    /**
     * Create a new point a specified distance and direction away from
     * this point.
     *
     * @param distance the distance the new point should be created at.
     * @param angle    the angle the new point should be created at.
     * @return a new point, {@code distance} away in {@code angle} direction.
     */
    public PointXY inDirection(double distance,
                               Angle angle) {
        return inDirection(this, distance, angle);
    }

    /**
     * Determine if {@code a} is near to this point.
     *
     * @param a         the point to compare.
     * @param tolerance the maximum distance value.
     * @return whether or not the two points are near.
     */
    public boolean isNear(PointXY a,
                          double tolerance) {
        return isNear(this, a, tolerance);
    }

    /**
     * Rotate this point around a center point.
     *
     * @param center the center of rotation.
     * @param angle  how far to rotate this point.
     * @return a new, rotated point.
     */
    public PointXY rotate(PointXY center,
                          Angle angle) {
        return rotate(this, center, angle);
    }

    /**
     * Is this point collinear with {@code a} and {@code b}?
     *
     * @param a one of the points to check.
     * @param b one of the points to check.
     * @return whether or not all three points are collinear.
     */
    public boolean isCollinearWith(PointXY a,
                                   PointXY b) {
        return areCollinear(this, a, b);
    }

    /**
     * Get the midpoint between this point and {@code a}.
     *
     * @param a the point to get the midpoint from.
     * @return the midpoint between this point and {@code a}.
     */
    public PointXY midpoint(PointXY a) {
        return midpoint(this, a);
    }

    /**
     * Create a new point with the same Y value as this point and whatever
     * X value you provide.
     *
     * @param x the X value the new point should have.
     * @return a new point with the X value you specified.
     */
    public PointXY withX(double x) {
        return new PointXY(x, this.y);
    }

    /**
     * Create a new point with the same X value as this point and whatever
     * Y value you provide.
     *
     * @param y the Y value the new point should have.
     * @return a new point with the Y value you specified.
     */
    public PointXY withY(double y) {
        return new PointXY(this.x, y);
    }
}
