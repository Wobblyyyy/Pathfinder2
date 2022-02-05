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
import me.wobblyyyy.pathfinder2.utils.Toggle;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Manager responsible for several {@link Listener}s. Each {@link Listener}
 * is stored in a {@link Map} with {@link String} keys so that listeners
 * can be activated and deactivated based on a shared name. For most normal
 * use cases, you shouldn't manually use this class' {@link #tick(Pathfinder)}
 * method - rather, you'd want to have an instance of {@link Pathfinder} tick
 * it instead in the {@link Pathfinder#tick()} method.
 *
 * <p>
 * The {@link String} values used as keys do not require any certain rules
 * to be followed - they can be literally anything you want. If you're out
 * of ideas, check out {@link me.wobblyyyy.pathfinder2.utils.RandomString#randomString(int)}.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class ListenerManager implements Tickable {
    private final Pathfinder pathfinder;
    private Map<String, Listener> listeners;

    /**
     * Create a new {@code ListenerManager}.
     */
    public ListenerManager(Pathfinder pathfinder) {
        listeners = new HashMap<>();
        this.pathfinder = pathfinder;
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

        // sort the listeners on insertion so that the map doesn't have to
        // be sorted every tick
        listeners = listeners.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().getPriority()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (l1, l2) -> l1
                ));

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
        List<String> expiredListeners = new ArrayList<String>(listeners.size());

        for (Map.Entry<String, Listener> entry : listeners.entrySet()) {
            String name = entry.getKey();
            Listener listener = entry.getValue();

            // remove expired listeners, tick non-expired listeners
            if (listener.hasExpired())
                expiredListeners.add(name);
            else
                listener.tick(pathfinder);
        }

        // actually remove the listener if it's expired
        for (String key : expiredListeners)
            listeners.remove(key);

        return true;
    }

    /**
     * Bind an action to whenever the button is pressed or not pressed.
     *
     * @param input     a supplier that indicates the physical state of
     *                  a button.
     * @param onPress   code to be executed whenever the button is initially
     *                  pressed.
     * @param onRelease code to be executed whenever the button is initially
     *                  released.
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager bindButton(Supplier<Boolean> input,
                                      Runnable onPress,
                                      Runnable onRelease) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                input
        )).addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                onRelease,
                input
        ));
    }

    public ListenerManager bindButton(Supplier<Boolean> input,
                                      Runnable onPress,
                                      Runnable whenHeld,
                                      Runnable onRelease) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                input
        )).addListener(new Listener(
                ListenerMode.CONDITION_IS_MET,
                whenHeld,
                input
        )).addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                onRelease,
                input
        ));
    }

    public ListenerManager bindTriggerPressed(Supplier<Double> input,
                                              Runnable onPress) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                () -> input.get() > 0
        ));
    }

    public ListenerManager bindTrigger(Supplier<Double> input,
                                       Runnable onPress,
                                       Runnable onRelease) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                () -> input.get() > 0
        )).addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                onRelease,
                () -> input.get() > 0
        ));
    }

    public ListenerManager bindTrigger(Supplier<Double> input,
                                       Runnable onPress,
                                       Runnable whenHeld,
                                       Runnable onRelease) {
        return bindButton(
                () -> input.get() > 0,
                onPress,
                whenHeld,
                onRelease
        );
    }

    public ListenerManager bindTrigger(Supplier<Double> input,
                                       Runnable onPress,
                                       Runnable whenHeld,
                                       Runnable onRelease,
                                       Runnable whenNotHeld) {
        return bindButton(
                () -> input.get() > 0,
                onPress,
                whenHeld,
                onRelease,
                whenNotHeld
        );
    }

    public ListenerManager bindButton(Supplier<Boolean> input,
                                      Runnable onPress,
                                      Runnable whenHeld,
                                      Runnable onRelease,
                                      Runnable whenNotHeld) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                input
        )).addListener(new Listener(
                ListenerMode.CONDITION_IS_MET,
                whenHeld,
                input
        )).addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                onRelease,
                input
        )).addListener(new Listener(
                ListenerMode.CONDITION_IS_NOT_MET,
                whenNotHeld,
                input
        ));
    }

    /**
     * Bind an action to a button press - whenever the button is initially
     * pressed, that is.
     *
     * @param input   a supplier that indicates the physical state of
     *                a button.
     * @param onPress code to be executed whenever the button is initially
     *                pressed.
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager bindButtonPress(Supplier<Boolean> input,
                                           Runnable onPress) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                onPress,
                input
        ));
    }

    /**
     * Bind an action to a button release - whenever the button is initially
     * released, that is.
     *
     * @param input     a supplier that indicates the physical state of
     *                  a button.
     * @param onRelease code to be executed whenever the button is initially
     *                  pressed.
     * @return {@code this}, used for method chaining.
     */
    public ListenerManager bindButtonRelease(Supplier<Boolean> input,
                                             Runnable onRelease) {
        return addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                onRelease,
                input
        ));
    }

    public ListenerManager bindToggle(Toggle toggle,
                                      Supplier<Boolean> input,
                                      Consumer<Toggle> onToggle) {
        return bind(
                ListenerMode.CONDITION_NEWLY_MET,
                input,
                (b) -> {
                    toggle.toggle();
                    onToggle.accept(toggle);
                }
        );
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
     * @param runnable functionality that will be executed whenever the listener
     *                 is triggered.
     * @param <T>      the type of {@link Object} that's state is being
     *                 observed.
     * @return {@code this}, used for method chaining.
     */
    public <T> ListenerManager bind(ListenerMode mode,
                                    Supplier<T> input,
                                    Predicate<T> checker,
                                    Runnable runnable) {
        return addListener(new Listener(
                mode,
                runnable,
                () -> checker.test(input.get())
        ));
    }

    public ListenerManager bind(ListenerMode mode,
                                Supplier<Boolean> input,
                                Consumer<Boolean> onFinish) {
        return bind(
                mode,
                input,
                (b) -> b,
                onFinish
        );
    }

    public <T> ListenerManager bindIsMet(Supplier<T> supplier,
                                         Predicate<T> predicate,
                                         Consumer<T> consumer) {
        return bind(
                ListenerMode.CONDITION_IS_MET,
                supplier,
                predicate,
                consumer
        );
    }

    public ListenerManager bindIsMet(Supplier<Boolean> supplier,
                                     Consumer<Boolean> consumer) {
        return bind(
                ListenerMode.CONDITION_IS_MET,
                supplier,
                consumer
        );
    }

    public <T> ListenerManager bindIsNotMet(Supplier<T> supplier,
                                            Predicate<T> predicate,
                                            Consumer<T> consumer) {
        return bind(
                ListenerMode.CONDITION_IS_NOT_MET,
                supplier,
                predicate,
                consumer
        );
    }

    public ListenerManager bindIsNotMet(Supplier<Boolean> supplier,
                                        Consumer<Boolean> consumer) {
        return bind(
                ListenerMode.CONDITION_IS_NOT_MET,
                supplier,
                consumer
        );
    }

    public <T> ListenerManager bindNewlyMet(Supplier<T> supplier,
                                            Predicate<T> predicate,
                                            Consumer<T> consumer) {
        return bind(
                ListenerMode.CONDITION_NEWLY_MET,
                supplier,
                predicate,
                consumer
        );
    }

    public ListenerManager bindNewlyMet(Supplier<Boolean> supplier,
                                        Consumer<Boolean> consumer) {
        return bind(
                ListenerMode.CONDITION_NEWLY_MET,
                supplier,
                consumer
        );
    }

    public <T> ListenerManager bindNewlyNotMet(Supplier<T> supplier,
                                               Predicate<T> predicate,
                                               Consumer<T> consumer) {
        return bind(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                supplier,
                predicate,
                consumer
        );
    }

    public ListenerManager bindNewlyNotMet(Supplier<Boolean> supplier,
                                           Consumer<Boolean> consumer) {
        return bind(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                supplier,
                consumer
        );
    }

    public <T> ListenerManager bindNewlyChanged(Supplier<T> supplier,
                                                Predicate<T> predicate,
                                                Consumer<T> consumer) {
        return bind(
                ListenerMode.CONDITION_NEWLY_CHANGED,
                supplier,
                predicate,
                consumer
        );
    }

    public ListenerManager bindNewlyChanged(Supplier<Boolean> supplier,
                                            Consumer<Boolean> consumer) {
        return bind(
                ListenerMode.CONDITION_NEWLY_CHANGED,
                supplier,
                consumer
        );
    }

    /**
     * Get the instance of {@code Pathfinder} that's controlling this
     * {@code ListenerManager}.
     *
     * @return the instance of {@code Pathfinder} that's controlling this
     * {@code ListenerManager}.
     */
    public Pathfinder getPathfinder() {
        return pathfinder;
    }
}
