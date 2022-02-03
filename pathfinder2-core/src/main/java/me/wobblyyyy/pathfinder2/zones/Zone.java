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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.Shape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A zone is a wrapper for a shape that provides some additional utilities
 * you can make use of. In addition to providing utilities, the {@code Zone}
 * class allows you to avoid the {@link Shape} class' generics.
 *
 * <p>
 * Zones allow your robot to perform certain actions or act in certain ways
 * whenever it's position matches a certain set of requirements. Zones are
 * based on shapes - each zone has one parent shape.
 * </p>
 *
 * <p>
 * You can overload some methods to control the zone's behavior.
 * <ul>
 *     <li>{@link #onEnter(Pathfinder)}</li>
 *     <li>{@link #onExit(Pathfinder)}</li>
 *     <li>{@link #whileInside(Pathfinder)}</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Zone implements Serializable {
    private final Shape<?> shape;

    /**
     * Create a {@code Zone} based on a {@link Shape}.
     *
     * @param shape the shape that represents the zone.
     */
    public Zone(Shape<?> shape) {
        this.shape = shape;
    }

    public static Zone inflate(Zone zone,
                               double inflationRadius) {
        return new Zone((Shape<?>) zone.getShape().growBy(inflationRadius)) {
            @Override
            public void onEnter(Pathfinder pathfinder) {
                zone.onEnter(pathfinder);
            }

            @Override
            public void onExit(Pathfinder pathfinder) {
                zone.onExit(pathfinder);
            }

            @Override
            public void whileInside(Pathfinder pathfinder) {
                zone.whileInside(pathfinder);
            }

            @Override
            public boolean isSolid() {
                return zone.isSolid();
            }
        };
    }

    public static List<Zone> inflate(List<Zone> zones,
                                     double inflationRadius) {
        List<Zone> inflated = new ArrayList<>(zones.size());

        for (Zone zone : zones) {
            inflated.add(inflate(zone, inflationRadius));
        }

        return inflated;
    }

    /**
     * Get the zone's shape.
     *
     * @return the zone's shape.
     */
    public final Shape<?> getShape() {
        return shape;
    }

    /**
     * Is a given point contained inside the zone's parent shape?
     *
     * @param point the point to test.
     * @return if the point is inside the zone, return true. Otherwise, false.
     */
    public final boolean isPointInShape(PointXY point) {
        return shape.isPointInShape(point);
    }

    /**
     * Is a given point not contained inside the zone's parent shape?
     *
     * @param point the point to test.
     * @return if the point is outside the zone, return true. Otherwise, false.
     */
    public final boolean isPointOutsideOfShape(PointXY point) {
        return !isPointInShape(point);
    }

    /**
     * Does this zone collide with another zone?
     *
     * @param zone the zone to test.
     * @return true if there's a collision, otherwise, false.
     */
    public final boolean doesCollideWith(Zone zone) {
        return shape.doesCollideWith(zone.getShape());
    }

    /**
     * Does this zone collide with a given shape?
     *
     * @param shape the shape to test.
     * @return true if there's a collision, otherwise, false.
     */
    public final boolean doesCollideWith(Shape<?> shape) {
        return this.shape.doesCollideWith(shape);
    }

    /**
     * Code to be executed whenever a robot enters the zone. This code will
     * executed once when the robot enters the zone, and will not be executed
     * again until the robot leaves the zone and re-enters it.
     *
     * @param pathfinder the instance of Pathfinder.
     */
    public void onEnter(Pathfinder pathfinder) {

    }

    /**
     * Code to be executed whenever a robot exits the zone.
     *
     * @param pathfinder the instance of Pathfinder.
     */
    public void onExit(Pathfinder pathfinder) {

    }

    /**
     * Code to be executed whenever the robot is inside the zone. This code is
     * executed every time Pathfinder's {@code tick()} method is called if the
     * robot is inside the zone.
     *
     * @param pathfinder the instance of Pathfinder.
     */
    public void whileInside(Pathfinder pathfinder) {

    }

    /**
     * Is the zone solid? There's no use for this right now, but it'll be
     * implemented later on.
     *
     * @return true if the zone is solid, otherwise, false.
     */
    public boolean isSolid() {
        return true;
    }
}
