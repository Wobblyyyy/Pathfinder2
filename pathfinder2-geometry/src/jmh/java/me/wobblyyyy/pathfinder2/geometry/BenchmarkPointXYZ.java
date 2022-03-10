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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class BenchmarkPointXYZ {
    private static final PointXYZ[] points = new PointXYZ[] {
        new PointXYZ(0, 0, 0),
        new PointXYZ(10, 0, 45),
        new PointXYZ(0, 10, 90),
        new PointXYZ(10, 10, 135),
        new PointXYZ(-10, -10, 180),
        new PointXYZ(-10, 0, 215),
        new PointXYZ(0, -10, 270),
        new PointXYZ(0, 315)
    };

    private static void forPoint(Consumer<PointXYZ> consumer) {
        for (PointXYZ a : points) consumer.accept(a);
    }

    private static void forPointCombos(
        BiConsumer<PointXYZ, PointXYZ> consumer
    ) {
        for (PointXYZ a : points) for (PointXYZ b : points) consumer.accept(
            a,
            b
        );
    }

    @Benchmark
    public void addPointXYZ(Blackhole bh) {
        forPointCombos(
            (a, b) -> {
                bh.consume(PointXYZ.add(a, b));
            }
        );
    }

    @Benchmark
    public void multiplyPointXYZ(Blackhole bh) {
        forPointCombos(
            (a, b) -> {
                bh.consume(PointXYZ.multiply(a, b));
            }
        );
    }

    @Benchmark
    public void inDirection1(Blackhole bh) {
        forPoint(
            a -> {
                bh.consume(PointXYZ.inDirection(a, 5, Angle.DEG_0));
            }
        );
    }

    @Benchmark
    public void inDirection2(Blackhole bh) {
        forPoint(
            a -> {
                bh.consume(PointXYZ.inDirection(a, 10, Angle.DEG_0));
            }
        );
    }

    @Benchmark
    public void inDirection3(Blackhole bh) {
        forPoint(
            a -> {
                bh.consume(PointXYZ.inDirection(a, 10, Angle.DEG_45));
            }
        );
    }

    @Benchmark
    public void inDirection4(Blackhole bh) {
        forPoint(
            a -> {
                bh.consume(PointXYZ.inDirection(a, 10, Angle.DEG_315));
            }
        );
    }

    @Benchmark
    public void inDirection5(Blackhole bh) {
        forPoint(
            a -> {
                bh.consume(PointXYZ.inDirection(a, -10, Angle.DEG_315));
            }
        );
    }

    @Benchmark
    public void benchmarkAvg(Blackhole bh) {
        forPointCombos(
            (a, b) -> {
                bh.consume(PointXYZ.avg(a, b));
            }
        );
    }

    @Benchmark
    public void benchmarkIsNear(Blackhole bh) {
        forPointCombos(
            (a, b) -> {
                bh.consume(PointXYZ.isNear(a, b, 0.1));
                bh.consume(PointXYZ.isNear(a, b, 20));
            }
        );
    }

    public void benchmarkAngleTo(Blackhole bh) {}
}
