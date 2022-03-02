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

import me.wobblyyyy.pathfinder2.geometry.Geometry;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

import java.io.Serializable;

/**
 * A range of numbers. A {@code Range} is made up of two {@link RangeNode}s,
 * which can either be inclusive or exclusive.
 *
 * @author Colin Robertson
 * @since 1.1.0
 */
public class Range implements Serializable, Comparable<Range> {
    private final RangeNode minimum;
    private final RangeNode maximum;

    /**
     * Create a new {@code Range} with two {@link RangeNode}s.
     *
     * @param minimum the range's minimum node.
     * @param minimum the range's maximum node.
     */
    public Range(RangeNode minimum,
                 RangeNode maximum) {
        if (minimum.node() < maximum.node())
            throw new IllegalArgumentException("Cannot create a Range if " +
                    "the minimum value is greater than the maximum!");

        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Create a new {@code Range} by copying an existing one.
     *
     * @param range the {@code Range} to copy.
     */
    public Range(Range range) {
        this(range.minimum, range.maximum);
    }

    /**
     * Create a new {@code Range} with inclusive end points.
     *
     * @param minimum the range's minimum value (inclusive).
     * @param maximum the range's maximum value (inclusive).
     * @return a new {@code Range}.
     */
    public static Range inclusive(double minimum,
                                  double maximum) {
        return new Range(
                new RangeNode(minimum, true),
                new RangeNode(maximum, true)
        );
    }

    /**
     * Create a new {@code Range} with exclusive end points.
     *
     * @param minimum the range's minimum value (exclusive).
     * @param maximum the range's maximum value (exclusive).
     * @return
     */
    public static Range exclusive(double minimum,
                                  double maximum) {
        return new Range(
                new RangeNode(minimum, false),
                new RangeNode(maximum, false)
        );
    }

    /**
     * Create a range extending infinitely in one direction.
     *
     * @param value       the range's base value.
     * @param isInclusive should {@code value} be included in the range?
     * @param isPositive  true if the range should extend towards positive
     *                    infinity, otherwise, false.
     * @return a new {@code Range}.
     */
    public static Range infinite(double value,
                                 boolean isInclusive,
                                 boolean isPositive) {
        return new Range(
                isPositive ? new RangeNode(value, isInclusive)
                        : RangeNode.NEGATIVE_INFINITY,
                !isPositive ? new RangeNode(value, isInclusive)
                        : RangeNode.POSITIVE_INFINITY
        );
    }

    /**
     * Create a range extending infinitely in one direction.
     *
     * @param node       the base range node.
     * @param isPositive true if the range should extend towards positive
     *                   infinity, otherwise, false.
     * @return a new {@code Range}.
     */
    public static Range infinite(RangeNode node,
                                 boolean isPositive) {
        return infinite(node.node(), node.isInclusive(), isPositive);
    }

    /**
     * Return true if the ranges overlap. Otherwise, return false.
     *
     * @param a one of the two ranges.
     * @param b one of the two ranges.
     * @return true if the ranges overlap. Otherwise, false.
     */
    public static boolean doRangesOverlap(Range a,
                                          Range b) {
        ValidationUtils.validate(a);
        ValidationUtils.validate(b);

        double aMin = a.minimum.node();
        double aMax = a.maximum.node();

        double bMin = b.minimum.node();
        double bMax = b.maximum.node();

        if (aMin > bMax) return false;
        else if (bMin > aMax) return false;
        else return true;
    }

    /**
     * Get the range's minimum.
     *
     * @return the range's minimum.
     */
    public RangeNode minimum() {
        return minimum;
    }

    /**
     * Get the range's maximum.
     *
     * @return the range's maximum.
     */
    public RangeNode maximum() {
        return maximum;
    }

    /**
     * Does the range include a value?
     *
     * @param value the value to test.
     * @return if the range includes the value, true. Otherwise, false
     */
    public boolean includes(double value) {
        boolean greaterThanMin = minimum.isValueGreaterThanNode(value);
        boolean lessThanMax = maximum.isValueLessThanNode(value);

        return greaterThanMin && lessThanMax;
    }

    /**
     * Does the range exlucde a value?
     *
     * @param value the value to test.
     * @return if the range exlucdes the value, true. Otherwise, false
     */
    public boolean excludes(double value) {
        return !includes(value);
    }

    /**
     * Does the range include all of the values?
     *
     * @param values the values.
     * @return true if the range includes all of the values, otherwise,
     * false.
     */
    public boolean includesAll(double... values) {
        for (double d : values)
            if (excludes(d))
                return false;

        return true;
    }

    /**
     * Does the range include any of the values?
     *
     * @param values the values.
     * @return true if the range includes any of the values, otherwise,
     * false.
     */
    public boolean includesAny(double... values) {
        for (double d : values)
            if (includes(d))
                return true;

        return false;
    }

    /**
     * Does the range exclude all of the values?
     *
     * @param values the values.
     * @return true if the range excludes all of the values, otherwise,
     * false.
     */
    public boolean excludesAll(double... values) {
        for (double d : values)
            if (includes(d))
                return false;

        return true;
    }

    /**
     * Does the range exclude any of the values?
     *
     * @param values the values.
     * @return true if the range excludes any of the values, otherwise,
     * false.
     */
    public boolean excludesAny(double... values) {
        for (double d : values)
            if (excludes(d))
                return true;

        return false;
    }

    /**
     * Get the range's size.
     *
     * @return the range's size.
     */
    public double size() {
        return maximum.node() - minimum.node();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range r = (Range) obj;

            return minimum.equals(r.minimum) &&
                    maximum.equals(r.maximum);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (minimum.hashCode() * 10) + maximum.hashCode();
    }

    @Override
    public String toString() {
        return StringUtils.format(
                Geometry.formatRange,
                minimum.isInclusive() ? '[' : ']',
                minimum.node(),
                maximum.isInclusive() ? '[' : ']',
                maximum.node()
        );
    }

    @Override
    public int compareTo(Range range) {
        return Double.compare(size(), range.size());
    }
}
