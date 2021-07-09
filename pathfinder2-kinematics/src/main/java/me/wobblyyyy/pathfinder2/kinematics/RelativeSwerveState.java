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
    private SwerveModuleState fr;
    private SwerveModuleState fl;
    private SwerveModuleState br;
    private SwerveModuleState bl;

    public RelativeSwerveState(SwerveModuleState fr,
                               SwerveModuleState fl,
                               SwerveModuleState br,
                               SwerveModuleState bl) {
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;
    }

    public SwerveModuleState fr() {
        return this.fr;
    }

    public SwerveModuleState fl() {
        return this.fl;
    }

    public SwerveModuleState br() {
        return this.br;
    }

    public SwerveModuleState bl() {
        return this.bl;
    }
}
