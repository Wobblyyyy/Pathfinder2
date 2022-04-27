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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.math.Average;

/**
 * Odometry for a differential drive train. This uses a gyroscope and two
 * encoders to determine the robot's position.
 *
 * <p>
 * This is one of the few odometry implementations that does not use
 * velocity to determine the robot's position. Such, time is not involved!
 * </p>
 *
 * @author Colin Robertson
 * @since 2.3.0
 */
public class DifferentialDriveOdometry {
    private PointXYZ position;

    private Angle gyroOffset;
    private Angle previousAngle;

    private double previousLeftDistance;
    private double previousRightDistance;

    /**
     * Create a new instance of {@code DifferentialDriveOdometry}.
     *
     * @param gyroAngle       a {@code Supplier<Angle>} that gets the angle
     *                        of the gyroscope. This is used in calculating
     *                        the gyroscope angle offset.
     * @param initialPosition the initial position for the odometry.
     * @see #resetPosition(PointXYZ, Angle)
     */
    public DifferentialDriveOdometry(
        Angle gyroAngle,
        PointXYZ initialPosition
    ) {
        this.position = initialPosition;
        this.gyroOffset = initialPosition.z().subtract(gyroAngle);
        this.previousAngle = initialPosition.z();
    }

    /**
     * Reset the position of the odometry.
     *
     * @param position  the new position.
     * @param gyroAngle the gyroscope's current angle.
     */
    public void resetPosition(PointXYZ position, Angle gyroAngle) {
        this.position = position;
        this.gyroOffset = position.z().subtract(gyroAngle);
        this.previousAngle = position.z();
    }

    /**
     * Get the robot's position, without updating it.
     *
     * @return the robot's position.
     */
    public PointXYZ getPosition() {
        return position;
    }

    /**
     * Update the robot's position. After updating the position, return the
     * newly-updated position.
     *
     * @param gyroAngle     the angle of the gyroscope.
     * @param rightDistance the distance of the right encoder. This should
     *                      NOT be the "elapsed distance," or distance since
     *                      the last update: rather, this should be the
     *                      encoder's distance, in total.
     * @param leftDistance  the distance of the left encoder.
     *                      NOT be the "elapsed distance," or distance since
     *                      the last update: rather, this should be the
     *                      encoder's distance, in total.
     * @return the robot's updated posititon.
     */
    public PointXYZ update(
        Angle gyroAngle,
        double rightDistance,
        double leftDistance
    ) {
        double deltaRight = rightDistance - previousRightDistance;
        double deltaLeft = leftDistance - previousLeftDistance;

        previousRightDistance = rightDistance;
        previousLeftDistance = leftDistance;

        double averageDelta = Average.of(deltaRight, deltaLeft);
        Angle angle = gyroAngle.add(gyroOffset);

        PointXYZ newPosition = position.applyTranslation(
            new Translation(
                0.0,
                averageDelta,
                angle.subtract(previousAngle).deg()
            )
        );

        previousAngle = angle;

        position = newPosition.withZ(angle);

        return position;
    }
}
