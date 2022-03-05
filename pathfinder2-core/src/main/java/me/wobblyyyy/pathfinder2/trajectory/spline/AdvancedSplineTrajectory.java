/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.spline;

import me.wobblyyyy.pathfinder2.Core;
import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.math.Max;
import me.wobblyyyy.pathfinder2.math.Min;
import me.wobblyyyy.pathfinder2.math.MinMax;
import me.wobblyyyy.pathfinder2.math.Spline;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A {@link Trajectory} that utilizes multiple splines to control values
 * for the trajectory. Those values are as follows:
 *
 * <ul>
 *     <li>The target position</li>
 *     <li>The target heading</li>
 *     <li>The speed of the robot</li>
 * </ul>
 *
 * <p>
 * The idea behind this trajectory is that several different components can
 * be controlled using splines. This makes it very easy to precisely tune
 * your trajectory so that it does exactly what you want.
 * </p>
 *
 * <p>
 * This is the suggested way to utilize spline trajectories, as it gives the
 * most flexibility without inhibiting usability. Furthermore, it's a good
 * idea to use builder classes (see the "see" section of this doc).
 * </p>
 *
 * @author Colin Robertson
 * @see AdvancedSplineTrajectoryBuilder
 * @see MultiSplineBuilder
 * @since 0.6.1
 */
public class AdvancedSplineTrajectory implements Trajectory {
    private final Spline spline;
    private final AngleSpline angleSpline;
    private final Spline speedSpline;
    private final double step;
    private final double tolerance;
    private final Angle angleTolerance;
    private final boolean isDecreasing;

    private final double minX;
    private final double maxX;

    private boolean hasCompletedTrajectory = false;

    /**
     * Create a new {@code AdvancedSplineTrajectory}.
     *
     * @param spline         a spline responsible for controlling the target point
     *                       of the trajectory. This target point should be updated
     *                       dynamically so that the robot is constantly given
     *                       a new marker/target point.
     * @param angleSpline    a spline responsible for controlling the angle
     *                       target of the trajectory. Because splines only work
     *                       with X and Y values, this has to be separate from
     *                       the original spline.
     * @param speedSpline    a spline responsible for controlling the speed of
     *                       the robot. This allows your robot to accelerate
     *                       and decelerate with relative ease. If you'd
     *                       like to have your robot move at a consistent
     *                       speed, you can use a {@code ZeroSlopeSpline},
     *                       which makes the spline return the same value,
     *                       no matter what input is provided.
     * @param step           how large each "step" value should be. A larger
     *                       step value makes the trajectory slightly less
     *                       accurate, but makes it have coarser movement. A
     *                       smaller step makes the trajectory more accurate, but
     *                       might be hard to work with at high velocities.
     *                       If your spline is moving in a positive X
     *                       direction, this value should also be positive.
     *                       Likewise, if your spline is moving in a negative
     *                       X direction, this value should also be negative.
     *                       Having a positive step with a negative spline
     *                       (or vice versa) will cause your robot to never
     *                       complete the trajectory, because it'll try to go
     *                       to the wrong target point. If your robot is not
     *                       moving at all, you might want to try increasing
     *                       the step value, just in case the value is so
     *                       small that the robot thinks it's at the target
     *                       position.
     * @param tolerance      the tolerance used in determining if the robot is
     *                       actually at the target point. This tolerance
     *                       only affects the LAST of the points in the
     *                       trajectory - all of the other points ignore
     *                       whatever this value is. This value must be
     *                       greater than or equal to 0, although I'd suggest
     *                       you don't make it zero, because... well, that's
     *                       not going to work out too well for you.
     * @param angleTolerance the tolerance used for determining if the robot
     *                       is facing the correct direction. Like the
     *                       {@code tolerance} parameter, this only affects
     *                       the LAST of the points in the trajectory. This
     *                       tolerance value must be greater than 0 degrees.
     * @see AdvancedSplineTrajectoryBuilder
     * @see MultiSplineBuilder
     */
    public AdvancedSplineTrajectory(
        Spline spline,
        AngleSpline angleSpline,
        Spline speedSpline,
        double step,
        double tolerance,
        Angle angleTolerance
    ) {
        if (spline == null) throw new IllegalArgumentException(
            "The 'spline' argument of the AdvancedSplineTrajectory " +
            "constructor was null!"
        );

        if (angleSpline == null) throw new IllegalArgumentException(
            "The 'angleSpline' argument of the AdvancedSplineTrajectory " +
            "constructor was null!"
        );

        if (speedSpline == null) throw new IllegalArgumentException(
            "The 'speedSpline' argument of the AdvancedSplineTrajectory " +
            "constructor was null!"
        );

        InvalidToleranceException.throwIfInvalid(
            "Invalid tolerance in AdvancedSplineTrajectory " +
            "constructor! Make sure this value is greater " +
            "than or equal to zero.",
            tolerance
        );

        if (step == 0) throw new IllegalArgumentException(
            "Attempted to create an AdvancedSplineTrajectory with " +
            "a step value of 0. Please make sure this number " +
            "is NOT equal to zero. Or just don't have " +
            "working code - it's up to you, really."
        );

        if (tolerance < 0) throw new InvalidToleranceException(
            "Attempted to create a LinearTrajectory instance with a " +
            "tolerance value less than 0. Tolerance values must " +
            "be greater than or equal to 0."
        );

        if (angleTolerance == null) throw new NullAngleException(
            "Attempted to create a LinearTrajectory instance with " +
            "a null angle tolerance value. Make sure whatever " +
            "angle tolerance you pass isn't null next time, " +
            "okay? Cool."
        );

        if (angleTolerance.deg() < 0) throw new IllegalArgumentException(
            "Attempted to create a LinearTrajectory instance with " +
            "an invalid angle tolerance! The angle tolerance " +
            "must be positive."
        );

        double startX = spline.getStartPoint().x();
        double endX = spline.getEndPoint().x();
        this.isDecreasing = startX > endX;

        // if the step value is positive when it should be negative
        if ((isDecreasing && step > 0 || (!isDecreasing && step < 0))) step *=
            -1;

        this.spline = spline;
        this.angleSpline = angleSpline;
        this.speedSpline = speedSpline;
        this.step = step;
        this.tolerance = tolerance;
        this.angleTolerance = angleTolerance;

        this.minX = Min.of(startX, endX);
        this.maxX = Max.of(startX, endX);
    }

