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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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

    @Test
    public void testFullyObstructedPath() {
        List<Zone> zones = new ArrayList<Zone>() {{
            add(new Zone(new Rectangle(0, 3, 10, 4)));
        }};

        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);

        PointXY start = new PointXY(0, 0);
        PointXY end = new PointXY(10, 10);

        List<PointXY> path = gen.getPath(start, end);

        Assertions.assertNull(path);
    }

    @Test
    public void testPartiallyObstructedPath() {
        List<Zone> zones = new ArrayList<Zone>() {{
            add(new Zone(new Rectangle(1, 3, 10, 4)));
        }};

        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);

        PointXY start = new PointXY(0, 0);
        PointXY end = new PointXY(10, 10);

        List<PointXY> path = gen.getPath(start, end);

        for (PointXY point : path) {
            Assertions.assertFalse(zones.get(0).isPointInShape(point));
        }

        Assertions.assertNotNull(path);
    }

    @Test
    public void testHugePath() {
        List<Zone> zones = new ArrayList<Zone>() {{
            add(new Zone(new Rectangle(10, 30, 100, 40)));
        }};

        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.75, 0.75);

        PointXY start = new PointXY(0, 0);
        PointXY end = new PointXY(100, 100);

        List<PointXY> path = gen.getPath(start, end);

        for (PointXY point : path) {
            Assertions.assertFalse(zones.get(0).isPointInShape(point));
        }

        Assertions.assertNotNull(path);
    }

    @Test
    public void testUnobstructedNegativePathfinding() {
        List<Zone> zones = new ArrayList<>();
        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);
        PointXY start = new PointXY(-5, -5);
        PointXY end = new PointXY(5, 5);
        List<PointXY> path = gen.getPath(start, end);
        Assertions.assertNotNull(path);
        Assertions.assertEquals(2, path.size());
    }

    @Test
    public void testObstructedNegativePathfinding() {
        List<Zone> zones = new ArrayList<Zone>() {{
            add(new Zone(new Rectangle(-3, -2, 8, 3)));
        }};
        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);
        PointXY start = new PointXY(-5, -5);
        PointXY end = new PointXY(5, 5);
        List<PointXY> path = gen.getPath(start, end);
        Assertions.assertNotNull(path);
    }
}
