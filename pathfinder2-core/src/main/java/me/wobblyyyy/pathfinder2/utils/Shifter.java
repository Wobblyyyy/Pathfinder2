/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;

/**
 * A controller for moving up and down levels, like some kind of shifter.
 * Using two buttons, you can shift up and down levels.
 *
 * @author Colin Robertson
 * @since 0.8.0
 */
public class Shifter {
    private final int minGear;
    private final int maxGear;
    private final boolean shouldWrap;
    private final Consumer<Integer> onShift;
    private int currentGear;

    /**
     * Create a new shifter.
     *
     * @param minGear     the minimum gear the shifter can be in.
     * @param currentGear the gear the shifter starts in.
     * @param maxGear     the maximum gear the shifter can be in.
     * @param shouldWrap  if you try to shift below the minimum gear or above
     *                    the maximum gear, should the shifter "wrap" around?
     *                    Meaning this: if you shift below the minimum gear,
     *                    should you actually shift into the highest gear?
     * @param onShift     a callback function to be run whenever the shifter
     *                    shifts up or down. This should accept an integer
     *                    parameter, representing the new level.
     */
    public Shifter(
        int minGear,
        int currentGear,
        int maxGear,
        boolean shouldWrap,
        Consumer<Integer> onShift
    ) {
        this.minGear = minGear;
        this.currentGear = currentGear;
        this.maxGear = maxGear;
        this.shouldWrap = shouldWrap;
        this.onShift = onShift;
    }

    public Shifter(int minGear, int currentGear, int maxGear) {
        this(minGear, currentGear, maxGear, false, gear -> {});
    }

    public Shifter(
        int minGear,
        int currentGear,
        int maxGear,
        boolean shouldWrap,
        Supplier<Boolean> shiftUpButton,
        Supplier<Boolean> shiftDownButton,
        Consumer<Integer> onShift,
        Pathfinder pathfinder
    ) {
        this(minGear, currentGear, maxGear, shouldWrap, onShift);
        pathfinder
            .getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () ->
                    SupplierFilter.trueThenAllFalse(
                        shiftUpButton,
                        shiftDownButton
                    ),
                isPressed -> shift(ShifterDirection.UP)
            )
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                () ->
                    SupplierFilter.trueThenAllFalse(
                        shiftDownButton,
                        shiftUpButton
                    ),
                isPressed -> shift(ShifterDirection.DOWN)
            );
    }

    /**
     * Shift the shifter. Or at least try to. If the shifter cannot be
     * shifted (say you're at the minimum gear and wrapping is disabled),
     * nothing happens. If the gear changed, the "on shift" {@link Consumer}
     * will be called.
     *
     * @param direction the direction to shift in.
     */
    public void shift(ShifterDirection direction) {
        int previousGear = currentGear;

        if (direction == ShifterDirection.UP) {
            if (++currentGear > maxGear) if (shouldWrap) currentGear =
                minGear; else currentGear--;
        } else if (direction == ShifterDirection.DOWN) {
            if (--currentGear < minGear) if (shouldWrap) currentGear =
                maxGear; else currentGear++;
        }

        if (previousGear != currentGear) onShift.accept(currentGear);
    }

    public void setGear(int gear) {
        currentGear = Math.max(Math.min(gear, maxGear), minGear);
        if (gear <= maxGear && gear >= minGear) onShift.accept(currentGear);
    }

    public int getMinGear() {
        return minGear;
    }

    public int getCurrentGear() {
        return currentGear;
    }

    public int getMaxGear() {
        return maxGear;
    }
}
