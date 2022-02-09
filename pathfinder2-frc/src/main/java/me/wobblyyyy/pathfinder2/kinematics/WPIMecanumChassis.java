package me.wobblyyyy.pathfinder2.kinematics;

import java.util.function.Function;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.components.Motor;
import me.wobblyyyy.pathfinder2.wpilib.WPIAdapter;

/**
 * A wpilib-specific mecanum chassis implementation. The utilizes math from
 * wpilib, which is probably written better than whatever I came up with,
 * mostly because I'm surprisingly really bad at math. I'd encourage you to
 * only operate the robot through {@link #setTranslation(Translation)} so that
 * the chassis' translation is always recorded accurately.
 *
 * @author Colin Robertson
 * @since 0.10.8
 **/
public class WPIMecanumChassis implements Drive {
    private MecanumDriveKinematics kinematics;

    private final Motor frontRightMotor;
    private final Motor frontLeftMotor;
    private final Motor backRightMotor;
    private final Motor backLeftMotor;

    private Function<Translation, Translation> modifier;
    private Translation translation;

    /**
     * Create a new {@code WPIMecanumChassis}.
     *
     * @param frontBackDistance the distance in between the front and the back
     *                          set of wheels.
     * @param rightLeftDistance the distance in between the right and left
     *                          set of wheels.
     * @param frontRightMotor   the chassis' front right motor.
     * @param frontLeftMotor    the chassis' front left motor.
     * @param backRightMotor    the chassis' back right motor.
     * @param backLeftMotor     the chassis' back left motor.
     */
    public WPIMecanumChassis(double frontBackDistance,
                             double rightLeftDistance,
                             Motor frontRightMotor,
                             Motor frontLeftMotor,
                             Motor backRightMotor,
                             Motor backLeftMotor) {
        double vertical = frontBackDistance / 2;
        double horizontal = rightLeftDistance / 2;

        Translation2d frontRight = new Translation2d(vertical, horizontal);
        Translation2d frontLeft = new Translation2d(vertical, -horizontal);
        Translation2d backRight = new Translation2d(-vertical, horizontal);
        Translation2d backLeft = new Translation2d(-vertical, -horizontal);

        kinematics = new MecanumDriveKinematics(
                frontRight,
                frontLeft,
                backRight,
                backLeft
        );

        this.frontRightMotor = frontRightMotor;
        this.frontLeftMotor = frontLeftMotor;
        this.backRightMotor = backRightMotor;
        this.backLeftMotor = backLeftMotor;
    }

    public void drive(ChassisSpeeds chassisSpeeds) {
        MecanumDriveWheelSpeeds speeds = 
            kinematics.toWheelSpeeds(chassisSpeeds);

        frontRightMotor.setPower(speeds.frontRightMetersPerSecond);
        frontLeftMotor.setPower(speeds.frontLeftMetersPerSecond);
        backRightMotor.setPower(speeds.rearRightMetersPerSecond);
        backLeftMotor.setPower(speeds.rearLeftMetersPerSecond);
    }

    @Override
    public Function<Translation, Translation> getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public void setTranslation(Translation translation) {
        this.translation = translation;

        drive(WPIAdapter.speedsFromTranslation(translation));
    }
}

