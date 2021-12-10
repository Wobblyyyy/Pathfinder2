/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.robot.components.Motor;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AbstractEncodedMotor extends AbstractEncoder implements Motor {
    private final Consumer<Double> setPower;
    private double lastPower;
    private final Supplier<Integer> getTicks;

    public AbstractEncodedMotor(Consumer<Double> setPower,
                                Supplier<Integer> getTicks) {
        this.setPower = setPower;
        this.getTicks = getTicks;
    }

    @Override
    public int getRawTicks() {
        return getTicks.get();
    }

    @Override
    public double getPower() {
        return lastPower;
    }

    @Override
    public void setPower(double power) {
        lastPower = power;
        setPower.accept(power);
    }
}
