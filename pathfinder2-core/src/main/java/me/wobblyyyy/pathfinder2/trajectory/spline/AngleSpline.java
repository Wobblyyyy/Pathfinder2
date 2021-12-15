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

public class AngleSpline {
    private final Spline spline;

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

    public Angle getAngleTarget(double x) {
        return Angle.fixedDeg(spline.interpolateY(x));
    }

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
