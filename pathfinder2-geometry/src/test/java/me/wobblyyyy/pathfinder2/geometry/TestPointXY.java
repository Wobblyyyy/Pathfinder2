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

import org.junit.jupiter.api.Test;

/**
 * Test methods for the {@link PointXY} class.
 *
 * @author Colin Robertson
 */
public class TestPointXY {
    private static final PointXY A = new PointXY(0, 0);
    private static final PointXY B = new PointXY(1, 0);
    private static final PointXY C = new PointXY(0, 1);
    private static final PointXY D = new PointXY(1, 1);
    private static final PointXY E = new PointXY(-1, 0);
    private static final PointXY F = new PointXY(0, -1);
    private static final PointXY G = new PointXY(-1, -1);

    @Test
    public void testPointAdd() {
        assert A.add(B).x() == 1;
        assert A.add(B).y() == 0;
        assert A.add(C).x() == 0;
        assert A.add(C).y() == 1;

        assert G.add(A).x() == -1;
        assert G.add(A).y() == -1;
        assert G.add(D).x() == 0;
        assert G.add(D).y() == 0;
    }

    @Test
    public void testPointMultiply() {
        assert A.multiply(B).x() == 0;
        assert A.multiply(B).y() == 0;
        assert A.multiply(C).x() == 0;
        assert A.multiply(C).y() == 0;
        assert A.multiply(D).x() == 0;
        assert A.multiply(D).y() == 0;
        assert A.multiply(E).x() == 0;
        assert A.multiply(E).y() == 0;

        assert D.multiply(0.5).x() == 0.5;
        assert D.multiply(0.5).y() == 0.5;
        assert D.multiply(2.0).x() == 2;
        assert D.multiply(2.0).y() == 2;
        assert D.multiply(-1D).x() == -1;
        assert D.multiply(-1D).y() == -1;
        assert D.multiply(2).multiply(0.5).multiply(-1D).multiply(-1D).x() == 1;
        assert D.multiply(2).multiply(0.5).multiply(-1D).multiply(-1D).y() == 1;
    }

    @Test
    public void testDistance() {
        assert A.distance(D) == Math.hypot(1, 1);
        assert G.distance(D) == Math.hypot(2, 2);
    }

    @Test
    public void testDistanceX() {
        assert A.distanceX(D) == 1;
        assert G.distanceX(D) == 2;
    }

    @Test
    public void testDistanceY() {
        assert A.distanceY(D) == 1;
        assert G.distanceY(D) == 2;
    }

    @Test
    public void testRotate() {
        assert B.rotate(A, Angle.fromDeg(90)).x() <= 0.1;
        assert B.rotate(A, Angle.fromDeg(90)).y() >= 1.0;
    }

    @Test
    public void testCollinear() {
        assert PointXY.areCollinear(G, A, D);
    }
}
