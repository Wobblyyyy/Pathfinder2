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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.wobblyyyy.pathfinder2.exceptions.SplineException;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.utils.ArrayUtils;
import me.wobblyyyy.pathfinder2.utils.DoubleUtils;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A monotone cubic spline... whatever that means. This is the standard
 * implementation of the {@link Spline} interface.
 *
 * <p>
 * I'm not very good at math, interestingly enough. Don't expect this code
 * to make very much sense.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class MonotoneCubicSpline implements Spline {
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
     * implementation notes time, baby! hell yeah!
     *
     * this algorithm only works for splines with strictly increasing X
     * values and strictly monotonic Y values. because in real life, splines
     * will often need to go "backwards," this class can be "inverted,"
     * meaning all of the X values are reflected over the first X value.
     * this makes all of the X values increasing. in order for the spline to
     * still work as intended, any inputted X values must also be reflected
     * over the first X value.
     *
     * if the interpolateY(double) method is called and provided an X value
     * that is either less than the minimum X value or greater than the
     * maximum X value, the value will be set to either the min or max value.
     */

    private final double[] mx;
    private final double[] my;
    private final double[] mm;

    private PointXY start;
    private PointXY end;

    private boolean isInverted;
    private boolean isXY = false;

    /**
     * Create a new {@code MonotoneCubicSpline}. Both of the arrays provided
     * as parameters should be the same length. Additionally, both arrays
     * should have a length of at least 2. If the X values are decreasing,
     * rather than increasing, the spline will automatically be inverted, so
     * that externally, it appears to be decreasing, but internally, it's
     * not. This constructor copies the arrays provided as a parameters, as
     * to ensure they're not unintentionally modified.
     *
     * @param xInitial an array of X values.
     * @param yInitial an array of Y values.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public MonotoneCubicSpline(double[] xInitial, double[] yInitial)
        throws SplineException {
        // verify the inputted values
        // 1. neither can be null
        // 2. they must both have the same length
        // 3. length must be 2 or greater (at least 2 points)

        if (xInitial == null || yInitial == null) throw new SplineException(
            "Splines must be created " +
            "with non-null control points and arrays"
        );

        if (xInitial.length < 2) throw new SplineException(
            "Splines must be created " +
            "with at least 2 control points and arrays of " +
            "equal lengths."
        );

        if (xInitial.length != yInitial.length) throw new SplineException(
            "Splines must be created " + "with arrays of equal lengths!"
        );

        // create new arrays so that the inputted arrays aren't modified
        // if the spline is inverted, the x values need to be reflected over
        // the first x value. there were some problems i was having where i
        // would reflect the x value, but because arrays are mutable, it would
        // mess with x values in other splines
        double[] x = new double[xInitial.length];
        double[] y = new double[yInitial.length];

        // make sure they're both valid arrays before continuing
        xInitial = DoubleUtils.validate(xInitial);
        yInitial = DoubleUtils.validate(yInitial);

        // copy over the data from the initial array to the actual array
        System.arraycopy(xInitial, 0, x, 0, x.length);
        System.arraycopy(yInitial, 0, y, 0, y.length);

        // check to see if the points are inverted
        isInverted = false;
        double initialX = x[0];
        for (int i = 1; i < x.length; i++) if (x[i] < initialX) {
            isInverted = true;
            break;
        }

        // check to see if there are any duplicate x values. if there are
        // duplicate x values, switch the x and y values
        List<Double> xValues = new ArrayList<>();

        for (double d : x) if (xValues.contains(d)) {
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

        // if the spline is inverted, invert/reflect all of the points over
        // the very first x value
        if (isInverted) for (int i = 1; i < x.length; i++) x[i] =
            reflectX(x[i], x[0]);

        final int n = x.length;

        double[] d = new double[n - 1];
        double[] m = new double[n];

        // verify that x values are increasing
        // these values MUST be increasing in order for the interpolation to
        // actually work, so decreasing values should all be reflected
        for (int i = 0; i < n - 1; i++) {
            double h = x[i + 1] - x[i];

            // if the difference between this point and the previous point
            // is negative (or 0) then the control points did not have
            // increasing x values, which is not very cool
            if (h <= 0) {
                throw new IllegalArgumentException(
                    "Control points " +
                    "must have strictly increasing X values. If you're " +
                    "seeing this message, you probably had a spline with " +
                    "X values that increased then decreased or vice versa. " +
                    "All X values must be approaching the same infinity. " +
                    "Those X values are: " +
                    Arrays.toString(x)
                );
            }

            d[i] = (y[i + 1] - y[i]) / h;
        }

        m[0] = d[0];

        // calculate the secant slopes between adjacent points
        for (int i = 1; i < n - 1; i++) m[i] = (d[i - 1] + d[i]) * 0.5D;

        m[n - 1] = d[n - 2];

        for (int i = 0; i < n - 1; i++) if (d[i] == 0D) {
            m[i] = 0D;
            m[i + 1] = 0F;
        } else {
            double a = m[i] / d[i];
            double b = m[i + 1] / d[i];

            // verify that all y values are approaching the same infinity
            // there's a workaround for this: use MultiSplineBuilder
            if (a < 0D || b < 0D) throw new IllegalArgumentException(
                "The control points " +
                "must have monotonic Y values. If you're seeing " +
                "this message, you probably tried to create a " +
                "spline that had Y values that decreased then " +
                "increased or vice versa. Basically, all of your " +
                "Y values must be going in the same direction - " +
                "either positive or negative, but not both. " +
                "Those Y values were: " +
                Arrays.toString(y)
            );

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

    public static Spline create(List<? extends PointXY> controlPoints) {
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

    private static double reflectX(double x, double axis) {
        return (axis * 2) - x;
    }

    private double reflectX(double x) {
        return reflectX(x, mx[0]);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public double interpolateY(double x) {
        final int n = mx.length;

        // if x and y values are swapped, swap them locally here
        final double[] mx = isXY ? this.my : this.mx;
        final double[] my = isXY ? this.mx : this.my;

        if (isInverted) {
            // if the spline is inverted, the x value should be reflected
            // over the first x value
            x = reflectX(x);
        }

        if (Double.isNaN(x)) {
            // NaN -> return NaN
            return x;
        } else if (x <= mx[0]) {
            // if it's less than the minimum, return the y value associated
            // with the minimum point
            return my[0];
        } else if (x >= mx[n - 1]) {
            // if it's greater than the maximum, return the y value associated
            // with the maximum point
            return my[n - 1];
        }

        // iterate over all of the values in the secant slope array, if the
        // inputted x value matches with any of them, just return that value
        // (so if the x value is a control point, return that control point's
        // associated y value instead of interpolating it)
        int i = 0;
        while (x >= mx[i + 1]) {
            i += 1;

            if (x == mx[i]) return my[i];
        }

        double h = mx[i + 1] - mx[i];
        double t = (x - mx[i]) / h;

        // i am not even going to pretend to understand this.
        // just kidding!
        // if you're curious, you should go do some reading on hermite spline
        // interpolation: it's too much to write in a comment
        return (
            (my[i] * (1 + 2 * t) + h * mm[i] * t) *
            (1 - t) *
            (1 - t) +
            (my[i + 1] * (3 - 2 * t) + h * mm[i + 1] * (t - 1)) *
            t *
            t
        );
    }

    @Override
    public PointXY interpolate(double x) {
        return new PointXY(x, interpolateY(x));
    }

    @Override
    public PointXY getStartPoint() {
        // start point never has to be inverted
        return start;
    }

    @Override
    public PointXY getEndPoint() {
        // this is for sure a really bad way of doing this but to be honest
        // it works. you know the saying "if it ain't broke, don't fix it,"
        // right? well, it's not broke, so i'm not going to fix it.
        if (!isInverted) return end; else return end.withX(reflectX(end.x()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MonotoneCubicSpline) {
            MonotoneCubicSpline s = (MonotoneCubicSpline) obj;

            boolean sameX = ArrayUtils.arrayEquals(mx, s.mx);
            boolean sameY = ArrayUtils.arrayEquals(my, s.my);

            return sameX && sameY;
        }

        return false;
    }

    @Override
    public String toString() {
        return StringUtils.format(
            "MonotoneCubicSpline from <%s> to <%s>",
            start,
            isInverted ? end.withX(reflectX(end.x())) : end
        );
    }
}
