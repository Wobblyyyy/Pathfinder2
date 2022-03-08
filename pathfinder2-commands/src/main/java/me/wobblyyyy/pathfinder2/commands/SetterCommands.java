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

public class SetterCommands {

    private SetterCommands() {}

    public static Command SET_SPEED_COMMAND = new Command(
        "setSpeed",
        (pathfinder, args) -> {
            pathfinder.setSpeed(Double.parseDouble(args[0]));
        }
    );

    public static Command SET_TOLERANCE_COMMAND = new Command(
        "setTolerance",
        (pathfinder, args) -> {
            pathfinder.setTolerance(Double.parseDouble(args[0]));
        }
    );

    public static Command SET_ANGLE_TOLERANCE_COMMAND = new Command(
        "setAngleTolerance",
        (pathfinder, args) -> {
            pathfinder.setAngleTolerance(Angle.parse(args[0]));
        }
    );

    public static void addSetterCommands(CommandRegistry registry) {
        registry.add(SET_SPEED_COMMAND);
        registry.add(SET_TOLERANCE_COMMAND);
        registry.add(SET_ANGLE_TOLERANCE_COMMAND);
    }
}
