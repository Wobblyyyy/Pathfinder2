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
import me.wobblyyyy.pathfinder2.math.Equals;
import me.wobblyyyy.pathfinder2.zones.Zone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestPathOptimizer {
    @Test
    public void testLinearOptimization() {
        List<Zone> zones = new ArrayList<>();

        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);

        PointXY start = new PointXY(0, 0);
        PointXY end = new PointXY(10, 10);

        List<PointXY> path = gen.getPath(start, end);

        path = PathOptimizer.optimize(path);

        Assertions.assertNotNull(path);
    }

    @Test
    public void testNonlinearOptimization() {
        Rectangle blockerShape = new Rectangle(5, 1, 6, 10);
        Zone blockerZone = new Zone(blockerShape);
        List<Zone> zones = new ArrayList<Zone>() {{
            add(blockerZone);
        }};

        LocalizedPathGen gen = new LocalizedPathGen(zones, 0.5, 0.5);

        PointXY start = new PointXY(0, 0);
        PointXY end = new PointXY(10, 10);

        List<PointXY> unoptimized = gen.getPath(start, end);
        List<PointXY> optimized = PathOptimizer.optimize(unoptimized);
        List<PointXY> overOptimized = PathOptimizer.optimize(optimized);

        Assertions.assertNotNull(unoptimized);
        Assertions.assertNotNull(optimized);
        Assertions.assertNotNull(overOptimized);

        int unoptimizedSize = unoptimized.size();
        int optimizedSize = optimized.size();
        int overOptimizedSize = overOptimized.size();

        double unoptimizedLength = PathOptimizer.determineLength(unoptimized);
        double optimizedLength = PathOptimizer.determineLength(optimized);
        double overOptimizedLength = PathOptimizer.determineLength(overOptimized);

//        Assertions.assertEquals(30, unoptimizedSize);
//        Assertions.assertEquals(16, optimizedSize);
//        Assertions.assertEquals(16, overOptimizedSize);

//        Assertions.assertTrue(Equals.soft(16.778, unoptimizedLength, 0.01));
//        Assertions.assertEquals(optimizedLength, overOptimizedLength);
    }
}
