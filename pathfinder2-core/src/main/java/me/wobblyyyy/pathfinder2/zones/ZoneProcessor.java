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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPluginManager;

/**
 * A {@code ZoneProcessor} is responsible for dealing with {@link Zone}
 * objects, allowing you to do some pretty cool stuff. Unfortunately, I
 * still can't juggle chainsaws (which is incredibly cool), but you wanna
 * know what's just as cool? A {@code ZoneProcessor}.
 *
 * <p>
 * Ever wanted to have your robot perform some certain action automatically?
 * Well, of course you have. But have you ever wanted your robot to perform
 * something automatically as soon as it enters (or exits) (or is inside)
 * a certain area?
 * </p>
 *
 * <p>
 * {@code ZoneProcessor} is operated by using the {@link #update(Pathfinder)}
 * method. It should accept the instance of Pathfinder that you're using
 * as a parameter. With that, it'll do magical stuff... basically.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class ZoneProcessor {
    private final Map<String, Zone> zones = new HashMap<>();
    private List<Zone> currentZones = new ArrayList<>();

    /**
     * Create a new {@code ZoneProcessor}.
     */
    public ZoneProcessor() {}

    private static List<Zone> getEnteredZones(
        List<Zone> last,
        List<Zone> current
    ) {
        List<Zone> enteredZones = new ArrayList<>();

        for (Zone zone : current) {
            if (!last.contains(zone)) enteredZones.add(zone);
        }

        return enteredZones;
    }

    private static List<Zone> getExitedZones(
        List<Zone> last,
        List<Zone> current
    ) {
        List<Zone> exitedZones = new ArrayList<>();

        for (Zone zone : last) {
            if (!current.contains(zone)) exitedZones.add(zone);
        }

        return exitedZones;
    }

    /**
     * Add a zone to collection of zones the processor is handling.
     *
     * @param name the name of the zone. This name will be needed if you're
     *             planning on removing the zone or referencing it at some
     *             point. This is also used for getting a list of names
     *             of zones.
     * @param zone the zone.
     */
    public void addZone(String name, Zone zone) {
        if (name == null) throw new IllegalArgumentException(
            "Zones must have a non-null name!"
        );
        if (zone == null) throw new IllegalArgumentException(
            "Zones cannot be null!"
        );

        if (zones.containsKey(name)) {
            throw new IllegalArgumentException(
                "Zones cannot have duplicate names! Make sure " +
                "every name is unique."
            );
        }

        zones.put(name, zone);
    }

    /**
     * Remove a zone based on the zone's name.
     *
     * @param name the name of the zone to remove.
     */
    public void removeZone(String name) {
        if (name == null) throw new IllegalArgumentException(
            "Zones must have a non-null name!"
        );

        zones.remove(name);
    }

    /**
     * Get a list of zones that contain the provided point.
     *
     * @param point the point to test.
     * @return a list of zones that contain the provided point.
     */
    public List<Zone> getContainingZones(PointXY point) {
        List<Zone> containingZones = new ArrayList<>();

        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            Zone zone = entry.getValue();

            if (zone.isPointInShape(point)) containingZones.add(zone);
        }

        return containingZones;
    }

    /**
     * Get a list of zones that do not contain the provided point.
     *
     * @param point the point to test.
     * @return a list of zones that do not contain the provided point.
     */
    public List<Zone> getNonContainingZones(PointXY point) {
        List<Zone> containingZones = new ArrayList<>();

        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            Zone zone = entry.getValue();

            if (!zone.isPointInShape(point)) containingZones.add(zone);
        }

        return containingZones;
    }

    /**
     * Based on the provided point, call the correct methods of each of
     * the zones.
     *
     * <ul>
     *     <li>
     *         If the robot has ENTERED the zone (previously, it was not in
     *         the zone, but now it is), the zone's {@link Zone#onEnter(Pathfinder)}
     *         method will be called.
     *     </li>
     *     <li>
     *         If the robot has EXITED the zone (previously, it was in the zone,
     *         but now it is not), the zone's {@link Zone#onExit(Pathfinder)}
     *         method will be called.
     *     </li>
     *     <li>
     *         If the robot is INSIDE the zone (this will be activated every
     *         time the {@code onEnter} method is called, as well as whenever
     *         the robot is inside the zone), the zone's
     *         {@link Zone#whileInside(Pathfinder)} method will be called.
     *     </li>
     * </ul>
     *
     * @param pathfinder the instance of Pathfinder.
     */
    public void update(Pathfinder pathfinder) {
        if (zones.size() == 0) return;

        PathfinderPluginManager manager = pathfinder.getPluginManager();

        List<Zone> lastZones = currentZones;
        currentZones = getContainingZones(pathfinder.getPosition());

        List<Zone> enteredZones = getEnteredZones(lastZones, currentZones);
        List<Zone> exitedZones = getExitedZones(lastZones, currentZones);

        for (Zone zone : enteredZones) {
            zone.onEnter(pathfinder);
            manager.onEnterZone(pathfinder, zone);
        }

        for (Zone zone : currentZones) {
            zone.whileInside(pathfinder);
            manager.whileInsideZone(pathfinder, zone);
        }

        for (Zone zone : exitedZones) {
            zone.onExit(pathfinder);
            manager.onExitZone(pathfinder, zone);
        }
    }
}
