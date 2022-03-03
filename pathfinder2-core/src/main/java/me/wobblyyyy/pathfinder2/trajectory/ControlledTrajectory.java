/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * An extension of the {@link LinearTrajectory} that adds a way to control
 * the speed of the robot at different points throughout the trajectory.
 *
 * <p>
 * The controller you provide to instances of this class will have a target
 * value of zero. The controller will attempt to minimize the distance the
 * robot is from the target point.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class ControlledTrajectory extends LinearTrajectory {
    /**
     * The trajectory's speed controller.
     */
    private final Controller speedController;

    /**
     * Create a new {@code ControlledTrajectory}. Calling this modify the
     * inputted controller as follows:
     *
     * <code><pre>
     * speedController.setMin(-1.0);
     * speedController.setMax(1.0);
     * </pre></code>
     *
     * @param target          the trajectory's target point.
     * @param speedController the controller that controls the speed of the
     *                        robot. This controller's input values are
     *                        the distance the robot is from the target point.
     * @param tolerance       the tolerance used in determining whether the
     *                        robot's X and Y coordinates match up with those
     *                        of the target point.
     * @param angleTolerance  the tolerance used in determining whether the
     *                        robot's heading matches up with whatever heading
     */
    public ControlledTrajectory(PointXYZ target,
                                Controller speedController,
                                double tolerance,
                                Angle angleTolerance) {
        super(
                ValidationUtils.validate(target, "target"),
                0,
                ValidationUtils.validate(tolerance, "tolerance"),
                ValidationUtils.validate(angleTolerance, "angleTolerance")
        );

        ValidationUtils.validate(speedController, "speedController");

        speedController.setMin(-1.0);
        speedController.setMax(1.0);

        this.speedController = speedController;
    }

    @Override
    public double speed(PointXYZ current) {
        ValidationUtils.validate(current, "current");

        double distance = current.distance(getTarget());

        ValidationUtils.validate(distance, "distance");

        double speed = Math.abs(speedController.calculate(distance, 0));

        InvalidSpeedException.throwIfInvalid(StringUtils.format(
                "Turn controller calculated an invalid speed! The speed " +
                        "value that was calculated was <%s>. This value " +
                        "should be greater than 0 and less than or " +
                        "equal to 1!",
                speed
        ), speed);

        return speed;
    }
}
