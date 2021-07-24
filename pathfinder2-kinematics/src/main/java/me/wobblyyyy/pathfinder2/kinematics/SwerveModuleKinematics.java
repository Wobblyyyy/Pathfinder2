/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import me.wobblyyyy.pathfinder2.control.AngleDeltaController;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * Kinematics for a single swerve module. In essence, a swerve module's
 * kinematics is a fancy way of saying "a controller that looks at the
 * module's current angle and the module's target angle and determines
 * how to get to that angle."
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SwerveModuleKinematics {
    /**
     * The swerve module's turn controller.
     */
    private final Controller controller;

    /**
     * Create a new instance of the swerve module kinematics class.
     *
     * @param controller the swerve module's controller. This controller
     *                   will receive two inputs. The controller's current
     *                   value will always be the module's current angle,
     *                   in degrees. The controller's target value will
     *                   always be the module's target angle, once again,
     *                   in degrees.
     */
    public SwerveModuleKinematics(Controller controller) {
        this.controller = controller;
    }

    /**
     * Make use of an {@link AngleDeltaController}. Unless you understand
     * what that is, and what the coefficient value means, you should probably
     * not use this. Or actually, better yet, you should go read the
     * JavaDoc in the {@link AngleDeltaController} class.
     *
     * @param coefficient the coefficient that will be used in creating the
     *                    angle delta controller. Please note that the
     *                    controller will ALWAYS be in degrees mode.
     * @see AngleDeltaController
     */
    public SwerveModuleKinematics(double coefficient) {
        this(new AngleDeltaController(coefficient));
    }

    /**
     * Calculate a power value for the swerve module's turn motor based on
     * a current and a target angle.
     *
     * @param current the swerve module's current angle.
     * @param target the swerve module's target angle.
     * @return how much power the swerve module's turn motor should receive
     * in order to make the current angle match up with the target angle.
     */
    public double calculate(Angle current,
                            Angle target) {
        return controller.calculate(
                current.deg(),
                target.deg()
        );
    }

    /**
     * Get the swerve module kinematics' controller.
     *
     * @return the module's controller.
     */
    public Controller getController() {
        return this.controller;
    }
}
