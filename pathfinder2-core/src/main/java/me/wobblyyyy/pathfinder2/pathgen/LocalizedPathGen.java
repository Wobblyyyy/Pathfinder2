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

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.zones.Zone;

/**
 * A localized implementation of the {@link PathGen} class. This allows you
 * to use {@link PointXY} instead of {@link Coord} or {@link Node} for paths.
 * If you're trying to dynamically generate paths, this is probably the class
 * you should use - it's designed to be as simple as possible.
 *
 * <p>
 * Please note that my experience with algorithms is... just about none, and
 * this is an absolutely horribly inefficient implementation of the A*
 * pathfinding algorithm. You should not expect performant pathfinding, and
 * you should not try to stress-test the path generator - it will break,
 * I promise you that.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class LocalizedPathGen {
    private final List<Zone> zones;
    private final double xScaling;
    private final double yScaling;

    public LocalizedPathGen(double xScaling, double yScaling) {
        this(new ArrayList<>(0), xScaling, yScaling);
    }

    public LocalizedPathGen(
        List<Zone> zones,
        double xScaling,
        double yScaling
    ) {
        this.zones = zones;
        this.xScaling = xScaling;
        this.yScaling = yScaling;
    }

    public static LocalizedPathGen withInflatedZones(
        List<Zone> zones,
        double xScaling,
        double yScaling,
        double robotRadius
    ) {
        return new LocalizedPathGen(
            Zone.inflate(zones, robotRadius),
            xScaling,
            yScaling
        );
    }

    public static LocalizedPathGen withInflatedZones(
        List<Zone> zones,
        double xScaling,
        double yScaling,
        double robotX,
        double robotY
    ) {
        return new LocalizedPathGen(
            Zone.inflate(zones, Math.hypot(robotX, robotY)),
            xScaling,
            yScaling
        );
    }

    /**
     * Get a path from point A to point B.
     *
     * @param start the start point.
     * @param end   the end point.
     * @return a path between the two points. If there is no valid path,
     * this will return null. If there aren't any obstacles, this will return
     * a list containing the start and end points. Otherwise, this will
     * contain several points that allow you to go from point A to point B.
     */
    public List<PointXY> getPath(PointXY start, PointXY end) {
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

        if (points.size() < 1) return null;

        if (!points.get(0).equals(start)) points.add(0, start);
        if (!points.get(points.size() - 1).equals(end)) points.add(end);

        // optimize points based on collinear lines because we're
        // just so incredibly cool like that
        return PathOptimizer.optimize(points);
    }
}
