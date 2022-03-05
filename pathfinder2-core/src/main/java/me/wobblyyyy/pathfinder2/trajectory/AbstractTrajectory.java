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

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * A {@code Trajectory} that adds support for end conditions, execution
 * requirements, and independent input and output modifiers for
 * {@link #isDone(PointXYZ)}, {@link #speed(PointXYZ)}, and
 * {@link #nextMarker(PointXYZ)}.
 *
 * @author Colin Robertson
 * @since 1.2.0
 */
public abstract class AbstractTrajectory implements Trajectory {
    private final Collection<Supplier<Boolean>> endConditions;
    private final Collection<Supplier<Boolean>> requirements;

    private final Collection<Function<PointXYZ, PointXYZ>> nextMarkerInputModifiers;
    private final Collection<Function<PointXYZ, PointXYZ>> nextMarkerOutputModifiers;

    private final Collection<Function<PointXYZ, PointXYZ>> speedInputModifiers;
    private final Collection<Function<Double, Double>> speedOutputModifiers;

    private final Collection<Function<PointXYZ, PointXYZ>> isDoneInputModifiers;
    private final Collection<Function<Boolean, Boolean>> isDoneOutputModifiers;

    private final Iterable<Collection<?>> collections;

    public AbstractTrajectory() {
        this(
            new ArrayList<>(1),
            new ArrayList<>(1),
            new ArrayList<>(1),
            new ArrayList<>(1),
            new ArrayList<>(1),
            new ArrayList<>(1),
            new ArrayList<>(1),
            new ArrayList<>(1)
        );
    }

    public AbstractTrajectory(
        Collection<Supplier<Boolean>> endConditions,
        Collection<Supplier<Boolean>> requirements,
        Collection<Function<PointXYZ, PointXYZ>> nextMarkerInputModifiers,
        Collection<Function<PointXYZ, PointXYZ>> nextMarkerOutputModifiers,
        Collection<Function<PointXYZ, PointXYZ>> speedInputModifiers,
        Collection<Function<Double, Double>> speedOutputModifiers,
        Collection<Function<PointXYZ, PointXYZ>> isDoneInputModifiers,
        Collection<Function<Boolean, Boolean>> isDoneOutputModifiers
    ) {
        this.endConditions = endConditions;
        this.requirements = requirements;
        this.nextMarkerInputModifiers = nextMarkerInputModifiers;
        this.nextMarkerOutputModifiers = nextMarkerOutputModifiers;
        this.speedInputModifiers = speedInputModifiers;
        this.speedOutputModifiers = speedOutputModifiers;
        this.isDoneInputModifiers = isDoneInputModifiers;
        this.isDoneOutputModifiers = isDoneOutputModifiers;

        this.collections =
            new ArrayList<Collection<?>>() {

                {
                    add(endConditions);
                    add(requirements);
                    add(nextMarkerInputModifiers);
                    add(nextMarkerOutputModifiers);
                    add(speedInputModifiers);
                    add(speedOutputModifiers);
                    add(isDoneInputModifiers);
                    add(isDoneOutputModifiers);
                }
            };
    }

    private static <T> T applyModifiers(
        T value,
        Iterable<Function<T, T>> modifiers
    ) {
        for (Function<T, T> modifier : modifiers) value = modifier.apply(value);

        return value;
    }

    public abstract PointXYZ abstractNextMarker(PointXYZ current);

    public abstract double abstractSpeed(PointXYZ current);

    public abstract boolean abstractIsDone(PointXYZ current);

    public void clear() {
        for (Collection<?> collection : collections) collection.clear();
    }

    public void addInputModifier(Function<PointXYZ, PointXYZ> modifier) {
        nextMarkerInputModifiers.add(modifier);
        speedInputModifiers.add(modifier);
        isDoneInputModifiers.add(modifier);
    }

    public Iterable<Collection<?>> getCollections() {
        return collections;
    }

    public Collection<Function<PointXYZ, PointXYZ>> getNextMarkerInputModifiers() {
        return nextMarkerInputModifiers;
    }

    public Collection<Function<PointXYZ, PointXYZ>> getNextMarkerOutputModifiers() {
        return nextMarkerOutputModifiers;
    }

    public Collection<Function<PointXYZ, PointXYZ>> getSpeedInputModifiers() {
        return speedInputModifiers;
    }

    public Collection<Function<Double, Double>> getSpeedOutputModifiers() {
        return speedOutputModifiers;
    }

    public Collection<Function<PointXYZ, PointXYZ>> getIsDoneInputModifiers() {
        return isDoneInputModifiers;
    }

    public Collection<Function<Boolean, Boolean>> getIsDoneOutputModifiers() {
        return isDoneOutputModifiers;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        current = applyModifiers(current, nextMarkerInputModifiers);
        PointXYZ nextMarker = abstractNextMarker(current);
        nextMarker = applyModifiers(nextMarker, nextMarkerOutputModifiers);
        return nextMarker;
    }

    @Override
    public double speed(PointXYZ current) {
        current = applyModifiers(current, speedInputModifiers);
        double speed = abstractSpeed(current);
        speed = applyModifiers(speed, speedOutputModifiers);
        return speed;
    }

    @Override
    public boolean isDone(PointXYZ current) {
        for (Supplier<Boolean> supplier : endConditions) if (
            supplier.get()
        ) return true;

        for (Supplier<Boolean> supplier : requirements) if (
            !supplier.get()
        ) return true;

        current = applyModifiers(current, isDoneInputModifiers);
        boolean isDone = abstractIsDone(current);
        isDone = applyModifiers(isDone, isDoneOutputModifiers);
        return isDone;
    }
}
