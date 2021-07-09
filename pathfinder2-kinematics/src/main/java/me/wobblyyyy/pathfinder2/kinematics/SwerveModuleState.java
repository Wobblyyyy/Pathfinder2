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

public class SwerveModuleState {
    private final double turn;
    private final double drive;

    public SwerveModuleState(double turn,
                             double drive) {
        this.turn = turn;
        this.drive = drive;
    }

    public double getTurn() {
        return this.turn;
    }

    public double getDrive() {
        return this.drive;
    }
}
