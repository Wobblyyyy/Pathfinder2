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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.math.Rounding;
import me.wobblyyyy.pathfinder2.utils.ArrayUtils;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A 2d coordinate with X and Y values. The foundation Pathfinder's
 * geometry is based upon. As a Pathfinder-specific notice: these points
 * should almost never be negative. The library assumes that everything
 * takes places in the first quadrant. You can try to use negative values
 * if you want, but I haven't tested the library with it because, to be
 * completely honest here, I'm pretty lazy.
 *
 * <p>
 * A point is defined as a pair of X and Y values. These values are stored
 * as doubles. Points are immutable objects, meaning they can not be modified
 * once created. Many methods in the {@code PointXY} class will return a new
 * {@code PointXY} based on whatever modifications you will have made.
 * </p>
 *
 * <p>
 * Points will be one of the most important concepts in using Pathfinder -
 * all of the advanced geometry (rectangles, circles, shapes, etc) is based
 * on lines and points. And you know what lines are based on? Points.
 * </p>
 *
 * <p>
 * The concept of a point is relatively straightforwards - it's literally just
 * a pair of X and Y values. There's a ton of methods available for use,
 * even for very niche situations. Some of the most important methods (at
 * least the ones that I've used the most) are as follows...
 * <ul>
 *     <li>{@link #distance(PointXY)}</li>
 *     <li>{@link #inDirection(double, Angle)}</li>
 *     <li>{@link #angleTo(PointXY)}</li>
 * </ul>
 * Additionally, it's worth noting that most (possibly all) of the methods
 * in this class have a corresponding static method.
 * </p>
 *
 * @author Colin Robertson
 * @see PointXYZ
 * @since 0.0.0
 */
@SuppressWarnings("DuplicatedCode")
public class PointXY implements Comparable<PointXY>, Serializable {
    /**
     * A point with X and Y values of 0.
     */
    public static final PointXY ZERO = zero();

    public static long COUNT = 0;

    /**
     * The point's X value.
     */
    private final double x;

    /**
     * The point's Y value.
     */
    private final double y;

    /**
     * Create a new {@code PointXY} with 0 for X and Y values.
     */
    public PointXY() {
        this(0, 0);
    }

    /**
     * Create a new {@code PointXY} by copying an existing point.
     *
     * @param point the point to copy.
     */
    public PointXY(PointXY point) {
        this(point.x, point.y);
    }

    /**
     * Create a new {@code PointXY}.
     *
     * @param x the point's X value.
     * @param y the point's Y value.
     */
    public PointXY(double x, double y) {
        ValidationUtils.validate(x, "x");
        ValidationUtils.validate(y, "y");

        COUNT++;

        this.x = x;
        this.y = y;
    }

    /**
     * Create a new {@code PointXY} and convert the provided values from
     * meters to inches.
     *
     * @param xMeters the X value.
     * @param yMeters the Y value.
     * @return a new {@code PointXY}.
     */
    public static PointXY fromMetersToInches(double xMeters, double yMeters) {
        return new PointXY(xMeters * 39.37, yMeters * 39.37);
    }

    /**
     * Create a new {@code PointXY} and convert the provided values from
     * inches to meters.
     *
     * @param xInches the X value.
     * @param yInches the Y value.
     * @return a new {@code PointXY}.
     */
    public static PointXY fromInchesToMeters(double xInches, double yInches) {
        return new PointXY(xInches * 0.025, yInches * 0.025);
    }

    /**
     * Create a new {@code PointXY} and convert the provided values from
     * meters to centimeters.
     *
     * @param xMeters the X value.
     * @param yMeters the Y value.
     * @return a new {@code PointXY}.
     */
    public static PointXY fromMetersToCentimeters(
        double xMeters,
        double yMeters
    ) {
        return new PointXY(xMeters * 100, yMeters * 100);
    }

    /**
     * Create a new {@code PointXY} and convert the provided values from
     * centimeters to inches.
     *
     * @param xCentimeters the X value.
     * @param yCentimeters the Y value.
     * @return a new {@code PointXY}.
     */
    public static PointXY fromCentimetersToInches(
        double xCentimeters,
        double yCentimeters
    ) {
        return new PointXY(xCentimeters / 100, yCentimeters / 100);
    }

    /**
     * Create a new {@code PointXY} and convert the provided values from
     * inches to centimeters.
     *
     * @param xInches the X value.
     * @param yInches the Y value.
     * @return a new {@code PointXY}.
     */
    public static PointXY fromInchesToCentimeters(
        double xInches,
        double yInches
    ) {
        return new PointXY((xInches * 0.025) / 100, (yInches * 0.025) / 100);
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
    public static PointXY add(PointXY a, PointXY b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new PointXY(a.x() + b.x(), a.y() + b.y());
    }

    /**
     * Add two points together by creating another point with an X value
     * of the sum of A and B's X values and a Y value of the sum of A and
     * B's Y values.
     *
     * @param a a point.
     * @param x the X value to add.
     * @param y the Y value to add.
     * @return a new point, created by adding the component X and Y values of
     * each of the points together.
     */
    public static PointXY add(PointXY a, double x, double y) {
        return new PointXY(a.x + x, a.y + y);
    }

    /**
     * Subtract point b from point a.
     *
     * @param a the first point.
     * @param b the second point.
     * @return a new point - the difference between a and b.
     */
    public static PointXY subtract(PointXY a, PointXY b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new PointXY(a.x() - b.x(), a.y() - b.y());
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
    public static PointXY multiply(PointXY a, PointXY b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new PointXY(a.x() * b.x(), a.y() * b.y());
    }

    /**
     * Multiply a point's X and Y values by a single multiplier.
     *
     * @param a the point that will be multiplied by {@code b}.
     * @param b the multiplier to multiply {@code a}'s X and Y values.
     * @return a new point, the result of multiplying {@code a}'s component
     * X and Y values by {@code b}.
     */
    public static PointXY multiply(PointXY a, double b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new PointXY(a.x() * b, a.y() * b);
    }

    /**
     * Does an object have the same X value as a point?
     *
     * @param a      the base point.
     * @param object the object to compare.
     * @return if they have the same value, true. Otherwise, false.
     */
    public static boolean equalsX(PointXY a, Object object) {
        if (object instanceof PointXY) {
            PointXY b = (PointXY) object;

            return Equals.soft(a.x, b.x, Geometry.tolerancePointXY);
        }

        return false;
    }

    /**
     * Does an object have the same Y value as a point?
     *
     * @param a      the base point.
     * @param object the object to compare.
     * @return if they have the same value, true. Otherwise, false.
     */
    public static boolean equalsY(PointXY a, Object object) {
        if (object instanceof PointXY) {
            PointXY b = (PointXY) object;

            return Equals.soft(a.y, b.y, Geometry.tolerancePointXY);
        }

        return false;
    }

    /**
     * Does an object have the same Y value as a point?
     *
     * @param a      the base point.
     * @param object the object to compare.
     * @return if they have the same value, true. Otherwise, false.
     */
    public static boolean equals(PointXY a, Object object) {
        if (object instanceof PointXY) {
            PointXY b = (PointXY) object;

            boolean sameX = Equals.soft(a.x, b.x, Geometry.tolerancePointXY);
            boolean sameY = Equals.soft(a.y, b.y, Geometry.tolerancePointXY);

            return sameX && sameY;
        }

        return false;
    }

    /**
     * Get the average between two points.
     *
     * @param a one of the two points to average.
     * @param b one of the two points to average.
     * @return the average (midpoint, if you will) of the two points.
     */
    public static PointXY avg(PointXY a, PointXY b) {
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
     * Get the slope between two points.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the slope between the two points. This value is calculated
     * by taking the change in Y over the change in X - in other words,
     * (y2 minus y1) divided by (x2 minus x1).
     */
    public static double slope(PointXY a, PointXY b) {
        return distanceY(a, b) / distanceX(a, b);
    }

    /**
     * Get the distance between two points.
     *
     * <p>
     * Code example:
     * <code><pre>
     * double distance = PointXY.distance(
     *     new PointXY(0, 0),
     *     new PointXY(1, 1)
     * ); // distance is equal to sqrt2/2 (about 1.41)
     * </pre></code>
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the distance between the two points.
     * @see <a href="https://en.wikipedia.org/wiki/Distance">Distance</a>
     */
    public static double distance(PointXY a, PointXY b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        // The distance formula is essentially as follows:
        // sqrt((Bx-Ax)^2+(By-Ay)^2)
        // Obviously, there's some minor formatting issues there, but you
        // get the general idea.
        double distance = Math.hypot(b.x - a.x, b.y - a.y);

        ValidationUtils.validate(
            distance,
            "distance",
            StringUtils.format("point a: <%s>, point b: <%s>", a, b)
        );

        return distance;
    }

    /**
     * Get the difference in X values between the two points.
     *
     * <p>
     * Code example:
     * <code><pre>
     * double distance = PointXY.distanceX(
     *     new PointXY(0, 0),
     *     new PointXY(1, 1)
     * ); // distance is equal to 1
     * </pre></code>
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return {@code b}'s X value minus {@code a}'s X value.
     */
    public static double distanceX(PointXY a, PointXY b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return b.x() - a.x();
    }

    /**
     * Get the difference in Y values between the two points.
     *
     * <p>
     * Code example:
     * <code><pre>
     * double distance = PointXY.distanceY(
     *     new PointXY(0, 0),
     *     new PointXY(1, 1)
     * ); // distance is equal to 1
     * </pre></code>
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return {@code b}'s Y value minus {@code a}'s Y value.
     */
    public static double distanceY(PointXY a, PointXY b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return b.y() - a.y();
    }

    /**
     * Calculate the angle from {@code a} to {@code b}.
     *
     * <p>
     * Code example:
     * <code><pre>
     * PointXY a = new PointXY(0, 0);
     * PointXY b = new PointXY(1, 1);
     * PointXY c = new PointXY(1, 2);
     *
     * Angle angleA = PointXY.angleTo(a, b); // 45 degrees
     * Angle angleB = PointXY.angleTo(b, c); // 90 degrees
     * Angle angleC = PointXY.angleTo(b, a); // 135 degrees
     * Angle angleD = PointXY.angleTo(c, b); // 270 degrees
     * </pre></code>
     * </p>
     *
     * @param a the origin point.
     * @param b the target point.
     * @return the angle from {@code a} to {@code b}.
     * @see <a href="https://en.wikipedia.org/wiki/Atan2">atan2</a>
     */
    public static Angle angleTo(PointXY a, PointXY b) {
        return Angle.atan2(distanceY(a, b), distanceX(a, b));
    }

    /**
     * Calculate the angle from {@code b} to {@code a}.
     *
     * <p>
     * Code example:
     * <code><pre>
     * PointXY a = new PointXY(0, 0);
     * PointXY b = new PointXY(1, 1);
     * PointXY c = new PointXY(1, 2);
     *
     * Angle angleA = PointXY.angleTo(a, b); // 135 degrees
     * Angle angleB = PointXY.angleTo(b, c); // 270 degrees
     * Angle angleC = PointXY.angleTo(b, a); // 45 degrees
     * Angle angleD = PointXY.angleTo(c, b); // 90 degrees
     * </pre></code>
     * </p>
     *
     * @param a the origin point.
     * @param b the target point.
     * @return the angle from {@code a} to {@code b}.
     * @see <a href="https://en.wikipedia.org/wiki/Atan2">atan2</a>
     */
    public static Angle angleFrom(PointXY a, PointXY b) {
        return angleTo(b, a);
    }

    /**
     * Check a set of points for any duplicates. If there are any duplicate
     * points (points that have the same X and Y values), return true.
     * If duplicates are not present, return false.
     *
     * @param points the set of points to check for duplicates.
     * @return if there are no duplicates, return false. If there are
     * duplicates, return true.
     */
    public static boolean areDuplicatesPresent(PointXY... points) {
        List<PointXY> ps = new ArrayList<>(points.length);

        for (PointXY p : points) {
            for (PointXY x : ps) {
                boolean sameX = Equals.soft(
                    x.x(),
                    p.x(),
                    Geometry.tolerancePointXY
                );
                boolean sameY = Equals.soft(
                    x.y(),
                    p.y(),
                    Geometry.tolerancePointXY
                );

                if (sameX && sameY) return true;
            }

            ps.add(p);
        }

        return false;
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
    public static PointXY inDirection(
        PointXY base,
        double distance,
        Angle angle
    ) {
        ValidationUtils.validate(base, "base");
        ValidationUtils.validate(distance, "distance");
        ValidationUtils.validate(angle, "angle");

        return new PointXY(
            base.x() + (distance * angle.cos()),
            base.y() + (distance * angle.sin())
        );
    }

    /**
     * Apply a translation to a point.
     *
     * @param base        the base point. This serves as the center of
     *                    whatever transformation the translation will cause.
     * @param translation the translation to apply.
     * @return a new point with the provided translation applied to it.
     */
    public static PointXY applyTranslation(
        PointXY base,
        Translation translation
    ) {
        return inDirection(base, translation.magnitude(), translation.angle());
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
    public static boolean isNear(PointXY a, PointXY b, double tolerance) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");
        InvalidToleranceException.throwIfInvalid(
            "Invalid tolerance!",
            tolerance
        );

        if (tolerance < 0) throw new InvalidToleranceException(
            StringUtils.format(
                "Cannot have a tolerance below 0! Tolerance was: <%s>",
                tolerance
            )
        );

        return Math.abs(distance(a, b)) <= Math.abs(tolerance);
    }

    public static boolean isNear(
        PointXY a,
        double tolerance,
        PointXY... points
    ) {
        InvalidToleranceException.throwIfInvalid(
            "Invalid tolerance!",
            tolerance
        );

        for (PointXY point : points) if (
            isNear(a, point, tolerance)
        ) return true;

        return false;
    }

    /**
     * Rotate a point around a center point.
     *
     * @param point  the point that should be rotated.
     * @param center the center point (center of rotation).
     * @param angle  how far the point should be rotated.
     * @return a new point, rotated around {@code center} by {@code angle}.
     */
    public static PointXY rotate(PointXY point, PointXY center, Angle angle) {
        checkArgument(point);
        checkArgument(center);
        Angle.checkArgument(angle);

        return inDirection(
            center,
            distance(center, point),
            angleTo(center, point).fix().add(angle).fix()
        );
    }

    /**
     * Rotate a {@code Collection} of points around a center point.
     *
     * @param points the points to rotate.
     * @param center the point to rotate all of the points around.
     * @param angle  how far to rotate the points.
     * @return a new {@code List} of rotated points.
     */
    public static List<PointXY> rotate(
        Collection<PointXY> points,
        PointXY center,
        Angle angle
    ) {
        List<PointXY> rotated = new ArrayList<>(points.size());

        for (PointXY point : points) rotated.add(rotate(point, center, angle));

        return rotated;
    }

    /**
     * Rotate a {@code Collection} of points around a center point.
     *
     * @param points the points to rotate.
     * @param center the point to rotate all of the points around.
     * @param angle  how far to rotate the points.
     * @return a new {@code List} of rotated points.
     */
    public static PointXY[] rotate(
        PointXY[] points,
        PointXY center,
        Angle angle
    ) {
        return ArrayUtils.toPointXYArray(
            rotate(Arrays.asList(points), center, angle)
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
    public static boolean areCollinear(PointXY a, PointXY b, PointXY c) {
        checkArgument(a);
        checkArgument(b);
        checkArgument(c);

        double dx1 =
            (b.x() + Geometry.toleranceCollinear) -
            (a.x() + Geometry.toleranceCollinear);
        double dy1 =
            (b.y() + Geometry.toleranceCollinear) -
            (a.y() + Geometry.toleranceCollinear);
        double dx2 =
            (c.x() + Geometry.toleranceCollinear) -
            (a.x() + Geometry.toleranceCollinear);
        double dy2 =
            (c.y() + Geometry.toleranceCollinear) -
            (a.y() + Geometry.toleranceCollinear);

        return Equals.soft(
            (dx1 * dy2),
            (dx2 * dy1),
            Geometry.toleranceCollinear
        );
    }

    /**
     * Are a set of points collinear?
     *
     * @param points the points to check.
     * @return true if the points are collinear, otherwise, false.
     */
    public static boolean areCollinear(PointXY... points) {
        if (points.length < 3) throw new IllegalArgumentException(
            "Must provide at least " +
            "3 points, you only provided <" +
            points.length +
            ">"
        );

        for (int i = 0; i < points.length - 3; i++) {
            PointXY a = points[i];
            PointXY b = points[i + 1];
            PointXY c = points[i + 2];

            if (!areCollinear(a, b, c)) return false;
        }

        return true;
    }

    /**
     * Create a new point TOWARDS the target point. This point will be
     * {@code distance} away.
     *
     * @param origin   the origin point.
     * @param target   the target point.
     * @param distance how far away the point should be.
     * @return a new point.
     */
    public static PointXY towards(
        PointXY origin,
        PointXY target,
        double distance
    ) {
        checkArgument(origin);
        checkArgument(target);
        ValidationUtils.validate(distance, "distance");

        Angle angle = origin.angleTo(target);

        return origin.inDirection(distance, angle);
    }

    /**
     * Get the midpoint between two points.
     *
     * @param a one of the two points.
     * @param b one of the two points.
     * @return the midpoint between the two points.
     */
    public static PointXY midpoint(PointXY a, PointXY b) {
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
     * Based on a reference point, return the point in the set of points
     * that is provided that's closest to the reference point.
     *
     * @param reference the point to use for reference.
     * @param points    a set of points to test against the reference.
     * @return out of the set of points provided, the point that is closest
     * to the reference point.
     */
    public static PointXY getClosestPoint(
        PointXY reference,
        PointXY... points
    ) {
        double distance = Double.POSITIVE_INFINITY;
        PointXY point = points[0];

        for (PointXY p : points) {
            double d = reference.absDistance(p);

            if (d < distance) {
                distance = d;
                point = p;
            }
        }

        return point;
    }

    /**
     * Based on a reference point, return the point in the set of points
     * that is provided that's closest to the reference point.
     *
     * @param reference the point to use for reference.
     * @param points    a set of points to test against the reference.
     * @return out of the set of points provided, the point that is closest
     * to the reference point.
     */
    public static PointXY getClosestPoint(
        PointXY reference,
        List<PointXY> points
    ) {
        double distance = Double.POSITIVE_INFINITY;
        PointXY point = points.get(0);

        for (PointXY p : points) {
            double d = reference.absDistance(p);

            if (d < distance) {
                distance = d;
                point = p;
            }
        }

        return point;
    }

    /**
     * Based on a reference point, return the point in the set of points
     * that is provided that's furthest from the reference point.
     *
     * @param reference the point to use for reference.
     * @param points    a set of points to test against the reference.
     * @return out of the set of points provided, the point that is furthest
     * from the reference point.
     */
    public static PointXY getFurthestPoint(
        PointXY reference,
        PointXY... points
    ) {
        double distance = Double.NEGATIVE_INFINITY;
        PointXY point = points[0];

        for (PointXY p : points) {
            double d = reference.absDistance(p);

            if (d > distance) {
                distance = d;
                point = p;
            }
        }

        return point;
    }

    /**
     * Based on a reference point, return the point in the set of points
     * that is provided that's furthest from the reference point.
     *
     * @param reference the point to use for reference.
     * @param points    a set of points to test against the reference.
     * @return out of the set of points provided, the point that is furthest
     * from the reference point.
     */
    public static PointXY getFurthestPoint(
        PointXY reference,
        List<PointXY> points
    ) {
        double distance = Double.NEGATIVE_INFINITY;
        PointXY point = points.get(0);

        for (PointXY p : points) {
            double d = reference.absDistance(p);

            if (d > distance) {
                distance = d;
                point = p;
            }
        }

        return point;
    }

    /**
     * Get a point by averaging all the X and Y values in the set of
     * provided points.
     *
     * @param points the points to average.
     * @return an averaged point.
     */
    public static PointXY avg(List<PointXY> points) {
        double totalX = 0;
        double totalY = 0;

        for (PointXY point : points) {
            totalX += point.x();
            totalY += point.y();
        }

        return new PointXY(totalX / points.size(), totalY / points.size());
    }

    /**
     * Get a point by averaging all the X and Y values in the set of
     * provided points.
     *
     * @param points the points to average.
     * @return an averaged point.
     */
    public static PointXY avg(PointXY... points) {
        double totalX = 0;
        double totalY = 0;

        for (PointXY point : points) {
            totalX += point.x();
            totalY += point.y();
        }

        return new PointXY(totalX / points.length, totalY / points.length);
    }

    /**
     * For a set of points, get the minimum X value.
     *
     * @param points the points to iterate over.
     * @return the minimum X value.
     */
    public static double minimumX(PointXY... points) {
        double v = points[0].x();

        for (PointXY point : points) {
            v = Math.min(v, point.x());
        }

        return v;
    }

    /**
     * For a set of points, get the minimum Y value.
     *
     * @param points the points to iterate over.
     * @return the minimum Y value.
     */
    public static double minimumY(PointXY... points) {
        double v = points[0].y();

        for (PointXY point : points) {
            v = Math.min(v, point.y());
        }

        return v;
    }

    /**
     * For a set of points, get the maximum X value.
     *
     * @param points the points to iterate over.
     * @return the maximum X value.
     */
    public static double maximumX(PointXY... points) {
        double v = points[0].x();

        for (PointXY point : points) {
            v = Math.max(v, point.x());
        }

        return v;
    }

    /**
     * For a set of points, get the maximum Y value.
     *
     * @param points the points to iterate over.
     * @return the maximum Y value.
     */
    public static double maximumY(PointXY... points) {
        double v = points[0].y();

        for (PointXY point : points) {
            v = Math.max(v, point.y());
        }

        return v;
    }

    /**
     * Get the "minimum point." This point has the minimum X and Y values
     * from all the points.
     *
     * @param points the points.
     * @return the minimum point.
     */
    public static PointXY minimumPoint(PointXY... points) {
        double x = minimumX(points);
        double y = minimumX(points);

        return new PointXY(x, y);
    }

    /**
     * Get the "maximum point." This point has the maximum X and Y values
     * from all the points.
     *
     * @param points the points.
     * @return the maximum point.
     */
    public static PointXY maximumPoint(PointXY... points) {
        double x = maximumX(points);
        double y = maximumY(points);

        return new PointXY(x, y);
    }

    public static void checkArgument(PointXY point, String message) {
        if (point == null) {
            throw new IllegalArgumentException(message);
        }

        double x = point.x();
        double y = point.y();

        boolean invalidX = Double.isNaN(x) || Double.isInfinite(x);
        boolean invalidY = Double.isNaN(y) || Double.isInfinite(y);

        if (invalidX || invalidY) {
            throw new IllegalArgumentException(
                "Invalid X or Y value - please make sure " +
                "your X and Y values are finite real numbers."
            );
        }
    }

    public static void checkArgument(PointXY point) {
        checkArgument(
            point,
            "Attempted to operate on a null PointXY, please " +
            "make sure you're not passing a null point to a method."
        );
    }

    /**
     * Get the closest point in the shape.
     *
     * @param reference the reference point.
     * @param shape     the shape.
     * @return the closest point in the shape.
     */
    public static PointXY closestPoint(PointXY reference, Shape<?> shape) {
        return shape.getClosestPoint(reference);
    }

    /**
     * Shift a point by an X and Y offset.
     *
     * @param reference the reference point.
     * @param shiftX    the X translation/shift.
     * @param shiftY    the Y translation/shift.
     * @return the shifted point.
     */
    public static PointXY shift(
        PointXY reference,
        double shiftX,
        double shiftY
    ) {
        return new PointXY(reference.x() + shiftX, reference.y() + shiftY);
    }

    /**
     * Shift a point by an X offset.
     *
     * @param reference the reference point.
     * @param shiftX    the X translation/shift.
     * @return the shifted point.
     */
    public static PointXY shiftX(PointXY reference, double shiftX) {
        return shift(reference, shiftX, 0);
    }

    /**
     * Shift a point by an Y offset.
     *
     * @param reference the reference point.
     * @param shiftY    the Y translation/shift.
     * @return the shifted point.
     */
    public static PointXY shiftY(PointXY reference, double shiftY) {
        return shift(reference, 0, shiftY);
    }

    /**
     * Shift a list of points.
     *
     * @param shiftX the X shift.
     * @param shiftY the Y shift.
     * @param points the points to shift.
     * @return shifted points.
     */
    public static List<PointXY> shift(
        double shiftX,
        double shiftY,
        List<PointXY> points
    ) {
        List<PointXY> newPoints = new ArrayList<>(points.size());

        for (PointXY point : points) {
            newPoints.add(point.shift(shiftX, shiftY));
        }

        return newPoints;
    }

    /**
     * Shift a set of points.
     *
     * @param shiftX the X shift.
     * @param shiftY the Y shift.
     * @param points the points to shift.
     * @return shifted points.
     */
    public static List<PointXY> shift(
        double shiftX,
        double shiftY,
        PointXY... points
    ) {
        List<PointXY> newPoints = new ArrayList<>(points.length);

        for (PointXY point : points) {
            newPoints.add(point.shift(shiftX, shiftY));
        }

        return newPoints;
    }

    /**
     * Is this point close to any point in a set of points?
     *
     * @param reference the reference point.
     * @param tolerance the maximum distance value that is considered "near."
     *                  If the distance between this point and the shape is
     *                  greater than this value, it's not considered to
     *                  be "near" to the point.
     * @param points    the set of points to test.
     * @return if the reference point is near any of the points in the provided
     * set of points, return true. Otherwise, return false.
     */
    public static boolean isPointNearPoints(
        PointXY reference,
        double tolerance,
        PointXY... points
    ) {
        for (PointXY point : points) if (
            reference.isNear(point, tolerance)
        ) return true;

        return false;
    }

    /**
     * Is a point close to a given shape?
     *
     * @param reference the reference point.
     * @param shape     the shape to test.
     * @param tolerance the maximum distance value that is considered "near."
     *                  If the distance between this point and the shape is
     *                  greater than this value, it's not considered to
     *                  be "near" to the point.
     * @return if the reference point is "near" the shape, return true.
     * Otherwise, return false.
     */
    public static boolean isPointNearShape(
        PointXY reference,
        Shape<?> shape,
        double tolerance
    ) {
        ValidationUtils.validate(reference, "reference");
        ValidationUtils.validate(shape, "shape");
        ValidationUtils.validate(tolerance, "tolerance");
        InvalidToleranceException.throwIfInvalid(
            "Invalid tolerance! " + "Must be greater than 0.",
            tolerance
        );

        return (
            reference.absDistance(shape.getClosestPoint(reference)) <= tolerance
        );
    }

    /**
     * Create a new point TOWARDS the target point. This point will be
     * {@code distance} away.
     *
     * @param target   the target point.
     * @param distance how far away the point should be.
     * @return a new point.
     */
    public PointXY towards(PointXY target, double distance) {
        return towards(this, target, distance);
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
     * Get the absolute value of the point's X value.
     *
     * @return the absolute value of the point's X value.
     */
    public double absX() {
        return Math.abs(this.x);
    }

    /**
     * Get the absolute value of the point's Y value.
     *
     * @return the absolute value of the point's Y value.
     */
    public double absY() {
        return Math.abs(this.y);
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
     * Add a value to this point's X value and return a new point with
     * that new value.
     *
     * @param x the value to add.
     * @return a new point with an added X value.
     */
    public PointXY addX(double x) {
        return new PointXY(this.x + x, y);
    }

    /**
     * Add a value to this point's Y value and return a new point with
     * that new value.
     *
     * @param y the value to add.
     * @return a new point with an added Y value.
     */
    public PointXY addY(double y) {
        return new PointXY(x, this.y + y);
    }

    /**
     * Subtract another point from this point.
     *
     * @param b the point to subtract.
     * @return the difference of the two points.
     */
    public PointXY subtract(PointXY b) {
        return subtract(this, b);
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
     * Create a {@link PointXYZ} by adding a heading value to this point.
     *
     * @param point the heading that should be added to the new point.
     * @return a new {@code PointXYZ}.
     */
    public PointXYZ withHeading(PointXYZ point) {
        return new PointXYZ(x(), y(), point.z());
    }

    public PointXYZ withHeadingDegrees(double degrees) {
        return new PointXYZ(x(), y(), Angle.fromDeg(degrees));
    }

    public PointXYZ withHeadingRadians(double radians) {
        return new PointXYZ(x(), y(), Angle.fromDeg(radians));
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
     * Get the absolute value of the distance from a given point.
     *
     * @param a the point to get the difference from.
     * @return the absolute value of the distance between two points.
     */
    public double absDistance(PointXY a) {
        return Math.abs(distance(this, a));
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
     * Get the absolute value of the distance from a given point.
     *
     * @param a the point to get the difference from.
     * @return the absolute value of the distance between two points.
     */
    public double absDistanceX(PointXY a) {
        return Math.abs(distanceX(this, a));
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
     * Get the absolute value of the distance from a given point.
     *
     * @param a the point to get the difference from.
     * @return the absolute value of the distance between two points.
     */
    public double absDistanceY(PointXY a) {
        return Math.abs(distanceY(this, a));
    }

    /**
     * Determine the angle to {@code a} from this point. To reiterate,
     * this method gets the angle FROM the calling point TO the provided
     * point (parameter {@code a}).
     *
     * <p>
     * Code example:
     * <code><pre>
     * PointXY a = new PointXY(0, 0);
     * PointXY b = new PointXY(1, 1);
     * PointXY c = new PointXY(1, 2);
     *
     * Angle angleA = a.angleTo(b); // 45 degrees
     * Angle angleB = b.angleTo(c); // 90 degrees
     * Angle angleC = b.angleTo(a); // 135 degrees
     * Angle angleD = c.angleTo(b); // 270 degrees
     * </pre></code>
     * </p>
     *
     * @param a the point to determine the angle to.
     * @return the angle from this point to {@code a}.
     */
    public Angle angleTo(PointXY a) {
        return angleTo(this, a);
    }

    /**
     * Determine the angle to {@code this} point from {@code a}.
     *
     * <p>
     * Code example:
     * <code><pre>
     * PointXY a = new PointXY(0, 0);
     * PointXY b = new PointXY(1, 1);
     * PointXY c = new PointXY(1, 2);
     *
     * Angle angleA = a.angleTo(b); // 135 degrees
     * Angle angleB = b.angleTo(c); // 270 degrees
     * Angle angleC = b.angleTo(a); // 45 degrees
     * Angle angleD = c.angleTo(b); // 90 degrees
     * </pre></code>
     * </p>
     *
     * @param a the point to determine the angle to.
     * @return the angle from this point to {@code a}.
     */
    public Angle angleFrom(PointXY a) {
        return angleFrom(this, a);
    }

    /**
     * Create a new point a specified distance and direction away from
     * this point.
     *
     * @param distance the distance the new point should be created at.
     * @param angle    the angle the new point should be created at.
     * @return a new point, {@code distance} away in {@code angle} direction.
     */
    public PointXY inDirection(double distance, Angle angle) {
        return inDirection(this, distance, angle);
    }

    /**
     * Apply a translation to a point.
     *
     * @param translation the translation to apply.
     * @return a new point with the provided translation applied to it.
     */
    public PointXY applyTranslation(Translation translation) {
        return applyTranslation(this, translation);
    }

    /**
     * Determine if {@code a} is near to this point.
     *
     * @param a         the point to compare.
     * @param tolerance the maximum distance value.
     * @return whether the two points are near.
     */
    public boolean isNear(PointXY a, double tolerance) {
        return isNear(this, a, tolerance);
    }

    /**
     * Rotate this point around a center point.
     *
     * @param center the center of rotation.
     * @param angle  how far to rotate this point.
     * @return a new, rotated point.
     */
    public PointXY rotate(PointXY center, Angle angle) {
        return rotate(this, center, angle);
    }

    /**
     * Is this point collinear with {@code a} and {@code b}?
     *
     * @param a one of the points to check.
     * @param b one of the points to check.
     * @return whether all three points are collinear.
     */
    public boolean isCollinearWith(PointXY a, PointXY b) {
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

    public PointXYZ withZ(Angle z) {
        return withHeading(z);
    }

    /**
     * Is this point inside a given shape?
     *
     * @param shape the shape to test.
     * @return if the point is contained in the shape, return true. If the
     * point is not contained in the shape, return false.
     */
    public boolean isInside(Shape<?> shape) {
        return shape.isPointInShape(this);
    }

    /**
     * Is this point collinear with a given line's start and end points?
     *
     * @param line the line to test.
     * @return true if the point is collinear, otherwise, false.
     */
    public boolean isCollinearWithLine(Line line) {
        return line.isPointOnLine(this);
    }

    /**
     * Is a point on a given line segment?
     *
     * @param line the line to test.
     * @return true if the point is on the line segment, otherwise, false.
     */
    public boolean isOnLineSegment(Line line) {
        return line.isPointOnLineSegment(this);
    }

    /**
     * Get the closest point in the shape.
     *
     * @param shape the shape.
     * @return the closest point in the shape.
     */
    public PointXY closestPoint(Shape<?> shape) {
        return closestPoint(this, shape);
    }

    /**
     * Shift a point by an X and Y offset.
     *
     * @param shiftX the X translation/shift.
     * @param shiftY the Y translation/shift.
     * @return the shifted point.
     */
    public PointXY shift(double shiftX, double shiftY) {
        return shift(this, shiftX, shiftY);
    }

    /**
     * Shift a point by an X offset.
     *
     * @param shiftX the X translation/shift.
     * @return the shifted point.
     */
    public PointXY shiftX(double shiftX) {
        return shiftX(this, shiftX);
    }

    /**
     * Shift a point by an Y offset.
     *
     * @param shiftY the Y translation/shift.
     * @return the shifted point.
     */
    public PointXY shiftY(double shiftY) {
        return shiftY(this, shiftY);
    }

    /**
     * Is this point close to any point in a set of points?
     *
     * @param tolerance the maximum distance value that is considered "near."
     *                  If the distance between this point and the shape is
     *                  greater than this value, it's not considered to
     *                  be "near" to the point.
     * @param points    the set of points to test.
     * @return if the reference point is near any of the points in the provided
     * set of points, return true. Otherwise, return false.
     */
    public boolean isPointNearPoints(double tolerance, PointXY... points) {
        return isPointNearPoints(this, tolerance, points);
    }

    /**
     * Is this point close to a given shape?
     *
     * @param shape     the shape to test.
     * @param tolerance the maximum distance value that is considered "near."
     *                  If the distance between this point and the shape is
     *                  greater than this value, it's not considered to
     *                  be "near" to the point.
     * @return if this point is "near" the shape, return true. Otherwise,
     * return false.
     */
    public boolean isPointNearShape(Shape<?> shape, double tolerance) {
        return isPointNearShape(this, shape, tolerance);
    }

    /**
     * Compare two {@code PointXY}s. This has a tolerance of 0.01: if
     * (0, 0) was compared to (0.005, 0), it would still say they're equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointXY) {
            PointXY point = (PointXY) obj;

            boolean sameX = Equals.soft(
                x,
                point.x(),
                Geometry.tolerancePointXY
            );
            boolean sameY = Equals.soft(
                y,
                point.y(),
                Geometry.tolerancePointXY
            );

            return sameX && sameY;
        }

        return super.equals(obj);
    }

    /**
     * Convert this {@code PointXY} into a string.
     *
     * <p>
     * The string takes the following format:
     * <code>
     * (x, y)
     * </code>
     * ... where X and Y are the X and Y values.
     * </p>
     */
    @Override
    public String toString() {
        return StringUtils.format(
            Geometry.formatPointXY,
            Rounding.fastRound(x),
            Rounding.fastRound(y)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(PointXY o) {
        double d1 = distance(PointXY.ZERO);
        double d2 = o.distance(PointXY.ZERO);

        return Double.compare(d1, d2);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public PointXY clone() {
        return new PointXY(x, y);
    }
}
