/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.zones;

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Shape;

import java.io.Serializable;

public class Zone implements Serializable {
    private final Shape<?> shape;

    public Zone(Shape<?> shape) {
        this.shape = shape;
    }

    public final Shape<?> getShape() {
        return shape;
    }

    public final boolean isPointInShape(PointXY point) {
        return shape.isPointInShape(point);
    }

    public final boolean isPointOutsideOfShape(PointXY point) {
        return !isPointInShape(point);
    }

    public final boolean doesCollideWith(Zone zone) {
        return shape.doesCollideWith(zone.getShape());
    }

    public final boolean doesCollideWith(Shape<?> shape) {
        return this.shape.doesCollideWith(shape);
    }

    public void onEnter(PointXYZ robotPosition) {

    }

    public void onExit(PointXYZ robotPosition) {

    }

    public void whileInside(PointXYZ robotPosition) {

    }
}
