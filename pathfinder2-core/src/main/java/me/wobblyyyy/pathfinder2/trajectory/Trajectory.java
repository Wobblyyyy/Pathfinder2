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
import me.wobblyyyy.pathfinder2.trajectory.multi.segment.MultiSegmentTrajectory;

import java.util.Arrays;

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
 * <p>
 * Each {@code Trajectory} is executed via a {@link me.wobblyyyy.pathfinder2.follower.Follower}.
 * A follower is responsible for interpreting a trajectory, as well as the
 * robot's current position, to determine how the robot should move. Long
 * story short, {@code Trajectory} instances cannot directly control how
 * Pathfinder operates the robot - rather, you'll have to encapsulate each
 * trajectory in an individual follower. These followers are then executed
 * via a {@link me.wobblyyyy.pathfinder2.execution.FollowerExecutor}, which,
 * in turn, are managed via a {@link me.wobblyyyy.pathfinder2.execution.ExecutorManager}.
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
     * <p>
     * A marker position should provide the robot with a "target." The robot
     * should then calculate how it should move to reach that target point.
     * In essence, a holonomic robot should calculate the angle between its
     * own position and the next target point, and, from there, determine
     * how to move in that angle.
     * </p>
     *
     * <p>
     * A {@code Trajectory} is, in essence, a long sequence of points the
     * robot should navigate to. These points are typically generated
     * dynamically. Simple trajectories, such as linear trajectories, only need
     * a single marker point.
     * </p>
     *
     * @param current the robot's current position. This position is used in
     *                determining the next marker position.
     * @return the next marker the robot should attempt to navigate to. The
     * robot should calculate, based on the current and marker positions,
     * how to move in order to reach the target position.
     */
    PointXYZ nextMarker(PointXYZ current);

    /**
     * Has the robot finished executing this trajectory? This method should
     * return true if the robot has finished executing the trajectory and
     * can move on to the next trajectory or stop. If the robot hasn't finished
     * it's trajectory, meaning it should still continue executing it, this
     * method should return false.
     *
     * <p>
     * Each trajectory follower can only be marked as "completed" if this
     * method returns true. Also note that as soon as this method returns
     * true, the follower responsible for executing that trajectory will
     * be de-queued, meaning that this trajectory will no longer be
     * executed. Scary.
     * </p>
     *
     * @param current the robot's current position.
     * @return true if the robot has finished executing the trajectory, false
     * if it has not. Frequently, this is defined by a target position: after
     * the robot reaches a certain position, the trajectory will end. However,
     * this can also have other requirements - for example, the robot must be
     * below a certain velocity, or the robot must not be accelerating, etc.
     */
    boolean isDone(PointXYZ current);

    /**
     * Determine the speed at which the robot should be moving while executing
     * this section of the trajectory.
     *
     * <p>
     * Trajectories can have different speeds at different points. This method
     * CAN return the same value constantly, regardless of the position of
     * the robot. However, if you'd like to have some more control over the
     * speed of your robot at different points throughout the trajectory,
     * you're more than free to do so.
     * </p>
     *
     * <p>
     * If you'd like to control the speed of a trajectory, I'd suggest you
     * make note of the following class:
     * {@link me.wobblyyyy.pathfinder2.control.SplineController}
     * There's absolutely no need to, and you can control the speed using
     * other methods, but the {@code SplineController} gives you the most
     * control over what speed your robot will have at what point.
     * </p>
     *
     * @param current the robot's current position. This position is used in
     *                determining how fast the robot should move from here.
     * @return the speed at which the robot should be moving. This value should
     * always be positive. A value of 0 corresponds to not moving at all, and
     * a value of 1 corresponds to moving at full speed.
     */
    double speed(PointXYZ current);

    /**
     * Convert this trajectory to a {@link MultiSegmentTrajectory}, adding
     * all of the provided additional trajectories.
     *
     * @param additionalTrajectories a variable length array containing
     *                               trajectories that should be conjoined
     *                               with {@code this} trajectory.
     * @return a new {@link MultiSegmentTrajectory}. This new trajectory will
     * have {@code this} trajectory as the first segment, and then any of the
     * trajectories in the variable argument will be appended afterwards.
     */
    default Trajectory toMultiSegmentTrajectory(Trajectory... additionalTrajectories) {
        Trajectory[] trajectories = new Trajectory[additionalTrajectories.length + 1];
        System.arraycopy(
                additionalTrajectories, 0,
                trajectories, 1,
                additionalTrajectories.length
        );
        trajectories[0] = this;
        return new MultiSegmentTrajectory(Arrays.asList(trajectories));
    }
}
