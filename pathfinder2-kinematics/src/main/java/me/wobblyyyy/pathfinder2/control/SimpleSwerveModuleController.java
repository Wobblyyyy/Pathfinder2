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

/**
 * The simplest form of control for a swerve module. This class is an
 * extension of the {@link AngleDeltaController} class and only provides
 * a single constructor with a single parameter - the coefficient. Higher
 * coefficient values will mean the swerve module will turn faster. Lower
 * coefficient values will mean the swerve module will turn slower. It
 * typically takes some fine tuning to get this right.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SimpleSwerveModuleController extends AngleDeltaController {

    /**
     * Create a new {@code SimpleSwerveModuleController}.
     *
     * @param coefficient the turn speed coefficient. Higher values will
     *                    make the swerve module turn faster, and lower values
     *                    will make the swerve module turn slower. This value
     *                    should almost always be determined experimentally,
     *                    but a decent place to start is around 0.02.
     */
    public SimpleSwerveModuleController(double coefficient) {
        super(coefficient);
    }
}
