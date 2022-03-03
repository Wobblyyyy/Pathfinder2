/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import org.junit.jupiter.api.Assertions;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Geometry;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.math.Rounding;

/**
 * Assertion utilities specific to Pathfinder. This class requires the
 * {@code Assertions} class from the jUnit API - if you do not have an
 * implementation of jUnit available at runtime, you'll get compilation errors.
 * Such, I'd suggest you don't make use of this class, save tests used
 * inside of Pathfinder.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public class AssertionUtils {
    private AssertionUtils() {

    }

    public static void assertSoftEqualsWithoutValidation(double expected,
                                                         double actual,
                                                         double tolerance) {
        Assertions.assertTrue(
                Equals.softWithoutValidation(expected, actual, tolerance),
                StringUtils.format(
                        "Expected <%s> but got <%s>, tolerance of <%s>",
                        Rounding.fastRound(expected),
                        Rounding.fastRound(actual),
                        Rounding.fastRound(tolerance)
                )
        );
    }

    public static void assertSoftEquals(double expected,
                                        double actual,
                                        double tolerance) {
        ValidationUtils.validate(expected, "expected");
        ValidationUtils.validate(actual, "actual");
        ValidationUtils.validate(tolerance, "tolerance");

        assertSoftEqualsWithoutValidation(expected, actual, tolerance);
    }

    /**
     * Assert that two points are within given tolerances of eachother. This
     * uses {@link Assertions#assertTrue(boolean, String)} to assert that
     * the distance between the two objects is less than the tolerance. This
     * WILL NOT perform any validation.
     *
     * @param a         one of the two points to compare.
     * @param b         one of the two points to compare.
     * @param tolerance the tolerance to use in determining if the points
     *                  are near. If the absolute value of the distance
     *                  between the points is less than or equal to this
     *                  value, the points are considered near. If the
     *                  absolute value of the distance between the two
     *                  points is greater than this value, the points are
     *                  not considered near.
     */
    public static void assertIsNearWithoutValidation(PointXY a,
                                                     PointXY b,
                                                     double tolerance) {
        double absoluteDistance = Math.abs(a.distance(b));

        Assertions.assertTrue(
                absoluteDistance <= tolerance,
                StringUtils.format(
                        "Expected a maximum distance of <%s> but got " +
                                "a distance of <%s> between points <%s> " +
                                "and <%s>",
                        Rounding.fastRound(tolerance),
                        Rounding.fastRound(absoluteDistance),
                        a,
                        b
                )
        );
    }

    /**
     * Assert that two points are within given tolerances of eachother. This
     * uses {@link Assertions#assertTrue(boolean, String)} to assert that
     * the distance between the two objects is less than the tolerance.
     *
     * @param a         one of the two points to compare.
     * @param b         one of the two points to compare.
     * @param tolerance the tolerance to use in determining if the points
     *                  are near. If the absolute value of the distance
     *                  between the points is less than or equal to this
     *                  value, the points are considered near. If the
     *                  absolute value of the distance between the two
     *                  points is greater than this value, the points are
     *                  not considered near.
     */
    public static void assertIsNear(PointXY a,
                                    PointXY b,
                                    double tolerance) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");
        ValidationUtils.validate(tolerance, "tolerance");

        assertIsNearWithoutValidation(a, b, tolerance);
    }

    public static void assertIsNear(PointXY a,
                                    PointXY b) {
        assertIsNear(
                a,
                b,
                Geometry.tolerancePointXY
        );
    }

    /**
     * Assert that two angles are within given tolerances of eachother. This
     * uses {@link Assertions#assertTrue(boolean, String)} to assert that
     * the distance between the two objects is less than the tolerance. This
     * WILL NOT perform any validation.
     *
     * @param a         one of the two points to compare.
     * @param b         one of the two points to compare.
     * @param tolerance the angle tolerance used in determining if the
     *                  angles of each of the points is near. If the
     *                  distance between the two angles is less than or
     *                  equal to this value, the angles are considered
     *                  near. Otherwise, the angles are not near.
     */
    public static void assertIsNearWithoutValidation(Angle a,
                                                     Angle b,
                                                     Angle tolerance) {
        double minimumDeltaDeg = Math.abs(Angle.minimumDelta(a, b));
        double toleranceDeg = Math.abs(tolerance.deg());

        Assertions.assertTrue(
                minimumDeltaDeg <= toleranceDeg,
                StringUtils.format(
                        "Expected a maximum distance of <%s deg> but got " +
                                "a distance of <%s deg> between angles " +
                                "<%s> and <%s>",
                        Rounding.fastRound(toleranceDeg),
                        Rounding.fastRound(minimumDeltaDeg),
                        a,
                        b
                )
        );
    }

    /**
     * Assert that two angles are within given tolerances of eachother. This
     * uses {@link Assertions#assertTrue(boolean, String)} to assert that
     * the distance between the two objects is less than the tolerance.
     *
     * @param a         one of the two points to compare.
     * @param b         one of the two points to compare.
     * @param tolerance the angle tolerance used in determining if the
     *                  angles of each of the points is near. If the
     *                  distance between the two angles is less than or
     *                  equal to this value, the angles are considered
     *                  near. Otherwise, the angles are not near.
     */
    public static void assertIsNear(Angle a,
                                    Angle b,
                                    Angle tolerance) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");
        ValidationUtils.validate(tolerance, "tolerance");

        assertIsNearWithoutValidation(a, b, tolerance);
    }

    public static void assertIsNear(Angle a,
                                    Angle b) {
        assertIsNear(
                a,
                b,
                Geometry.toleranceAngle
        );
    }

    /**
     * Assert that two points are within given tolerances of eachother. This
     * uses {@link Assertions#assertTrue(boolean, String)} to assert that
     * the distance between the two objects is less than the tolerance. This
     * WILL NOT perform any data validation.
     *
     * @param a              one of the two points to compare.
     * @param b              one of the two points to compare.
     * @param tolerance      the tolerance to use in determining if the points
     *                       are near. If the absolute value of the distance
     *                       between the points is less than or equal to this
     *                       value, the points are considered near. If the
     *                       absolute value of the distance between the two
     *                       points is greater than this value, the points are
     *                       not considered near.
     * @param angleTolerance the angle tolerance used in determining if the
     *                       angles of each of the points is near. If the
     *                       distance between the two angles is less than or
     *                       equal to this value, the angles are considered
     *                       near. Otherwise, the angles are not near.
     */
    public static void assertIsNearWithoutValidation(PointXYZ a,
                                                     PointXYZ b,
                                                     double tolerance,
                                                     Angle angleTolerance) {
        assertIsNearWithoutValidation(a, b, tolerance);
        assertIsNearWithoutValidation(a.z(), b.z(), angleTolerance);
    }

    /**
     * Assert that two points are within given tolerances of eachother. This
     * uses {@link Assertions#assertTrue(boolean, String)} to assert that
     * the distance between the two objects is less than the tolerance.
     *
     * @param a              one of the two points to compare.
     * @param b              one of the two points to compare.
     * @param tolerance      the tolerance to use in determining if the points
     *                       are near. If the absolute value of the distance
     *                       between the points is less than or equal to this
     *                       value, the points are considered near. If the
     *                       absolute value of the distance between the two
     *                       points is greater than this value, the points are
     *                       not considered near.
     * @param angleTolerance the angle tolerance used in determining if the
     *                       angles of each of the points is near. If the
     *                       distance between the two angles is less than or
     *                       equal to this value, the angles are considered
     *                       near. Otherwise, the angles are not near.
     */
    public static void assertIsNear(PointXYZ a,
                                    PointXYZ b,
                                    double tolerance,
                                    Angle angleTolerance) {
        ValidationUtils.validate(a, "a");
        ValidationUtils.validate(b, "b");
        ValidationUtils.validate(tolerance, "tolerance");
        ValidationUtils.validate(angleTolerance, "angleTolerance");

        assertIsNearWithoutValidation(a, b, tolerance, angleTolerance);
    }

    public static void assertIsNear(PointXYZ a,
                                    PointXYZ b) {
        assertIsNear(
                a,
                b,
                Geometry.tolerancePointXYZ,
                Geometry.toleranceAngle
        );
    }
}
