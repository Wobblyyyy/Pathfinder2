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
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class for using {@link Spline}s with {@link Angle}s.
 *
 * @author Colin Robertson
 * @since 0.6.1.
 */
public class AngleSpline {
    private final Spline spline;

    /**
     * Create a new {@code AngleSpline}, given a set of X values and a set
     * of angles.
     *
     * @param x      the X value.
     * @param angles the target angle. Note that this angle will be
     *               normalized, so it will always fit between 0 and 360
     *               degrees, inclusive.
     */
    public AngleSpline(double[] x,
                       Angle[] angles) {
        double[] anglesDeg = new double[angles.length];
        for (int i = 0; i < angles.length; i++) {
            anglesDeg[i] = angles[i].fix().deg();
        }
        this.spline = new MonotoneCubicSpline(
                x,
                anglesDeg
        );
    }

    /**
     * Get the angle at a certain X value.
     *
     * @param x the X value to get the angle of.
     * @return the angle at that X value.
     */
    public Angle getAngleTarget(double x) {
        return Angle.fixedDeg(spline.interpolateY(x));
    }

    /**
     * A builder for the {@link AngleSpline} class.
     */
    public static class AngleSplineBuilder {
        private final List<Double> x = new ArrayList<>();
        private final List<Angle> z = new ArrayList<>();

        public AngleSplineBuilder() {

        }

        public AngleSplineBuilder add(double x,
                                      Angle angle) {
            this.x.add(x);
            this.z.add(angle);

            return this;
        }

        public AngleSpline build() {
            int size = x.size();

            Double[] boxedX = new Double[size];
            x.toArray(boxedX);
            double[] unboxedX = new double[size];
            for (int i = 0; i < size; i++) {
                unboxedX[i] = boxedX[i];
            }

            Angle[] angles = new Angle[size];
            z.toArray(angles);

            return new AngleSpline(unboxedX, angles);
        }
    }

    public Spline getSpline() {
        return spline;
    }
}
