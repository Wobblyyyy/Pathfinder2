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
 * Odometry for a tank drive.
 *
 * @author Colin Robertson
 * @since 2.2.0
 */
public class TankDriveOdometry extends AbstractOdometry {
    private final EncoderTracker rightTracker;
    private final EncoderTracker leftTracker;
    private final Supplier<Angle> getGyroAngle;
    private final GenericOdometry<TankState> tankOdometry;

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

    @Override
    public PointXYZ getRawPosition() {
        double rightSpeed = rightTracker.getSpeed();
        double leftSpeed = leftTracker.getSpeed();

        TankState state = new TankState(rightSpeed, leftSpeed);

        return tankOdometry.updateWithTime(
            Time.ms(),
            getGyroAngle.get(),
            state
        );
    }
}
