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
 * An exception to be thrown when there's an issue with creating a spline.
 *
 * @since 1.1.0
 */
public class SplineException extends RuntimeException {
    public SplineException(String message) {
        super(message);
    }
}
