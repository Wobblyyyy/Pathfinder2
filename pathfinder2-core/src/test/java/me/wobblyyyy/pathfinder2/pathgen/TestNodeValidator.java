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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestNodeValidator {
    @Test
    public void testNodeValidation() {
        LocalizedGrid grid = new LocalizedGrid(
                Grid.generateGrid(10, 10),
                0,
                0,
                10,
                10
        );

        Rectangle blocker = new Rectangle(5, 0, 10, 10);
        Zone blockerZone = new Zone(blocker);
        List<Zone> zones = new ArrayList<>(1) {{
            add(blockerZone);
        }};

        NodeValidator.validateNodes(grid, zones);

        Assertions.assertTrue(grid.getNode(new PointXY(0, 0)).isValid());
        Assertions.assertFalse(grid.getNode(new PointXY(6, 6)).isValid());
        Assertions.assertFalse(grid.getNode(new PointXY(5, 0)).isValid());
        Assertions.assertTrue(grid.getNode(new PointXY(3, 5)).isValid());
        Assertions.assertFalse(grid.getNode(new PointXY(7, 7)).isValid());
        Assertions.assertFalse(grid.getNode(new PointXY(8, 8)).isValid());
    }
}
