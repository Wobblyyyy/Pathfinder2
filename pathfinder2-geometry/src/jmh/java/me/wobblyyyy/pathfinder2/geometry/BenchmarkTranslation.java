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
public class BenchmarkTranslation {
    private static final Translation[] translations = new Translation[] {
        new Translation(0, 0, 0),
        new Translation(1, 0, 0),
        new Translation(0, 1, 0),
        new Translation(0, 0, 1),
        new Translation(1, 1, 0),
        new Translation(1, 0, 1),
        new Translation(0, 1, 1),
        new Translation(1, 1, 1)
    };

    private static void forTranslation(Consumer<Translation> consumer) {
        for (Translation a : translations)
            consumer.accept(a);
    }

    private static void forTranslationCombos(BiConsumer<Translation, Translation> consumer) {
        for (Translation a : translations)
            for (Translation b : translations)
                consumer.accept(a, b);
    }

    @Benchmark
    public void addTranslation(Blackhole bh) {
        forTranslationCombos((a, b) -> {
            bh.consume(Translation.add(a, b));
        });
    }

    @Benchmark
    public void multiplyTranslation(Blackhole bh) {
        forTranslationCombos((a, b) -> {
            bh.consume(Translation.multiply(a, b));
        });
    }

    @Benchmark
    public void absoluteToRelative1(Blackhole bh) {
        forTranslation((a) -> {
            bh.consume(Translation.absoluteToRelative(a, Angle.DEG_0));
        });
    }

    @Benchmark
    public void absoluteToRelative2(Blackhole bh) {
        forTranslation((a) -> {
            bh.consume(Translation.absoluteToRelative(a, Angle.DEG_45));
        });
    }

    @Benchmark
    public void absoluteToRelative3(Blackhole bh) {
        forTranslation((a) -> {
            bh.consume(Translation.absoluteToRelative(a, Angle.DEG_135));
        });
    }
}
