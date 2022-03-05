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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.time.Time;

/**
 * Responsible for listening for a condition and executing some functionality
 * if that condition is met. Listeners allow code to be executed whenever
 * a certain condition is met: for example, your robot could automatically
 * slow down if it's approaching a wall, or it could automatically activate
 * some servo if some condition is met, etc. This is completely synchronous -
 * there's only a single thread, and blocking code will prevent listeners from
 * functioning correctly.
 *
 * <p>
 * Listeners have a variety of configuration options, some of which are
 * disabled by default. Those configuration options are as follows:
 * <ul>
 *     <li>
 *         {@code Priority} is related to the listener's execution order.
 *         A listener with a higher priority will be executed before any
 *         other listeners that are also activated if it has a higher priority
 *         than them. The default priority level is 0. This can be useful in
 *         situations where you need the code of a certain listener to be
 *         executed before the code of another listener because a value updated
 *         in the latter is required in the former.
 *     </li>
 *     <li>
 *         {@code Expiration} is the time the listener will "expire," in
 *         milliseconds from epoch (use {@link Time#ms()}!). This is infinity
 *         by default. Expired listeners are removed from the listener manager
 *         and will no longer be executed. If you need to add a time out to
 *         your listener (it only needs to listen for 10 seconds, say), you'd
 *         give it an expiration date of the current time plus 10 seconds,
 *         like so:
 *         <code>
 *             listener.setExpiration(Time.ms() + 10_000);
 *         </code>
 *     </li>
 *     <li>
 *         {@code Maximum executions} is the maximum amount of times the
 *         listener is allowed to activate before it expires. If you have
 *         a maximum execution of 5, the 5th execution of your listener will
 *         also be its last. By default, this is infinite, meaning there's
 *         no limit on how many times your {@code Listener} can execute.
 *     </li>
 *     <li>
 *         {@code Cooldown} is how long in between activations the listener
 *         will wait. If you need to wait a certain amount of time between
 *         listener execution, a cooldown will be quite helpful. The listener
 *         will not be activated until the amount of milliseconds since the
 *         last time the listener was activated. Cooldown is measured in
 *         milliseconds.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * To simplify the creation of a {@code Listener}, check out the
 * {@link ListenerBuilder} utility class.
 * </p>
 *
 * @author Colin Robertson
 * @see ListenerManager
 * @see ListenerMode
 * @since 0.7.1
 */
public class Listener implements Tickable {
    private final ListenerMode mode;
    private final Runnable whenTriggered;
    private final Supplier<Boolean>[] input;

    private boolean previousInput;
    private boolean hasBeenMet;
    private boolean hasBeenNotMet;
    private int executions;
    private double lastExecMs = 0;

    private int priority;
    private double expiration = Double.MAX_VALUE;
    private int maximumExecutions = Integer.MAX_VALUE;
    private double cooldownMs = 0;

    /**
     * Create a new {@code Listener}.
     *
     * @param mode          the mode the listener should operate in.
     *                      See the {@link ListenerMode} documentation to
     *                      learn more.
     * @param whenTriggered functionality to be executed whenever the
     *                      condition is triggered.
     * @param input         a variable argument parameter: a set of
     *                      {@link Supplier}s that are used for determining
     *                      the listener's input state. For example, if all
     *                      of these suppliers return true, meaning all of the
     *                      required conditions are met, then the whenTriggered
     *                      {@link Runnable} will be invoked. If any of the
     *                      suppliers return false, then the input state is
     *                      false. The only way to trigger the listener
     *                      being "true" is by having all of the suppliers
     *                      provided in this parameter also return true.
     */
    public Listener(
        ListenerMode mode,
        Runnable whenTriggered,
        Supplier<Boolean>... input
    ) {
        this(0, mode, whenTriggered, input);
    }

    /**
     * Create a new {@code Listener}.
     *
     * @param priority      the priority of the listener. Listeners with a
     *                      higher priority will be checked for before
     *                      listeners with a lower priority.
     * @param mode          the mode the listener should operate in.
     *                      See the {@link ListenerMode} documentation to
     *                      learn more.
     * @param whenTriggered functionality to be executed whenever the
     *                      condition is triggered.
     * @param input         a variable argument parameter: a set of
     *                      {@link Supplier}s that are used for determining
     *                      the listener's input state. For example, if all
     *                      of these suppliers return true, meaning all of the
     *                      required conditions are met, then the whenTriggered
     *                      {@link Runnable} will be invoked. If any of the
     *                      suppliers return false, then the input state is
     *                      false. The only way to trigger the listener
     *                      being "true" is by having all of the suppliers
     *                      provided in this parameter also return true.
     */
    public Listener(
        int priority,
        ListenerMode mode,
        Runnable whenTriggered,
        Supplier<Boolean>... input
    ) {
        this.priority = priority;
        this.mode = mode;
        this.whenTriggered = whenTriggered;
        this.input = input;
    }

