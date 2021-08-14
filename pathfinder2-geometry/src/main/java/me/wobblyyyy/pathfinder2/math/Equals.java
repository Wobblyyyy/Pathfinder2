/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;

/**
 * Simple utilities for determining if numbers are equal to another.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Equals {
    /**
     * Soft-equals - are two numbers within a given distance of another?
     *
     * @param a         the first of the two numbers.
     * @param b         the second of the two numbers.
     * @param tolerance the maximum allowable value that's still considered
     *                  to be "equal."
     * @return true if the numbers are "equal" and false if not.
     */
    public static boolean soft(double a,
                               double b,
                               double tolerance) {
        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Cannot have a tolerance value less than 0!"
            );
        }

        return Math.abs(a - b) <= tolerance;
    }

    /**
     * Soft-equals - are two numbers within a given distance of another?
     *
     * @param a         the first of the two numbers.
     * @param b         the second of the two numbers.
     * @param tolerance the maximum allowable value that's still considered
     *                  to be "equal."
     * @return true if the numbers are "equal" and false if not.
     */
    public static boolean soft(int a,
                               int b,
                               int tolerance) {
        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Cannot have a tolerance value less than 0!"
            );
        }

        return Math.abs(a - b) <= tolerance;
    }

    /**
     * Soft-equals - are two numbers within a given distance of another?
     *
     * @param a         the first of the two numbers.
     * @param b         the second of the two numbers.
     * @param tolerance the maximum allowable value that's still considered
     *                  to be "equal."
     * @return true if the numbers are "equal" and false if not.
     */
    public static boolean soft(float a,
                               float b,
                               float tolerance) {
        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Cannot have a tolerance value less than 0!"
            );
        }

        return Math.abs(a - b) <= tolerance;
    }

    /**
     * Soft-equals - are two numbers within a given distance of another?
     *
     * @param a         the first of the two numbers.
     * @param b         the second of the two numbers.
     * @param tolerance the maximum allowable value that's still considered
     *                  to be "equal."
     * @return true if the numbers are "equal" and false if not.
     */
    public static boolean soft(long a,
                               long b,
                               long tolerance) {
        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Cannot have a tolerance value less than 0!"
            );
        }

        return Math.abs(a - b) <= tolerance;
    }
}
