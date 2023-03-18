/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.exceptions;

/**
 * When something goes wrong while executing a user script, a
 * {@code ScriptingException} will be thrown.
 *
 * @author Colin Robertson
 * @since 3.0.0-alpha
 */
public class ScriptingException extends RuntimeException {

    /**
     * Create a new {@code ScriptingException}.
     *
     * @param msg the message the exception should carry. This message should
     *            clearly explain what what wrong, as well as how to fix it.
     */
    public ScriptingException(String msg) {
        super(msg);
    }
}
