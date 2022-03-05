/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.listening.ListenerBuilder;
import me.wobblyyyy.pathfinder2.listening.ListenerManager;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

/**
 * A very simple boolean button. Buttons are based on a {@link Supplier} that
 * indicates the state of a physical button: this supplier should return true
 * if the button is pressed, and false if it is not.
 *
 * <p>
 * {@code Button}s were pretty useful in older versions of Pathfinder, but
 * there's a new listener system ({@code me.wobblyyyy.pathfinder2.listening})
 * that's even better.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Button {
    private final ListenerManager manager;
    private final Supplier<Boolean> stateSupplier;
    private boolean lastState;

    /**
     * Create a new {@code Button}.
     *
     * @param stateSupplier a method that returns the button's state. This
     *                      {@code Supplier} should return {@code true}
     *                      whenever the physical button is pressed, and
     *                      {@code false} whenever it is not.
     */
    public Button(Supplier<Boolean> stateSupplier) {
        this(null, stateSupplier);
    }

    /**
     * Create a new {@code Button}.
     *
     * @param manager       the listener manager instance.
     * @param stateSupplier a method that returns the button's state. This
     *                      {@code Supplier} should return {@code true}
     *                      whenever the physical button is pressed, and
     *                      {@code false} whenever it is not.
     */
    public Button(ListenerManager manager, Supplier<Boolean> stateSupplier) {
        this.manager = manager;
        this.stateSupplier = stateSupplier;
    }

    public <T>Button(
        ListenerManager manager,
        Supplier<T> supplier,
        Predicate<T> predicate
    ) {
        this(manager, () -> predicate.test(supplier.get()));
    }

    /**
     * Is the button currently active?
     *
     * @return true if the button is pressed, false if the button is not
     * pressed.
     */
    public boolean isPressed() {
        boolean state = stateSupplier.get();
        lastState = state;
        return state;
    }

    /**
     * Is the button currently NOT active?
     *
     * @return false if the button is pressed, true if the button is
     * pressed.
     */
    public boolean isNotPressed() {
        return !stateSupplier.get();
    }

    /**
     * Was the button just pressed? When a button is pressed, this method will
     * return true one time, until the button is released and pressed again.
     *
     * @return was the button just pressed? This method will only return true
     * once per button press.
     */
    public boolean wasPressed() {
        return (!lastState) && isPressed();
    }

    /**
     * Was the button just released? When a button is released, this method will
     * return true one time, until the button is pressed and released again.
     *
     * @return was the button just released? This method will only return true
     * once per button press.
     */
    public boolean wasReleased() {
        return (lastState) && (!isPressed());
    }

    private void addListener(ListenerMode mode, Runnable runnable) {
        if (manager == null) throw new NullPointerException(
            "Cannot apply any bindings " +
            "to this button because the listener manager was not " +
            "set in the Button's constructor!"
        );

        manager.addListener(
            new ListenerBuilder()
                .addInput(stateSupplier)
                .setPriority(0)
                .setMode(mode)
                .setWhenTriggered(runnable)
                .setMaximumExecutions(Integer.MAX_VALUE)
                .setExpiration(Double.MAX_VALUE)
                .setCooldownMs(0)
                .build()
        );
    }

    public Button whenPressed(Runnable runnable) {
        addListener(ListenerMode.CONDITION_NEWLY_MET, runnable);

        return this;
    }

    public Button whilePressed(Runnable runnable) {
        addListener(ListenerMode.CONDITION_IS_MET, runnable);

        return this;
    }

    public Button whenReleased(Runnable runnable) {
        addListener(ListenerMode.CONDITION_NEWLY_NOT_MET, runnable);

        return this;
    }

    public Button whileReleased(Runnable runnable) {
        addListener(ListenerMode.CONDITION_IS_NOT_MET, runnable);

        return this;
    }
}
