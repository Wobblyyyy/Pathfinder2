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

import me.wobblyyyy.pathfinder2.Core;
import me.wobblyyyy.pathfinder2.time.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Builder tool for {@link Listener}. This is designed to make it easier to
 * create a listener. What else would it do? Good question.
 *
 * @author Colin Robertson
 * @since 0.10.4
 */
public class ListenerBuilder {
    private ListenerMode mode;
    private Runnable whenTriggered;
    private List<Supplier<Boolean>> inputs = new ArrayList<>();
    private int priority;
    private double expiration = Core.listenerBuilderDefaultExpiration;
    private int maximumExecutions;
    private double cooldownMs;

    public ListenerBuilder() {

    }

    /**
     * Set the listener's mode.
     *
     * @param mode the listener's mode.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setMode(ListenerMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Set code to be executed whenever the listener's condition is met.
     *
     * @param whenTriggered code to be run whenever the listener's condition
     *                      is met.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setWhenTriggered(Runnable whenTriggered) {
        this.whenTriggered = whenTriggered;
        return this;
    }

    /**
     * Add an input to the listener.
     *
     * @param input the input to add.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder addInput(Supplier<Boolean> input) {
        inputs.add(input);
        return this;
    }

    /**
     * Set the list of the listener's inputs.
     *
     * @param inputs a list of inputs the listener should use.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setInputs(List<Supplier<Boolean>> inputs) {
        this.inputs = inputs;
        return this;
    }

    /**
     * Set the priority of the listener.
     *
     * @param priority the priority of the listener.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Set the listener's expiration time, in milliseconds from epoch.
     *
     * @param expiration an expiration date, in milliseconds from epoch. This
     *                   number should generally be over a billion - if it's
     *                   not, you might be using too small of a number.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setExpiration(double expiration) {
        this.expiration = expiration;
        return this;
    }

    /**
     * Make a new expiration by getting the system's current time and adding
     * the provided value to it, making the listener expire a given time (in
     * milliseconds) from now.
     *
     * @param msFromNow how many milliseconds from now before the listener
     *                  will expire.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder makeExpiration(double msFromNow) {
        return setExpiration(Time.ms() + msFromNow);
    }

    /**
     * Set the maximum amount of times the listener is allowed to execute.
     *
     * @param maximumExecutions the amount of times the listener is allowed to
     *                          execute.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setMaximumExecutions(int maximumExecutions) {
        this.maximumExecutions = maximumExecutions;
        return this;
    }

    /**
     * Set the listener's cooldown, in milliseconds.
     *
     * @param cooldownMs the listener's cooldown, in milliseconds.
     * @return {@code this}, used for method chaining.
     */
    public ListenerBuilder setCooldownMs(double cooldownMs) {
        this.cooldownMs = cooldownMs;
        return this;
    }

    /**
     * Build the listener.
     *
     * @return a freshly created {@code Listener}, hand-made just for you!
     */
    public Listener build() {
        Supplier<Boolean>[] inputs = new Supplier[this.inputs.size()];

        return new Listener(
                mode,
                whenTriggered,
                this.inputs.toArray(inputs)
        )
                .setPriority(priority)
                .setExpiration(expiration)
                .setCooldownMs(cooldownMs)
                .setMaximumExecutions(maximumExecutions);
    }
}
