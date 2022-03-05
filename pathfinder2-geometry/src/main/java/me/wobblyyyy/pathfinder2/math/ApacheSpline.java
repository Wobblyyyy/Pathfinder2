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

import java.util.Arrays;
import me.wobblyyyy.pathfinder2.exceptions.SplineException;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 * Implementation of {@link PolynomialSplineFunction} - very cool! This
 * allows you to use math from Apache Commons Math, which is a good thing,
 * because I am absolutely terrible at math. In order to make use of this,
 * you'll need to add Apache Commons Math as a dependency (preferably
 * version 3.6.1).
 *
 * @author Colin Robertson
 * @since 1.0.0
 */
public class ApacheSpline implements Spline {
    private final boolean isInverted;
    private final Interpolator interpolator;
    private final PolynomialSplineFunction function;
    private final PointXY startPoint;
    private final PointXY endPoint;
    private final double minX;
    private final double maxX;

    /**
     * Create a new {@code ApacheSpline}.
     *
     * @param x control point X values.
     * @param y control point Y values.
     */
    public ApacheSpline(double[] x, double[] y) {
        this(Interpolator.CUBIC, x, y);
    }

    /**
     * Create a new {@code ApacheSpline}.
     *
     * @param interpolator the type of interpolation the spline should use.
     * @param x            control point X values.
     * @param y            control point Y values.
     */
    public ApacheSpline(Interpolator interpolator, double[] x, double[] y) {
        if (x == null) throw new SplineException("X value array was null!");
        if (y == null) throw new SplineException("Y value array was null!");

        if (x.length != y.length) throw new SplineException(
            "Cannot create a spline with arrays " +
            "of unequal lengths! X length: " +
            x.length +
            "; Y length: " +
            y.length
        );

        if (x.length < 3) throw new SplineException(
            "Cannot create a spline with less " +
            "than three control points! The control X values you " +
            "provided were: " +
            Arrays.toString(x)
        );

        if (!Spline.areMonotonic(x)) throw new SplineException(
            "cannot create a spline with " +
            "non-monotonic x values! the invalid values were: " +
            Arrays.toString(x)
        );

        this.isInverted = Spline.areDecreasing(x);
        this.interpolator = interpolator;

        startPoint = new PointXY(x[0], y[0]);
        endPoint = new PointXY(x[x.length - 1], y[y.length - 1]);

        if (isInverted) {
            double startX = x[0];

            for (int i = 1; i < x.length; i++) x[i] = startX - (x[i] - startX);
        }

        switch (interpolator) {
            case CUBIC:
                this.function = new SplineInterpolator().interpolate(x, y);
                break;
            case AKIMA:
                if (x.length < 4) throw new SplineException(
                    "You need at least 4 points " +
                    "for Akima spline interpolation, you only " +
                    "provided " +
                    x.length
                );

                this.function = new AkimaSplineInterpolator().interpolate(x, y);
                break;
            default:
                throw new RuntimeException();
        }

        minX = Min.of(x);
        maxX = Max.of(x);
    }

    @Override
    public double interpolateY(double x) {
        if (isInverted) {
            double startX = startPoint.x();
            x = startX - (x - startX);
        }

        if (x < minX) x = minX; else if (x > maxX) x = maxX;
        return function.value(x);
    }

    @Override
    public PointXY getStartPoint() {
        return startPoint;
    }

    @Override
    public PointXY getEndPoint() {
        return endPoint;
    }

    public Interpolator interpolator() {
        return interpolator;
    }

    public enum Interpolator {
        CUBIC,
        AKIMA,
    }
}
