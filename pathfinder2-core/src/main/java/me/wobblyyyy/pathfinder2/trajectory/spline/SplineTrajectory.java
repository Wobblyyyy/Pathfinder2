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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Spline;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * The most simple spline-based trajectory - there's a single spline that
 * controls the target X and Y values. The robot will attempt to turn to
 * the end heading the entire time it's in motion.
 *
 * <p>
 * For more advanced application of splines, look at
 * {@link AdvancedSplineTrajectory}. Actually, I would strongly encourage you
 * to use that aforementioned class instead.
 * </p>
 *
 * @author Colin Robertson
 * @see AdvancedSplineTrajectory
 * @since 0.6.1
 */
public class SplineTrajectory implements Trajectory {
    private final Spline spline;
    private final Angle targetHeading;
    private final double speed;
    private final double step;
    private final double tolerance;

    /**
     * Create a new {@code SplineTrajectory}.
     *
     * @param spline        a spline responsible for controlling the target point
     *                      of the trajectory. This target point should be updated
     *                      dynamically so that the robot is constantly given
     *                      a new marker/target point.
     * @param targetHeading the desired heading of the robot - the robot will
     *                      attempt to turn to this heading the entire time
     *                      it is in motion.
     * @param speed         the speed of the robot.
     * @param step          how large each "step" value should be. A larger
     *                      step value makes the trajectory slightly less
     *                      accurate, but makes it have coarser movement. A
     *                      smaller step makes the trajectory more accurate, but
     *                      might be hard to work with at high velocities.
     *                      If your spline is moving in a positive X
     *                      direction, this value should also be positive.
     *                      Likewise, if your spline is moving in a negative
     *                      X direction, this value should also be negative.
     *                      Having a positive step with a negative spline
     *                      (or vice versa) will cause your robot to never
     *                      complete the trajectory, because it'll try to go
     *                      to the wrong target point.
     * @param tolerance     the tolerance used in determining if the robot is
     *                      actually at the target point. This tolerance
     *                      only affects the LAST of the points in the
     *                      trajectory - all of the other points ignore
     *                      whatever this value is.
     */
    public SplineTrajectory(Spline spline,
                            Angle targetHeading,
                            double speed,
                            double step,
                            double tolerance) {
        this.spline = spline;
        this.targetHeading = targetHeading;
        this.speed = speed;
        this.step = step;
        this.tolerance = tolerance;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        double x = current.x() + step;
        return spline.interpolate(x).withHeading(targetHeading);
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return current.isNear(spline.getEndPoint(), tolerance);
    }

    @Override
    public double speed(PointXYZ current) {
        return speed;
    }
}
