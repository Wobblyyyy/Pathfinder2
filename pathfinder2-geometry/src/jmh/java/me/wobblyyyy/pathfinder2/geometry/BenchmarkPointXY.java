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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class BenchmarkPointXY {
    private static final PointXY[] points = new PointXY[] {
        new PointXY(0, 0),
        new PointXY(10, 0),
        new PointXY(0, 10),
        new PointXY(10, 10),
        new PointXY(-10, -10),
        new PointXY(-10, 0),
        new PointXY(0, -10),
        new PointXY(0, 0)
    };

    private static void forPointCombos(BiConsumer<PointXY, PointXY> consumer) {
        for (PointXY a : points)
            for (PointXY b : points)
                consumer.accept(a, b);
    }

    @Benchmark
    public void addPointXY(Blackhole bh) {
        forPointCombos((a, b) -> {
            bh.consume(PointXY.add(a, b));
        });
    }

    @Benchmark
    public void multiplyPointXY(Blackhole bh) {
        forPointCombos((a, b) -> {
            bh.consume(PointXY.multiply(a, b));
        });
    }

    @Benchmark
    public void inDirection(Blackhole bh) {
        forPointCombos((a, b) -> {
            bh.consume(PointXY.inDirection(a, 5, Angle.DEG_45));
            bh.consume(PointXY.inDirection(b, 5, Angle.DEG_45));
        });
    }
}
