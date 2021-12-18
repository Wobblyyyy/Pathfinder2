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
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.AdvancedSplineTrajectoryBuilder;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;

/**
 * Here's a brief overview of using {@link AdvancedSplineTrajectoryBuilder}
 * to create {@code AdvancedSplineTrajectory} (trajectories, actually). This
 * is among the most customizable and extensible ways to create a spline
 * trajectory - it gives you a lot of control over how the splines are
 * created, allowing you to finely tune your spline so it's just so incredibly
 * awesome, fantastic, and cool.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class ExampleSpline {
    public void run() {
        // before anything else, we have to get some stuff set up.
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

        // alright! trajectory time! let's see what's up.
        // as you can see, this is mostly pretty self-explanatory.
        // you create an AdvancedSplineTrajectoryBuilder and use the
        // add methods provided by that class to construct a trajectory.
        // this method works well, but it's a bit verbose - there's a
        // solution to that problem you'll see in just a moment.
        Trajectory trajectory1 = new AdvancedSplineTrajectoryBuilder()
                .setSpeed(0.5)
                .setStep(0.1)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5))
                .add(new PointXYZ(0, 0, 0))
                .add(new PointXYZ(4, 6, 0))
                .add(new PointXYZ(6, 12, 0))
                .add(new PointXYZ(8, 24, 0))
                .build();
        Trajectory trajectory2 = new AdvancedSplineTrajectoryBuilder()
                .setSpeed(0.5)
                .setStep(0.1)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5))
                .add(new PointXYZ(8, 24, 0))
                .add(new PointXYZ(6, 36, 0))
                .add(new PointXYZ(4, 40, 0))
                .add(new PointXYZ(0, 42, 0))
                .build();

        // thankfully, there's an easier way to create trajectories just like
        // that - we can make use of a "SplineBuilderFactory"
        SplineBuilderFactory factory = new SplineBuilderFactory()
                .setSpeed(0.5)
                .setStep(0.1)
                .setTolerance(2)
                .setAngleTolerance(Angle.fromDeg(5));
        // set the default speed, step, tolerance, and angle tolerance
        // values for the factory. all of the spline builders produced by
        // the factory will have have these values by default.

        // now we can create new trajectories, without having to repeat
        // the same 4 lines for each of the trajectories.
        Trajectory trajectory3 = factory.builder()
                .add(0, 60, Angle.fromDeg(0))
                .add(new PointXYZ(20, 60, 0))
                .add(new PointXYZ(30, 60, 0))
                .add(new PointXYZ(40, 70, 0))
                .build();
        Trajectory trajectory4 = factory.builder()
                .add(new PointXYZ(40, 70, 0))
                .add(new PointXYZ(30, 60, 0))
                .add(new PointXYZ(20, 60, 0))
                .add(0, 60, Angle.fromDeg(0))
                .build();

        // time to actually make the robot move now! once again, most of
        // these methods are fairly self-explanatory. basically, follow
        // the first two trajectories, come to a complete stop, and then
        // follow the next two trajectories, but each of those trajectories
        // should have an individual timeout of 10 seconds.
        pathfinder
                .followTrajectories(trajectory1, trajectory2)
                .andThen(pf -> {
                    // any other code you want to be executed after the
                    // trajectory is finished
                    pf.setTranslation(new Translation(0, 0, 0));
                })
                .followTrajectory(trajectory3)
                .tickUntil(10_000)
                .followTrajectory(trajectory4)
                .tickUntil(10_000);
    }
}
