/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.odometry;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.kinematics.SwerveModuleState;

import java.util.function.Supplier;

public class SwerveModuleOdometry {
    private final Supplier<Double> getSpeed;
    private final Supplier<Angle> getAngle;

    public SwerveModuleOdometry(Supplier<Double> getSpeed,
                                Supplier<Angle> getAngle) {
        this.getSpeed = getSpeed;
        this.getAngle = getAngle;
    }

    public double getSpeed() {
        return getSpeed.get();
    }

    public Angle getAngle() {
        return getAngle.get();
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(
                getSpeed(),
                getAngle()
        );
    }
}
