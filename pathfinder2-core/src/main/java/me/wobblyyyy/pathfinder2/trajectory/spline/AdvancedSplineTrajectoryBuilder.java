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

import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.LinearEquation;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.SlopeIntercept;
import me.wobblyyyy.pathfinder2.math.ApacheSpline;
import me.wobblyyyy.pathfinder2.math.LinearSpline;
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;
import me.wobblyyyy.pathfinder2.math.ApacheSpline.Interpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * A builder for the {@link AdvancedSplineTrajectory} class. Please read over
 * the documentation in {@link AdvancedSplineTrajectory} before using this
 * class!
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class AdvancedSplineTrajectoryBuilder {
    public static InterpolationMode DEFAULT_INTERPOLATION_MODE = InterpolationMode.DEFAULT;

    private final List<Double> xValues = new ArrayList<>();
    private final List<Double> yValues = new ArrayList<>();
    private final List<Angle> angleTargets = new ArrayList<>();
    private final List<Double> speeds = new ArrayList<>();
    private double step = Double.MAX_VALUE;
    private double speed = Double.MAX_VALUE;
    private double tolerance = Double.MAX_VALUE;
    private Angle angleTolerance;
    private InterpolationMode interpolationMode = DEFAULT_INTERPOLATION_MODE;
    private BiFunction<Double[], Double[], Spline> customSplineGenerator = null;

    public AdvancedSplineTrajectoryBuilder() {

    }

    /**
     * Set the trajectory's step value.
     *
     * @param step the trajectory's step value.
     * @return {@code this}, used for method chaining.
     */
    public AdvancedSplineTrajectoryBuilder setStep(double step) {
        this.step = step;

        return this;
    }

    /**
     * Set the trajectory's speed value.
     *
     * @param speed the trajectory's speed value.
     * @return {@code this}, used for method chaining.
     */
    public AdvancedSplineTrajectoryBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    /**
     * Set the trajectory's tolerance value.
     *
     * @param tolerance the trajectory's tolerance value.
     * @return {@code this}, used for method chaining.
     */
    public AdvancedSplineTrajectoryBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    /**
     * Set the trajectory's angle tolerance value.
     *
     * @param step the trajectory's angle tolerance value.
     * @return {@code this}, used for method chaining.
     */
    public AdvancedSplineTrajectoryBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    /**
     * Set the trajectory's interpolation mode.
     *
     * @param interpolationMode the trajectory's interpolationMode value.
     * @return {@code this}, used for method chaining.
     */
    public AdvancedSplineTrajectoryBuilder setInterpolationMode(InterpolationMode interpolationMode) {
        this.interpolationMode = interpolationMode;

        return this;
    }

    /**
     * Set the builder's custom spline generator. If you would like to use
     * a custom implementation of a spline, you must create a function
     * that accepts two {@code Double} arrays as values and returns a new
     * spline. These two values are X and Y respectively.
     *
     * @param func the function responsible for generating a spline. Read the
     *             documentation for {@link #setCustomSplineGenerator(BiFunction)}
     *             to learn more.
     * @return {@code this}, used for method chaining.
     */
    public AdvancedSplineTrajectoryBuilder setCustomSplineGenerator(
            BiFunction<Double[], Double[], Spline> func) {
        this.customSplineGenerator = func;

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
                                               double zDegrees,
                                               double speed) {
        return add(
                new PointXYZ(x, y, zDegrees),
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
        boolean invalidStep = step == Double.MAX_VALUE;
        boolean invalidSpeed = speed == Double.MAX_VALUE;
        boolean invalidTolerance = tolerance == Double.MAX_VALUE;
        boolean invalidAngleTolerance = angleTolerance == null;

        if (invalidStep &&
                invalidSpeed &&
                invalidTolerance &&
                invalidAngleTolerance
        ) throw new IllegalArgumentException(
                "Did not set a step, speed, tolerance, and angle tolerance " +
                        "value! You need to use setStep(), setSpeed(), " +
                        "setTolerance(), and setAngleTolerance() before " +
                        "calling the build() method."
        );

        if (invalidStep)
            throw new IllegalArgumentException(
                    "Did not set a step value - use setStep().");

        if (invalidSpeed)
            throw new InvalidSpeedException(
                    "Did not set a speed - use setSpeed().");

        if (invalidTolerance)
            throw new InvalidToleranceException(
                    "Did not set a tolerance - use setTolerance().");

        if (invalidAngleTolerance)
            throw new NullAngleException(
                    "Null angle tolerance while creating an " +
                            "AdvancedSplineTrajectory.");

        int size = xValues.size();
        Double[] xBoxed = new Double[size];
        Double[] yBoxed = new Double[size];
        Double[] speedBoxed = new Double[size];
        Angle[] z = new Angle[size];

        xValues.toArray(xBoxed);
        yValues.toArray(yBoxed);
        angleTargets.toArray(z);
        speeds.toArray(speedBoxed);

        double[] x = new double[size];
        double[] y = new double[size];
        double[] speed = new double[size];
        boolean sameSpeedValue = true;

        for (int i = 0; i < xBoxed.length; i++) {
            x[i] = xBoxed[i];
            y[i] = yBoxed[i];
            speed[i] = speedBoxed[i];

            if (i != 0)
                if (speed[i] != speed[i - 1]) 
                    sameSpeedValue = false;
        }

        // add support for different types of spline interpolation!
        // this is a really bad way to implement support for multiple
        // types of spline interpolation, but... oh well.
        Spline spline;
        if (interpolationMode == InterpolationMode.DEFAULT) {
            spline = new MonotoneCubicSpline(x, y);
        } else {
            if (interpolationMode == InterpolationMode.CUBIC) {
                spline = new ApacheSpline(Interpolator.CUBIC, x, y);
            } else if (interpolationMode == InterpolationMode.AKIMA) {
                spline = new ApacheSpline(Interpolator.AKIMA, x, y);
            } else if (interpolationMode == InterpolationMode.CUSTOM) {
                if (customSplineGenerator != null) {
                    spline = customSplineGenerator.apply(xBoxed, yBoxed);
                } else {
                    throw new NullPointerException("Tried to use custom " +
                            "spline generator without having set it first: " +
                            "use setCustomSplineGenerator() to do so. The " +
                            "function you pass in should accept two arrays " +
                            "of Double values (x and y).");
                }
            } else {
                throw new RuntimeException("How did you even get here?");
            }
        }

        AngleSpline angleSpline = new AngleSpline(x, z);

        Spline speedSpline;
        if (sameSpeedValue)
            speedSpline = new LinearSpline(new SlopeIntercept(0, speed[0]));
        else
            speedSpline = new MonotoneCubicSpline(x, speed);

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
