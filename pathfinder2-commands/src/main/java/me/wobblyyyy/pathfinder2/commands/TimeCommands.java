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

import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;

public class TimeCommands {

    private TimeCommands() {}

    public static Command WAIT_COMMAND = new Command(
        "wait",
        (pathfinder, args) -> {
            double time = Double.parseDouble(args[0]);
            Logger.debug(TimeCommands.class, "waiting for <%s> ms", time);
            ElapsedTimer.wait(time);
        },
        1
    );

    public static void addTimeCommands(CommandRegistry registry) {
        registry.unsafeAdd(WAIT_COMMAND);
    }
}
