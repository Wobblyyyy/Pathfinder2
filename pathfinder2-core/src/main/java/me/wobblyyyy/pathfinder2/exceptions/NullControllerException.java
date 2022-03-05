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
 * Exception thrown when a required {@code Controller} is null.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class NullControllerException extends RuntimeException {

    public NullControllerException(String message) {
        super(message);
    }
}
