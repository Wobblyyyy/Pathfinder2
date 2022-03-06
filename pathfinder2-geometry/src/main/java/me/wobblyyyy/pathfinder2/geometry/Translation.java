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
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.math.Rounding;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A two-dimensional translation. Robots should be capable of receiving
 * these translations and moving appropriately. Translations have no units
 * and are completely relative. {@code Translation}s are mostly used internally
 * by Pathfinder to control the robot's movement.
 *
 * <p>
 * Translations are defined by three components.
 * <ul>
 *     <li>X translation (translation along the X axis)</li>
 *     <li>Y translation (translation along the Y axis)</li>
 *     <li>Z translation (rotation around the center of the robot)</li>
 * </ul>
 * Please note, however, that these X and Y axes are relative to the robot.
 * A translation with a Y value of 1 would make the robot go "forwards,"
 * forwards, of course, meaning whatever direction the robot "thinks"
 * is forwards - not absolute positioning.
 * </p>
 *
 * <p>
 * In almost all Pathfinder-specific use cases, {@code Translation}s will
 * have translational values under 1.0: vx, vy, and vz should all be under
 * 1.0. On occasion, translational values will be greater than 1.0, but it's
 * uncommon, and Pathfinder's internals are all based on 1.0-max translations.
 * </p>
 *
 * <p>
 * Absolute translations can be converted to relative translations using the
 * following methods:
 * <ul>
 *     <li>
 *         {@link #absoluteToRelative(Translation, Angle)}:
 *         Convert an absolute translation into a relative translation
 *         by using the robot's current heading.
 *     </li>
 *     <li>
 *         {@link #toRelative(Angle)}:
 *         Convert the calling translation into a relative translation
 *         by using the robot's current heading.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * {@code Translation}s are the core of Pathfinder's movement. Robots operate
 * based exclusively on {@code Translation} instances. Such, translations
 * aren't confined to being used in autonomous navigation. If you'd like
 * to operate the robot, say, in a manual control mode, you can utilize
 * translations (and the {@link #absoluteToRelative(Translation, Angle)} method)
 * to do exactly that.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
@SuppressWarnings("SameReturnValue")
public class Translation implements Serializable {
    /**
     * A translation with values of (0, 0, 0).
     */
    public static final Translation ZERO = new Translation(0, 0, 0);

    /**
     * A translation with values of (0, 1, 0).
     */
    public static final Translation FORWARDS = new Translation(0, 1, 0);

    /**
     * A translation with values of (1, 0, 0).
     */
    public static final Translation RIGHTWARDS = new Translation(1, 0, 0);

    /**
     * A translation with values of (0, -1, 0).
     */
    public static final Translation BACKWARDS = new Translation(0, -1, 0);

    /**
     * A translation with values of (-1, 0, 0).
     */
    public static final Translation LEFTWARDS = new Translation(-1, 0, 0);

    /**
     * The robot's translation along its X axis.
     */
    private final double vx;

    /**
     * The robot's translation along its Y axis.
     */
    private final double vy;

    /**
     * The robot's rotation around its center of rotation.
     */
    private final double vz;

    /**
     * Create a new {@code Translation} using two translational values. This
     * translation will have a {@code vz} value (rotation) of 0.
     *
     * @param vx the robot's translation along its X axis. This value should
     *           not have any units, but should be scaled the same as vy.
     * @param vy the robot's translation along its Y axis. This value should
     *           not have any units, but should be scaled the same as vx.
     */
    public Translation(double vx, double vy) {
        this(vx, vy, 0);
    }

    /**
     * Create a new {@code Translation} using three translational values.
     *
     * @param vx the robot's translation along its X axis. This value should
     *           not have any units, but should be scaled the same as vy.
     * @param vy the robot's translation along its Y axis. This value should
     *           not have any units, but should be scaled the same as vx.
     * @param vz the robot's rotation around its center of rotation.
     */
    public Translation(double vx, double vy, double vz) {
        ValidationUtils.validate(vx, "vx");
        ValidationUtils.validate(vy, "vx");
        ValidationUtils.validate(vz, "vx");

        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    /**
     * Create a new {@code Translation} by copying an existing
     * {@code Translation}.
     *
     * @param translation the translation to copy. The new translation will
     *                    have the same vx, vy, and vz values as
     *                    this translation.
     */
    public Translation(Translation translation) {
        this(translation.vx, translation.vy, translation.vz);
    }

    /**
     * Create a new {@code Translation} by using the X and Y values of the
     * provided point. The point will have a vz value of 0.
     *
     * @param point the point to create a {@code Translation} based on.
     */
    public static Translation fromPointXY(PointXY point) {
        return new Translation(point.x(), point.y());
    }

    /**
     * Create a new {@code Translation} by using the X and Y values of the
     * provided point.
     *
     * @param point the point to create a {@code Translation} based on.
     * @param vz    the translation's vz value.
     */
    public static Translation fromPointXY(PointXY point, double vz) {
        return new Translation(point.x(), point.y(), vz);
    }

    /**
     * Create a new {@code Translation} by using the X and Y values of the
     * provided point. The point will have a vz value of the degrees value
     * of the point.
     *
     * @param point the point to create a {@code Translation} based on.
     * @return a new {@code Translation}.
     */
    public static Translation fromPointXYZ(PointXYZ point) {
        return new Translation(point.x(), point.y(), point.z().deg());
    }

    /**
     * Convert an absolute translation into a relative translation.
     *
     * <p>
     * You may be more familiar with this concept if we use the term
     * "field-relative" and "robot-relative". This method converts a field
     * relative translation into a robot relative one.
     * </p>
     *
     * @param translation the original (absolute) {@code Translation}.
     * @param heading     the heading the robot is currently facing. This value
     *                    should almost always come directly from the robot's
     *                    odometry system.
     * @return a relative translation.
     * @see <a href="https://pdocs.kauailabs.com/navx-mxp/examples/field-oriented-drive/">Field-oriented drive</a>
     */
    public static Translation absoluteToRelative(
        Translation absoluteTranslation,
        Angle heading
    ) {
        ValidationUtils.validate(absoluteTranslation, "absoluteTranslation");
        ValidationUtils.validate(heading, "heading");

        PointXY point = absoluteTranslation
            .point()
            .rotate(PointXY.ZERO, heading.multiply(-1));
        Translation relativeTranslation = fromPointXY(
            point,
            absoluteTranslation.vz()
        );

        Logger.trace(
            Translation.class,
            "Converted absolute %s to relative %s (heading: %s)",
            absoluteTranslation,
            relativeTranslation,
            heading
        );

        return relativeTranslation;
    }

    /**
     * Create a new {@code Translation} with the following values:
     *
     * <ul>
     *     <li>vX: 0</li>
     *     <li>vY: 0</li>
     *     <li>vZ: 0</li>
     * </ul>
     *
     * @return a new translation with vX, vY, and vZ values of 0.
     */
    public static Translation zero() {
        return ZERO;
    }

    /**
     * Add two translations together.
     *
     * @param a one of the translations.
     * @param b one of the translations.
     * @return the sum of the two translations.
     */
    public static Translation add(Translation a, Translation b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new Translation(
            a.vx() + b.vx(),
            a.vy() + b.vy(),
            a.vz() + b.vz()
        );
    }

    /**
     * Multiply two translations together.
     *
     * @param a one of the translations.
     * @param b one of the translations.
     * @return the product of the two translations.
     */
    public static Translation multiply(Translation a, Translation b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new Translation(
            a.vx() * b.vx(),
            a.vy() * b.vy(),
            a.vz() * b.vz()
        );
    }

    /**
     * Multiply a translation by a value.
     *
     * @param a one of the translations.
     * @param b the value to multiply each of the translation's component
     *          values.
     * @return the product of the translation and the multiplier.
     */
    public static Translation multiply(Translation a, double b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new Translation(a.vx() * b, a.vy() * b, a.vz() * b);
    }

    /**
     * Multiply each of the values of a translation by a value.
     *
     * @param a           the initial translation.
     * @param xMultiplier vx multiplier.
     * @param yMultiplier vy multiplier.
     * @param zMultiplier vz multiplier.
     * @return a new {@code Translation}.
     */
    public static Translation multiply(
        Translation a,
        double xMultiplier,
        double yMultiplier,
        double zMultiplier
    ) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(xMultiplier, "xMultiplier");
        ValidationUtils.validate(yMultiplier, "yMultiplier");
        ValidationUtils.validate(zMultiplier, "zMultiplier");

        return new Translation(
            a.vx() * xMultiplier,
            a.vy() * yMultiplier,
            a.vz() * zMultiplier
        );
    }

    /**
     * Subtract translation B from translation A.
     *
     * @param a the first of the two translations.
     * @param b the second of the two translations.
     * @return the remainder of the two translations.
     */
    public static Translation subtract(Translation a, Translation b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return add(a, multiply(b, -1));
    }

    /**
     * Divide a translation by another translation.
     *
     * @param a the numerator.
     * @param b the denominator.
     * @return the quotient of the two translations.
     */
    public static Translation divide(Translation a, Translation b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new Translation(
            a.vx() / b.vx(),
            a.vy() / b.vy(),
            a.vz() / b.vz()
        );
    }

    /**
     * Divide a translation by a number.
     *
     * @param a the numerator.
     * @param b the denominator.
     * @return the quotient.
     */
    public static Translation divide(Translation a, double b) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");

        return new Translation(a.vx() / b, a.vy() / b, a.vz() / b);
    }

    /**
     * Get the translation's X component.
     *
     * @return the translation's X component.
     */
    public double vx() {
        return this.vx;
    }

    /**
     * Get the translation's Y component.
     *
     * @return the translation's Y component.
     */
    public double vy() {
        return this.vy;
    }

    /**
     * Get the translation's Z component.
     *
     * @return the translation's Z component.
     */
    public double vz() {
        return this.vz;
    }

    /**
     * Get a certain axis.
     *
     * @param axis the axis to get.
     * @return that axis' value.
     */
    public double getAxis(Axis axis) {
        switch (axis) {
            case X:
                return vx();
            case Y:
                return vy();
            case Z:
                return vz();
            default:
                throw new RuntimeException();
        }
    }

    /**
     * Create a new translation with a new axis for a certain value.
     *
     * @param axis  the axis.
     * @param value the axis' value.
     * @return a new {@code Translation}.
     */
    public Translation withAxis(Axis axis, double value) {
        switch (axis) {
            case X:
                return withVx(value);
            case Y:
                return withVy(value);
            case Z:
                return withVz(value);
            default:
                throw new RuntimeException();
        }
    }

    /**
     * Get the translation's magnitude.
     *
     * <p>
     * This value is calculated by using the {@link Math#hypot(double, double)}
     * method. It assumes the {@code vx} and {@code vy} values are legs in
     * a right triangle and calculates a theoretical hypotenuse based on that.
     * This is the distance from the origin, or the magnitude at which
     * the translation operates.
     * </p>
     *
     * @return the {@code Translation}'s magnitude, or total distance from
     * zero. This method uses the {@link Math#hypot(double, double)} method.
     */
    public double magnitude() {
        return Math.hypot(this.vx, this.vy);
    }

    /**
     * Create a point using the component vx and vy translation values.
     *
     * @return a point located at ({@link #vx()}, {@link #vy()}).
     */
    public PointXY point() {
        return new PointXY(this.vx, this.vy);
    }

    /**
     * Create an imaginary point at (vx, vy) and determine the angle from
     * an origin (0, 0) to that point.
     *
     * @return the angle between an imaginary point and an imaginary origin.
     * That doesn't make sense, does it? No. Probably not.
     */
    public Angle angle() {
        return PointXY.zero().angleTo(point());
    }

    /**
     * Convert the calling translation into a relative translation.
     *
     * @param heading the heading of the robot. This should come directly
     *                from the odometry system the robot uses.
     * @return a relative translation generated by converting the calling
     * translation into a relative translation. Swag.
     * @see #absoluteToRelative(Translation, Angle)
     */
    public Translation toRelative(Angle heading) {
        return absoluteToRelative(this, heading);
    }

    /**
     * Create a new {@code Translation} with the provided vx value.
     *
     * @param vx the new value to assign to the translation.
     * @return a new {@code Translation}.
     */
    public Translation withVx(double vx) {
        return new Translation(vx, this.vy, this.vz);
    }

    /**
     * Create a new {@code Translation} with the provided vy value.
     *
     * @param vy the new value to assign to the translation.
     * @return a new {@code Translation}.
     */
    public Translation withVy(double vy) {
        return new Translation(this.vx, vy, this.vz);
    }

    /**
     * Create a new {@code Translation} with the provided vz value.
     *
     * @param vz the new value to assign to the translation.
     * @return a new {@code Translation}.
     */
    public Translation withVz(double vz) {
        return new Translation(this.vx, this.vy, vz);
    }

    /**
     * Add another translation to this translation.
     *
     * @param a the translation to add.
     * @return the sum of the two translations.
     */
    public Translation add(Translation a) {
        return add(this, a);
    }

    /**
     * Multiply this translation by another translation.
     *
     * @param a the translation to multiply.
     * @return the product of the two translations.
     */
    public Translation multiply(Translation a) {
        return multiply(this, a);
    }

    /**
     * Multiply this translation by a number.
     *
     * @param a the value to multiply this translation by.
     * @return the product of this translation and the multiplier.
     */
    public Translation multiply(double a) {
        return multiply(this, a);
    }

    /**
     * Multiply this translation.
     *
     * @param xMultiplier vx multiplier.
     * @param yMultiplier vy multiplier.
     * @param zMultiplier vz multiplier.
     * @return a new {@code Translation}.
     */
    public Translation multiply(
        double xMultiplier,
        double yMultiplier,
        double zMultiplier
    ) {
        return multiply(this, xMultiplier, yMultiplier, zMultiplier);
    }

    /**
     * Subtract a translation from this translation.
     *
     * @param a the translation to subtract.
     * @return the remainder of the two translations.
     */
    public Translation subtract(Translation a) {
        return subtract(this, a);
    }

    /**
     * Divide this translation by another translation.
     *
     * @param a the translation to divide by.
     * @return the quotient of the two translations.
     */
    public Translation divide(Translation a) {
        return divide(this, a);
    }

    /**
     * Divide this translation by a value.
     *
     * @param a the value to divide by.
     * @return the quotient.
     */
    public Translation divide(double a) {
        return divide(this, a);
    }

    @Override
    public String toString() {
        return StringUtils.format(
            Geometry.formatTranslation,
            Rounding.fastRound(vx),
            Rounding.fastRound(vy),
            Rounding.fastRound(vz)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Translation) {
            Translation t = (Translation) obj;

            boolean sameVx = Equals.soft(
                t.vx,
                this.vx,
                Geometry.toleranceTranslation
            );
            boolean sameVy = Equals.soft(
                t.vy,
                this.vy,
                Geometry.toleranceTranslation
            );
            boolean sameVz = Equals.soft(
                t.vz,
                this.vz,
                Geometry.toleranceTranslation
            );

            return sameVx && sameVy && sameVz;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int x = (int) (vx * 1_000_000);
        int y = (int) (vy * 100_100);
        int z = (int) (vz * 100);

        return x + y + z;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Translation clone() {
        return new Translation(vx, vy, vz);
    }

    /**
     * Different axes for a translation.
     */
    public enum Axis {
        /**
         * The vx axis.
         */
        X,

        /**
         * The vy axis.
         */
        Y,

        /**
         * The vz axis.
         */
        Z,
    }
}
