/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.exceptions;

/**
 * Exception to be thrown whenever a provided tolerance value is invalid.
 * This exception will almost exclusively be thrown when a tolerance value is
 * negative - all tolerance values must be positive.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class InvalidToleranceException extends RuntimeException {
    public InvalidToleranceException(String s) {
        super(s);
    }

    public static void throwIfInvalid(String message,
                                      double tolerance) {
        if (tolerance < 0) throw new InvalidToleranceException(message);
    }
}
