/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

public class AccelerationProfile {
    private final double acceleration;

    public AccelerationProfile(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDisplacement(double initialVelocity,
                                  double time) {
        // s = ut + 1/2at^2
        return (initialVelocity * time) + (0.5 * time * acceleration);
    }

    public double getDisplacement(double time) {
        return getDisplacement(0, time);
    }
}
