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
 * {@code Odometry} implementation that uses two encoders and a gyroscope
 * to determine the position of a tank drive. This uses
 * {@link DifferentialDriveOdometry} to calculate the robot's position.
 *
 * @author Colin Robertson
 * @since 2.3.0
 */
public class DifferentialOdometry extends AbstractOdometry {
    private final DifferentialDriveOdometry odometry;
    private final EncoderConverter converter;
    private final Supplier<Integer> getRightTicks;
    private final Supplier<Integer> getLeftTicks;
    private final Supplier<Angle> getGyroAngle;

    /**
     * Create a new {@code DifferentialOdometry} instance.
     *
     * @param odometry      {@code DifferentialDriveOdometry} instance, used
     *                      to calculate the robot's position. If you're
     *                      unsure of how you should instantiate
     *                      {@code DifferentialDriveOdometry}, just provide
     *                      the gyroscope's current angle as "gyroAngle"
     *                      and {@link PointXYZ#ZERO} as the initial position.
     * @param converter     used in converting encoder tick values to distance
     *                      values. You can generally find the counts per
     *                      revolution and wheel circumference values by
     *                      reading your encoder's documentation and measuring
     *                      your wheel.
     * @param getRightTicks a {@code Supplier<Integer>} that returns the
     *                      ticks of the right encoder. This value should
     *                      come directly from the encoder and should not
     *                      be adjusted.
     * @param getLeftTicks  a {@code Supplier<Integer>} that returns the
     *                      ticks of the left encoder. This value should
     *                      come directly from the encoder and should not
     *                      be adjusted.
     * @param getGyroAngle  a {@code Supplier<Angle>} that returns the
     *                      angle of the chassis.
     */
    public DifferentialOdometry(
        DifferentialDriveOdometry odometry,
        EncoderConverter converter,
        Supplier<Integer> getRightTicks,
        Supplier<Integer> getLeftTicks,
        Supplier<Angle> getGyroAngle
    ) {
        this.odometry = odometry;
        this.converter = converter;
        this.getRightTicks = getRightTicks;
        this.getLeftTicks = getLeftTicks;
        this.getGyroAngle = getGyroAngle;
    }

    @Override
    public PointXYZ getRawPosition() {
        int rightTicks = getRightTicks.get();
        int leftTicks = getLeftTicks.get();

        double rightDistance = converter.distanceFromTicks(rightTicks);
        double leftDistance = converter.distanceFromTicks(leftTicks);

        return odometry.update(getGyroAngle.get(), rightDistance, leftDistance);
    }
}
