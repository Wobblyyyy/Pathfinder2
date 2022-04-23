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

public class DifferentialOdometry extends AbstractOdometry {
    private final DifferentialDriveOdometry odometry;
    private final EncoderConverter converter;
    private final Supplier<Integer> getRightTicks;
    private final Supplier<Integer> getLeftTicks;
    private final Supplier<Angle> getGyroAngle;

    private int previousRightTicks;
    private int previousLeftTicks;

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

        int deltaRight = rightTicks - previousRightTicks;
        int deltaLeft = leftTicks - previousLeftTicks;

        previousRightTicks = rightTicks;
        previousLeftTicks = leftTicks;

        double rightDistance = converter.distanceFromTicks(rightTicks);
        double leftDistance = converter.distanceFromTicks(leftTicks);

        return odometry.update(getGyroAngle.get(), rightDistance, leftDistance);
    }
}
