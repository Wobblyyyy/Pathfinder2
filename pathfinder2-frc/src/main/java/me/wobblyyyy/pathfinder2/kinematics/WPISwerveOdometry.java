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

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import me.wobblyyyy.pathfinder2.robot.sensors.Encoder;
import me.wobblyyyy.pathfinder2.robot.sensors.Gyroscope;
import me.wobblyyyy.pathfinder2.wpilib.WPIAdapter;

/**
 * wpilib-specific swerve drive odometry implementation based on
 * {@link WPISwerveChassis}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class WPISwerveOdometry extends AbstractOdometry {
    private final WPISwerveChassis chassis;
    private final Gyroscope gyroscope;
    private final double velocityToUnitsPerSec;
    private Pose2d pose;
    private PointXYZ position;

    private final SwerveDriveOdometry odometry;
    private final Encoder[] driveEncoders;

    /**
     * Create a new {@code WPISwerveOdometry}.
     *
     * @param chassis               the swerve chassis.
     * @param gyroscope             a gyroscope that reports the robot's
     *                              current angle.
     * @param initialPosition       the robot's initial position.
     * @param velocityToUnitsPerSec the multiplier that is used to convert
     *                              ticks per second into units per second.
     *                              If a motor is spinning at 4096 ticks per
     *                              second (the motor's velocity), what value
     *                              should that velocity be multiplied by in
     *                              order to provide the robot's velocity in
     *                              whatever units Pathfinder is using, instead
     *                              of ticks.
     * @param driveEncoders         an array of encoders that track the position
     *                              of each drive wheel. Each of these
     *                              encoders should correspond to a swerve
     *                              module on the chassis. If you have four
     *                              swerve modules labelled front right, front
     *                              left, back right, and back left, the
     *                              encoders in this array should be, with
     *                              respect to order, front right, front left,
     *                              back right, and back left.
     */
    public WPISwerveOdometry(WPISwerveChassis chassis,
                             Gyroscope gyroscope,
                             Pose2d initialPosition,
                             double velocityToUnitsPerSec,
                             Encoder... driveEncoders) {
        if (chassis.getModules().length != driveEncoders.length)
            throw new IllegalArgumentException(
                    "must have same amount of drive encoders as " +
                            "swerve modules!"
            );

        this.chassis = chassis;
        this.gyroscope = gyroscope;
        this.velocityToUnitsPerSec = velocityToUnitsPerSec;
        this.pose = initialPosition;

        this.odometry = new SwerveDriveOdometry(
                chassis.getKinematics(),
                WPIAdapter.rotationFromAngle(gyroscope.getAngle()),
                initialPosition
        );
        this.driveEncoders = driveEncoders;
    }

    /**
     * Update the odometry once. This will use the robot's translation over
     * time to determine its position based on elapsed time.
     */
    public void update() {
        Rotation2d rotation = WPIAdapter.rotationFromAngle(gyroscope.getAngle());

        SwerveModuleState[] states = new SwerveModuleState[driveEncoders.length];
        WPISwerveModule[] modules = chassis.getModules();

        for (int i = 0; i < modules.length; i++)
            states[i] = new SwerveModuleState(
                    driveEncoders[i].getVelocity() * velocityToUnitsPerSec,
                    modules[i].getTurnRotation()
            );

        pose = odometry.update(rotation, states);
        position = WPIAdapter.pointXYZFromPose(pose);
    }

    @Override
    public PointXYZ getRawPosition() {
        return position;
    }
}
