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


import me.wobblyyyy.pathfinder2.geometry.PointXY;

import java.util.List;

public class MonotoneCubicSpline implements Spline {
    private final double[] mx;
    private final double[] my;
    private final double[] mm;

    public MonotoneCubicSpline(double[] x,
                               double[] y) {
        if (x == null || y == null || x.length != y.length || x.length < 2) {
            throw new IllegalArgumentException("Splines must be created " +
                    "with at least 2 control points and arrays of " +
                    "equal lengths.");
        }

        final int n = x.length;
        double[] d = new double[n - 1];
        double[] m = new double[n];

        // Compute slopes of secant lines between successive points.
        for (int i = 0; i < n - 1; i++) {
            double h = x[i + 1] - x[i];
            if (h <= 0D) {
                throw new IllegalArgumentException("Control points " +
                        "must have strictly increasing X values.");
            }
            d[i] = (y[i + 1] - y[i]) / h;
        }

        // Initialize the tangents as the average of the secants.
        m[0] = d[0];
        for (int i = 1; i < n - 1; i++) {
            m[i] = (d[i - 1] + d[i]) * 0.5D;
        }
        m[n - 1] = d[n - 2];

        // Update the tangents to preserve monotonicity.
        for (int i = 0; i < n - 1; i++) {
            if (d[i] == 0D) { // successive Y values are equal
                m[i] = 0D;
                m[i + 1] = 0F;
            } else {
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];
                if (a < 0D || b < 0D) {
                    throw new IllegalArgumentException("The control points " +
                            "must have monotonic Y values.");
                }
                double h = Math.hypot(a, b);
                if (h > 3D) {
                    double t = 3D / h;
                    m[i] *= t;
                    m[i + 1] *= t;
                }
            }
        }

        this.mx = x;
        this.my = y;
        this.mm = m;
    }

    @Override
    public Spline create(List<PointXY> controlPoints) {
        // Create arrays for X and Y values - they're both the same size.
        double[] x = new double[controlPoints.size()];
        double[] y = new double[controlPoints.size()];

        // Iterate through the controlPoints list and set the X and Y arrays
        // to have the same contents as the points.
        for (int i = 0; i < controlPoints.size(); i++) {
            PointXY controlPoint = controlPoints.get(i);

            x[i] = controlPoint.x();
            y[i] = controlPoint.y();
        }

        // Return a new instance of this using the x and y arrays.
        // Note that exceptions aren't handled in this method - rather,
        // you should see the MonotoneCubicSpline constructor.
        return new MonotoneCubicSpline(x, y);
    }

    @Override
    public double interpolateY(double x) {
        final int n = mx.length;

        if (Double.isNaN(x)) {
            // noinspection SuspiciousNameCombination
            return x;
        }

        // If X is either greater than the last X value or lesser than the
        // first X value in the mx array, return, respectively, the first
        // or last Y value.
        if (x <= mx[0]) {
            return my[0];
        } else if (x >= mx[n - 1]) {
            return my[n - 1];
        }

        int i = 0;
        while (x >= mx[i + 1]) {
            i += 1;
            if (x == mx[i]) {
                return my[i];
            }
        }

        // I'm not even going to pretend to understand what's going on here.
        // Sorry.
        double h = mx[i + 1] - mx[i];
        double t = (x - mx[i]) / h;
        return (my[i] * (1 + 2 * t) + h * mm[i] * t) * (1 - t) * (1 - t) +
                (my[i + 1] * (3 - 2 * t) + h * mm[i + 1] * (t - 1)) * t * t;
    }

    @Override
    public PointXY interpolate(double x) {
        return new PointXY(x, interpolateY(x));
    }
}
