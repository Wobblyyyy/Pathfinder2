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
}
