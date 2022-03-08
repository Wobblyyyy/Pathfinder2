/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class BenchmarkLinearTrajectory extends GenericTrajectoryBenchmarker {
    private static final double speed = 0.5;
    private static final double tolerance = 2;
    private static final Angle angleTolerance = Angle.fromDeg(5);

    private static final Trajectory trajectory = new LinearTrajectory(
        new PointXYZ(-10, -10, 45),
        speed,
        tolerance,
        angleTolerance
    );

    @Benchmark
    public void benchmark(Blackhole bh) {
        followTrajectories(bh, trajectory);
    }
}
