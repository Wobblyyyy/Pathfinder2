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

import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * Two-dimensional integer coordinate.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x,
                      int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public PointXY toPointXY() {
        return new PointXY(
                this.x,
                this.y
        );
    }

    public PointXYZ toPointXYZ(Angle z) {
        return new PointXYZ(
                this.x,
                this.y,
                z
        );
    }

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(
                this.x + coordinate.x,
                this.y + coordinate.y
        );
    }

    public Coordinate subtract(Coordinate coordinate) {
        return new Coordinate(
                this.x - coordinate.x,
                this.y - coordinate.y
        );
    }

    public Coordinate multiply(Coordinate coordinate) {
        return new Coordinate(
                this.x * coordinate.x,
                this.y * coordinate.y
        );
    }

    public Coordinate divide(Coordinate coordinate) {
        return new Coordinate(
                this.x / coordinate.x,
                this.y / coordinate.y
        );
    }

    @Override
    public int hashCode() {
        return x * 1_000_000 + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate that = (Coordinate) obj;
            return this.x == that.x && this.y == that.y;
        }

        return false;
    }

    @Override
    public String toString() {
        return StringUtils.format(
                "(%s, %s)",
                this.x,
                this.y
        );
    }
}
