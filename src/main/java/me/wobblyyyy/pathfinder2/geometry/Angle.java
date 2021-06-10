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
 * An angle, represented both in degrees and radians.
 *
 * <p>
 * This class was created primarily to simplify operations related to angles.
 * Much of the trigonometry previously delegated to {@link Math} has been
 * made available, such as...
 * <ul>
 *     <li>{@link #sin()}</li>
 *     <li>{@link #cos()}</li>
 *     <li>{@link #tan()}</li>
 *     <li>{@link #csc()}</li>
 *     <li>{@link #sec()}</li>
 *     <li>{@link #cot()}</li>
 *     <li>{@link #asin(double)}</li>
 *     <li>{@link #acos(double)}</li>
 *     <li>{@link #atan(double)}</li>
 *     <li>{@link #atan2(double, double)}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Additionally, because {@code Angle} objects store both degrees and radians
 * measures of the angle, you can use whichever you'd prefer.
 * <ul>
 *     <li>{@link #deg()} - angle in degrees</li>
 *     <li>{@link #rad()} - angle in radians</li>
 * </ul>
 * </p>
 *
 * <p>
 * Finally, one of the key features of the {@code Angle} class is the ability
 * to create so-called "fixed" angles. A fixed angle is an angle that fits
 * within some certain bounds. Both the degree and radian representations of
 * the angle will fit within the bounds 0-360 and 0-2pi respectively. To access
 * these fixed angles, see...
 * <ul>
 *     <li>{@link #fixDeg(double)} - fix degrees measurement</li>
 *     <li>{@link #fixRad(double)} - fix radians measurement</li>
 *     <li>{@link #fixed(AngleUnit, double)} - create fixed angle</li>
 *     <li>{@link #fixedDeg(double)} - create fixed angle from degrees</li>
 *     <li>{@link #fixedRad(double)} - create fixed angle from radians</li>
 *     <li>{@link #fixedFlip()} - add 180 degrees to the current angle</li>
 *     <li>{@link #fixedRotateDeg(Angle, double)}</li>
 *     <li>{@link #fixedRotateRad(Angle, double)}</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Angle implements Comparable<Angle>, Serializable {
    /**
     * "rad"
     */
    public static final String FORMAT_RAD_SHORT = "rad";

    /**
     * "deg"
     */
    public static final String FORMAT_DEG_SHORT = "deg";

    /**
     * "radians"
     */
    public static final String FORMAT_RAD_LONG = "radians";

    /**
     * "degrees"
     */
    public static final String FORMAT_DEG_LONG = "degrees";

    /**
     * Minimum radians - 0
     */
    public static final double MIN_RAD = 0;

    /**
     * Maximum radians - 6.28318531
     */
    public static final double MAX_RAD = 6.28318531;

    /**
     * Minimum degrees - 0
     */
    public static final double MIN_DEG = 0;

    /**
     * Maximum degrees - 360
     */
    public static final double MAX_DEG = 360;

    /**
     * {@link #fromDeg(double)} with an angle of 0
     */
    public static final Angle DEG_0 = Angle.fromDeg(0);

    /**
     * {@link #fromDeg(double)} with an angle of 45
     */
    public static final Angle DEG_45 = Angle.fromDeg(45);

    /**
     * {@link #fromDeg(double)} with an angle of 90
     */
    public static final Angle DEG_90 = Angle.fromDeg(90);

    /**
     * {@link #fromDeg(double)} with an angle of 135
     */
    public static final Angle DEG_135 = Angle.fromDeg(135);

    /**
     * {@link #fromDeg(double)} with an angle of 180
     */
    public static final Angle DEG_180 = Angle.fromDeg(180);

    /**
     * {@link #fromDeg(double)} with an angle of 225
     */
    public static final Angle DEG_225 = Angle.fromDeg(225);

    /**
     * {@link #fromDeg(double)} with an angle of 270
     */
    public static final Angle DEG_270 = Angle.fromDeg(270);

    /**
     * {@link #fromDeg(double)} with an angle of 315
     */
    public static final Angle DEG_315 = Angle.fromDeg(315);

    /**
     * {@link #fromDeg(double)} with an angle of 360
     */
    public static final Angle DEG_360 = Angle.fromDeg(360);

    /**
     * {@link #fromRad(double)} with an angle of 45 deg
     */
    public static final Angle PI_OVER_4 = Angle.fromDeg(45);

    /**
     * {@link #fromRad(double)} with an angle of 90 deg
     */
    public static final Angle PI_OVER_2 = Angle.fromDeg(90);

    /**
     * {@link #fromRad(double)} with an angle of 135 deg
     */
    public static final Angle THREE_PI_OVER_4 = Angle.fromDeg(135);

    /**
     * {@link #fromRad(double)} with an angle of 180 deg
     */
    public static final Angle PI = Angle.fromDeg(180);

    /**
     * {@link #fromRad(double)} with an angle of 225 deg
     */
    public static final Angle FIVE_PI_OVER_4 = Angle.fromDeg(225);

    /**
     * {@link #fromRad(double)} with an angle of 270 deg
     */
    public static final Angle THREE_PI_OVER_2 = Angle.fromDeg(270);

    /**
     * {@link #fromRad(double)} with an angle of 315 deg
     */
    public static final Angle SEVEN_PI_OVER_4 = Angle.fromDeg(315);

    /**
     * {@link #fromRad(double)} with an angle of 360 deg
     */
    public static final Angle TWO_PI = Angle.fromDeg(360);

    /**
     * Angle stored in radians.
     */
    private final double rad;

    /**
     * Angle stored in degrees.
     */
    private final double deg;

    /**
     * Private constructor - angles can only be created with static methods.
     * We do this so we don't have to convert degrees to radians and so
     * on and so forth over and over again.
     *
     * @param rad radians value.
     * @param deg degrees value.
     */
    private Angle(double rad,
                  double deg) {
        this.rad = rad;
        this.deg = deg;
    }

    /**
     * Ensure a radian value fits within 0-2pi.
     *
     * @param rad the value to fix.
     * @return the value within the range of 0-2pi.
     */
    public static double fixRad(double rad) {
        while (rad < MIN_RAD) rad += MAX_RAD;
        while (rad > MAX_RAD) rad -= MAX_RAD;
        return rad;
    }

    /**
     * Ensure a radian value fits within 0-360.
     *
     * @param deg the value to fix.
     * @return the value within the range of 0-360.
     */
    public static double fixDeg(double deg) {
        while (deg < MIN_DEG) deg += MAX_DEG;
        while (deg > MAX_DEG) deg -= MAX_DEG;
        return deg;
    }

    /**
     * Create a new angle from a radians value.
     *
     * @param rad the value to create the angle based on.
     * @return a new angle.
     */
    public static Angle fromRad(double rad) {
        return new Angle(rad, Math.toDegrees(rad));
    }


    /**
     * Create a new angle from a degrees value.
     *
     * @param deg the value to create the angle based on.
     * @return a new angle.
     */
    public static Angle fromDeg(double deg) {
        return new Angle(Math.toRadians(deg), deg);
    }

    /**
     * Create a new angle from a radians value.
     *
     * @param rad the value to create the angle based on.
     * @return a new angle.
     * @see #fixRad(double)
     */
    public static Angle fixedRad(double rad) {
        return fromRad(fixRad(rad));
    }

    /**
     * Create a new angle from a degrees value.
     *
     * @param deg the value to create the angle based on.
     * @return a new angle.
     * @see #fixDeg(double)
     */
    public static Angle fixedDeg(double deg) {
        return fromDeg(fixDeg(deg));
    }

    /**
     * Create a new angle.
     *
     * @param unit  what unit the angle is given in.
     * @param value the value of the angle.
     * @return a new angle.
     */
    public static Angle angle(AngleUnit unit,
                              double value) {
        if (unit == AngleUnit.RADIANS) return fromRad(value);
        else return fromDeg(value);
    }

    /**
     * Create a new angle and use the {@link #fixDeg(double)} or
     * {@link #fixRad(double)} method on it.
     *
     * @param unit  the unit the angle is given in.
     * @param value the angle's value.
     * @return a new angle.
     */
    public static Angle fixed(AngleUnit unit,
                              double value) {
        return angle(
                unit,
                unit == AngleUnit.RADIANS ?
                        fixRad(value) :
                        fixDeg(value)
        );
    }

    /**
     * Add two angles together.
     *
     * @param a angle 1
     * @param b angle 2
     * @return angle 1 + angle 2
     */
    public static Angle add(Angle a,
                            Angle b) {
        return Angle.fromRad(a.rad() + b.rad());
    }

    /**
     * Subtract two angles.
     *
     * @param a angle 1
     * @param b angle 2
     * @return angle 1 - angle 2
     */
    public static Angle subtract(Angle a,
                                 Angle b) {
        return add(a, multiply(b, -1));
    }

    /**
     * Multiply two angles together.
     *
     * @param a angle 1
     * @param b angle 2
     * @return angle 1 * angle 2
     */
    public static Angle multiply(Angle a,
                                 Angle b) {
        return Angle.fromRad(a.rad() * b.rad());
    }

    /**
     * Multiply an angle.
     *
     * @param a angle
     * @param b multiplier
     * @return angle * multiplier
     */
    public static Angle multiply(Angle a,
                                 double b) {
        return Angle.fromRad(a.rad() * b);
    }

    /**
     * Create a new angle with the following values.
     *
     * <ul>
     *     <li>Degrees: {@code 0}</li>
     *     <li>Radians: {@code 0}</li>
     * </ul>
     *
     * @return zero angle.
     */
    public static Angle zero() {
        return new Angle(0, 0);
    }

    /**
     * Are two angles equal?
     *
     * @param a angle 1
     * @param b angle 2
     * @return angle 1 == angle 2
     */
    public static boolean equals(Angle a,
                                 Angle b) {
        return a.rad() == b.rad() || a.deg() == b.deg();
    }

    /**
     * Are two angles close in radians?
     *
     * @param a         angle 1
     * @param b         angle 2
     * @param tolerance max tolerance
     * @return if the two angles are close in radians
     */
    public static boolean isCloseRad(Angle a,
                                     Angle b,
                                     double tolerance) {
        return Math.abs(a.rad() - b.rad()) < tolerance;
    }

    /**
     * Are two angles close in degrees?
     *
     * @param a         angle 1
     * @param b         angle 2
     * @param tolerance max tolerance
     * @return if the two angles are close in degrees
     */
    public static boolean isCloseDeg(Angle a,
                                     Angle b,
                                     double tolerance) {
        return Math.abs(a.deg() - b.deg()) < tolerance;
    }

    /**
     * Create a new angle from the {@link Math#acos(double)} method.
     *
     * @param a the value to create the angle from.
     * @return a new angle.
     */
    public static Angle acos(double a) {
        return Angle.fromRad(Math.acos(a));
    }

    /**
     * Create a new angle from the {@link Math#asin(double)} method.
     *
     * @param a the value to create the angle from.
     * @return a new angle.
     */
    public static Angle asin(double a) {
        return Angle.fromRad(Math.asin(a));
    }

    /**
     * Create a new angle from the {@link Math#atan(double)} method.
     *
     * @param a the value to create the angle from.
     * @return a new angle.
     */
    public static Angle atan(double a) {
        return Angle.fromRad(Math.atan(a));
    }

    /**
     * Create a new angle from the {@link Math#atan2(double, double)} method.
     *
     * @param y y value
     * @param x x value
     * @return a new angle.
     */
    public static Angle atan2(double y, double x) {
        return Angle.fromRad(Math.atan2(y, x));
    }

    /**
     * Rotate an angle by a given amount of radians.
     *
     * @param a   angle to rotate
     * @param rad how much to rotate the angle by.
     * @return rotated angle.
     */
    public static Angle rotateRad(Angle a,
                                  double rad) {
        return Angle.fromRad(a.rad() + rad);
    }

    /**
     * Rotate an angle by a given amount of degrees.
     *
     * @param a   angle to rotate
     * @param deg how much to rotate the angle by.
     * @return rotated angle.
     */
    public static Angle rotateDeg(Angle a,
                                  double deg) {
        return Angle.fromDeg(a.deg() + deg);
    }

    /**
     * Rotate an angle by a given amount of radians and use the fix method.
     *
     * @param a   angle to rotate
     * @param rad how much to rotate the angle by.
     * @return rotated angle.
     */
    public static Angle fixedRotateRad(Angle a,
                                       double rad) {
        return Angle.fromRad(fixRad(a.rad() + rad));
    }

    /**
     * Rotate an angle by a given amount of degrees and use the fix method.
     *
     * @param a   angle to rotate
     * @param deg how much to rotate the angle by.
     * @return rotated angle.
     */
    public static Angle fixedRotateDeg(Angle a,
                                       double deg) {
        return Angle.fromDeg(fixDeg(a.deg() + deg));
    }

    /**
     * Flip an angle (add 180 degrees)
     *
     * @param a the angle to flip
     * @return the flipped angle
     */
    public static Angle flip(Angle a) {
        return rotateDeg(a, 180);
    }

    /**
     * Flip an angle and use the fix method.
     *
     * @param a the angle to flip.
     * @return the fixed flipped angle.
     */
    public static Angle fixedFlip(Angle a) {
        return fixedRotateDeg(a, 180);
    }

    /**
     * Convert rad to deg.
     *
     * @param rad input
     * @return output
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Convert deg to rad.
     *
     * @param deg input
     * @return output
     */
    public static double toRad(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Get the angle's value in radians.
     *
     * @return the angle's value in radians.
     */
    public double rad() {
        return this.rad;
    }

    /**
     * Get the angle's value in degrees.
     *
     * @return the angle's value in degrees.
     */
    public double deg() {
        return this.deg;
    }

    /**
     * Get the sine of the angle.
     *
     * @return the sine of the angle.
     */
    public double sin() {
        return Math.sin(rad);
    }

    /**
     * Get the cosine of the angle.
     *
     * @return the cosine of the angle.
     */
    public double cos() {
        return Math.cos(rad);
    }

    /**
     * Get the tangent of the angle.
     *
     * @return the tangent of the angle.
     */
    public double tan() {
        return Math.tan(rad);
    }

    /**
     * Get the cosecant of the angle.
     *
     * @return the cosecant of the angle.
     */
    public double csc() {
        return 1 / sin();
    }

    /**
     * Get the secant of the angle.
     *
     * @return the secant of the angle.
     */
    public double sec() {
        return 1 / cos();
    }

    /**
     * Get the cotangent of the angle.
     *
     * @return the cotangent of the angle.
     */
    public double cot() {
        return 1 / tan();
    }

    /**
     * Add an angle to this angle.
     *
     * @param a the angle to add.
     * @return added angles.
     */
    public Angle add(Angle a) {
        return add(this, a);
    }

    /**
     * Subtract an angle from this angle.
     *
     * @param a the angle to subtract.
     * @return this angle minus {@code a}.
     */
    public Angle subtract(Angle a) {
        return subtract(this, a);
    }

    /**
     * Multiply an angle by an angle.
     *
     * @param a the angle to multiply this angle by.
     * @return the multiplied angle.
     */
    public Angle multiply(Angle a) {
        return multiply(this, a);
    }

    /**
     * Multiply an angle by a number.
     *
     * @param a the value to multiply this angle by.
     * @return the multiplied angle.
     */
    public Angle multiply(double a) {
        return multiply(this, a);
    }

    /**
     * Does {@code a} equal this angle?
     *
     * @param a the angle to compare.
     * @return are the angles equal?
     */
    public boolean equals(Angle a) {
        return equals(this, a);
    }

    /**
     * Are two angles close, in radians?
     *
     * @see #isCloseRad(Angle, Angle, double)
     */
    public boolean isCloseRad(Angle a,
                              double tolerance) {
        return isCloseRad(this, a, tolerance);
    }

    /**
     * Are two angles close, in degrees?
     *
     * @see #isCloseDeg(Angle, Angle, double)
     */
    public boolean isCloseDeg(Angle a,
                              double tolerance) {
        return isCloseDeg(this, a, tolerance);
    }

    /**
     * @see #rotateRad(Angle, double)
     */
    public Angle rotateRad(double rad) {
        return rotateRad(this, rad);
    }

    /**
     * @see #rotateDeg(Angle, double)
     */
    public Angle rotateDeg(double deg) {
        return rotateDeg(this, deg);
    }

    /**
     * @see #fixedRotateRad(Angle, double)
     */
    public Angle fixedRotateRad(double rad) {
        return fixedRotateRad(this, rad);
    }

    /**
     * @see #fixedRotateDeg(Angle, double)
     */
    public Angle fixedRotateDeg(double deg) {
        return fixedRotateDeg(this, deg);
    }

    /**
     * @see #flip(Angle)
     */
    public Angle flip() {
        return flip(this);
    }

    /**
     * @see #fixedFlip(Angle)
     */
    public Angle fixedFlip() {
        return fixedFlip(this);
    }

    /**
     * Fix the current angle.
     *
     * <p>
     * If the current angle meets the following conditions:
     * <ul>
     *     <li>Radian measure is outside of the bounds 0-2pi.</li>
     *     <li>Degree measure is outside of the bounds 0-360.</li>
     * </ul>
     * ... the angle will be adjusted so that neither of those statements are
     * true anymore. Pretty cool, right?
     * </p>
     *
     * @return a newly fixed angle.
     */
    public Angle fix() {
        return Angle.fromDeg(fixDeg(deg));
    }

    /**
     * Rotate the angle by 45 degrees.
     *
     * @return the angle, but rotated by 45 degrees.
     */
    public Angle rotate45Deg() {
        return rotateDeg(45);
    }

    /**
     * Rotate the angle by 90 degrees.
     *
     * @return the angle, but rotated by 90 degrees.
     */
    public Angle rotate90Deg() {
        return rotateDeg(90);
    }

    /**
     * Rotate the angle by 180 degrees.
     *
     * @return the angle, but rotated by 180 degrees.
     */
    public Angle rotate180Deg() {
        return rotateDeg(180);
    }

    /**
     * Rotate the angle by 45 degrees and then call the fix method.
     *
     * @return the angle, rotated by 45 degrees.
     */
    public Angle fixedRotate45Deg() {
        return fixedRotateDeg(45);
    }

    /**
     * Rotate the angle by 90 degrees and then call the fix method.
     *
     * @return the angle, rotated by 90 degrees.
     */
    public Angle fixedRotate90Deg() {
        return fixedRotateDeg(90);
    }

    /**
     * Rotate the angle by 180 degrees and then call the fix method.
     *
     * @return the angle, rotated by 180 degrees.
     */
    public Angle fixedRotate180Deg() {
        return fixedRotateDeg(180);
    }

    /**
     * Format like so.
     *
     * @return "? rad"
     */
    public String formatAsRadShort() {
        return rad() + " " + FORMAT_RAD_SHORT;
    }

    /**
     * Format like so.
     *
     * @return "? deg"
     */
    public String formatAsDegShort() {
        return deg() + " " + FORMAT_DEG_SHORT;
    }

    /**
     * Format like so.
     *
     * @return "? radians"
     */
    public String formatAsRadLong() {
        return rad() + " " + FORMAT_RAD_LONG;
    }

    /**
     * Format like so.
     *
     * @return "? degrees"
     */
    public String formatAsDegLong() {
        return deg() + " " + FORMAT_DEG_LONG;
    }

    /**
     * Compare!
     *
     * @param o yeah.
     * @return yup.
     */
    @Override
    public int compareTo(Angle o) {
        return Double.compare(this.rad(), o.rad());
    }

    /**
     * Convert to string.
     *
     * @return "? deg"
     */
    @Override
    public String toString() {
        return deg() + " " + FORMAT_DEG_SHORT;
    }

    /**
     * Different units of angles.
     */
    public enum AngleUnit {
        /**
         * Radians. Very cool, right?
         */
        RADIANS,

        /**
         * Degrees. Even cooler, I know.
         */
        DEGREES
    }
}
