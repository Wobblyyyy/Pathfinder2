/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.drive;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.kinematics.SwerveModuleState;
import me.wobblyyyy.pathfinder2.robot.components.Motor;

import java.util.function.Supplier;

public class SwerveModule {
    private final Motor turn;
    private final Motor drive;
    private final Supplier<Angle> getAngle;

    public SwerveModule(Motor turn,
                        Motor drive,
                        Supplier<Angle> getAngle) {
        this.turn = turn;
        this.drive = drive;
        this.getAngle = getAngle;
    }

    public Motor turn() {
        return this.turn;
    }

    public Motor drive() {
        return this.drive;
    }

    public void set(SwerveModuleState state) {
        turn.setPower(state.getTurn());
        drive.setPower(state.getDrive());
    }

    public Angle getAngle() {
        return getAngle.get();
    }
}
