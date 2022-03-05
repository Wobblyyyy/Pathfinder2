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

public class RelativeSwerveState {
    private final RelativeSwerveModuleState fr;
    private final RelativeSwerveModuleState fl;
    private final RelativeSwerveModuleState br;
    private final RelativeSwerveModuleState bl;

    public RelativeSwerveState(
        RelativeSwerveModuleState fr,
        RelativeSwerveModuleState fl,
        RelativeSwerveModuleState br,
        RelativeSwerveModuleState bl
    ) {
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;
    }

    public RelativeSwerveModuleState fr() {
        return this.fr;
    }

    public RelativeSwerveModuleState fl() {
        return this.fl;
    }

    public RelativeSwerveModuleState br() {
        return this.br;
    }

    public RelativeSwerveModuleState bl() {
        return this.bl;
    }
}
