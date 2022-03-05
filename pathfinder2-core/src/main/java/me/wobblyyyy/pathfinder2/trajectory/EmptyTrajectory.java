/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * An {@code EmptyTrajectory} is a trajectory that does absolutely nothing.
 * The next marker point is always the current point, the trajectory is never
 * done, and the speed is always 0. This should be used when you want
 * Pathfinder to do absolutely nothing.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class EmptyTrajectory implements Trajectory {

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return current;
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return false;
    }

    @Override
    public double speed(PointXYZ current) {
        return 0;
    }
}
