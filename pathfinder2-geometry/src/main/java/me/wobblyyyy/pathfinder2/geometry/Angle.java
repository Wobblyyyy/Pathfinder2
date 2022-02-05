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

import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.math.Equals;

import java.io.Serializable;

/**
 * An angle, represented both in degrees and radians. {@code Angle} objects
 * are designed to replace primitives, such as {@code double}s, wherever
 * any angles are used. Because Pathfinder is so heavily focused on geometry,
 * the {@code Angle} class is designed to accommodate for just about whatever
 * angle-related things you can think of. Super cool!
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
 * It's strongly suggested that you use the {@code Angle} class just about
 * anywhere you would use an angle. Although yes, it is technically slower
 * and requires more memory to use objects instead of just using primitives,
 * such as {@code double}, it keeps your code readable and makes it easier
 * to modify down the line. Also, if you're planning on making any changes
 * to Pathfinder2's source code, ALL angles should be denoted using the
 * {@code Angle} class. There should be next to no primitives whatsoever.
 * </p>
 *
 * <p>
 * In addition to tons of cool and very epic math stuff, the {@code Angle}
 * class provides some formatting utilities. See:
 * <ul>
 *     <li>{@link #formatAsDegLong()}</li>
 *     <li>{@link #formatAsDegShort()}</li>
 *     <li>{@link #formatAsRadLong()}</li>
 *     <li>{@link #formatAsRadShort()}</li>
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
    public static long COUNT = 0;

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
     * We do this, so we don't have to convert degrees to radians and so
     * on and so forth over and over again.
     *
     * @param rad radians value.
     * @param deg degrees value.
     */
    private Angle(double rad,
                  double deg) {
        COUNT++;

        this.rad = rad;
        this.deg = deg;
    }

    public static Angle fromTrig(double sin,
                                 double cos) {
        return atan2(sin, cos);
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
        return Angle.fixedRad(a.rad() + b.rad());
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
        return Angle.fixedRad(a.rad() * b.rad());
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
        return Angle.fixedRad(a.rad() * b);
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
        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Cannot have a tolerance below 0!"
            );
        }

        return Math.abs(a.fix().rad() - b.fix().rad()) <= tolerance;
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
        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Cannot have a tolerance below 0!"
            );
        }

        return Math.abs(a.fix().deg() - b.fix().deg()) <= Math.abs(tolerance);
    }

    /**
     * Get the minimum delta between two angles.
     *
     * <p>
     * The way this works is a little bit confusing, so I'll do my best to
     * explain. Here we go. A robot is capable of rotating rightwards and
     * leftwards around its center - that's pretty simple, right? However,
     * if every angle we use is fixed, we run into some issues. Say we're at
     * 0 degrees and we want to turn to 270 degrees. The obvious solution is
     * to turn 270 degrees. However, you can get to the same angle by turning
     * -90 degrees. This method will always calculate the minimum delta
     * between two angles.
     * </p>
     *
     * @param a the initial angle. In most applications, this is a robot's
     *          current heading. There are some situations where this isn't
     *          the case, but as a rule of thumb, this should almost definitely
     *          be the robot's current heading.
     * @param b the target angle. This one should not be the robot's heading.
     *          Well, I mean, it can be, but I don't know why you'd want it to
     *          be. It's up to you, dawg!
     * @return the minimum delta between these two angles. This is measured
     * in degrees, not radians.
     */
    public static double minimumDelta(Angle a,
                                      Angle b) {
        // There's definitely a much better way to do this, but for now,
        // there aren't any problems with this method being too slow.

        // Fix both of the angles (so we have easy-to-work-with numbers) and
        // convert them into degrees.
        double a_deg = a.fix().deg();
        double b_deg = b.fix().deg();

        // Determine the current delta. We assume angle B is the target
        // angle and angle A is the initial angle.
        double delta = b_deg - a_deg;

        // If the delta is above 180 degrees, which it never should be,
        // there has to be a shorter way to get there.
        if (Math.abs(delta) > 180) {
            // Fix and flip both angles a and b. Flipping an angle just
            // adds 180 degrees to it, and fixing it ensures it's within
            // our lovely 0-360 range.
            a_deg = a.fixedFlip().deg();
            b_deg = b.fixedFlip().deg();

            // Recalculate the delta. Once again, we assume b is the target
            // and an is the initial. This delta should always be below
            // 180 - if it's not, something's gone seriously wrong.
            delta = b_deg - a_deg;
        }

        // And we're done! Would you look at that? Truly incredible, isn't it?
        return delta;
    }

    /**
     * Get the minimum delta between two angles.
     *
     * @param a the first of the two angles.
     * @param b the second of the two angles.
     * @return the minimum delta between the two angles.
     * @see #minimumDelta(Angle, Angle)
     */
    public static Angle angleDelta(Angle a,
                                   Angle b) {
        return fromDeg(minimumDelta(a, b));
    }

    /**
     * Get the minimum delta between two angles, in degrees.
     *
     * @param a the first of the two angles.
     * @param b the second of the two angles.
     * @return the minimum delta between the two angles.
     * @see #minimumDelta(Angle, Angle)
     * @see #angleDelta(Angle, Angle)
     */
    public static double angleDeltaDeg(Angle a,
                                       Angle b) {
        return angleDelta(a, b).deg();
    }

    /**
     * Get the minimum delta between two angles, in radians.
     *
     * @param a the first of the two angles.
     * @param b the second of the two angles.
     * @return the minimum delta between the two angles.
     * @see #minimumDelta(Angle, Angle)
     * @see #angleDelta(Angle, Angle)
     */
    public static double angleDeltaRad(Angle a,
                                       Angle b) {
        return angleDelta(a, b).rad();
    }

    /**
     * Create a new angle from the {@link Math#acos(double)} method.
     *
     * @param a the value to create the angle from.
     * @return a new angle.
     * @see #asin(double)
     * @see #atan(double)
     * @see #atan2(double, double)
     */
    public static Angle acos(double a) {
        return Angle.fromRad(Math.acos(a));
    }

    /**
     * Create a new angle from the {@link Math#asin(double)} method.
     *
     * @param a the value to create the angle from.
     * @return a new angle.
     * @see #acos(double)
     * @see #atan(double)
     * @see #atan2(double, double)
     */
    public static Angle asin(double a) {
        return Angle.fromRad(Math.asin(a));
    }

    /**
     * Create a new angle from the {@link Math#atan(double)} method.
     *
     * @param a the value to create the angle from.
     * @return a new angle.
     * @see #asin(double)
     * @see #acos(double)
     * @see #atan2(double, double)
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
     * @see #asin(double)
     * @see #acos(double)
     * @see #atan(double)
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
     * @see #rotateDeg(Angle, double)
     * @see #fixedRotateRad(Angle, double)
     * @see #fixedRotateDeg(Angle, double)
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
     * @see #rotateRad(Angle, double)
     * @see #fixedRotateRad(Angle, double)
     * @see #fixedRotateDeg(Angle, double)
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
     * @see #rotateRad(Angle, double)
     * @see #rotateDeg(Angle, double)
     * @see #fixedRotateDeg(Angle, double)
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
     * @see #rotateRad(Angle, double)
     * @see #rotateDeg(Angle, double)
     * @see #fixedRotateRad(Angle, double)
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

    public static void checkArgument(Angle angle,
                                     String message) {
        if (angle == null)
            throw new IllegalArgumentException(message);
    }

    public static void checkArgument(Angle angle) {
        checkArgument(
                angle,
                "Attempted to operate on a null angle, please " +
                        "make sure you're not passing a null angle to a method."
        );
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
     * @see #cos()
     * @see #tan()
     * @see #csc()
     * @see #sec()
     * @see #cot()
     */
    public double sin() {
        return Math.sin(rad);
    }

    /**
     * Get the cosine of the angle.
     *
     * @return the cosine of the angle.
     * @see #sin()
     * @see #tan()
     * @see #csc()
     * @see #sec()
     * @see #cot()
     */
    public double cos() {
        return Math.cos(rad);
    }

    /**
     * Get the tangent of the angle.
     *
     * @return the tangent of the angle.
     * @see #sin()
     * @see #cos()
     * @see #csc()
     * @see #sec()
     * @see #cot()
     */
    public double tan() {
        return Math.tan(rad);
    }

    /**
     * Get the cosecant of the angle.
     *
     * @return the cosecant of the angle.
     * @see #sin()
     * @see #cos()
     * @see #tan()
     * @see #sec()
     * @see #cot()
     */
    public double csc() {
        return 1 / sin();
    }

    /**
     * Get the secant of the angle.
     *
     * @return the secant of the angle.
     * @see #sin()
     * @see #cos()
     * @see #tan()
     * @see #csc()
     * @see #cot()
     */
    public double sec() {
        return 1 / cos();
    }

    /**
     * Get the cotangent of the angle.
     *
     * @return the cotangent of the angle.
     * @see #sin()
     * @see #cos()
     * @see #tan()
     * @see #csc()
     * @see #sec()
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
     * Is this angle less than the provided angle?
     *
     * @param angle the angle to compare.
     * @return true if this angle is less than the provided angle,
     * otherwise, false.
     */
    public boolean lessThan(Angle angle) {
        return minimumDelta(this, angle) <= 0;
    }

    /**
     * Is this angle greater than the provided angle?
     *
     * @param angle the angle to compare.
     * @return true if this angle is greater than the provided angle,
     * otherwise, false.
     */
    public boolean greaterThan(Angle angle) {
        return minimumDelta(this, angle) >= 0;
    }

    /**
     * Is this angle less than or equal to the provided angle?
     *
     * @param angle the angle to compare.
     * @return true if this angle is less than or equal to the provided angle,
     * otherwise, false.
     */
    public boolean lessThanOrEqualTo(Angle angle) {
        return fixDeg(deg()) <= fixDeg(angle.deg());
    }

    /**
     * Is this angle greater than or equal to the provided angle?
     *
     * @param angle the angle to compare.
     * @return true if this angle is greater than or equal to the provided angle,
     * otherwise, false.
     */
    public boolean greaterThanOrEqualTo(Angle angle) {
        return fixDeg(deg()) >= fixDeg(angle.deg());
    }

    /**
     * The description is too long for a single sentence, so keep reading.
     * If this angle is less than the minimum angle, return the minimum
     * angle. If this angle is greater than the maximum angle, return the
     * maximum angle. If this angle is neither, return this angle.
     *
     * @param minimum the minimum angle.
     * @param maximum the maximum angle.
     * @return if this angle is less than the minimum angle, return the
     * minimum angle; if this angle is greater than the maximum angle, return
     * the maximum angle; otherwise, return this angle.
     */
    public Angle angleWithMinAndMax(Angle minimum,
                                    Angle maximum) {
        if (greaterThan(maximum)) return maximum;
        else if (lessThan(minimum)) return minimum;
        else return this;
    }

    /**
     * Convert this {@code Angle} into a translation by using the
     * {@link #toTranslation(double, double)} and providing a vz value of
     * 0 and a {@code translationMagnitude} value of 1.0.
     *
     * @return the {@link Translation} representation of this angle.
     */
    public Translation toTranslation() {
        return toTranslation(0);
    }

    /**
     * Convert this {@code Angle} into a {@link Translation}. This creates
     * a translation by creating a point that is {@code 1}
     * away from (0, 0) in this angle's direction, and then creating a
     * translation based on that point's component X and Y values. At the end,
     * the supplied {@code vz} value is added on to the translation.
     *
     * @param vz the vz value to add on to the translation.
     * @return the {@link Translation} representation of this angle.
     */
    public Translation toTranslation(double vz) {
        return toTranslation(
                1.0,
                vz
        );
    }

    /**
     * Convert this {@code Angle} into a {@link Translation}. This creates
     * a translation by creating a point that is {@code translationMagnitude}
     * away from (0, 0) in this angle's direction, and then creating a
     * translation based on that point's component X and Y values. At the end,
     * the supplied {@code vz} value is added on to the translation.
     *
     * @param translationMagnitude the magnitude of the translation. A
     *                             magnitude of 1 is default. The higher the
     *                             magnitude is, the "larger" the translation
     *                             will be.
     * @param vz                   the vz value to add on to the translation.
     * @return the {@link Translation} representation of this angle.
     */
    public Translation toTranslation(double translationMagnitude,
                                     double vz) {
        PointXY point = PointXY.ZERO.inDirection(
                translationMagnitude,
                this
        );

        return new Translation(
                point.x(),
                point.y(),
                vz
        );
    }

    /**
     * Compare!
     *
     * @param o yeah.
     * @return yup.
     */
    @Override
    public int compareTo(Angle o) {
        return Double.compare(this.fix().rad(), o.fix().rad());
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

    @Override
    public int hashCode() {
        return (int) (fix().deg() * 1_000_000);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Angle) {
            Angle a = (Angle) obj;

            boolean sameDeg = Equals.soft(a.deg, this.deg, 0.01);
            boolean sameRad = Equals.soft(a.rad, this.rad, 0.01);

            return sameDeg || sameRad;
        }

        return false;
    }

    /**
     * Different units of angles.
     */
    public enum AngleUnit {
        /**
         * Radians. Very cool, right?
         *
         * <p>
         * <ul>
         *     <li>Short formatting: "rad"</li>
         *     <li>Long formatting: "radians"</li>
         * </ul>
         * </p>
         */
        RADIANS,

        /**
         * Degrees. Even cooler, I know.
         *
         * <p>
         * <ul>
         *     <li>Short formatting: "deg"</li>
         *     <li>Long formatting: "degrees"</li>
         * </ul>
         * </p>
         */
        DEGREES
    }
}
