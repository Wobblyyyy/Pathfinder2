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

/**
 * A spline with a slope of 0.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class ZeroSlopeSpline implements Spline {
    private final double returnValue;

    public ZeroSlopeSpline(double returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public double interpolateY(double x) {
        return this.returnValue;
    }

    @Override
    public PointXY interpolate(double x) {
        return new PointXY(x, this.returnValue);
    }

    @Override
    public PointXY getStartPoint() {
        return new PointXY(0, returnValue);
    }

    @Override
    public PointXY getEndPoint() {
        return new PointXY(0, returnValue);
    }
}
