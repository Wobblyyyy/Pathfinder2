/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;

import java.util.function.Supplier;

/**
 * A two-axis joystick. This joystick has two axes - an X axis and a Y
 * axis.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Joystick {
    private final Supplier<Double> xAxis;
    private final Supplier<Double> yAxis;
    private final double deadzone;

    /**
     * Create a new {@code Joystick} with the default deadzone of 0.05.
     *
     * @param xAxis the X axis supplier.
     * @param yAxis the Y axis supplier.
     */
    public Joystick(Supplier<Double> xAxis,
                    Supplier<Double> yAxis) {
        this(
                xAxis,
                yAxis,
                0.05
        );
    }

    /**
     * Create a new {@code Joystick}.
     *
     * @param xAxis    the X axis supplier.
     * @param yAxis    the Y axis supplier.
     * @param deadzone the joystick's deadzone.
     */
    public Joystick(Supplier<Double> xAxis,
                    Supplier<Double> yAxis,
                    double deadzone) {
        if (xAxis == null || yAxis == null)
            throw new NullPointerException("Null X or Y axis - not good!");

        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.deadzone = deadzone;
    }

    /**
     * Get the joystick's X value.
     *
     * @return the joystick's X value.
     */
    public double getX() {
        double x = xAxis.get();

        if (x <= deadzone) return 0;
        else return x;
    }

    /**
     * Get the joystick's Y value.
     *
     * @return the joystick's Y value.
     */
    public double getY() {
        double y = yAxis.get();

        if (y <= deadzone) return 0;
        else return y;
    }

    /**
     * Get a point in the form of (x, y), where x and y are joystick axis
     * values.
     *
     * @return a {@code PointXY} representation of the joystick's state.
     */
    public PointXY getPoint() {
        return new PointXY(getX(), getY());
    }

    /**
     * Get the magnitude of the joystick input. This is determined by
     * getting the distance between (0, 0) and (x, y).
     *
     * @return the magnitude of the joystick input.
     */
    public double getMagnitude() {
        return PointXY.distance(
                getPoint(),
                PointXY.zero()
        );
    }

    /**
     * Get the angle formed by the X and Y axes. This angle is determined
     * by looking at the angle between (0, 0) and (x, y).
     *
     * @return the angle formed by the X and Y axes.
     */
    public Angle getAngle() {
        return PointXY.zero().angleTo(getPoint());
    }

    /**
     * Does the X axis have a non-zero value?
     *
     * @return true if the X axis has a non-zero value; false otherwise.
     */
    public boolean isNonZeroX() {
        return Math.abs(getX()) > 0;
    }

    /**
     * Does the Y axis have a non-zero value?
     *
     * @return true if the Y axis has a non-zero value; false otherwise.
     */
    public boolean isNonZeroY() {
        return Math.abs(getY()) > 0;
    }

    /**
     * Do either of the axes have a non-zero value?
     *
     * @return true if X or Y has a non-zero value; false otherwise.
     */
    public boolean isNonZero() {
        return isNonZeroX() ||
                isNonZeroY();
    }
}
