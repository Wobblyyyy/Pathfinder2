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
import me.wobblyyyy.pathfinder2.geometry.Rectangle;

/**
 * Wrapper class for localizing a {@link Grid}.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class LocalizedGrid {
    private final Grid grid;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final double xRatio;
    private final double yRatio;

    /**
     * Create a new {@code LocalizedGrid}.
     *
     * @param grid the grid to localize.
     * @param minX the "real" minimum x.
     * @param minY the "real" minimum y.
     * @param maxX the "real" maximum x.
     * @param maxY the "real" maximum y.
     */
    public LocalizedGrid(
        Grid grid,
        double minX,
        double minY,
        double maxX,
        double maxY
    ) {
        this.grid = grid;

        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        double sizeX = maxX - minX;
        double sizeY = maxY - minY;

        int xCount = grid.getWidth();
        int yCount = grid.getHeight();

        this.xRatio = sizeX / xCount;
        this.yRatio = sizeY / yCount;
    }

    /**
     * Create a new {@code LocalizedGrid}.
     *
     * @param xScaling the x scaling value.
     * @param yScaling the y scaling value.
     * @param minX     the grid's minimum X value.
     * @param minY     the grid's minimum Y value.
     * @param maxX     the grid's maximum X value.
     * @param maxY     the grid's maximum Y value.
     * @return a new localized grid.
     */
    public static LocalizedGrid generateLocalizedGrid(
        double xScaling,
        double yScaling,
        double minX,
        double minY,
        double maxX,
        double maxY
    ) {
        double sizeX = maxX - minX;
        double sizeY = maxY - minY;

        Grid grid = Grid.generateGrid(
            (int) Math.ceil(sizeX / xScaling),
            (int) Math.ceil(sizeY / yScaling)
        );

        return new LocalizedGrid(grid, minX, minY, maxX, maxY);
    }

    /**
     * Convert a point to a coordinate.
     *
     * @param point the point to convert.
     * @return the converted coordinate.
     */
    public Coord toCoord(PointXY point) {
        int x = (int) Math.round((point.x() - minX) / xRatio);
        int y = (int) Math.round((point.y() - minY) / yRatio);

        if (x >= grid.getWidth() - 1) x--;
        if (y >= grid.getWidth() - 1) y--;

        return new Coord(x, y);
    }

    /**
     * Convert a coordinate to a point.
     *
     * @param coord the coordinate to convert.t
     * @return the converted point.
     */
    public PointXY toPoint(Coord coord) {
        double x = (coord.x() * xRatio) + minX;
        double y = (coord.y() * yRatio) + minY;

        return new PointXY(x, y);
    }

    public Node getNode(PointXY point) {
        return grid.findNode(toCoord(point));
    }

    public List<PointXY> toPoints(List<Coord> coords) {
        List<PointXY> points = new ArrayList<>(coords.size());

        for (Coord coord : coords) {
            points.add(toPoint(coord));
        }

        return points;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public Grid getGrid() {
        return grid;
    }

    public Rectangle getRectangle() {
        return new Rectangle(minX, minY, maxX, maxY);
    }
}
