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

public class TankState {
    private final double right;
    private final double left;

    public TankState(double right,
                     double left) {
        this.right = right;
        this.left = left;
    }

    public double right() {
        return right;
    }

    public double left() {
        return left;
    }

    public TankState add(TankState state) {
        return new TankState(
                right + state.right,
                left + state.left
        );
    }

    public TankState multiply(TankState state) {
        return new TankState(
                right * state.right,
                left * state.left
        );
    }

    public TankState multiply(double multiplier) {
        return new TankState(
                right * multiplier,
                left * multiplier
        );
    }
}
