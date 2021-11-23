/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.modifiers;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Easy-to-use builder class for creating odometry modifiers.
 *
 * <p>
 * The order these modifications are applied in is as follows:
 * <ul>
 *     <li>Swap X and Y</li>
 *     <li>Reflect new X</li>
 *     <li>Reflect new Y</li>
 *     <li>Apply X, Y, and Z offsets</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class OdometryModifier {
    /**
     * Should the X and Y values be swapped?
     */
    private boolean swapXY = false;

    /**
     * Should X values be reflected?
     */
    private boolean reflectX = false;

    /**
     * Should Y values be reflected?
     */
    private boolean reflectY = false;

    /**
     * X reflection axis.
     */
    private double xReflectionAxis = 0.0;

    /**
     * Y reflection axis.
     */
    private double yReflectionAxis = 0.0;

    /**
     * X offset.
     */
    private double xOffset = 0.0;

    /**
     * Y offset.
     */
    private double yOffset = 0.0;

    /**
     * Z offset.
     */
    private Angle zOffset = Angle.zero();

    /**
     * Should the outputted X and Y values be swapped?
     *
     * @param swapXY whether outputted X and Y values should be swapped.
     *               True means X now equals Y and Y now equals X. False means
     *               there's no modification.
     */
    public void swapXY(boolean swapXY) {
        this.swapXY = swapXY;
    }

    /**
     * Should X values be reflected across an axis? And if so, what is that
     * axis?
     *
     * @param reflectX should X values be reflected across a specified Y
     *                 axis? True means yes, false means no.
     * @param axis     the Y value that X values will be reflected over. If
     *                 you'd like to "invert" and X value (positive now equals
     *                 negative, negative now equals positive) you should
     *                 use an axis of 0.0.
     */
    public void reflectX(boolean reflectX,
                         double axis) {
        this.reflectX = reflectX;
        this.xReflectionAxis = axis;
    }

    /**
     * Should Y values be reflected across an axis? And if so, what is that
     * axis?
     *
     * @param reflectY should Y values be reflected across a specified X
     *                 axis? True means yes, false means no.
     * @param axis     the X value that Y values will be reflected over. If
     *                 you'd like to "invert" and Y value (positive now equals
     *                 negative, negative now equals positive) you should
     *                 use an axis of 0.0.
     */
    public void reflectY(boolean reflectY,
                         double axis) {
        this.reflectY = reflectY;
        this.yReflectionAxis = axis;
    }

    /**
     * Set the offset applied to outputted X values.
     *
     * @param xOffset X offset.
     */
    public void offsetX(double xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Set the offset applied to outputted Y values.
     *
     * @param yOffset Y offset.
     */
    public void offsetY(double yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * Set the offset applied to outputted Z values.
     *
     * @param zOffset Z offset.
     */
    public void offsetZ(Angle zOffset) {
        this.zOffset = zOffset;
    }

    private double reflect(double value,
                           double axis) {
        return axis - (value - axis);
    }

    /**
     * Modify an inputted point according to whatever rules have been applied
     * to the {@code OdometryModifier} this method belongs to.
     *
     * @param point the point you'd like to modify.
     * @return the modified point.
     */
    public PointXYZ modify(PointXYZ point) {
        if (swapXY) {
            point = new PointXYZ(
                    point.y(),
                    point.x(),
                    point.z()
            );
        }

        if (reflectX) {
            point = point.withX(reflect(point.x(), xReflectionAxis));
        }

        if (reflectY) {
            point = point.withY(reflect(point.y(), yReflectionAxis));
        }

        PointXYZ offset = new PointXYZ(
                xOffset,
                yOffset,
                zOffset
        );

        return point.add(offset);
    }
}
