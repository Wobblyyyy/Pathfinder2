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

/**
 * Odometry for a {@code TankDrive}.
 *
 * @author Colin Robertson
 * @since 2.2.0
 */
public class TankOdometry extends GenericOdometry<TankState> {

    public TankOdometry(
        Kinematics<TankState> kinematics,
        Angle gyroAngle,
        PointXYZ initialPosition,
        double updateIntervalMs
    ) {
        super(kinematics, gyroAngle, initialPosition, updateIntervalMs);
    }
}
