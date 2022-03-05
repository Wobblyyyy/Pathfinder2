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

/**
 * A single value that can be either inclusive or exclusive. This class is
 * used by {@link Range}.
 *
 * @author Colin Robertson
 * @since 1.1.0
 */
public class RangeNode {
    public static final RangeNode POSITIVE_INFINITY = inclusive(
        Double.POSITIVE_INFINITY
    );
    public static final RangeNode NEGATIVE_INFINITY = inclusive(
        Double.NEGATIVE_INFINITY
    );

    private final double node;
    private final boolean isInclusive;

    /**
     * Create a new {@code RangeNode}.
     *
     * @param node        the node's value.
     * @param isInclusive is the node inclusive?
     */
    public RangeNode(double node, boolean isInclusive) {
        this.node = node;
        this.isInclusive = isInclusive;
    }

    public static RangeNode inclusive(double value) {
        return new RangeNode(value, true);
    }

    public static RangeNode exclusive(double value) {
        return new RangeNode(value, false);
    }

    public double node() {
        return node;
    }

    public boolean isInclusive() {
        return isInclusive;
    }

    public boolean isExclusive() {
        return !isInclusive;
    }

    public boolean isValueGreaterThanNode(double value) {
        return isInclusive ? value >= node : value > node;
    }

    public boolean isValueLessThanNode(double value) {
        return isInclusive ? value <= node : value < node;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RangeNode) {
            RangeNode n = (RangeNode) obj;

            return isInclusive == n.isInclusive && node == n.node;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int value = (int) (node * 100);

        return isInclusive ? value : value * -1;
    }

    @Override
    public String toString() {
        return node + (isInclusive ? " (inclusive)" : " (exclusive)");
    }
}
