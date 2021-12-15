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
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSplineTrajectoryBuilder {
    private final List<Double> xValues = new ArrayList<>();
    private final List<Double> yValues = new ArrayList<>();
    private final List<Angle> angleTargets = new ArrayList<>();
    private final List<Double> speeds = new ArrayList<>();
    private double step;
    private double speed;
    private double tolerance;
    private Angle angleTolerance;

    public AdvancedSplineTrajectoryBuilder() {

    }

    public AdvancedSplineTrajectoryBuilder setStep(double step) {
        this.step = step;

        return this;
    }

    public AdvancedSplineTrajectoryBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public AdvancedSplineTrajectoryBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public AdvancedSplineTrajectoryBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    public AdvancedSplineTrajectoryBuilder add(PointXYZ target) {
        return add(target, speed);
    }

    public AdvancedSplineTrajectoryBuilder add(double x,
                                               double y) {
        return add(
                new PointXYZ(x, y, angleTargets.get(angleTargets.size() - 1)),
                speed
        );
    }

    public AdvancedSplineTrajectoryBuilder add(double x,
                                               double y,
                                               Angle z) {
        return add(
                new PointXYZ(x, y, z),
                speed
        );
    }

    public AdvancedSplineTrajectoryBuilder add(double x,
                                               double y,
                                               Angle z,
                                               double speed) {
        return add(
                new PointXYZ(x, y, z),
                speed
        );
    }

    public AdvancedSplineTrajectoryBuilder add(double x,
                                               double y,
                                               double speed) {
        return add(
                new PointXYZ(x, y, angleTargets.get(angleTargets.size() - 1)),
                speed
        );
    }

    public AdvancedSplineTrajectoryBuilder add(PointXYZ target,
                                               double speed) {
        this.speed = speed;

        xValues.add(target.x());
        yValues.add(target.y());
        angleTargets.add(target.z());
        speeds.add(speed);

        return this;
    }

    public AdvancedSplineTrajectory build() {
        int size = xValues.size();
        Double[] xBoxed = new Double[size];
        Double[] yBoxed = new Double[size];
        Angle[] z = new Angle[size];
        Double[] speedBoxed = new Double[size];
        xValues.toArray(xBoxed);
        yValues.toArray(yBoxed);
        angleTargets.toArray(z);
        speeds.toArray(speedBoxed);
        double[] x = new double[size];
        double[] y = new double[size];
        double[] speed = new double[size];
        for (int i = 0; i < xBoxed.length; i++) {
            x[i] = xBoxed[i];
            y[i] = yBoxed[i];
            speed[i] = speedBoxed[i];
        }
        Spline spline = new MonotoneCubicSpline(x, y);
        AngleSpline angleSpline = new AngleSpline(x, z);
        Spline speedSpline = new MonotoneCubicSpline(x, speed);
        return new AdvancedSplineTrajectory(
                spline,
                angleSpline,
                speedSpline,
                step,
                tolerance,
                angleTolerance
        );
    }
}
