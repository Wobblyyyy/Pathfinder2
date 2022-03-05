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

import me.wobblyyyy.pathfinder2.math.ApacheSpline;
import me.wobblyyyy.pathfinder2.math.MonotoneCubicSpline;
import me.wobblyyyy.pathfinder2.math.Spline;

/**
 * Modes splines can use. Changing the mode a spline operates in will
 * change the underlying {@link Spline} that's being used by the trajectory.
 * You can create your own custom spline interpolator generator if you
 * so desire by using the {@link #CUSTOM} option.
 *
 * @author Colin Robertson
 * @since 1.0.0
 */
public enum InterpolationMode {
    /**
     * Use {@link MonotoneCubicSpline}.
     */
    DEFAULT,

    /**
     * Use {@link ApacheSpline} with {@code SplineInterpolator}.
     */
    CUBIC,

    /**
     * Use {@link ApacheSpline} with {@code AkimaSplineInterpolator}.
     */
    AKIMA,

    /**
     * Use a custom spline interpolator generator.
     */
    CUSTOM,
}
