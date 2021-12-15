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
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Spline;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

public class AngleSplineTrajectory implements Trajectory {
    private final Spline spline;
    private final AngleSpline angleSpline;
    private final double speed;
    private final double step;
    private final double tolerance;

    public AngleSplineTrajectory(Spline spline,
                                 AngleSpline angleSpline,
                                 double speed,
                                 double step,
                                 double tolerance) {
        this.spline = spline;
        this.angleSpline = angleSpline;
        this.speed = speed;
        this.step = step;
        this.tolerance = tolerance;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        double x = current.x() + step;
        PointXY interpolatedPoint = spline.interpolate(x);
        Angle interpolatedAngle = angleSpline.getAngleTarget(x);
        return interpolatedPoint.withHeading(interpolatedAngle);
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
