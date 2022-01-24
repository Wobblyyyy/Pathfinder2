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

import me.wobblyyyy.pathfinder2.time.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Builder tool for {@link Listener}.
 *
 * @author Colin Robertson
 * @since 0.10.4
 */
public class ListenerBuilder {
    private ListenerMode mode;
    private Runnable whenTriggered;
    private List<Supplier<Boolean>> inputs = new ArrayList<>();
    private int priority;
    private double expiration = Double.MAX_VALUE;
    private int maximumExecutions;
    private double cooldownMs;

    public ListenerBuilder() {

    }

    public ListenerBuilder setMode(ListenerMode mode) {
        this.mode = mode;
        return this;
    }

    public ListenerBuilder setWhenTriggered(Runnable whenTriggered) {
        this.whenTriggered = whenTriggered;
        return this;
    }

    public ListenerBuilder addInput(Supplier<Boolean> input) {
        inputs.add(input);
        return this;
    }

    public ListenerBuilder setInputs(List<Supplier<Boolean>> inputs) {
        this.inputs = inputs;
        return this;
    }

    public ListenerBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public ListenerBuilder setExpiration(double expiration) {
        this.expiration = expiration;
        return this;
    }

    public ListenerBuilder makeExpiration(double msFromNow) {
        return setExpiration(Time.ms() + msFromNow);
    }

    public ListenerBuilder setMaximumExecutions(int maximumExecutions) {
        this.maximumExecutions = maximumExecutions;
        return this;
    }

    public ListenerBuilder setCooldownMs(double cooldownMs) {
        this.cooldownMs = cooldownMs;
        return this;
    }

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
