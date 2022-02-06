/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

import me.wobblyyyy.pathfinder2.math.RollingAverage;

import java.util.function.Supplier;

/**
 * Wrapper for sensors that supply double values, meant to smoothen results
 * by averaging past values.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class SensorBuffer implements Supplier<Double> {
    private final Supplier<Double> input;
    private final RollingAverage average;

    public SensorBuffer(Supplier<Double> input,
                        int size) {
        this.input = input;
        this.average = new RollingAverage(size);
    }

    @Override
    public Double get() {
        return average.add(input.get());
    }
}
