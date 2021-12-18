/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.examples.splines;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * A demonstration of the {@link Pathfinder#splineTo(PointXYZ...)} method,
 * allowing you to quickly create splines without having to even worry
 * about trajectories! How convenient. This is the least verbose method to
 * follow splines, but it also gives you the least control over those splines.
 * With {@code AdvancedSplineTrajectory} (or it's builder/factory), you have
 * a lot of control over how exactly the spline functions, but that requires
 * you to create a trajectory and all that, and we all know how much of a
 * pain that is (not really, to be honest).
 *
 * The {@link Pathfinder#splineTo(PointXYZ...)} method that's being used here
 * is the most expressive/idiomatic way to utilize splines, so I'd encourage
 * you to use it if you're not entirely sure what you're doing or if you just
 * want to make a super simple autonomous program.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class ExampleSplineTo {
    public void run() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

        pathfinder
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5))
                .splineTo(
                        new PointXYZ(0, 10, 0),
                        new PointXYZ(5, 15, 0),
                        new PointXYZ(10, 25, 0)
                )
                .tickUntil(10_000)
                .splineTo(
                        new PointXYZ(20, 15, 0),
                        new PointXYZ(25, 10, 0),
                        new PointXYZ(30, 5, 0)
                )
                .tickUntil(10_000);
    }
}
