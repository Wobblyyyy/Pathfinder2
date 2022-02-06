/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.wpilib;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * An adapter for converting between WPILIB and Pathfinder types.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class WPIAdapter {
    public static Pose2d poseFromPointXY(PointXY point) {
        return new Pose2d(
                point.x(),
                point.y(),
                new Rotation2d()
        );
    }

    public static PointXY pointXYFromPose(Pose2d pose) {
        return new WPIPointXY(pose);
    }

    public static Pose2d poseFromPointXYZ(PointXYZ point) {
        return new Pose2d(
                point.x(),
                point.y(),
                rotationFromAngle(point.z())
        );
    }

    public static PointXY pointXYZFromPose(Pose2d pose) {
        return new WPIPointXYZ(pose);
    }

    public static Angle angleFromRotation(Rotation2d rotation) {
        return Angle.fixedDeg(rotation.getDegrees());
    }

    public static Rotation2d rotationFromAngle(Angle angle) {
        return new Rotation2d(angle.rad());
    }
}
