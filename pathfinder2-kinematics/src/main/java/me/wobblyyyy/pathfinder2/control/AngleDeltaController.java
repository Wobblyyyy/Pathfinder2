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

import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * This controller is designed specifically for operations where the robot
 * is being fed angles in degrees or radians. This controller requires a
 * coefficient to be provided. This coefficient will be multiplied by the
 * minimum angle delta.
 *
 * <p>
 * For example, let's say the robot is currently facing 90 degrees. We want
 * the robot to face 135 degrees. This controller will calculate that the
 * robot must turn 45 degrees, and then multiply that number (45) by whatever
 * coefficient you provide. This number should typically be very low, but
 * it'll require some testing to get it right.
 * </p>
 *
 * <p>
 * This controller can operate in one of two modes. Firstly, there's the
 * degrees mode. Secondly, there's the radians mode. By default, unless
 * you specify otherwise, the controller will operate in degrees mode. Anyways.
 * When the {@link #calculate(double)} method is called, it will generate
 * a delta and a coefficient. If the controller is in degrees mode, the delta
 * will be the amount of degrees between the two angles. If the controller
 * is in radians mode, the delta will be the amount of radians between the
 * two angles. Pretty neat, right? Of course it is.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class AngleDeltaController extends AbstractController {
    private final double coefficient;

    private final Angle.AngleUnit angleUnit;

    /**
     * Create a new {@code AngleDeltaController}. By default, this controller
     * will be set to degrees mode.
     *
     * @param coefficient the coefficient that should be multiplied by the
     *                    angle delta.
     * @see AngleDeltaController
     */
    public AngleDeltaController(double coefficient) {
        this(coefficient, Angle.AngleUnit.DEGREES);
    }

    /**
     * Create a new {@code AngleDeltaController}. This constructor allows
     * you to select either degrees or radians mode.
     *
     * @param coefficient the coefficient that should be multiplied by
     *                    the angle delta.
     * @param angleUnit   the unit the angle's delta is measured in
     * @see AngleDeltaController
     */
    public AngleDeltaController(double coefficient, Angle.AngleUnit angleUnit) {
        this.coefficient = coefficient;
        this.angleUnit = angleUnit;
    }

    @Override
    public double calculate(double value) {
        boolean isDegrees = angleUnit == Angle.AngleUnit.DEGREES;

        Angle current = Angle.fixedDeg(value);
        Angle target = Angle.fixedRad(this.getTarget());

        double delta = isDegrees
            ? Angle.angleDeltaDeg(current, target)
            : Angle.angleDeltaRad(current, target);

        return delta * this.coefficient;
    }
}
