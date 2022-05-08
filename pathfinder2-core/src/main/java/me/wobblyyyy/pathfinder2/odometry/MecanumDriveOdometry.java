/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.odometry;

import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.kinematics.EncoderConverter;
import me.wobblyyyy.pathfinder2.kinematics.EncoderTracker;
import me.wobblyyyy.pathfinder2.kinematics.GenericOdometry;
import me.wobblyyyy.pathfinder2.kinematics.MecanumState;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import me.wobblyyyy.pathfinder2.robot.sensors.Encoder;
import me.wobblyyyy.pathfinder2.time.Time;

/**
 * Odometry for a mecanum drive.
 *
 * @author Colin Robertson
 * @since 3.0.0
 */
public class MecanumDriveOdometry extends AbstractOdometry {
    private final GenericOdometry<MecanumState> odometry;
    private final Supplier<Angle> getGyroAngle;
    private final EncoderTracker frontRightTracker;
    private final EncoderTracker frontLeftTracker;
    private final EncoderTracker backRightTracker;
    private final EncoderTracker backLeftTracker;

    public MecanumDriveOdometry(
        GenericOdometry<MecanumState> odometry,
        Supplier<Angle> getGyroAngle,
        EncoderConverter converter,
        Encoder frontRight,
        Encoder frontLeft,
        Encoder backRight,
        Encoder backLeft
    ) {
        this.odometry = odometry;
        this.getGyroAngle = getGyroAngle;

        frontRightTracker = new EncoderTracker(converter, frontRight::getTicks);
        frontLeftTracker = new EncoderTracker(converter, frontLeft::getTicks);
        backRightTracker = new EncoderTracker(converter, backRight::getTicks);
        backLeftTracker = new EncoderTracker(converter, backLeft::getTicks);
    }

    private MecanumState getState() {
        double frontRightVelocity = frontRightTracker.getSpeed();
        double frontLeftVelocity = frontLeftTracker.getSpeed();
        double backRightVelocity = backRightTracker.getSpeed();
        double backLeftVelocity = backLeftTracker.getSpeed();

        return new MecanumState(
            frontLeftVelocity,
            frontRightVelocity,
            backLeftVelocity,
            backRightVelocity
        );
    }

    @Override
    public PointXYZ getRawPosition() {
        double currentTimeMs = Time.ms();
        Angle gyroAngle = getGyroAngle.get();
        MecanumState state = getState();

        return odometry.updateWithTime(currentTimeMs, gyroAngle, state);
    }
}
