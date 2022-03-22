/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A state for a tank (or differential) drive train.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class TankState {
    private final double right;
    private final double left;

    /**
     * Create a new {@code TankState}.
     *
     * @param right the state of the right side of the drive train.
     * @param left  the state of the left side of the drive train.
     */
    public TankState(double right, double left) {
        this.right = right;
        this.left = left;
    }

    /**
     * Get the state's right component.
     *
     * @return the state's right component.
     */
    public double right() {
        return right;
    }

    /**
     * Get the state's left component.
     *
     * @return the state's left component.
     */
    public double left() {
        return left;
    }

    public TankState add(TankState state) {
        return new TankState(right + state.right, left + state.left);
    }

    public TankState addRight(double right) {
        return add(new TankState(right, 0));
    }

    public TankState addLeft(double left) {
        return add(new TankState(0, left));
    }

    public TankState add(double right, double left) {
        return add(new TankState(right, left));
    }

    public TankState multiply(TankState state) {
        return new TankState(right * state.right, left * state.left);
    }

    public TankState multiply(double multiplier) {
        return new TankState(right * multiplier, left * multiplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TankState) {
            TankState t = (TankState) obj;

            boolean sameRight = Equals.soft(right, t.right, 0.01);
            boolean sameLeft = Equals.soft(left, t.left, 0.01);

            return sameRight && sameLeft;
        }

        return false;
    }

    @Override
    public String toString() {
        return StringUtils.format("(r: %s, l: %s)", right, left);
    }
}
