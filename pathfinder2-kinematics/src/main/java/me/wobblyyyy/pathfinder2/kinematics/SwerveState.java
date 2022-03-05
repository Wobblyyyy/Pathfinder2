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

/**
 * A state for a swerve chassis.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class SwerveState {
    private final SwerveModuleState frontRight;
    private final SwerveModuleState frontLeft;
    private final SwerveModuleState backRight;
    private final SwerveModuleState backLeft;

    /**
     * Create a new {@code SwerveModuleState}.
     *
     * @param frontRight the front right module state.
     * @param frontLeft  the front left module.
     * @param backRight  the back right module state.
     * @param backLeft   the back left module position.
     */
    public SwerveState(
        SwerveModuleState frontRight,
        SwerveModuleState frontLeft,
        SwerveModuleState backRight,
        SwerveModuleState backLeft
    ) {
        this.frontRight = frontRight;
        this.frontLeft = frontLeft;
        this.backRight = backRight;
        this.backLeft = backLeft;
    }

    public SwerveModuleState frontRight() {
        return frontRight;
    }

    public SwerveModuleState frontLeft() {
        return frontLeft;
    }

    public SwerveModuleState backRight() {
        return backRight;
    }

    public SwerveModuleState backLeft() {
        return backLeft;
    }
}
