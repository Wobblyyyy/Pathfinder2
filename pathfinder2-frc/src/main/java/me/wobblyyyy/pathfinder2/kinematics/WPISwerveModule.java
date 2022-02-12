/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.robot.components.Motor;
import me.wobblyyyy.pathfinder2.robot.sensors.AngleEncoder;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.wpilib.WPIAdapter;

/**
 * A wpilib-specific implementation of a swerve module. This is preferable to
 * Pathfinder's own swerve module implementation because I'm really bad at math,
 * so using this (which is confirmed to work) instead of my (probably wrong
 * math) (that's not exactly confirmed to work perfectly) is a pretty good
 * idea if I do say so myself.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class WPISwerveModule {
    private final Motor turnMotor;
    private final AngleEncoder turnEncoder;
    private final PIDController turnController;

    private final Motor driveMotor;

    private final boolean shouldOptimize;
    private final Translation2d modulePosition;

    /**
     * Create a new {@code WPISwerveModule}.
     *
     * @param turnMotor      the motor responsible for controlling the
     *                       angle the swerve module is facing. This motor
     *                       should rotate the drive motor, changing the
     *                       direction the module is moving in.
     * @param turnEncoder    the encoder responsible for tracking the angle
     *                       the swerve module is currently facing. This should
     *                       report the angle of a single swerve module.
     * @param turnController the {@code PIDController} responsible for dictating
     *                       what power the {@code turnMotor} will move at in
     *                       order to make the swerve module face a specified
     *                       angle, as determined with the {@code turnEncoder}.
     * @param driveMotor     the motor responsible for actually driving the
     *                       robot.
     * @param shouldOptimize should the {@code WPISwerveModule} automatically
     *                       perform swerve module state optimization with
     *                       {@link SwerveModuleState#optimize(SwerveModuleState, Rotation2d)}
     *                       whenever a new state is set to the module using
     *                       {@link #setState(SwerveModuleState)}? For most
     *                       use cases, this will improve the speed at which
     *                       your robot can navigate.
     * @param modulePosition the swerve module's position. This is used for
     *                       swerve drive kinematics and should represent the
     *                       module's position relative to the center of
     *                       the robot.
     */
    public WPISwerveModule(Motor turnMotor,
                           AngleEncoder turnEncoder,
                           PIDController turnController,
                           Motor driveMotor,
                           boolean shouldOptimize,
                           Translation2d modulePosition) {
        if (turnMotor == null)
            throw new RuntimeException("cannot have null turn motor!");
        if (turnEncoder == null)
            throw new RuntimeException("cannot have null turn encoder!");
        if (turnController == null)
            throw new RuntimeException("cannot have null turn controller!");
        if (driveMotor == null)
            throw new RuntimeException("cannot have null drive motor!");
        if (modulePosition == null)
            throw new RuntimeException("cannot have null module position!");

        this.turnMotor = turnMotor;
        this.turnEncoder = turnEncoder;
        this.turnController = turnController;
        this.driveMotor = driveMotor;
        this.shouldOptimize = shouldOptimize;
        this.modulePosition = modulePosition;
    }

    /**
     * Get the angle the module is currently facing.
     *
     * @return the angle the module is currently facing.
     */
    public Angle getTurnAngle() {
        return turnEncoder.getAngle();
    }

    /**
     * Get the {@link Rotation2d} the module is currently facing.
     *
     * @return the {@link Rotation2d} the module is currently facing.
     */
    public Rotation2d getTurnRotation() {
        return WPIAdapter.rotationFromAngle(getTurnAngle());
    }

    /**
     * Set the state of the swerve module. If {@link #shouldOptimize} is
     * enabled, this will also optimize the swerve module state using
     * {@link SwerveModuleState#optimize(SwerveModuleState, Rotation2d)}
     *
     * @param state the state to set to the swerve module. This state's
     *              {@link SwerveModuleState#speedMetersPerSecond} value
     *              should be a percent output value, meaning it fits within
     *              the bounds of -1 to 1, inclusive.
     */
    public void setState(SwerveModuleState state) {
        if (state == null)
            throw new IllegalArgumentException(
                    "cannot set a swerve module's state to a null object! " +
                            "the state you tried to set to the swerve module " +
                            "was null, make sure it is NOT"
            );
        if (shouldOptimize)
            state = SwerveModuleState.optimize(
                    state,
                    getTurnRotation()
            );

        double turnPower = turnController.calculate(
                getTurnAngle().rad(),
                state.angle.getRadians()
        );

        turnMotor.setPower(turnPower);
        driveMotor.setPower(state.speedMetersPerSecond);
    }

    /**
     * Get the module's turn motor.
     *
     * @return the module's turn motor.
     */
    public Motor getTurnMotor() {
        return turnMotor;
    }

    /**
     * Get the module's turn encoder.
     *
     * @return the module's turn encoder.
     */
    public AngleEncoder getTurnEncoder() {
        return turnEncoder;
    }

    /**
     * Get the module's turn controller.
     *
     * @return the module's turn controller.
     */
    public PIDController getTurnController() {
        return turnController;
    }

    /**
     * Get the module's drive motor.
     *
     * @return the module's drive motor.
     */
    public Motor getDriveMotor() {
        return driveMotor;
    }

    /**
     * Get whether the swerve module should optimize inputted swerve
     * module states.
     *
     * @return whether the swerve module should optimize inputted swerve
     * module states.
     */
    public boolean getShouldOptimize() {
        return shouldOptimize;
    }

    /**
     * Get the swerve module's position.
     *
     * @return the swerve module's position.
     */
    public Translation2d getModulePosition() {
        return modulePosition;
    }

    @Override
    public int hashCode() {
        int turn = turnMotor.hashCode();
        int drive = driveMotor.hashCode();

        return turn + drive;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WPISwerveModule) {
            WPISwerveModule m = (WPISwerveModule) obj;

            boolean sameTurnMotor = m.turnMotor.equals(this.turnMotor);
            boolean sameDriveMotor = m.driveMotor.equals(this.driveMotor);

            return sameTurnMotor && sameDriveMotor;
        }

        return false;
    }

    /**
     * Convert the swerve module into a string by creating a string containing
     * the module's current angle (in degrees) and the module's drive motor's
     * current power (between -1 and 1).
     *
     * @return {@code "(turn pos: %s, drive speed: %s)"}
     */
    @Override
    public String toString() {
        return StringUtils.format(
                "(turn pos: %s, drive speed: %s)",
                getTurnAngle().formatAsDegShort(),
                driveMotor.getPower()
        );
    }
}
