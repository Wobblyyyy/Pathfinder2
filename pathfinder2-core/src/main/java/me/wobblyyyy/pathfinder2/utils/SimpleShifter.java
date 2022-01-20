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

public class SimpleShifter {
    private final int minGear;
    private int currentGear;
    private final int maxGear;
    private final boolean shouldWrap;
    private final Consumer<Integer> onShift;

    public SimpleShifter(int minGear,
                         int currentGear,
                         int maxGear,
                         boolean shouldWrap,
                         Consumer<Integer> onShift) {
        this.minGear = minGear;
        this.currentGear = currentGear;
        this.maxGear = maxGear;
        this.shouldWrap = shouldWrap;
        this.onShift = onShift;
    }

    public SimpleShifter(int minGear,
                         int currentGear,
                         int maxGear) {
        this(
                minGear,
                currentGear,
                maxGear,
                false,
                (gear) -> {}
        );
    }

    public void shift(ShifterDirection direction) {
        if (direction == ShifterDirection.UP) {
            currentGear++;

            if (currentGear > maxGear)
                if (shouldWrap)
                    currentGear = minGear;
                else
                    currentGear--;
        } else if (direction == ShifterDirection.DOWN) {
            currentGear--;

            if (currentGear < minGear)
                if (shouldWrap)
                    currentGear = maxGear;
                else
                    currentGear++;
        }

        onShift.accept(currentGear);
    }

    public void setGear(int gear) {
        currentGear = Math.max(Math.min(gear, maxGear), minGear);
        if (gear <= maxGear && gear >= minGear) onShift.accept(currentGear);
    }
}
