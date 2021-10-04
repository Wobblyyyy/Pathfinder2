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
import me.wobblyyyy.pathfinder2.zones.Zone;

import java.util.List;

public class LocalizedPathGen {
    private final List<Zone> zones;
    private final double xScaling;
    private final double yScaling;

    public LocalizedPathGen(List<Zone> zones,
                            double xScaling,
                            double yScaling) {
        this.zones = zones;
        this.xScaling = xScaling;
        this.yScaling = yScaling;
    }

    public List<PointXY> getPath(PointXY start,
                                 PointXY end) {
        double minX = PointXY.minimumX(start, end);
        double minY = PointXY.minimumY(start, end);
        double maxX = PointXY.maximumX(start, end);
        double maxY = PointXY.maximumY(start, end);

        LocalizedGrid grid = LocalizedGrid.generateLocalizedGrid(
                xScaling,
                yScaling,
                minX,
                minY,
                maxX,
                maxY
        );

        NodeValidator.validateNodes(grid, zones);

        PathGen gen = new PathGen(
                grid.getGrid(),
                grid.getNode(start),
                grid.getNode(end)
        );

        List<PointXY> points = grid.toPoints(gen.findCoordPath());

        if (!points.get(0).equals(start)) points.add(0, start);
        if (!points.get(points.size() - 1).equals(end)) points.add(end);

        return PathOptimizer.optimize(points);
    }
}
