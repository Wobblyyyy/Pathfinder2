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
 * Exception to be thrown whenever a speed value is invalid.
 *
 * <p>
 * In most cases, speed values should always fit within the range of 0.0
 * to 1.0. If you're getting this exception during program execution, actually,
 * it's fairly likely the cause is a speed value outside of that range.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class InvalidSpeedException extends RuntimeException {
    public InvalidSpeedException(String s) {
        super(s);
    }
}
