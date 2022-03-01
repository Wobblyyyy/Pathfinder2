/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;


import me.wobblyyyy.pathfinder2.exceptions.SplineException;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A monotone cubic spline... whatever that means. This is the standard
 * implementation of the {@link Spline} interface.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
@SuppressWarnings("DuplicatedCode")
public class MonotoneCubicSpline implements Spline {
    private final double[] mx;
    private final double[] my;
    private final double[] mm;

    private PointXY start;
    private PointXY end;

    private boolean isInverted;
    private boolean isXY = false;

    /*
     * very important implementation notes here:
     *
     * i have no fucking idea what any of this code does. like... none. at
     * all. this all comes from... some website i found when i looked up
     * spline interpolation in java. hold on... i'll go find it just give me
     * one moment...
     *
     * https://gist.github.com/lecho/7627739
     *
     * also implementation notes here i guess... some of this code was
     * written by me. if the spline has decreasing x values, just invert
     * the x values so that they're increasing. it's pretty simple, to be
     * honest.
     */

    @SuppressWarnings("SuspiciousNameCombination")
    public MonotoneCubicSpline(double[] xInitial,
                               double[] yInitial) {

        if (xInitial == null || yInitial == null)
            throw new SplineException("Splines must be created " +
                    "with non-null control points and arrays");

        if (xInitial.length < 2)
            throw new SplineException("Splines must be created " +
                    "with at least 2 control points and arrays of " +
                    "equal lengths.");

        if (xInitial.length != yInitial.length)
            throw new SplineException("Splines must be created " +
                    "with arrays of equal lengths!");

        double[] x = new double[xInitial.length];
        double[] y = new double[yInitial.length];

        System.arraycopy(xInitial, 0, x, 0, x.length);
        System.arraycopy(yInitial, 0, y, 0, y.length);

        // check to see if the points are inverted
        isInverted = false;
        double initialX = x[0];
        for (int i = 1; i < x.length; i++)
            if (x[i] < initialX) {
                isInverted = true;
                break;
            }

        // check to see if there are any duplicate x values. if there are
        // duplicate x values, switch the x and y values
        List<Double> xValues = new ArrayList<>();
        for (double d : x)
            if (xValues.contains(d)) {
                isXY = true;
                break;
            } else {
                xValues.add(d);
            }
        if (isXY) {
            double[] tempX;
            double[] tempY;

            tempX = x;
            tempY = y;

            x = tempY;
            y = tempX;
        }

        // if the spline is inverted, invert/reflect all of the points
        if (isInverted)
            for (int i = 1; i < x.length; i++)
                x[i] = reflectX(x[i], x[0]);

        final int n = x.length;

        double[] d = new double[n - 1];
        double[] m = new double[n];

        for (int i = 0; i < n - 1; i++) {
            double h = x[i + 1] - x[i];
            if (h <= 0D) {
                throw new IllegalArgumentException("Control points " +
                        "must have strictly increasing X values. If you're " +
                        "seeing this message, you probably had a spline with " +
                        "X values that increased then decreased or vice versa. " +
                        "All X values must be approaching the same infinity. " +
                        "Those X values are: " + Arrays.toString(x));
            }
            d[i] = (y[i + 1] - y[i]) / h;
        }

        m[0] = d[0];

        for (int i = 1; i < n - 1; i++)
            m[i] = (d[i - 1] + d[i]) * 0.5D;

        m[n - 1] = d[n - 2];

        for (int i = 0; i < n - 1; i++)
            if (d[i] == 0D) {
                m[i] = 0D;
                m[i + 1] = 0F;
            } else {
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];

                if (a < 0D || b < 0D)
                    throw new IllegalArgumentException("The control points " +
                            "must have monotonic Y values. If you're seeing " +
                            "this message, you probably tried to create a " +
                            "spline that had Y values that decreased then " +
                            "increased or vice versa. Basically, all of your " +
                            "Y values must be going in the same direction - " +
                            "either positive or negative, but not both. " +
                            "Those Y values were: " + Arrays.toString(y));

                double h = Math.hypot(a, b);

                if (h > 3D) {
                    double t = 3D / h;
                    m[i] *= t;
                    m[i + 1] *= t;
                }
            }

        this.mx = x;
        this.my = y;
        this.mm = m;

        this.start = new PointXY(mx[0], my[0]);
        int last = mx.length - 1;
        this.end = new PointXY(mx[last], my[last]);
    }

    public static Spline create(List<PointXY> controlPoints) {
        double[] x = new double[controlPoints.size()];
        double[] y = new double[controlPoints.size()];

        for (int i = 0; i < controlPoints.size(); i++) {
            PointXY controlPoint = controlPoints.get(i);
            x[i] = controlPoint.x();
            y[i] = controlPoint.y();
        }

        return new MonotoneCubicSpline(x, y);
    }

    public static MonotoneCubicSpline fromPoints(PointXY[] points) {
        double[] x = new double[points.length];
        double[] y = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            x[i] = points[i].x();
            y[i] = points[i].y();
        }

        return new MonotoneCubicSpline(x, y);
    }

    public static MonotoneCubicSpline fromPoints(PointXYZ[] points) {
        double[] x = new double[points.length];
        double[] y = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            x[i] = points[i].x();
            y[i] = points[i].y();
        }

        return new MonotoneCubicSpline(x, y);
    }

    public MonotoneCubicSpline fromVarArgs(PointXY... points) {
        double[] x = new double[points.length];
        double[] y = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            x[i] = points[i].x();
            y[i] = points[i].y();
        }

        return new MonotoneCubicSpline(x, y);
    }

    public MonotoneCubicSpline fromVarArgs(PointXYZ... points) {
        double[] x = new double[points.length];
        double[] y = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            x[i] = points[i].x();
            y[i] = points[i].y();
        }

        return new MonotoneCubicSpline(x, y);
    }

    private static double reflectX(double x,
                                   double axis) {
        return (axis * 2) - x;
    }

    private double reflectX(double x) {
        return reflectX(x, mx[0]);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public double interpolateY(double x) {
        final int n = mx.length;

        double[] mx = isXY ? this.my : this.mx;
        double[] my = isXY ? this.mx : this.my;

        if (isInverted) x = reflectX(x);
        else if (Double.isNaN(x)) return x;
        else if (x <= mx[0]) return my[0];
        else if (x >= mx[n - 1]) return my[n - 1];

        int i = 0;
        while (x >= mx[i + 1]) {
            i += 1;
            if (x == mx[i]) return my[i];
        }

        double h = mx[i + 1] - mx[i];
        double t = (x - mx[i]) / h;

        // i am not even going to pretend to understand this.
        return (my[i] * (1 + 2 * t) + h * mm[i] * t) * (1 - t) * (1 - t) +
                (my[i + 1] * (3 - 2 * t) + h * mm[i + 1] * (t - 1)) * t * t;
    }

    @Override
    public PointXY interpolate(double x) {
        return new PointXY(x, interpolateY(x));
    }

    @Override
    public PointXY getStartPoint() {
        return start;
    }

    @Override
    public PointXY getEndPoint() {
        if (!isInverted)
            return end;
        else
            return end.withX(reflectX(end.x()));
    }
}
