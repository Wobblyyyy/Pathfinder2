/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.odometrycore.ThreeWheelOdometry;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.sensors.Encoder;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;

/**
 * An example implementation of {@link ThreeWheelOdometry}, which uses
 * {@code OdometryCore} to track a robot's position.
 *
 * @since 2.4.0
 */
public class ExampleThreeWheelOdometry {
    // pretend these values are accurate
    private static final double CPR = 0.0;
    private static final double WHEEL_DIAMETER = 0.0;
    private static final double OFFSET_LEFT = 0.0;
    private static final double OFFSET_RIGHT = 0.0;
    private static final double OFFSET_CENTER = 0.0;

    // these should not be null in a real implementation!
    private Encoder leftEncoder = null;
    private Encoder rightEncoder = null;
    private Encoder centerEncoder = null;

    /**
     * Create a new instance of the {@code Robot} class to demonstrate how
     * {@link ThreeWheelOdometry} is instantiated.
     */
    public void createRobot() {
        // using SimulatedDrive as a placeholder
        Drive drive = new SimulatedDrive();

        // create a ThreeWheelOdometryProfile to store constants
        ThreeWheelOdometry.ThreeWheelOdometryProfile odometryProfile = new ThreeWheelOdometry.ThreeWheelOdometryProfile(
            CPR,
            WHEEL_DIAMETER,
            OFFSET_LEFT,
            OFFSET_RIGHT,
            OFFSET_CENTER
        );

        // create an EncoderProfile to store references to methods that
        // provide the encoder's count. note that leftEncoder, rightEncoder,
        // and centerEncoder must all be initialized, or a NullPointerException
        // will show up whenever trying to determine the robot's position.
        //
        // specifically in FTC, it is likely easier to not use the Encoder
        // interface provided with Pathfinder, and rather get values directly
        // from the motors, like so:
        //
        // DcMotor rightEncoder;
        // DcMotor leftEncoder;
        // DcMotor centerEncoder;
        // ThreeWheelOdometry.EncoderProfile encoderProfile = new ThreeWheelOdometry.EncoderProfile(
        //     () -> (double) leftEncoder.getCurrentPosition(),
        //     () -> (double) rightEncoder.getCurrentPosition(),
        //     () -> (double) centerEncoder.getCurrentPosition()
        // );
        //
        // of course rightEncoder, leftEncoder, and centerEncoder will need
        // to be initialized with the hardware map first.
        ThreeWheelOdometry.EncoderProfile encoderProfile = new ThreeWheelOdometry.EncoderProfile(
            () -> (double) leftEncoder.getTicks(),
            () -> (double) rightEncoder.getTicks(),
            () -> (double) centerEncoder.getTicks()
        );

        // initialize ThreeWheelOdometry
        Odometry odometry = new ThreeWheelOdometry(
            odometryProfile,
            encoderProfile
        );

        // the robot's position can now be accessed by calling the following...
        PointXYZ position = odometry.getPosition();

        // create an instance of Robot... and we're done!
        Robot robot = new Robot(drive, odometry);
    }
}
