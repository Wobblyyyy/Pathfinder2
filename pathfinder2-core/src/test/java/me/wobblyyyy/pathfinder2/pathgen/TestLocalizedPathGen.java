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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestLocalizedPathGen {
    @Test
    public void testUnobstructedPath() {
        List<Zone> zones = new ArrayList<>();

        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);

        PointXY start = new PointXY(0, 0);
        PointXY end = new PointXY(10, 10);

        List<PointXY> path = gen.getPath(start, end);
    }
}
