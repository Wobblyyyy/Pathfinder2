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

public class SplineTrajectory implements Trajectory {
    private final Spline spline;
    private final Angle targetHeading;
    private final double speed;
    private final double step;
    private final double tolerance;

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
