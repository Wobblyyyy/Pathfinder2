/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.listening;

import me.wobblyyyy.pathfinder2.Pathfinder;

/**
 * A {@code Tickable} is something that can be ticked. This is basically
 * anything that needs to be regularly updated.
 *
 * @author Colin Robertson
 * @since 0.8.0
 */
@FunctionalInterface
public interface Tickable {
    /**
     * Tick the object once.
     *
     * @param pathfinder the instance of Pathfinder that is being ticked.
     * @return true if the {@code Tickable} should continue being executed.
     * Otherwise, false.
     */
    boolean tick(Pathfinder pathfinder);
}
