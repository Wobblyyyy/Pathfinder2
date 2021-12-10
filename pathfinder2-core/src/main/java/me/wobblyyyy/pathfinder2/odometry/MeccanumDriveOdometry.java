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
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.kinematics.*;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import me.wobblyyyy.pathfinder2.robot.Encoder;
import me.wobblyyyy.pathfinder2.time.Time;

import java.util.function.Supplier;

/**
 * Odometry for a lovely meccanum chassis! Because who doesn't love those?
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class MeccanumDriveOdometry extends AbstractOdometry {
    private final Supplier<Double> frontRightSpeed;
    private final Supplier<Double> frontLeftSpeed;
    private final Supplier<Double> backRightSpeed;
    private final Supplier<Double> backLeftSpeed;
    private final Supplier<Angle> getGyroAngle;
    private final MeccanumOdometry odometry;

    public MeccanumDriveOdometry(Kinematics<MeccanumState> kinematics,
                                 Supplier<Double> frontRightSpeed,
                                 Supplier<Double> frontLeftSpeed,
                                 Supplier<Double> backRightSpeed,
                                 Supplier<Double> backLeftSpeed,
                                 Supplier<Angle> getGyroAngle) {
        this.frontRightSpeed = frontRightSpeed;
        this.frontLeftSpeed = frontLeftSpeed;
        this.backRightSpeed = backRightSpeed;
        this.backLeftSpeed = backLeftSpeed;
        this.getGyroAngle = getGyroAngle;
        this.odometry = new MeccanumOdometry(
                kinematics,
                getGyroAngle.get(),
                new PointXYZ(0, 0, 0)
        );
    }

    public MeccanumDriveOdometry(MeccanumKinematics kinematics,
                                 EncoderTracker frontRight,
                                 EncoderTracker frontLeft,
                                 EncoderTracker backRight,
                                 EncoderTracker backLeft,
                                 Supplier<Angle> getGyroAngle) {
        this(
                kinematics,
                frontRight::getSpeed,
                frontLeft::getSpeed,
                backRight::getSpeed,
                backLeft::getSpeed,
                getGyroAngle
        );
    }

    public MeccanumDriveOdometry(MeccanumKinematics kinematics,
                                 EncoderConverter converter,
                                 Encoder frontRight,
                                 Encoder frontLeft,
                                 Encoder backRight,
                                 Encoder backLeft,
                                 Supplier<Angle> getGyroAngle) {
        this(
                kinematics,
                new EncoderTracker(converter, frontRight::getTicks),
                new EncoderTracker(converter, frontLeft::getTicks),
                new EncoderTracker(converter, backRight::getTicks),
                new EncoderTracker(converter, backLeft::getTicks),
                getGyroAngle
        );
    }

    @Override
    public PointXYZ getRawPosition() {
        double currentTimeMs = Time.ms();
        Angle gyroAngle = getGyroAngle.get();

        double frSpeed = frontRightSpeed.get();
        double flSpeed = frontLeftSpeed.get();
        double brSpeed = backRightSpeed.get();
        double blSpeed = backLeftSpeed.get();
        MeccanumState state = new MeccanumState(
                frSpeed,
                flSpeed,
                brSpeed,
                blSpeed
        );

        return odometry.updateWithTime(
                currentTimeMs,
                gyroAngle,
                state
        );
    }
}
