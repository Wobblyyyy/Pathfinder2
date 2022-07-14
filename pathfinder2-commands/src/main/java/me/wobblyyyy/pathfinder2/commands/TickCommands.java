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
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

public class TickCommands {

    private TickCommands() {}

    public static final Command TICK_COMMAND = new Command(
        "tick",
        (pathfinder, args) -> {
            pathfinder.tick();
        },
        0
    );

    public static final Command TICK_UNTIL_COMMAND = new Command(
        "tickUntil",
        (pathfinder, args) -> {
            if (args.length == 0) {
                pathfinder.tickUntil();
            } else {
                pathfinder.tickUntil(Double.parseDouble(args[0]));
            }
        },
        0,
        1
    );

    public static void addTickCommands(CommandRegistry registry) {
        registry.unsafeAdd(TICK_COMMAND);
        registry.unsafeAdd(TICK_UNTIL_COMMAND);
    }
}
