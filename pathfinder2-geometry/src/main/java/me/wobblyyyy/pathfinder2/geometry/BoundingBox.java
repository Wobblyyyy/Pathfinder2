/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

/**
 * Utilities for verifying numbers. I'd encourage you to not use this class,
 * it's only still here so nothing breaks.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class BoundingBox {

    private BoundingBox() {}

    public static boolean validate(double number, double min, double max) {
        return (min - 0.01) <= number && number <= (max + 0.01);
    }

    public static boolean isPointInBox(
        PointXY point,
        double minX,
        double minY,
        double maxX,
        double maxY
    ) {
        if (point == null) return false;

        double x = point.x();
        double y = point.y();

        boolean validX = validate(x, minX, maxX);
        boolean validY = validate(y, minY, maxY);

        return validX && validY;
    }

    public static boolean isPointInLine(PointXY point, Line line) {
        return isPointInBox(
            point,
            line.getMinX(),
            line.getMinY(),
            line.getMaxX(),
            line.getMaxY()
        );
    }
}
