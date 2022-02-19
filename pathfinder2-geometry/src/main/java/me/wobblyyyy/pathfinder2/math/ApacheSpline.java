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
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
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
    private final Interpolator interpolator;
    private final PolynomialSplineFunction function;

    private double minX;
    private double maxX;

    private final PointXY startPoint;
    private final PointXY endPoint;

    /**
     * Create a new {@code ApacheSpline}.
     *
     * @param x control point X values.
     * @param y control point Y values.
     */
    public ApacheSpline(double[] x,
                        double[] y) {
        this(Interpolator.CUBIC, x, y);
    }

    /**
     * Create a new {@code ApacheSpline}.
     *
     * @param interpolator the type of interpolation the spline should use.
     * @param x            control point X values.
     * @param y            control point Y values.
     */
    public ApacheSpline(Interpolator interpolator,
                        double[] x,
                        double[] y) {
        this.interpolator = interpolator;

        switch (interpolator) {
            case CUBIC:
                this.function = new SplineInterpolator().interpolate(x, y);
                break;
            case AKIMA:
                this.function = new AkimaSplineInterpolator().interpolate(x, y);
                break;
            default:
                throw new RuntimeException();
        }

        minX = Min.of(x);
        maxX = Max.of(x);

        startPoint = new PointXY(x[0], y[0]);
        endPoint = new PointXY(x[x.length - 1], y[y.length - 1]);
    }

    @Override
    public double interpolateY(double x) {
        if (x < minX) x = minX;
        else if (x > maxX) x = maxX;
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

    public enum Interpolator {
        CUBIC,
        AKIMA
    }
}
