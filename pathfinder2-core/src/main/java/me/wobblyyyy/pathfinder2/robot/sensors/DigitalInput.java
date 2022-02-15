/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

/**
 * A {@code DigitalInput} is pretty much a binary sensor - it can either be
 * on or off. Usually, this is implemented as something like a button.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public interface DigitalInput extends Sensor<Boolean> {
    /**
     * Is the switch currently active?
     *
     * @return if the switch is currently active, return true. Otherwise,
     * return false.
     */
    boolean isActive();

    /**
     * Is the switch currently inactive?
     *
     * @return if the switch is currently active, return false. Otherwise,
     * return true.
     */
    default boolean isInactive() {
        return !isActive();
    }

    @Override
    default Boolean read() {
        return isActive();
    }
}
