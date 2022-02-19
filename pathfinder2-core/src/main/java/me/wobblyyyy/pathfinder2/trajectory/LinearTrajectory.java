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

import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * The most simple type of trajectory. A linear trajectory does nothing other
 * than go to a point at a linear speed. Such, there's not much you can
 * customize here. But it's simple, and it works. Hopefully, that is.
 *
 * <p>
 * A linear trajectory will move to a target point at a predetermined speed.
 * If the robot's position is unintentionally altered - say, for example,
 * another robot collides with your robot - a {@code LinearTrajectory} should
 * compensate automatically.
 * </p>
 *
 * <p>
 * Linear trajectories are the easiest trajectories to work with, but don't
 * maximize movement efficiency. There are several issues (not specific to
 * linear trajectories) that make them, at times, slow: overcorrection, for
 * example, can cause your robot to circle around the point. This can be fixed
 * by increasing the tolerance value, but doing so will decrease the accuracy
 * of the trajectory.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class LinearTrajectory implements Trajectory {
    /**
     * Should Pathfinder dance? Please note that this should ALWAYS be false
     * in any project that's used in competition. This is just for fun.
     */
    public static boolean SHOULD_SHUFFLE = false;

    /**
     * The trajectory's target point.
     */
    private final PointXYZ target;

    /**
     * The speed at which the robot should follow the trajectory.
     */
    private final double speed;

    /**
     * The X/Y tolerance.
     */
    private final double tolerance;

    /**
     * The heading tolerance.
     */
    private final Angle angleTolerance;

    /**
     * Create a new {@code LinearTrajectory}.
     *
     * <p>
     * Here's a quick code example:
     * <code><pre>
     * Trajectory trajectory = new LinearTrajectory(
     *     new PointXYZ(10, 10, 0),  // the trajectory's destination
     *     0.5,                      // the speed (0-1) to move at
     *     2,                        // give the trajectory a tolerance of 2
     *     Angle.fromDeg(5)          // and an angle tolerance of 5 degrees
     * );
     * </pre></code>
     * </p>
     *
     * @param target         the trajectory's target point. The trajectory will
     *                       execute until it is interrupted, or it reaches the
     *                       target point.
     * @param speed          the speed at which the robot should move to the
     *                       target point. Note that this speed value can not
     *                       be changed while the trajectory is being followed.
     *                       This value should be between 0 and 1.
     * @param tolerance      the tolerance used in determining whether the
     *                       robot's X and Y coordinates match up with those
     *                       of the target point. A higher tolerance means
     *                       your robot can more quickly complete paths while
     *                       sacrificing accuracy, while a lower tolerance
     *                       does exactly the opposite: it makes your robot
     *                       more accurate, at the price of speed.
     * @param angleTolerance the tolerance used in determining whether the
     *                       robot's heading matches up with whatever heading
     *                       the robot is supposed to be facing. A higher angle
     *                       tolerance means the robot will complete the
     *                       trajectory more quickly, as it won't have to
     *                       compensate for any over or under adjustments. A
     *                       lower angle tolerance makes your robot more
     *                       precise.
     */
    public LinearTrajectory(PointXYZ target,
                            double speed,
                            double tolerance,
                            Angle angleTolerance) {
        if (target == null)
            throw new NullPointException(
                    "Attempted to create a LinearTrajectory instance with " +
                            "a null target point. Target points can't be " +
                            "null - crazy, I know!");

        if (speed < 0 || speed > 1)
            throw new InvalidSpeedException(
                    "Attempted to create a LinearTrajectory instance with speed " +
                            "(" + speed + "). Speed values must be greater " +
                            "than 0. and less than 1.0.");

        if (tolerance < 0)
            throw new InvalidToleranceException(
                    "Attempted to create a LinearTrajectory instance with a " +
                            "tolerance value less than 0. Tolerance values must " +
                            "be greater than or equal to 0.");

        if (angleTolerance == null)
            throw new NullAngleException(
                    "Attempted to create a LinearTrajectory instance with " +
                            "a null angle tolerance value. Make sure whatever " +
                            "angle tolerance you pass isn't null next time, " +
                            "okay? Cool.");

        this.target = target;
        this.speed = speed;
        this.tolerance = tolerance;
        this.angleTolerance = angleTolerance;
    }

    /**
     * Create a new {@code LinearTrajectory} by copying an existing trajectory.
     *
     * @param trajectory the trajectory to copy.
     */
    public LinearTrajectory(LinearTrajectory trajectory) {
        this(
                trajectory.target,
                trajectory.speed,
                trajectory.tolerance,
                trajectory.angleTolerance
        );
    }

    /**
     * Get the follower's target point.
     *
     * @return the follower's target point.
     */
    public PointXYZ getTarget() {
        return this.target;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        if (!SHOULD_SHUFFLE) {
            // if the robot has already reached the correct x and y coordinates,
            // set the target position's x and y values to those of the current
            // position, so that robot will stop trying to overcorrect. the heading
            // value MUST remain the same as the target value or the trajectory
            // may stop before reaching the correct angle
            if (isDoneXY(current))
                return current.withHeading(this.target);
        }

        // otherwise, return the actual marker point
        return this.target;
    }

    private boolean isDoneXY(PointXYZ current) {
        return current.isNear(
                this.target,
                this.tolerance
        );
    }

    private boolean isDoneHeading(PointXYZ current) {
        return current.z().isCloseDeg(
                target.z().fix(),
                angleTolerance.fix().deg()
        );
    }

    @Override
    public boolean isDone(PointXYZ current) {
        if (current == null) return false;

        return isDoneXY(current) && isDoneHeading(current);
    }

    @Override
    public double speed(PointXYZ current) {
        // if the robot has already reached the target X and Y positions,
        // return 0, so that the robot will not move.
        if (isDoneXY(current)) return 0;

        return speed;
    }

    /**
     * Convert this {@code LinearTrajectory} into a 
     * {@link MutableLinearTrajectory}.
     *
     * @return this trajectory represented as a {@link MutableLinearTrajectory}.
     */
    public MutableLinearTrajectory toMutableLinearTrajectory() {
        return new MutableLinearTrajectory(target, speed,
                tolerance, angleTolerance);
    }

    @Override
    public int hashCode() {
        return (int) ((target.hashCode() * speed) + (tolerance * 10));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LinearTrajectory) {
            LinearTrajectory t = (LinearTrajectory) obj;

            boolean sameTarget = t.target.equals(this.target);
            boolean sameSpeed = Equals.soft(t.speed, this.speed, 0.01);
            boolean sameTolerance = Equals.soft(t.tolerance, this.tolerance, 0.01);
            boolean sameAngleTolerance = t.angleTolerance.equals(this.angleTolerance);

            return sameTarget && sameSpeed && sameTolerance && sameAngleTolerance;
        }

        return false;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LinearTrajectory clone() {
        return new LinearTrajectory(
                target,
                speed,
                tolerance,
                angleTolerance
        );
    }

    @Override
    public String toString() {
        return StringUtils.format(
                "Linear trajectory to %s at %s speed (tolerance %s %s)",
                target,
                speed,
                tolerance,
                angleTolerance.formatAsDegShort()
        );
    }
}

