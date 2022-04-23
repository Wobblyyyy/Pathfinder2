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

public class DifferentialDriveOdometry {
    private PointXYZ position;

    private Angle gyroOffset;
    private Angle previousAngle;

    private double previousLeftDistance;
    private double previousRightDistance;

    public DifferentialDriveOdometry(
        Angle gyroAngle,
        PointXYZ initialPosition
    ) {
        this.position = initialPosition;
        this.gyroOffset = initialPosition.z().subtract(gyroAngle);
        this.previousAngle = initialPosition.z();
    }

    public void resetPosition(PointXYZ position, Angle gyroAngle) {
        this.position = position;
        this.gyroOffset = position.z().subtract(gyroAngle);
        this.previousAngle = position.z();
    }

    public PointXYZ getPosition() {
        return position;
    }

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