    public Listener(Listener listener) {
        this.priority = listener.priority;
        this.mode = listener.mode;
        this.whenTriggered = listener.whenTriggered;
        this.input = listener.input;
        this.previousInput = listener.previousInput;
        this.expiration = listener.expiration;
        this.hasBeenMet = listener.hasBeenMet;
        this.hasBeenNotMet = listener.hasBeenNotMet;
        this.maximumExecutions = listener.maximumExecutions;
        this.executions = listener.executions;
        this.cooldownMs = listener.cooldownMs;
        this.lastExecMs = listener.lastExecMs;
    }

    public static Listener withOneInput(
        int priority,
        ListenerMode mode,
        Runnable whenTriggered,
        Supplier<Boolean> input
    ) {
        return new Listener(priority, mode, whenTriggered, input);
    }

    public static Listener buttonHeld(
        Supplier<Boolean> input,
        Runnable whenTriggered
    ) {
        return withOneInput(
            0,
            ListenerMode.CONDITION_IS_MET,
            whenTriggered,
            input
        );
    }

    public static Listener buttonPressed(
        Supplier<Boolean> input,
        Runnable whenTriggered
    ) {
        return withOneInput(
            0,
            ListenerMode.CONDITION_NEWLY_MET,
            whenTriggered,
            input
        );
    }

    public static Listener buttonReleased(
        Supplier<Boolean> input,
        Runnable whenTriggered
    ) {
        return withOneInput(
            0,
            ListenerMode.CONDITION_NEWLY_NOT_MET,
            whenTriggered,
            input
        );
    }

    public static Listener joystick(
        Supplier<Double> x,
        Consumer<Double> whenTriggered
    ) {
        return withOneInput(
            0,
            ListenerMode.CONDITION_IS_MET,
            () -> whenTriggered.accept(x.get()),
            () -> x.get() != 0.0
        );
    }

    public static Listener trigger(
        Supplier<Double> triggerPos,
        Consumer<Double> whenTriggered
    ) {
        return withOneInput(
            0,
            ListenerMode.CONDITION_IS_MET,
            () -> whenTriggered.accept(triggerPos.get()),
            () -> triggerPos.get() > 0.0
        );
    }

    public static Listener joystick(
        Supplier<Double> x,
        Supplier<Double> y,
        BiConsumer<Double, Double> whenTriggered
    ) {
        return withOneInput(
            0,
            ListenerMode.CONDITION_IS_MET,
            () -> whenTriggered.accept(x.get(), y.get()),
            () -> x.get() != 0.0
        );
    }

    public double getExpiration() {
        return expiration;
    }

    public Listener setExpiration(double expiration) {
        this.expiration = expiration;

        return this;
    }

    public boolean hasExpired() {
        return Time.ms() > expiration || executions > maximumExecutions;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * Set the listener's priority.
     *
     * @param priority the priority of the listener. Listeners with a
     *                 higher priority will be checked for before
     *                 listeners with a lower priority.
     * @return {@code this}, used for method chaining.
     */
    public Listener setPriority(int priority) {
        this.priority = priority;

        return this;
    }

    public Listener setMaximumExecutions(int maximumExecutions) {
        this.maximumExecutions = maximumExecutions;

        return this;
    }

    public int getExecutions() {
        return executions;
    }

    public Listener setCooldownMs(double cooldownMs) {
        this.cooldownMs = cooldownMs;

        return this;
    }

    @Override
    public boolean tick(Pathfinder pathfinder) {
        if (executions++ > maximumExecutions) return false;

        double currentMs = Time.ms();
        if (currentMs - lastExecMs < cooldownMs) return true;
        lastExecMs = currentMs;

        boolean input = true;

        for (Supplier<Boolean> supplier : this.input) if (!supplier.get()) {
            input = false;
            break;
        }

        switch (mode) {
            case CONDITION_IS_MET:
                if (input) whenTriggered.run();
                break;
            case CONDITION_IS_NOT_MET:
                if (!input) whenTriggered.run();
                break;
            case CONDITION_NEWLY_MET:
                if (!previousInput && input) whenTriggered.run();
                break;
            case CONDITION_NEWLY_NOT_MET:
                if (previousInput && !input) whenTriggered.run();
                break;
            case CONDITION_NEWLY_CHANGED:
                if (
                    (!previousInput && input) || (previousInput && !input)
                ) whenTriggered.run();
                break;
            case CONDITION_NEVER_MET:
                if (hasBeenMet) setExpiration(0);
                if (hasBeenNotMet) whenTriggered.run();
                break;
            case CONDITION_ALWAYS_MET:
                if (hasBeenNotMet) setExpiration(0);
                if (hasBeenMet) whenTriggered.run();
                break;
        }

        previousInput = input;

        if (!hasBeenMet) hasBeenMet = true;

        if (!hasBeenNotMet) hasBeenNotMet = true;

        return true;
    }
}
