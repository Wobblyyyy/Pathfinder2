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
 * honest. Maybe just Google it? Good luck. As some potentially more useful
 * documentation, a spline is basically a gay line. Or a curvy line. I like
 * boys, I'm allowed to say that. Anyways. You provide a set of control points
 * and in turn you get an object you can use to calculate positions according
 * to the spline.
 *
 * <p>
 * For general purposes, you should use {@link MonotoneCubicSpline}.
 * </p>
 *
 * <p>
 * Splines are generally most useful for high-performance trajectories.
 * Although classic linear trajectories are incredibly cool, splines are
 * even cooler, especially because they allow you to cut some corners (quite
 * literally) with movement, meaning you have to traverse less distance.
 * </p>
 *
 * <p>
 * Splines must be created with points that have either strictly increasing
 * or strictly decreasing X values. For some examples...
 * <b>This would be VALID.</b>
 * <ul>
 *     <li>x = 10, y = 10</li>
 *     <li>x = 20, y = 10</li>
 *     <li>x = 30, y = 10</li>
 * </ul>
 * <b>This would be VALID.</b>
 * <ul>
 *     <li>x = 30, y = 10</li>
 *     <li>x = 20, y = 10</li>
 *     <li>x = 10, y = 10</li>
 * </ul>
 * <b>This would be INVALID.</b>
 * <ul>
 *     <li>x = 20, y = 10</li>
 *     <li>x = 30, y = 10</li>
 *     <li>x = 10, y = 10</li>
 * </ul>
 * As you can see, the X values simply have to go in the same direction.
 * </p>
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
     * Interpolate a {@link PointXY} from an X value. This should return a
     * point created with an X value equal to the provided X value and a Y
     * value found using {@link #interpolateY(double)}.
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
