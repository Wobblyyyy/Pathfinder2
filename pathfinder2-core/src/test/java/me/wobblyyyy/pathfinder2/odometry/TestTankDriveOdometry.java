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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.kinematics.EncoderConverter;
import me.wobblyyyy.pathfinder2.kinematics.EncoderTracker;
import me.wobblyyyy.pathfinder2.kinematics.GenericOdometry;
import me.wobblyyyy.pathfinder2.kinematics.TankKinematics;
import me.wobblyyyy.pathfinder2.kinematics.TankState;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTankDriveOdometry {
    private static final double TURN_COEFFICIENT = -0.05;
    private static final double ROBOT_WIDTH = 12;
    private static final double ENCODER_CPR = 1_024;
    private static final double WHEEL_CIRCUMFERENCE = 1;

    private TankKinematics kinematics;
    private PointXYZ initialPosition;
    private GenericOdometry<TankState> genericOdometry;

    private EncoderConverter rightConverter;
    private EncoderConverter leftConverter;

    private AtomicInteger rightTicks;
    private AtomicInteger leftTicks;
    private AtomicReference<Angle> gyroAngle;

    private EncoderTracker rightTracker;
    private EncoderTracker leftTracker;

    private TankDriveOdometry odometry;

    @BeforeEach
    public void beforeEach() {
        // track width of 12 inches
        kinematics = new TankKinematics(TURN_COEFFICIENT, ROBOT_WIDTH);
        initialPosition = PointXYZ.ZERO;
        genericOdometry =
            new GenericOdometry<>(
                kinematics,
                initialPosition.z(),
                initialPosition
            );

        rightConverter = new EncoderConverter(ENCODER_CPR, WHEEL_CIRCUMFERENCE);
        leftConverter = new EncoderConverter(ENCODER_CPR, WHEEL_CIRCUMFERENCE);

        rightTicks = new AtomicInteger(0);
        leftTicks = new AtomicInteger(0);
        gyroAngle = new AtomicReference<>(new Angle());

        rightTracker = new EncoderTracker(rightConverter, rightTicks::get);
        leftTracker = new EncoderTracker(leftConverter, leftTicks::get);

        odometry =
            new TankDriveOdometry(
                rightTracker,
                leftTracker,
                gyroAngle::get,
                genericOdometry
            );
    }

    private void updateAndAssert(
        double right,
        double left,
        PointXYZ target,
        double time
    ) {
        PointXYZ position = odometry.updateWithTime(right, left, time);

        AssertionUtils.assertIsNear(target, position, 0.01, Angle.fromDeg(5));
    }

    @Test
    public void testMoveStraightForwards() {
        updateAndAssert(0, 0, new PointXYZ(0, 0, 0), 0);
        updateAndAssert(1, 1, new PointXYZ(0, 1, 0), 1_000);
        updateAndAssert(1, 1, new PointXYZ(0, 2, 0), 2_000);
        updateAndAssert(1, 1, new PointXYZ(0, 2.5, 0), 2_500);
        updateAndAssert(0.5, 0.5, new PointXYZ(0, 3, 0), 3_500);
    }

    @Test
    public void testMoveStraightBackwards() {
        updateAndAssert(0, 0, new PointXYZ(0, 0, 0), 0);
        updateAndAssert(-1, -1, new PointXYZ(0, -1, 0), 1_000);
        updateAndAssert(-1, -1, new PointXYZ(0, -2, 0), 2_000);
        updateAndAssert(-1, -1, new PointXYZ(0, -2.5, 0), 2_500);
        updateAndAssert(-0.5, -0.5, new PointXYZ(0, -3, 0), 3_500);
    }
}
