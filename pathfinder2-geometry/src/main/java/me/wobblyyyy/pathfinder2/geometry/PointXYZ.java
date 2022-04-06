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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.math.Rounding;
import me.wobblyyyy.pathfinder2.utils.ArrayUtils;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A 2d coordinate with an X value, a Y value, and a value "Z" representing
 * a heading angle. This class extends {@link PointXY} and adds an additional
 * component (Z).
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
     * A point with...
     *
     * <ul>
     *     <li>X: 0</li>
     *     <li>Y: 0</li>
     *     <li>Z: 0</li>
     * </ul>
     */
    public static final PointXYZ ZERO = new PointXYZ(0, 0, 0);
    public static long COUNT = 0;
    /**
     * The point's angle, or heading, or whatever you want to call it.
     */
    private final Angle z;

    /**
     * Create a new {@code PointXYZ} with 0 for all of the values.j
     */
    public PointXYZ() {
        this(0, 0, 0);
    }

    /**
     * Create a new {@code PointXYZ} instance.
     *
     * @param point a {@code PointXY} to base the new {@code PointXYZ} off
     *              of. See: {@link #withHeading(Angle)}
     * @param z     the heading of the new {@code PointXYZ}.
     */
    public PointXYZ(PointXY point, Angle z) {
        this(point.x(), point.y(), z);
    }

    /**
     * Create a new {@code PointXYZ} instance.
     *
     * @param x the X coordinate of the point.
     * @param y the Y coordinate of the point.
     * @param z the point's heading, angle, Z - whatever you'd like to call it.
     */
    public PointXYZ(double x, double y, Angle z) {
        super(x, y);
        COUNT++;

        if (z == null) throw new NullPointerException(
            "Cannot have a null Z value!"
        );

        this.z = z;
    }

    /**
     * Create a new {@code PointXYZ} instance.
     *
     * @param x        the X coordinate of the point.
     * @param y        the Y coordinate of the point.
     * @param zDegrees the point's heading, IN DEGREES.
     */
    public PointXYZ(double x, double y, double zDegrees) {
        this(x, y, Angle.fixedDeg(zDegrees));
    }

    /**
     * Create a new {@code PointXYZ} instance.
     *
     * @param x the X coordinate of the point.
     * @param y the Y coordinate of the point.
     */
    public PointXYZ(double x, double y) {
        this(x, y, 0);
    }

    /**
     * Create a new {@code PointXYZ} instance by copying an existing
     * {@code PointXYZ}.
     *
     * @param point the point to copy.
     */
    public PointXYZ(PointXYZ point) {
        this(point.x(), point.y(), point.z);
    }

    public static PointXYZ parse(String string) {
        Logger.debug(PointXYZ.class, "parsing input <%s>", string);

        if (string.length() == 0) return ZERO;
        if (StringUtils.count(string, ',') == 1) {
            return PointXY.parse(string).withZ(Angle.DEG_0);
        }
        char[] chars = string.toCharArray();
        List<Double> list = new ArrayList<>(3);
        StringBuilder builder = new StringBuilder(string.length());

        Angle.AngleUnit unit;
        if (StringUtils.includesIgnoreCase(string, "deg")) {
            unit = Angle.AngleUnit.DEGREES;
        } else if (StringUtils.includesIgnoreCase(string, "rad")) {
            unit = Angle.AngleUnit.RADIANS;
        } else if (StringUtils.includesIgnoreCase(string, "r")) {
            unit = Angle.AngleUnit.RADIANS;
        } else if (StringUtils.includesIgnoreCase(string, "d")) {
            unit = Angle.AngleUnit.DEGREES;
        } else {
            unit = Angle.AngleUnit.DEGREES;
        }

        for (char c : chars) switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
            case '-':
                builder.append(c);
                break;
            case ',':
                if (builder.length() == 0) continue;
                list.add(Double.parseDouble(builder.toString()));
                builder.setLength(0);
                break;
            default:
                continue;
        }

        if (builder.length() != 0) list.add(
            Double.parseDouble(builder.toString())
        );

        if (list.size() == 2) list.add(Double.valueOf(0));

        if (list.size() != 3) throw new IllegalArgumentException(
            StringUtils.format(
                "Could not parse PointXYZ for String '%s'! Expected 3 values " +
                "but got %s. Parsed values: <%s>",
                string,
                list.size(),
                list
            )
        );

        double x = list.get(0);
        double y = list.get(1);
        Angle z = Angle.angle(unit, list.get(2));

        Logger.debug(
            PointXYZ.class,
            "parse input: <%s> x: <%s> y: <%s> z: <%s>",
            string,
            x,
            y,
            z
        );

        return new PointXYZ(x, y, z);
    }

    /**
     * Create a new {@code PointXYZ} and convert the provided values from
     * meters to inches.
     *
     * @param xMeters the X value.
     * @param yMeters the Y value.
     * @param angle   the angle the point should have.
     * @return a new {@code PointXYZ}.
     */
    public static PointXYZ fromMetersToInches(
        double xMeters,
        double yMeters,
        Angle angle
    ) {
        return new PointXYZ(
            xMeters * Geometry.METERS_TO_INCHES,
            yMeters * Geometry.METERS_TO_INCHES,
            angle
        );
    }

    /**
     * Create a new {@code PointXYZ} and convert the provided values from
     * inches to meters.
     *
     * @param xInches the X value.
     * @param yInches the Y value.
     * @param angle   the angle the point should have.
     * @return a new {@code PointXYZ}.
     */
    public static PointXYZ fromInchesToMeters(
        double xInches,
        double yInches,
        Angle angle
    ) {
        return new PointXYZ(
            xInches * Geometry.INCHES_TO_METERS,
            yInches * Geometry.INCHES_TO_METERS,
            angle
        );
    }

    /**
     * Create a new {@code PointXYZ} and convert the provided values from
     * meters to centimeters.
     *
     * @param xMeters the X value.
     * @param yMeters the Y value.
     * @param angle   the angle the point should have.
     * @return a new {@code PointXYZ}.
     */
    public static PointXYZ fromMetersToCentimeters(
        double xMeters,
        double yMeters,
        Angle angle
    ) {
        return new PointXYZ(xMeters * 100, yMeters * 100, angle);
    }

    /**
     * Create a new {@code PointXYZ} and convert the provided values from
     * centimeters to inches.
     *
     * @param xCentimeters the X value.
     * @param yCentimeters the Y value.
     * @param angle        the angle the point should have.
     * @return a new {@code PointXYZ}.
     */
    public static PointXYZ fromCentimetersToInches(
        double xCentimeters,
        double yCentimeters,
        Angle angle
    ) {
        return new PointXYZ(
            xCentimeters * Geometry.CENTIMETERS_TO_INCHES,
            yCentimeters * Geometry.CENTIMETERS_TO_INCHES,
            angle
        );
    }

    /**
     * Create a new {@code PointXYZ} and convert the provided values from
     * inches to centimeters.
     *
     * @param xInches the X value.
     * @param yInches the Y value.
     * @param angle   the angle the point should have.
     * @return a new {@code PointXYZ}.
     */
    public static PointXYZ fromInchesToCentimeters(
        double xInches,
        double yInches,
        Angle angle
    ) {
        return new PointXYZ(
            xInches * Geometry.INCHES_TO_CENTIMETERS,
            yInches * Geometry.INCHES_TO_CENTIMETERS,
            angle
        );
    }

    /**
     * Add two points together.
     *
     * @param a one of the two points to add.
     * @param b one of the two points to add.
     * @return the sum of the two points.
     */
    public static PointXYZ add(PointXYZ a, PointXYZ b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

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
    public static PointXYZ multiply(PointXYZ a, PointXYZ b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

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
    public static PointXYZ multiply(PointXYZ a, double b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new PointXYZ(a.x() * b, a.y() * b, Angle.multiply(a.z(), b));
    }

    /**
     * Get the average between two points.
     *
     * @param a one of the two points to average.
     * @param b one of the two points to average.
     * @return the average of the two provided points.
     */
    public static PointXYZ avg(PointXYZ a, PointXYZ b) {
        return multiply(add(a, b), 0.5);
    }

    /**
     * Create a new {@code PointXYZ} with 0 values for everything.
     *
     * @return a new {@code PointXYZ} instance with an X value of 0, a Y
     * value of 0, and a Z value of 0 degrees.
     */
    @SuppressWarnings("SameReturnValue")
    public static PointXYZ zero() {
        return PointXYZ.ZERO;
    }

    /**
     * Are two points close to each other?
     *
     * @param a              one of the two points.
     * @param b              one of the two points.
     * @param tolerance      the maximum distance value.
     * @param angleTolerance the maximum angle delta distance value.
     * @return if the `distance(a, b)` method call returns a value less than
     * or equal to the {@code tolerance}, this method will return true.
     * Else, this method will return false. Additionally, the minimum delta
     * between the two angles must be less than {@code angleTolerance}.
     */
    public static boolean isNear(
        PointXYZ a,
        PointXYZ b,
        double tolerance,
        Angle angleTolerance
    ) {
        if (!PointXY.isNear(a, b, tolerance)) return false;

        return Equals.soft(a.z, b.z, angleTolerance);
    }

    /**
     * Create a new {@code PointXYZ} a given distance away from a base point
     * in a specified direction.
     *
     * <p>
     * As an example... if you have a point at (0, 0, 0) and were to do
     * {@code inDirection} with a distance of 1 and an angle of 0 degrees,
     * you'd get (1, 0, 0) as your output.
     * </p>
     *
     * @param base     the base/origin point.
     * @param distance how far away the new point should be from the origin.
     * @param angle    the angle at which the new point should be drawn.
     * @return a new point, {@code distance} away from {@code base} in
     * {@code direction}.
     */
    public static PointXYZ inDirection(
        PointXYZ base,
        double distance,
        Angle angle
    ) {
        ValidationUtils.validate(base, "base");
        ValidationUtils.validate(distance, "distance");
        ValidationUtils.validate(angle, "angle");

        return new PointXYZ(
            base.x() + (distance * angle.cos()),
            base.y() + (distance * angle.sin()),
            base.z()
        );
    }

    /**
     * Apply a translation to a specified point.
     *
     * @param base        the point to apply the translation to.
     * @param translation the translation to apply.
     * @return the translated point.
     */
    public static PointXYZ applyTranslation(
        PointXYZ base,
        Translation translation
    ) {
        ValidationUtils.validate(base, "base");
        ValidationUtils.validate(translation, "translation");

        double distance = translation.magnitude();
        Angle angle = translation.angle().add(base.z);

        return new PointXYZ(
            base.x() + (distance * angle.cos()),
            base.y() + (distance * angle.sin()),
            base.z().add(Angle.fromDeg(translation.vz()))
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
    public static PointXYZ rotate(PointXYZ point, PointXY center, Angle angle) {
        return PointXY.rotate(point, center, angle).withHeading(point.z());
    }

    /**
     * Rotate a {@code Collection} of points around a center point.
     *
     * @param points the points to rotate.
     * @param center the point to rotate all of the points around.
     * @param angle  how far to rotate the points.
     * @return a new {@code List} of rotated points.
     */
    public static List<PointXYZ> rotatePoints(
        Collection<PointXYZ> points,
        PointXY center,
        Angle angle
    ) {
        List<PointXYZ> rotated = new ArrayList<>(points.size());

        for (PointXYZ point : points) rotated.add(
            PointXYZ.rotate(point, center, angle)
        );

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
    public static PointXYZ[] rotatePoints(
        PointXYZ[] points,
        PointXY center,
        Angle angle
    ) {
        return ArrayUtils.toPointXYZArray(
            rotatePoints(Arrays.asList(points), center, angle)
        );
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
     * Get the average of a set of points.
     *
     * @param points the points to get the average of.
     * @return the average of all the points.
     */
    @SuppressWarnings("DuplicatedCode")
    public static PointXYZ average(List<PointXYZ> points) {
        double totalX = 0;
        double totalY = 0;
        double totalZ = 0;

        for (PointXYZ point : points) {
            totalX += point.x();
            totalY += point.y();
            totalZ += point.z().deg();
        }

        return new PointXYZ(
            totalX / points.size(),
            totalY / points.size(),
            Angle.fixedDeg(totalZ / points.size())
        );
    }

    /**
     * Get the average of a set of points.
     *
     * @param points the points to get the average of.
     * @return the average of all the points.
     */
    @SuppressWarnings("DuplicatedCode")
    public static PointXY average(PointXYZ... points) {
        double totalX = 0;
        double totalY = 0;
        double totalZ = 0;

        for (PointXYZ point : points) {
            totalX += point.x();
            totalY += point.y();
            totalZ += point.z().deg();
        }

        return new PointXYZ(
            totalX / points.length,
            totalY / points.length,
            Angle.fixedDeg(totalZ / points.length)
        );
    }

    /**
     * Reflect a set of points over a given axis.
     *
     * @param axis   the axis to reflect the points over.
     * @param points the points to reflect over the given axis.
     * @return the same list of points, but reflected over a given axis.
     */
    public static List<PointXYZ> reflectPointsOverX(
        double axis,
        List<PointXYZ> points
    ) {
        List<PointXYZ> reflectedPoints = new ArrayList<>();

        for (PointXYZ point : points) {
            reflectedPoints.add(point.reflectOverX(axis));
        }

        return reflectedPoints;
    }

    /**
     * Reflect a set of points over a given axis.
     *
     * @param axis   the axis to reflect the points over.
     * @param points the points to reflect over the given axis.
     * @return the same list of points, but reflected over a given axis.
     */
    public static List<PointXYZ> reflectPointsOverX(
        double axis,
        PointXYZ... points
    ) {
        List<PointXYZ> reflectedPoints = new ArrayList<>();

        for (PointXYZ point : points) {
            reflectedPoints.add(point.reflectOverX(axis));
        }

        return reflectedPoints;
    }

    /**
     * Reflect a set of points over a given axis.
     *
     * @param axis   the axis to reflect the points over.
     * @param points the points to reflect over the given axis.
     * @return the same list of points, but reflected over a given axis.
     */
    public static List<PointXYZ> reflectPointsOverY(
        double axis,
        List<PointXYZ> points
    ) {
        List<PointXYZ> reflectedPoints = new ArrayList<>();

        for (PointXYZ point : points) {
            reflectedPoints.add(point.reflectOverY(axis));
        }

        return reflectedPoints;
    }

    /**
     * Reflect a set of points over a given axis.
     *
     * @param axis   the axis to reflect the points over.
     * @param points the points to reflect over the given axis.
     * @return the same list of points, but reflected over a given axis.
     */
    public static List<PointXYZ> reflectPointsOverY(
        double axis,
        PointXYZ... points
    ) {
        List<PointXYZ> reflectedPoints = new ArrayList<>();

        for (PointXYZ point : points) {
            reflectedPoints.add(point.reflectOverY(axis));
        }

        return reflectedPoints;
    }

    /**
     * Reflect a set of points over a given axis.
     *
     * @param points the points to reflect over the given axis.
     * @return the same list of points, but reflected over a given axis.
     */
    public static List<PointXYZ> reflectHeadings(List<PointXYZ> points) {
        List<PointXYZ> reflectedPoints = new ArrayList<>();

        for (PointXYZ point : points) {
            reflectedPoints.add(point.reflectHeading());
        }

        return reflectedPoints;
    }

    /**
     * Reflect a set of points over a given axis.
     *
     * @param points the points to reflect over the given axis.
     * @return the same list of points, but reflected over a given axis.
     */
    public static List<PointXYZ> reflectHeadings(PointXYZ... points) {
        List<PointXYZ> reflectedPoints = new ArrayList<>();

        for (PointXYZ point : points) {
            reflectedPoints.add(point.reflectHeading());
        }

        return reflectedPoints;
    }

    private static double reflect(double value, double axis) {
        return axis - (value - axis);
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
     * Add an X value to this point and return it.
     *
     * @param x the value to add.
     * @return the new point.
     */
    public PointXYZ addX(double x) {
        return new PointXYZ(x() + x, y(), z);
    }

    /**
     * Add an Y value to this point and return it.
     *
     * @param y the value to add.
     * @return the new point.
     */
    public PointXYZ addY(double y) {
        return new PointXYZ(x(), y() + y, z);
    }

    /**
     * Add a Z value to this point and return it.
     *
     * @param zDegrees the value to add.
     * @return the new point.
     */
    public PointXYZ addZ(double zDegrees) {
        return new PointXYZ(x(), y(), z.rotateDeg(zDegrees));
    }

    /**
     * Add a Z value to this point and return it.
     *
     * @param z the value to add.
     * @return the new point.
     */
    public PointXYZ addZ(Angle z) {
        return new PointXYZ(x(), y(), z.add(this.z));
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
     * Create a deep clone of this {@code PointXYZ}.
     *
     * @return a new {@code PointXYZ}.
     */
    public PointXYZ deepClone() {
        return new PointXYZ(x(), y(), z());
    }

    /**
     * Create a deep clone of this {@code PointXYZ} as a {@code PointXY}.
     *
     * @return a new {@code PointXY}.
     */
    public PointXY deepCloneAsPointXY() {
        return new PointXY(x(), y());
    }

    /**
     * Rotate this point around a center by a specified angle.
     *
     * @param center the center of rotation.
     * @param angle  how far to rotate this point.
     * @return the newly-rotated point.
     */
    public PointXYZ rotate(PointXY center, Angle angle) {
        return rotate(this, center, angle);
    }

    /**
     * Create a new point a specified distance away from this point.
     *
     * <p>
     * As an example... if you have a point at (0, 0, 0) and were to do
     * {@code inDirection} with a distance of 1 and an angle of 0 degrees,
     * you'd get (1, 0, 0) as your output.
     * </p>
     *
     * @param distance the distance the new point should be created at.
     * @param angle    the angle the new point should be created at.
     * @return a new point, {@code distance} away from this point, drawn
     * at {@code angle}.
     */
    @Override
    public PointXYZ inDirection(double distance, Angle angle) {
        return PointXYZ.inDirection(this, distance, angle);
    }

    /**
     * Apply a translation to {@code this} point.
     *
     * @param translation the translation to apply.
     * @return the translated point.
     */
    @Override
    public PointXYZ applyTranslation(Translation translation) {
        return PointXYZ.applyTranslation(this, translation);
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

    /**
     * Multiply the X value of {@code this} point by {@code multiplier} and
     * return a new point with the new value.
     *
     * @param multiplier the multiplier to apply.
     * @return the multiplied point.
     */
    public PointXYZ multiplyX(double multiplier) {
        return new PointXYZ(x() * multiplier, y(), z);
    }

    /**
     * Multiply the Y value of {@code this} point by {@code multiplier} and
     * return a new point with the new value.
     *
     * @param multiplier the multiplier to apply.
     * @return the multiplied point.
     */
    public PointXYZ multiplyY(double multiplier) {
        return new PointXYZ(x(), y() * multiplier, z);
    }

    /**
     * Multiply the Y value of {@code this} point by {@code multiplier} and
     * return a new point with the new value.
     *
     * @param multiplier the multiplier to apply.
     * @return the multiplied point.
     */
    public PointXYZ multiplyZ(double multiplier) {
        return new PointXYZ(x(), y(), z.multiply(multiplier));
    }

    @Override
    public String toString() {
        return StringUtils.format(
            Geometry.formatPointXYZ,
            StringUtils.roundAndFormatNumber(x()),
            StringUtils.roundAndFormatNumber(y()),
            StringUtils.roundAndFormatNumber(z.deg())
        );
    }

    /**
     * Check to see if an {@link Object} is equal to {@code this}.
     *
     * <p>
     * If the provided {@link Object} is an instance of {@link PointXYZ}, this
     * method will check if the points have the same X and Y values, as well
     * as if the points have angles that are equal, as determined by
     * {@link Angle#equals(Angle, Angle)}.
     * </p>
     *
     * <p>
     * If the provided {@link Object} is an instance of {@link PointXY}, this
     * method will check if the points have the same X and Y values, and if
     * the angle of {@code this} point is 0.
     * </p>
     *
     * @param obj the object to check.
     * @return true if the objects are "equal." Otherwise, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointXYZ) {
            PointXYZ p = (PointXYZ) obj;

            boolean sameX = Equals.soft(
                p.x(),
                this.x(),
                Geometry.tolerancePointXYZ
            );
            boolean sameY = Equals.soft(
                p.y(),
                this.y(),
                Geometry.tolerancePointXYZ
            );
            boolean sameZ = Angle.equals(p.z, this.z);

            return sameX && sameY && sameZ;
        } else if (obj instanceof PointXY) {
            PointXY p = (PointXY) obj;

            boolean sameX = Equals.soft(
                p.x(),
                this.x(),
                Geometry.tolerancePointXY
            );
            boolean sameY = Equals.soft(
                p.y(),
                this.y(),
                Geometry.tolerancePointXY
            );
            boolean zeroZ = Equals.soft(
                0,
                this.z().deg(),
                Geometry.tolerancePointXY
            );

            return sameX && sameY && zeroZ;
        }

        return false;
    }

    /**
     * Reflect a point over a given axis.
     *
     * @param xAxis the axis to reflect the point over.
     * @return the reflected point.
     */
    public PointXYZ reflectOverX(double xAxis) {
        return withX(reflect(x(), xAxis));
    }

    /**
     * Reflect a point over a given axis.
     *
     * @param yAxis the axis to reflect the point over.
     * @return the reflected point.
     */
    public PointXYZ reflectOverY(double yAxis) {
        return withY(reflect(y(), yAxis));
    }

    /**
     * Is {@code this} point close to another point?
     *
     * @param a              the point.
     * @param tolerance      the maximum distance value.
     * @param angleTolerance the maximum angle delta distance value.
     * @return if the `distance(a, b)` method call returns a value less than
     * or equal to the {@code tolerance}, this method will return true.
     * Else, this method will return false. Additionally, the minimum delta
     * between the two angles must be less than {@code angleTolerance}.
     */
    public boolean isNear(PointXYZ a, double tolerance, Angle angleTolerance) {
        return isNear(this, a, tolerance, angleTolerance);
    }

    /**
     * Reflect a point over a given axis.
     *
     * @return the reflected point.
     */
    public PointXYZ reflectHeading() {
        return withHeading(z.fixedFlip());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public PointXYZ clone() {
        return new PointXYZ(x(), y(), z);
    }
}
