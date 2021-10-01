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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLocalizedGrid {
    @Test
    public void testPointToCoordConversion() {
        double minX = 5;
        double minY = 5;
        double maxX = 15;
        double maxY = 15;

        PointXY point1 = new PointXY(5, 5);
        PointXY point2 = new PointXY(15, 15);
        PointXY point3 = PointXY.midpoint(point1, point2);

        Grid grid1 = Grid.generateGrid(10, 10);
        Grid grid2 = Grid.generateGrid(20, 20);

        LocalizedGrid localized1 = new LocalizedGrid(grid1, minX, minY, maxX, maxY);
        LocalizedGrid localized2 = new LocalizedGrid(grid2, minX, minY, maxX, maxY);

        Coord coord1 = localized1.toCoord(point1);
        Coord coord2 = localized1.toCoord(point2);
        Coord coord3 = localized1.toCoord(point3);

        Assertions.assertEquals(0, coord1.x());
        Assertions.assertEquals(0, coord1.y());
        Assertions.assertEquals(9, coord2.x());
        Assertions.assertEquals(9, coord2.y());
        Assertions.assertEquals(5, coord3.x());
        Assertions.assertEquals(5, coord3.y());

        coord1 = localized2.toCoord(point1);
        coord2 = localized2.toCoord(point2);
        coord3 = localized2.toCoord(point3);

        Assertions.assertEquals(0, coord1.x());
        Assertions.assertEquals(0, coord1.y());
        Assertions.assertEquals(19, coord2.x());
        Assertions.assertEquals(19, coord2.y());
        Assertions.assertEquals(10, coord3.x());
        Assertions.assertEquals(10, coord3.y());
    }
}
