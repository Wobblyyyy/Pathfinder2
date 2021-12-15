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
 * A spline. I don't know much of a better way to explain it, to be entirely
 * honest. Maybe just Google it? Good luck.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Spline {
    /**
     * Interpolate a Y value from an X value.
     *
     * @param x the current X value.
     * @return the interpolated Y value.
     */
    double interpolateY(double x);

    /**
     * Interpolate a {@link PointXY} from an X value.
     *
     * @param x the current X value.
     * @return the interpolated point.
     */
    PointXY interpolate(double x);

    /**
     * Get the spline's start point.
     *
     * @return the spline's start point.
     */
    PointXY getStartPoint();

    /**
     * Get the spline's end point.
     *
     * @return the spline's end point.
     */
    PointXY getEndPoint();
}
