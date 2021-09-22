/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

/**
 * Gear ratio implementation.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class GearRatio {
    private final double inOverOut;
    private final double outOverIn;

    @SuppressWarnings("RedundantCast")
    public GearRatio(int inputTeeth,
                     int outputTeeth) {
        this(
                (double) inputTeeth,
                (double) outputTeeth
        );
    }

    public GearRatio(double howManyIn,
                     double howManyOut) {
        this.inOverOut = howManyIn / howManyOut;
        this.outOverIn = howManyOut / howManyIn;
    }

    public double outputFor(double input) {
        return outOverIn * input;
    }

    public double inputFor(double output) {
        return inOverOut * output;
    }
}
