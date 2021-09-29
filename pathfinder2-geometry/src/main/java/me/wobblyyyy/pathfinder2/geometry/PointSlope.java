/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

/**
 * Another implementation of the {@link LinearEquation} interface.
 * {@code y - y1 = m(x - x1)} or {@code y = m(x - x1) + y1}
 *
 * <p>
 * Internally, this simply converts the point slope into a slope intercept.
 * This means you can't get "the point."
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class PointSlope extends SlopeIntercept {
    public PointSlope(PointXY point,
                      double slope) {
        super(
                slope,
                (slope * -point.x()) + point.y()
        );
    }
}
