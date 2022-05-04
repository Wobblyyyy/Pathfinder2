/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.recording;

/**
 * In order to use Pathfinder's state recording system, anything you'd like
 * to record must be a {@link Recordable}.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public interface Recordable<T> {
    /**
     * Get the {@code Recordable}'s value.
     *
     * @return the {@code Recordable}'s value.
     */
    T getRecordingValue();

    /**
     * Set the {@code Recordable}'s value.
     *
     * @param value the value for the {@code Recordable}.
     */
    void setRecordingValue(Object value);
}
