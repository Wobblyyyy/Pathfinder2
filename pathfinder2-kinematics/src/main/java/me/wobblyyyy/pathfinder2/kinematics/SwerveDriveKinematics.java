/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

/**
 * Kinematics for a swerve drive. This is stolen pretty shamelessly (and poorly
 * adapted) from wpilib.
 *
 * @author Colin Robertson
 * @since 0.7.0
 */
@SuppressWarnings("PointlessArithmeticExpression")
public class SwerveDriveKinematics implements Kinematics<SwerveState> {
    private final SimpleMatrix inverseKinematics;
    private final SimpleMatrix forwardKinematics;
    private final int moduleCount;
    private final PointXY[] modulePositions;
    private PointXY previousCenterOfRotation;

    /**
     * Create a new instance of {@code SwerveDriveKinematics}.
     *
     * @param modulePositions an array of swerve module positions.
     */
    public SwerveDriveKinematics(PointXY... modulePositions) {
        if (modulePositions.length < 2) {
            throw new IllegalArgumentException(
                    "A swerve drive requires at least two modules!"
            );
        }
        moduleCount = modulePositions.length;
        this.modulePositions = Arrays.copyOf(modulePositions, moduleCount);
        inverseKinematics = new SimpleMatrix(moduleCount * 2, 3);
        for (int i = 0; i < moduleCount; i++) {
            PointXY modulePosition = modulePositions[i];
            double x = modulePosition.x();
            double y = modulePosition.y();
            inverseKinematics.setRow(i * 2 + 0, 0, 1, 0, y);
            inverseKinematics.setRow(i * 2 + 1, 0, 0, 1, x);
        }
        forwardKinematics = inverseKinematics.pseudoInverse();
    }

    /**
     * Create a new instance of {@code SwerveDriveKinematics}.
     *
     * @param frontRightPosition front right module position.
     * @param frontLeftPosition  the front left module position.
     * @param backRightPosition  back right module position.
     * @param backLeftPosition   back left module position.
     */
    public SwerveDriveKinematics(PointXY frontRightPosition,
                                 PointXY frontLeftPosition,
                                 PointXY backRightPosition,
                                 PointXY backLeftPosition) {
        this(
                new PointXY[] {
                        frontRightPosition,
                        frontLeftPosition,
                        backRightPosition,
                        backLeftPosition
                }
        );
    }

    /**
     * Calculate a {@link SwerveState}, given a translation and a center
     * of rotation.
     *
     * @param translation      the translation to calculate a swerve state
     *                         based on.
     * @param centerOfRotation the center of rotation. To be entirely honest,
     *                         I have absolutely no idea what this means.
     * @return create a new swerve drive.
     */
    public SwerveState calculate(Translation translation,
                                 PointXY centerOfRotation) {
        if (!centerOfRotation.equals(previousCenterOfRotation)) {
            for (int i = 0; i < moduleCount; i++) {
                PointXY modulePosition = modulePositions[i];
                double x = modulePosition.x() - centerOfRotation.x();
                double y = -modulePosition.y() + centerOfRotation.y();
                inverseKinematics.setRow(i * 2 + 0, 0, 1, 0, y);
                inverseKinematics.setRow(i * 2 + 1, 0, 0, 1, x);
            }
            previousCenterOfRotation = centerOfRotation;
        }

        SimpleMatrix speedVector = new SimpleMatrix(3, 1);
        speedVector.setColumn(
                0,
                0,
                translation.vx(),
                translation.vy(),
                translation.vz()
        );

        SimpleMatrix moduleStateMatrix = inverseKinematics.mult(speedVector);
        SwerveModuleState[] states = new SwerveModuleState[moduleCount];

        for (int i = 0; i < moduleCount; i++) {
            double x = moduleStateMatrix.get(i * 2 + 0, 0);
            double y = moduleStateMatrix.get(i * 2 + 1, 0);
            double speed = Math.hypot(x, y);
            Angle angle = Angle.atan2(y, x);
            states[i] = new SwerveModuleState(speed, angle);
        }

        return new SwerveState(
                states[0],
                states[1],
                states[2],
                states[3]
        );
    }

    @Override
    public SwerveState calculate(Translation translation) {
        return calculate(translation, previousCenterOfRotation);
    }

    public Translation toTranslation(SwerveModuleState... states) {
        if (states.length != moduleCount) {
            throw new IllegalArgumentException(
                    "Number of modules is not consistent with number of " +
                            "wheel locations provided in constructor"
            );
        }
        SimpleMatrix matrix = new SimpleMatrix(moduleCount * 2, 1);
        for (int i = 0; i < moduleCount; i++) {
            SwerveModuleState state = states[i];
            matrix.set(i * 2 + 0, 0, state.speed(), state.direction().cos());
            matrix.set(i * 2 + 1, 0, state.speed(), state.direction().sin());
        }
        SimpleMatrix speedVector = forwardKinematics.mult(matrix);
        return new Translation(
                speedVector.get(0, 0),
                speedVector.get(1, 0),
                speedVector.get(2, 0)
        );
    }

    public Translation toTranslation(SwerveState state) {
        return toTranslation(
                state.frontRight(),
                state.frontLeft(),
                state.backRight(),
                state.backLeft()
        );
    }
}
