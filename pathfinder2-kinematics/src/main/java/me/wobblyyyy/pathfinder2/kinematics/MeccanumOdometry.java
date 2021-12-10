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
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * Odometry for a meccanum chassis.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class MeccanumOdometry extends GenericOdometry<MeccanumState> {
    public MeccanumOdometry(Kinematics<MeccanumState> kinematics,
                            Angle gyroAngle,
                            PointXYZ initialPosition) {
        super(kinematics, gyroAngle, initialPosition);
    }
}
