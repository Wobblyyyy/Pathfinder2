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

import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.kinematics.RelativeSwerveModuleState;
import me.wobblyyyy.pathfinder2.robot.components.Motor;

/**
 * A representation of a swerve module. In a typical swerve drive chassis,
 * there are four swerve modules - the front right, the front left, the
 * back right, and the back left modules. Each of these modules is composed
 * of two motors - the turn motor and the drive motor. In short, the turn
 * motor can control the angle at which the drive motor is facing, and the
 * drive motor can actually drive the robot.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SwerveModule {
    /**
     * The module's turn motor.
     */
    private final Motor turn;

    /**
     * The module's drive motor.
     */
    private final Motor drive;

    /**
     * A supplier to access the angle at which the module is currently
     * facing.
     */
    private final Supplier<Angle> getAngle;

    /**
     * Create a new {@code SwerveModule}.
     *
     * @param turn     the module's turn motor. This is the motor that controls
     *                 the angle the drive motor is facing, effectively
     *                 changing which way the chassis moves.
     * @param drive    the module's drive motor. This is the motor actually
     *                 responsible for applying force to the ground and
     *                 moving the chassis in whatever direction the module
     *                 is currently facing.
     * @param getAngle a supplier to access the module's angle. This angle
     *                 is relative to the module, not the field - 0 degrees,
     *                 for example, should always be the same for the module.
     *                 Even if the robot turns 45 degrees, 0 degrees should
     *                 be exactly the same, relative to the robot. This
     *                 angle should almost always come from an encoder on
     *                 the turn motor.
     */
    public SwerveModule(Motor turn, Motor drive, Supplier<Angle> getAngle) {
        this.turn = turn;
        this.drive = drive;
        this.getAngle = getAngle;
    }

    /**
     * Get the module's turn motor.
     *
     * @return the module's turn motor.
     */
    public Motor turn() {
        return this.turn;
    }

    /**
     * Get the module's drive motor.
     *
     * @return the module's drive motor.
     */
    public Motor drive() {
        return this.drive;
    }

    /**
     * Set a swerve module state to the swerve module. This will set power
     * to both the turn and drive motors.
     *
     * @param state the state to set to the turn module.
     */
    public void set(RelativeSwerveModuleState state) {
        turn.setPower(state.getTurn());
        drive.setPower(state.getDrive());
    }

    /**
     * Get the angle at which the swerve module is currently facing.
     *
     * @return the angle the swerve module is currently facing.
     */
    public Angle getAngle() {
        return getAngle.get();
    }
}
