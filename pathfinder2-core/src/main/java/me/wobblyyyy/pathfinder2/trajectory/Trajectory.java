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
 * A path-like thing that your robot can follow. Look, I know all this
 * documentation is supposed to be official and what not, but I can't really
 * define it any better than that.
 *
 * <p>
 * A trajectory works pretty simply, actually. There's a method called
 * {@link #nextMarker(PointXYZ)} that gets the next marker point the robot
 * should attempt to drive to based on the current point. And... that's about
 * it. Yeah. There's really not much more to it.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Trajectory {
    /**
     * Get the next marker that the robot should attempt to navigate to. This
     * should always be a point along the trajectory.
     *
     * @param current the robot's current position. This position is used in
     *                determining the next marker position.
     * @return the next marker the robot should attempt to navigate to.
     */
    PointXYZ nextMarker(PointXYZ current);

    /**
     * Has the robot finished executing this trajectory?
     *
     * @param current the robot's current position.
     * @return true if the robot has finished executing the trajectory, false
     * if it has not.
     */
    boolean isDone(PointXYZ current);

    /**
     * Determine the speed at which the robot should be moving while executing
     * this section of the trajectory.
     *
     * @param current the robot's current position. This position is used in
     *                determining how fast the robot should move from here.
     * @return the speed at which the robot should be moving. This value should
     * always be positive. A value of 0 corresponds to not moving at all, and
     * a value of 1 corresponds to moving at full speed.
     */
    double speed(PointXYZ current);
}
