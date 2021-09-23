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
 * @author Colin Robertson
 * @since 0.1.0
 */
public class PointSlopeForm implements EquationForm {
    private final PointXY point;
    private final double slope;

    public PointSlopeForm(PointXY point,
                          double slope) {
        this.point = point;
        this.slope = slope;
    }

    @Override
    public double getSlope() {
        return slope;
    }

    @Override
    public double getY(double x) {
        return (slope * (x - point.x())) + point.y();
    }

    @Override
    public PointXY getPoint(double x) {
        return new PointXY(
                x,
                getY(x)
        );
    }

    @Override
    public Angle getSlopeAngle() {
        return null;
    }

    @Override
    public Angle getPerpendicularSlopeAngle() {
        return null;
    }

    @Override
    public Line toLine(double minX,
                       double maxX) {
        return new SlopeInterceptForm(
                slope,
                point.y() - (slope * point.x())
        ).toLine(minX, maxX);
    }
}
