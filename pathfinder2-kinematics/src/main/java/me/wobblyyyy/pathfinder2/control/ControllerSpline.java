/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.control;

import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.math.Spline;

/**
 * A {@link Spline} implementation that makes use of a {@link Controller}.
 *
 * @author Colin Robertson
 * @since 2.0.0
 */
public class ControllerSpline implements Spline {
    private PointXY startPoint = PointXY.ZERO;
    private PointXY endPoint = PointXY.ZERO;
    private Controller controller;

    /**
     * Create a new {@code ControllerSpline}.
     *
     * @param controller the controller to use inside the spline.
     */
    public ControllerSpline(Controller controller) {
        this.controller = controller;
    }

    public ControllerSpline setController(Controller controller) {
        this.controller = controller;

        return this;
    }

    public ControllerSpline setStartPoint(PointXY startPoint) {
        this.startPoint = startPoint;

        return this;
    }

    public ControllerSpline setEndPoint(PointXY endPoint) {
        this.endPoint = endPoint;

        return this;
    }

    @Override
    public double interpolateY(double x) {
        return controller.calculate(x);
    }

    @Override
    public PointXY getStartPoint() {
        return startPoint;
    }

    @Override
    public PointXY getEndPoint() {
        return endPoint;
    }

    public Controller getController() {
        return controller;
    }
}
