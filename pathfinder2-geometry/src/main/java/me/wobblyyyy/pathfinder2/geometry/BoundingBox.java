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

import java.util.function.Supplier;

/**
 * A bounding box allows you to check if geometrical objects fit inside a
 * given range of X and Y values.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class BoundingBox {
    private final double minimumX;
    private final double minimumY;
    private final double maximumX;
    private final double maximumY;

    public BoundingBox(double minimumX,
                       double minimumY,
                       double maximumX,
                       double maximumY) {
        this.minimumX = minimumX;
        this.minimumY = minimumY;
        this.maximumX = maximumX;
        this.maximumY = maximumY;
    }

    public BoundingBox(PointXY point1,
                       PointXY point2) {
        this(
                Math.min(point1.x(), point2.x()),
                Math.min(point1.y(), point2.y()),
                Math.max(point1.x(), point2.x()),
                Math.max(point1.y(), point2.y())
        );
    }

    private static boolean checkValue(Supplier<Double> value,
                                      double minimum,
                                      double maximum) {
        double v = value.get();
        return v >= minimum && v <= maximum;
    }

    public static boolean isInBox(PointXY point,
                                  double minimumX,
                                  double minimumY,
                                  double maximumX,
                                  double maximumY) {
        boolean validX = checkValue(point::x, minimumX, maximumX);
        boolean validY = checkValue(point::y, minimumY, maximumY);

        return validX && validY;
    }

    public static boolean isInBox(PointXY point,
                                  PointXY boxPoint1,
                                  PointXY boxPoint2) {
        return isInBox(
                point,
                PointXY.minimumX(boxPoint1, boxPoint2),
                PointXY.minimumY(boxPoint1, boxPoint2),
                PointXY.maximumX(boxPoint1, boxPoint2),
                PointXY.maximumY(boxPoint1, boxPoint2)
        );
    }

    public static boolean isInBox(Line line,
                                  PointXY boxPoint1,
                                  PointXY boxPoint2) {
        boolean isStartValid = isInBox(
                line.getStartPoint(),
                boxPoint1,
                boxPoint2
        );

        boolean isEndValid = isInBox(
                line.getEndPoint(),
                boxPoint1,
                boxPoint2
        );

        return isStartValid && isEndValid;
    }

    public static boolean isInBox(Shape shape,
                                  PointXY boxPoint1,
                                  PointXY boxPoint2) {
        double minX = PointXY.minimumX(boxPoint1, boxPoint2);
        double minY = PointXY.minimumY(boxPoint1, boxPoint2);
        double maxX = PointXY.maximumX(boxPoint1, boxPoint2);
        double maxY = PointXY.maximumY(boxPoint1, boxPoint1);

        PointXY center = new PointXY(
                (minX + maxX) / 2,
                (minY + maxY) / 2
        );

        return new Rectangle(
                new PointXY(minX, minY),
                new PointXY(maxX, maxY)
        ).isPointInShape(shape.getClosestPoint(center));
    }

    public double getMinimumX() {
        return minimumX;
    }

    public double getMinimumY() {
        return minimumY;
    }

    public double getMaximumX() {
        return maximumX;
    }

    public double getMaximumY() {
        return maximumY;
    }

    public boolean isInBox(PointXY point) {
        return isInBox(
                point,
                this.minimumX,
                this.minimumY,
                this.maximumX,
                this.maximumY
        );
    }
}
