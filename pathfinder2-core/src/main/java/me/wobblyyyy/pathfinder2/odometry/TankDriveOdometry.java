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
import me.wobblyyyy.pathfinder2.kinematics.DifferentialDriveOdometry;
import me.wobblyyyy.pathfinder2.kinematics.EncoderConverter;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;

/**
 * Odometry for a tank drive chassis. This uses two encoders - one on the
 * right side of the chassis and one on the left side of the chassis - to
 * calculate the robot's position. Based on that velocity, the robot's position
 * will be calculated.
 *
 * @author Colin Robertson
 * @since 2.2.0
 */
public class TankDriveOdometry extends AbstractOdometry {
    private final DifferentialDriveOdometry odometry;
    private final Supplier<Double> getRightDistance;
    private final Supplier<Double> getLeftDistance;
    private final Supplier<Angle> getGyroAngle;

    /**
     * Create a new instance of {@code TankDriveOdometry}.
     *
     * @param getRightDistance a {@code Supplier} that returns the distance
     *                         the right side of the chassis has moved.
     * @param getLeftDistance  a {@code Supplier} that returns the distance
     *                         the left side of the chassis has moved.
     * @param getGyroAngle     a {@code Supplier} that returns the angle of
     *                         the chassis.
     */
    public TankDriveOdometry(
        Supplier<Double> getRightDistance,
        Supplier<Double> getLeftDistance,
        Supplier<Angle> getGyroAngle
    ) {
        this(
            new DifferentialDriveOdometry(getGyroAngle.get(), PointXYZ.ZERO),
            getRightDistance,
            getLeftDistance,
            getGyroAngle
        );
    }

    /**
     * Create a new instance of {@code TankDriveOdometry}.
     *
     * @param odometry         the {@code DifferentialDriveOdometry} instance
     *                         used in calculating the robot's position.
     * @param getRightDistance a {@code Supplier} that returns the distance
     *                         the right side of the chassis has moved.
     * @param getLeftDistance  a {@code Supplier} that returns the distance
     *                         the left side of the chassis has moved.
     * @param getGyroAngle     a {@code Supplier} that returns the angle of
     *                         the chassis.
     */
    public TankDriveOdometry(
        DifferentialDriveOdometry odometry,
        Supplier<Double> getRightDistance,
        Supplier<Double> getLeftDistance,
        Supplier<Angle> getGyroAngle
    ) {
        this.odometry = odometry;
        this.getRightDistance = getRightDistance;
        this.getLeftDistance = getLeftDistance;
        this.getGyroAngle = getGyroAngle;
    }

    /**
     * Create a new instance of {@code TankDriveOdometry}.
     *
     * @param converter            a converter that converts encoder counts
     *                             into distance traveled.
     * @param getRightEncoderCount a {@code Supplier} that returns the
     *                             right encoder's total counts.
     * @param getLeftEncoderCount  a {@code Supplier} that returns the
     *                             left encoder's total counts.
     * @param getGyroAngle         a {@code Supplier} that returns the angle
     *                             the chassis is currently facing.
     */
    public TankDriveOdometry(
        EncoderConverter converter,
        Supplier<Integer> getRightEncoderCount,
        Supplier<Integer> getLeftEncoderCount,
        Supplier<Angle> getGyroAngle
    ) {
        this(
            new DifferentialDriveOdometry(getGyroAngle.get(), PointXYZ.ZERO),
            converter,
            getRightEncoderCount,
            getLeftEncoderCount,
            getGyroAngle
        );
    }

    /**
     * Create a new instance of {@code TankDriveOdometry}.
     *
     * @param odometry             the odometry instance used in calculating
     *                             the robot's position.
     * @param converter            a converter that converts encoder counts
     *                             into distance traveled.
     * @param getRightEncoderCount a {@code Supplier} that returns the
     *                             right encoder's total counts.
     * @param getLeftEncoderCount  a {@code Supplier} that returns the
     *                             left encoder's total counts.
     * @param getGyroAngle         a {@code Supplier} that returns the angle
     *                             the chassis is currently facing.
     */
    public TankDriveOdometry(
        DifferentialDriveOdometry odometry,
        EncoderConverter converter,
        Supplier<Integer> getRightEncoderCount,
        Supplier<Integer> getLeftEncoderCount,
        Supplier<Angle> getGyroAngle
    ) {
        this(
            odometry,
            () -> converter.distanceFromTicks(getRightEncoderCount.get()),
            () -> converter.distanceFromTicks(getLeftEncoderCount.get()),
            getGyroAngle
        );
    }

    @Override
    public PointXYZ getRawPosition() {
        Angle angle = getGyroAngle.get();
        double right = getRightDistance.get();
        double left = getLeftDistance.get();

        return odometry.update(angle, right, left);
    }
}
