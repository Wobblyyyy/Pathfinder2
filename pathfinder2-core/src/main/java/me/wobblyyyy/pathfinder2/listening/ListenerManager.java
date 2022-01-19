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

import java.util.HashMap;
import java.util.Map;

/**
 * Manager responsible for several {@link Listener}s. Each {@link Listener}
 * is stored in a {@link Map} with {@link String} keys so that listeners
 * can be activated and deactivated based on a shared name.
 *
 * The {@link String} values used as keys do not require any certain rules
 * to be followed - they can be literally anything you want. If you're out
 * of ideas, check out {@link me.wobblyyyy.pathfinder2.utils.RandomString#randomString(int)}.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class ListenerManager {
    private final Map<String, Listener> listeners = new HashMap<>();

    /**
     * Create a new {@code ListenerManager}.
     */
    public ListenerManager() {

    }

    /**
     * Add a listener to the listener manager.
     *
     * @param name     the name of the listener. This can be pretty much
     *                 whatever you want it to be.
     * @param listener the actual listener to add.
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager addListener(String name,
                                       Listener listener) {
        listeners.put(name, listener);

        return this;
    }

    /**
     * Remove a listener from the {@code ListenerManager}.
     *
     * @param name the name of the listener to remove.
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager removeListener(String name) {
        listeners.remove(name);

        return this;
    }

    /**
     * Get a listener.
     *
     * @param name the name of the listener.
     * @return {@code this}, used for method chaining.
     */
    public Listener getListener(String name) {
        return listeners.get(name);
    }

    /**
     * "Tick", or update, the listener manager once by ticking/updating
     * each of the listeners operated by the manager.
     *
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager tick() {
        for (Listener listener : listeners.values())
            listener.update();

        return this;
    }
}
