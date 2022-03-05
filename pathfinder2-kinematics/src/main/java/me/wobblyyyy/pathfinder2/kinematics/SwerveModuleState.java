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

import me.wobblyyyy.pathfinder2.geometry.Angle;

public class SwerveModuleState {
    private final double speed;
    private final Angle direction;

    public SwerveModuleState(double speed, Angle direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public double speed() {
        return speed;
    }

    public Angle direction() {
        return direction;
    }

    public SwerveModuleState add(SwerveModuleState state) {
        return new SwerveModuleState(
            speed + state.speed,
            direction.add(state.direction)
        );
    }
}
