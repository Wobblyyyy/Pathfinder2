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
import me.wobblyyyy.pathfinder2.kinematics.EncoderTracker;
import me.wobblyyyy.pathfinder2.kinematics.GenericOdometry;
import me.wobblyyyy.pathfinder2.kinematics.TankState;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import me.wobblyyyy.pathfinder2.time.Time;

/**
 * Odometry for a tank drive chassis. This uses two encoders - one on the
 * right side of the chassis and one on the left side of the chassis - to
 * measure the robot's velocity over time. Based on that velocity, the robot's
 * position will be calculated.
 *
 * @author Colin Robertson
 * @since 2.2.0
 */
public class TankDriveOdometry extends AbstractOdometry {
    private final EncoderTracker rightTracker;
    private final EncoderTracker leftTracker;
    private final Supplier<Angle> getGyroAngle;
    private final GenericOdometry<TankState> tankOdometry;

    /**
     * Create a new instance of {@TankDriveOdometry}.
     *
     * @param rightTracker an {@code EncoderTracker} for the right side of
     *                     the tank drive chassis.
     * @param leftTracker  an {@code EncoderTracker} for the left side of
     *                     the tank drive chassis.
     * @param getGyroAngle a {@code Supplier<Angle>} that returns the angle
     *                     of the chassis, as determined by a gyroscope.
     * @param tankOdometry an instance of {@code GenericOdometry<TankState>}
     *                     that is used in calculating the robot's position.
     */
    public TankDriveOdometry(
        EncoderTracker rightTracker,
        EncoderTracker leftTracker,
        Supplier<Angle> getGyroAngle,
        GenericOdometry<TankState> tankOdometry
    ) {
        this.rightTracker = rightTracker;
        this.leftTracker = leftTracker;
        this.getGyroAngle = getGyroAngle;
        this.tankOdometry = tankOdometry;
    }

    public PointXYZ updateWithTime(
        double rightSpeed,
        double leftSpeed,
        double time
    ) {
        TankState state = new TankState(rightSpeed, leftSpeed);

        return tankOdometry.updateWithTime(time, getGyroAngle.get(), state);
    }

    public PointXYZ updateWithTime(double time) {
        double rightSpeed = rightTracker.getSpeed();
        double leftSpeed = leftTracker.getSpeed();

        return updateWithTime(rightSpeed, leftSpeed, time);
    }

    @Override
    public PointXYZ getRawPosition() {
        double time = Time.ms();

        return updateWithTime(time);
    }
}