    /**
     * Create a new {@code AdvancedSplineTrajectory}.
     *
     * @param trajectory the trajectory to copy.
     */
    @SuppressWarnings("CopyConstructorMissesField")
    public AdvancedSplineTrajectory(AdvancedSplineTrajectory trajectory) {
        this(
            trajectory.spline,
            trajectory.angleSpline,
            trajectory.speedSpline,
            trajectory.step,
            trajectory.tolerance,
            trajectory.angleTolerance
        );
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        double x = current.x() + step;

        if (x > maxX) x = maxX; else if (x < minX) x = minX;

        PointXY interpolatedPoint = spline.interpolate(x);
        Angle interpolatedAngle = angleSpline.getAngleTarget(x);

        return interpolatedPoint.withHeading(interpolatedAngle);
    }

    private boolean isDoneXY(PointXYZ current) {
        if (
            current.isNear(spline.getEndPoint(), tolerance)
        ) hasCompletedTrajectory = true;

        return hasCompletedTrajectory;
    }

    private boolean isDoneZ(PointXYZ current) {
        return Angle.isCloseDeg(
            current.z().fix(),
            Angle.fixedDeg(angleSpline.getSpline().getEndPoint().y()).fix(),
            angleTolerance.deg()
        );
    }

    @Override
    public boolean isDone(PointXYZ current) {
        if (current == null) throw new IllegalArgumentException(
            "The robot's current point " +
            "was null, which it shouldn't be. I'd suggest you " +
            "fix that, because that would be pretty cool."
        );

        return isDoneXY(current) && isDoneZ(current);
    }

    @Override
    public double speed(PointXYZ current) {
        if (current == null) throw new IllegalArgumentException(
            "The robot's current point " +
            "was null, which it shouldn't be. I'd suggest you " +
            "fix that, because that would be pretty cool."
        );

        double speed = speedSpline.interpolateY(
            MinMax.clip(current.x(), minX, maxX)
        );

        if (speed < 0 || speed > 1) throw new InvalidSpeedException(
            "AdvancedSplineTrajectory " +
            "calculated an invalid speed value, make sure your " +
            "speed spline is correctly constructed. Speed value: " +
            speed
        );

        return speed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AdvancedSplineTrajectory) {
            AdvancedSplineTrajectory t = (AdvancedSplineTrajectory) obj;

            boolean sameSpline = spline.equals(t.spline);
            boolean sameAngleSpline = angleSpline.equals(t.angleSpline);
            boolean sameSpeedSpline = speedSpline.equals(t.speedSpline);
            boolean sameStep = Equals.soft(
                step,
                t.step,
                Core.advancedSplineTrajectoryTolerance
            );
            boolean sameTolerance = Equals.soft(
                tolerance,
                t.tolerance,
                Core.advancedSplineTrajectoryTolerance
            );
            boolean sameAngleTolerance = angleTolerance.equals(
                t.angleTolerance
            );

            return (
                sameSpline &&
                sameAngleSpline &&
                sameSpeedSpline &&
                sameStep &&
                sameTolerance &&
                sameAngleTolerance
            );
        }

        return false;
    }
}
