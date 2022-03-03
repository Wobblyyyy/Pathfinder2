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

import me.wobblyyyy.pathfinder2.exceptions.ValidationException;

/**
 * Validation utilities.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public class ValidationUtils {
    private static final String NO_NAME = "parameter name not specified";

    public static double validateNotNaN(double value,
                                      String parameterName) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException(StringUtils.format(
                    "Failed to validate double <%s> because " +
                            "the value was not a number!",
                    parameterName
            ));

        return value;
    }

    public static double validateNotInfinite(double value,
                                             String parameterName) {
        if (Double.isInfinite(value))
            throw new IllegalArgumentException(StringUtils.format(
                    "Failed to validate double <%s> because " +
                            "the value was infinite!",
                    parameterName
            ));

        return value;
    }

    /**
     * Validate a double value by ensuring it's not {@code NaN} or infinite.
     * If the number is {@code NaN} or infinite, this method will throw
     * an {@link IllegalArgumentException}.
     *
     * @param value         the value to validate.
     * @param parameterName the name of the parameter that's being validated.
     * @return if the value is validated (meaning it's not {@code NaN} and
     * it's not infinite), return the value.
     */
    public static double validate(double value,
                                  String parameterName) {
        validateNotNaN(value, parameterName);
        validateNotInfinite(value, parameterName);

        return value;
    }

    /**
     * Validate a double value by ensuring it's not {@code NaN} or infinite.
     * If the number is {@code NaN} or infinite, this method will throw
     * a {@link ValidationException.
     *
     * @param value         the value to validate.
     * @return if the value is validated (meaning it's not {@code NaN} and
     * it's not infinite), return the value.
     */
    public static double validate(double value) {
        return validate(value, NO_NAME);
    }

    /**
     * Validate a float value by ensuring it's not {@code NaN} or infinite.
     * If the number is {@code NaN} or infinite, this method will throw
     * an {@link IllegalArgumentException}.
     *
     * @param value         the value to validate.
     * @param parameterName the name of the parameter that's being validated.
     * @return if the value is validated (meaning it's not {@code NaN} and
     * it's not infinite), return the value.
     */
    public static float validate(float value,
                                 String parameterName) {
        validate(value, parameterName);
        return value;
    }

    /**
     * Validate a double value by ensuring it's not {@code NaN} or infinite.
     * If the number is {@code NaN} or infinite, this method will throw
     * an {@link IllegalArgumentException}.
     *
     * @param value         the value to validate.
     * @return if the value is validated (meaning it's not {@code NaN} and
     * it's not infinite), return the value.
     */
    public static float validate(float value) {
        return validate(value, NO_NAME);
    }

    public static Object validateNotNull(Object object,
                                         String parameterName) {
        if (object == null) throw new ValidationException(StringUtils.format(
                    "Failed to validate object <%s> because it was null!",
                    parameterName
            ));

        return object;
    }

    /**
     * Validate an object by checking to see if it's null.
     *
     * @param object        the object to validate.
     * @param parameterName the name of the object.
     * @return the object.
     */
    public static Object validate(Object object,
                                  String parameterName) {
        return validateNotNull(object, parameterName);
    }

    /**
     * Validate an object by checking to see if it's null.
     *
     * @param object        the object to validate.
     * @param parameterName the name of the object.
     * @return the object.
     */
    public static Object validate(Object object) {
        return validate(object, NO_NAME);
    }
}
