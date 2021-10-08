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

/**
 * I'm not entirely sure why I made this class, to be honest.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class AStarPathFinder {
    private final LocalizedPathGen pathGen;

    public AStarPathFinder(GridScaling scale,
                           double robotWidth,
                           double robotLength,
                           List<Zone> zones) {
        pathGen = LocalizedPathGen.withInflatedZones(
                zones,
                scale.getScaleX(),
                scale.getScaleY(),
                robotWidth,
                robotLength
        );
    }

    public List<PointXY> getPath(PointXY start,
                                 PointXY end) {
        return pathGen.getPath(start, end);
    }
}
