/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.pathgen;

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.Rectangle;
import me.wobblyyyy.pathfinder2.zones.Zone;

import java.util.ArrayList;
import java.util.List;

public class NodeValidator {
    public static void validateNodes(LocalizedGrid grid,
                                     List<Zone> zones) {
        List<Node> nodes = grid.getGrid().getNodes();

        Rectangle bounds = grid.getRectangle();

        List<Zone> filteredZones = new ArrayList<>(zones.size());

        for (Zone zone : zones) {
            if (zone.isSolid() && zone.getShape().doesCollideWith(bounds)) {
                filteredZones.add(zone);
            }
        }

        if (filteredZones.size() == 0) return;

        for (Node node : nodes) {
            PointXY point = grid.toPoint(new Coord(node));

            for (Zone zone : filteredZones) {
                if (point.isInside(zone.getShape())) node.setValid(false);
            }
        }
    }
}
