/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A configuration Pathfinder can use while using the {@code tickUntil}
 * method. This allows you to customize how long Pathfinder is ticked for,
 * if there's a delay, what conditions must be met in order to continue
 * ticking, what conditions may not be met in order to continue ticking,
 * what code will be executed after each tick, and what code will be executed
 * after the entire ticking ordeal has finished.
 *
 * @since 3.0.0
 * @author Colin Robertson
 */
public class TickConfig {
    private double delayMs = 0;
    private double timeoutMs = Double.MAX_VALUE;
    private List<Supplier<Boolean>> shouldContinueRunning = new ArrayList<>(3);
    private List<Supplier<Boolean>> shouldStopRunning = new ArrayList<>(3);
    private List<Runnable> onTick = new ArrayList<>(3);
    private List<Runnable> onFinish = new ArrayList<>(3);

    /**
     * Set the configuration's delay, in milliseconds. The default delay is
     * 0. Pathfinder will not be ticked while a delay is active.
     *
     * @param delayMs the configuration's delay, in milliseconds.
     * @return {@code this}, used for method chaining.
     */
    public TickConfig setDelayMs(double delayMs) {
        this.delayMs = delayMs;
        return this;
    }

    /**
     * Set the configuration's timeout, in milliseconds.
     *
     * @param timeoutMs the configuration's timeout, in milliseconds.
     * @return {@code this}, used for method chaining.
     */
    public TickConfig setTimeoutMs(double timeoutMs) {
        this.timeoutMs = timeoutMs;
        return this;
    }

    /**
     * Add a condition that must be met in order for Pathfinder for to
     * continue ticking. If this condition is not met, the {@code tickUntil}
     * method will terminate.
     *
     * @param supplier the condition that must be met in order to continue
     *                 ticking.
     * @return {@code this}, used for method chaining.
     */
    public TickConfig addShouldContinueRunning(Supplier<Boolean> supplier) {
        shouldContinueRunning.add(supplier);
        return this;
    }

    /**
     * Add a condition that may not be met in order for Pathfinder for to
     * continue ticking. If this condition is met, the {@code tickUntil}
     * method's execution will terminate.
     *
     * @param supplier the condition that must be met in order to continue
     *                 ticking.
     * @return {@code this}, used for method chaining.
     */
    public TickConfig addShouldStopRunning(Supplier<Boolean> supplier) {
        shouldStopRunning.add(supplier);
        return this;
    }

    /**
     * Add a bit of code that will be executed after each time the
     * {@code tick} method is called.
     *
     * @param onTick the code to be executed after each tick.
     * @return {@code this}, used for method chaining.
     */
    public TickConfig addOnTick(Runnable onTick) {
        this.onTick.add(onTick);
        return this;
    }

    /**
     * Add a bit of code that will be executed after the entire ticking
     * loop has finished.
     *
     * @param onFinish the code to be executed after the entire ticking
     *                 loop has finished its execution.
     * @return {@code this}, used for method chaining.
     */
    public TickConfig addOnFinish(Runnable onFinish) {
        this.onFinish.add(onFinish);
        return this;
    }

    public double getDelayMs() {
        return delayMs;
    }

    public double getTimeoutMs() {
        return timeoutMs;
    }

    public List<Supplier<Boolean>> getShouldContinueRunning() {
        return shouldContinueRunning;
    }

    public List<Supplier<Boolean>> getShouldStopRunning() {
        return shouldStopRunning;
    }

    public List<Runnable> getOnTick() {
        return onTick;
    }

    public List<Runnable> getOnFinish() {
        return onFinish;
    }
}
