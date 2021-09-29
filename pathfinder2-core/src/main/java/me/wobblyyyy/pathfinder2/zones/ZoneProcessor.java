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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneProcessor {
    private final Map<String, Zone> zones = new HashMap<>();
    private List<Zone> currentZones = new ArrayList<>();

    public ZoneProcessor() {

    }

    public void addZone(String name,
                        Zone zone) {
        if (name == null)
            throw new IllegalArgumentException("Zones must have a non-null name!");
        if (zone == null)
            throw new IllegalArgumentException("Zones cannot be null!");

        if (zones.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Zones cannot have duplicate names! Make sure " +
                            "every name is unique."
            );
        }

        zones.put(name, zone);
    }

    public void removeZone(String name) {
        if (name == null)
            throw new IllegalArgumentException("Zones must have a non-null name!");

        zones.remove(name);
    }

    public List<Zone> getContainingZones(PointXY point) {
        List<Zone> containingZones = new ArrayList<>();

        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            Zone zone = entry.getValue();

            if (zone.isPointInShape(point)) containingZones.add(zone);
        }

        return containingZones;
    }

    public List<Zone> getNonContainingZones(PointXY point) {
        List<Zone> containingZones = new ArrayList<>();

        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            Zone zone = entry.getValue();

            if (!zone.isPointInShape(point)) containingZones.add(zone);
        }

        return containingZones;
    }

    private static List<Zone> getEnteredZones(List<Zone> last,
                                              List<Zone> current) {
        List<Zone> enteredZones = new ArrayList<>();

        for (Zone zone : current) {
            if (!last.contains(zone)) enteredZones.add(zone);
        }

        return enteredZones;
    }

    private static List<Zone> getExitedZones(List<Zone> last,
                                             List<Zone> current) {
        List<Zone> exitedZones = new ArrayList<>();

        for (Zone zone : last) {
            if (!current.contains(zone)) exitedZones.add(zone);
        }

        return exitedZones;
    }

    public void update(PointXYZ position) {
        List<Zone> lastZones = currentZones;
        currentZones = getContainingZones(position);

        List<Zone> enteredZones = getEnteredZones(lastZones, currentZones);
        List<Zone> exitedZones = getExitedZones(lastZones, currentZones);

        for (Zone zone : enteredZones) {
            zone.onEnter(position);
        }

        for (Zone zone : currentZones) {
            zone.whileInside(position);
        }

        for (Zone zone : exitedZones) {
            zone.onExit(position);
        }
    }
}
