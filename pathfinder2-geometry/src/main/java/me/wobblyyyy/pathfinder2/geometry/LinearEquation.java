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

import java.io.Serializable;

public interface LinearEquation extends Serializable {
    double getY(double x);
    PointXY getPoint(double x);
    PointXY getPoint();
    double getSlope();
    double getIntercept();
    boolean isVertical();
    double getVertical();
    PointXY getIntersection(LinearEquation equation);
    boolean intersectsWith(LinearEquation equation);
}
