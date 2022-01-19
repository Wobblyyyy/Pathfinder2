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

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A listener that accepts {@code Pathfinder} as a parameter.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class PathfinderListener implements Supplier<Boolean> {
    private final Pathfinder pathfinder;
    private final Predicate<Pathfinder> predicate;

    /**
     * Create a new {@code PathfinderListener}.
     *
     * @param pathfinder the {@code Pathfinder} instance that's being operated
     *                   on.
     * @param predicate  a predicate that determines whether this supplier
     *                   should return true.
     */
    public PathfinderListener(Pathfinder pathfinder,
                              Predicate<Pathfinder> predicate) {
        this.pathfinder = pathfinder;
        this.predicate = predicate;
    }

    @Override
    public Boolean get() {
        return predicate.test(pathfinder);
    }
}
