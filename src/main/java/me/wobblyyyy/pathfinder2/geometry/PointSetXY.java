package me.wobblyyyy.pathfinder2.geometry;

import java.util.ArrayList;
import java.util.List;

// MUTABLE
public class PointSetXY {
    private final List<PointXY> points;

    public PointSetXY() {
        this(new ArrayList<>());
    }

    public PointSetXY(List<PointXY> points) {
        this.points = points;
    }

    public void rotate(PointXY center,
                       Angle angle) {
        for (int i = 0; i < points.size(); i++) {
            points.set(i, PointXY.rotate(points.get(i), center, angle));
        }
    }

    public PointXY average() {
        PointXY sum = points.get(0);

        for (int i = 1; i < points.size(); i++) {
            sum = sum.add(points.get(i));
        }

        return sum.multiply(1D / points.size());
    }

    public void addX(double x) {
        add(new PointXY(x, 0));
    }

    public void addY(double y) {
        add(new PointXY(0, y));
    }

    public void multiplyX(double x) {
        multiply(new PointXY(x, 1));
    }

    public void multiplyY(double y) {
        multiply(new PointXY(1, y));
    }

    public void add(PointXY a) {
        for (int i = 0; i < points.size(); i++) {
            points.set(i, points.get(i).add(a));
        }
    }

    public void multiply(PointXY a) {
        for (int i = 0; i < points.size(); i++) {
            points.set(i, points.get(i).multiply(a));
        }
    }

    public void multiply(double a) {
        multiply(new PointXY(a, a));
    }

    public void sortByX() {
        points.sort((a, b) ->
                (int) (a.x() - b.x()));
    }

    public void sortByY() {
        points.sort((a, b) ->
                (int) (a.y() - b.y()));
    }

    public void sort() {
        points.sort((a, b) ->
                (int) ((a.x() + a.y()) / 2 - (b.x() + b.y() / 2)));
    }

    public double maxX() {
        sortByX();
        return points.get(points.size() - 1).x();
    }

    public double maxY() {
        sortByY();
        return points.get(points.size() - 1).y();
    }

    public double minX() {
        sortByX();
        return points.get(0).x();
    }

    public double minY() {
        sortByY();
        return points.get(0).y();
    }
}
