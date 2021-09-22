/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;

/**
 * An entirely empty odometry implementation. This will always return a
 * position of (0, 0, 0) using {@link PointXYZ#zero()}.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class EmptyOdometry extends AbstractOdometry {
    @Override
    public PointXYZ getRawPosition() {
        return PointXYZ.zero();
    }
}
