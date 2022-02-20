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

/**
 * A variation of {@link LinearTrajectory} that has mutable values. This
 * allows you to dynamically adjust the values of a trajectory. Additionally,
 * you can set on-finish callbacks, as well as manually setting the state
 * of whether the trajectory is done or not.
 *
 * <p>
 * The values in this class are as follows:
 * <ul>
 *     <li>
 *         <em>Target</em> is the trajectory's target point: this is where the
 *         robot will continually try to move to. If this point is changed,
 *         Pathfinder's movement will change as well.
 *     </li>
 *     <li>
 *         <em>Speed</em> is the speed at which the robot should move.
 *         This value should be between 0 and 1.
 *     </li>
 *     <li>
 *         <em>Tolerance</em> is the tolerance used in deciding if the
 *         robot is at the target point, meaning the trajectory is finished.
 *     </li>
 *     <li>
 *         <em>Angle tolerance</em> is the angle tolerance used in deciding if
 *         the robot is at the target point, meaning the trajectory is finished.
 *     </li>
 *     <li>
 *         <em>Is finished and can finished</em> are two variables that
 *         {@code MutableLinearTrajectory} exposes. {@code isFinished} will
 *         automatically be set to true whenever the robot is at the correct
 *         location. In order for the {@link #isDone(PointXYZ)} method to
 *         return true, the {@code canFinish} flag must be true, which can
 *         be changed via {@link #setCanFinish(boolean)}. By default, this
 *         value is true, but it can be set to false to manually override
 *         the trajectory's finish.
 *     </li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 1.0.2
 */
public class MutableLinearTrajectory implements Trajectory {
    private PointXYZ target;
    private double speed;
    private double tolerance;
    private Angle angleTolerance;
    private boolean isFinished = false;
    private boolean canFinish = true;

    /**
     * Create a new {@code MutableLinearTrajectory}.
     *
     * <p>
     * These values will default to:
     * <ul>
     *     <li>Target point: (0, 0, 0 deg)</li>
     *     <li>Speed: 0</li>
     *     <li>Tolerance: 0</li>
     *     <li>Angle tolerance: 0 deg</li>
     * </ul>
     * </p>
     */
    public MutableLinearTrajectory() {
        this(PointXYZ.ZERO, 0, 0, Angle.fromDeg(0));
    }

    public MutableLinearTrajectory(PointXYZ target,
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

    public MutableLinearTrajectory setTarget(PointXYZ target) {
        this.target = target;

        return this;
    }

    public MutableLinearTrajectory setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public MutableLinearTrajectory setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public MutableLinearTrajectory setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    public MutableLinearTrajectory setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;

        return this;
    }

    public MutableLinearTrajectory setCanFinish(boolean canFinish) {
        this.canFinish = canFinish;

        return this;
    }

    public PointXYZ getTarget() {
        return this.target;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        if (isDoneXY(current))
            return current.withHeading(this.target);
        else
            return target;
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
        if (!isFinished) {
            if (current == null)
                return false;
            if (isDoneXY(current) && isDoneHeading(current))
                isFinished = true;
        } else {
            return isFinished;
        }

        return isFinished && canFinish;
    }

    @Override
    public double speed(PointXYZ current) {
        if (isDoneXY(current)) return 0;

        return speed;
    }

    public LinearTrajectory toLinearTrajectory() {
        return new LinearTrajectory(
                target,
                speed,
                tolerance,
                angleTolerance
        );
    }

    @Override
    public int hashCode() {
        return (int) ((target.hashCode() * speed) + (tolerance * 10));
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}

