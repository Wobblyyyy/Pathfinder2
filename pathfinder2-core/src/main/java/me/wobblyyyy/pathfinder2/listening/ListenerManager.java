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
import me.wobblyyyy.pathfinder2.utils.RandomString;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Manager responsible for several {@link Listener}s. Each {@link Listener}
 * is stored in a {@link Map} with {@link String} keys so that listeners
 * can be activated and deactivated based on a shared name.
 * <p>
 * The {@link String} values used as keys do not require any certain rules
 * to be followed - they can be literally anything you want. If you're out
 * of ideas, check out {@link me.wobblyyyy.pathfinder2.utils.RandomString#randomString(int)}.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class ListenerManager implements Tickable {
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
     * Add a listener to the listener manager.
     *
     * @param listener the actual listener to add.
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager addListener(Listener listener) {
        return addListener(
                RandomString.randomString(10),
                listener
        );
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
     */
    @Override
    public boolean tick(Pathfinder pathfinder) {
        for (Listener listener : listeners.values())
            listener.tick(pathfinder);

        return true;
    }

    public ListenerManager bindButtonPress(Supplier<Boolean> input,
                                           Runnable onPress) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                input
        ));
    }

    public ListenerManager bindButtonRelease(Supplier<Boolean> input,
                                             Runnable onRelease) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                onRelease,
                input
        ));
    }

    /**
     * Bind a piece of functionality to a newly-created {@link Listener}.
     *
     * @param mode     the mode the listener will operate in. If you want
     *                 a button-like effect, use
     *                 {@link ListenerMode#CONDITION_NEWLY_MET}.
     *                 See {@link ListenerMode} for more information.
     * @param input    should provide inputs for the listener. This
     *                 {@link Supplier} will supply values for the
     *                 {@code checker} {@link Predicate}, which will in turn
     *                 control the listener's state of activation.
     * @param checker  a predicate that will test inputs for the listener. If
     *                 this predicate returns true, the listener is considered
     *                 to have met the condition. If this predicate returns
     *                 false, the listener has not yet met the condition.
     * @param consumer functionality that will be executed whenever the listener
     *                 is triggered. This accepts a parameter of type
     *                 {@code T}: this is the input that caused the listener
     *                 to be triggered.
     * @param <T>      the type of {@link Object} that's state is being
     *                 observed.
     * @return {@code this}, used for method chaining.
     */
    public <T> ListenerManager bind(ListenerMode mode,
                                    Supplier<T> input,
                                    Predicate<T> checker,
                                    Consumer<T> consumer) {
        return addListener(new Listener(
                mode,
                () -> consumer.accept(input.get()),
                () -> checker.test(input.get())
        ));
    }
}
