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
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

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
     * Create a new {@code ControlledTrajectory}.
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
        super(target, 0, tolerance, angleTolerance);

        this.speedController = speedController;
    }

    @Override
    public double speed(PointXYZ current) {
        double distance = current.distance(getTarget());

        return speedController.calculate(distance, 0);
    }
}
