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
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class WPIPointXYZ extends PointXYZ {

    public WPIPointXYZ(Pose2d pose) {
        super(pose.getX(), pose.getY(), pose.getRotation().getDegrees());
    }
}
