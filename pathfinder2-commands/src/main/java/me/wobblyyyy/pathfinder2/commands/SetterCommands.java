/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.commands;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;

public class SetterCommands {

    private SetterCommands() {}

    public static final Command SET_SPEED_COMMAND = new Command(
        "setSpeed",
        (pathfinder, args) -> {
            pathfinder.setSpeed(Double.parseDouble(args[0]));
        }
    );

    public static final Command SET_TOLERANCE_COMMAND = new Command(
        "setTolerance",
        (pathfinder, args) -> {
            pathfinder.setTolerance(Double.parseDouble(args[0]));
        }
    );

    public static final Command SET_ANGLE_TOLERANCE_COMMAND = new Command(
        "setAngleTolerance",
        (pathfinder, args) -> {
            pathfinder.setAngleTolerance(Angle.parse(args[0]));
        }
    );

    public static final Command SET_TRANSLATION_COMMAND = new Command(
        "setTranslation",
        (pathfinder, args) -> {
            pathfinder.setTranslation(Translation.parse(args[0]));
        }
    );

    public static final Command SET_VX_COMMAND = new Command(
        "setVx",
        (pathfinder, args) -> {
            pathfinder.setVx(Double.parseDouble(args[0]));
        }
    );

    public static final Command SET_VY_COMMAND = new Command(
        "setVy",
        (pathfinder, args) -> {
            pathfinder.setVy(Double.parseDouble(args[0]));
        }
    );

    public static final Command SET_VZ_COMMAND = new Command(
        "setVz",
        (pathfinder, args) -> {
            pathfinder.setVz(Double.parseDouble(args[0]));
        }
    );

    public static void addSetterCommands(CommandRegistry registry) {
        registry.unsafeAdd(SET_SPEED_COMMAND);
        registry.unsafeAdd(SET_TOLERANCE_COMMAND);
        registry.unsafeAdd(SET_ANGLE_TOLERANCE_COMMAND);
        registry.unsafeAdd(SET_TRANSLATION_COMMAND);
        registry.unsafeAdd(SET_VX_COMMAND);
        registry.unsafeAdd(SET_VY_COMMAND);
        registry.unsafeAdd(SET_VZ_COMMAND);
    }
}
