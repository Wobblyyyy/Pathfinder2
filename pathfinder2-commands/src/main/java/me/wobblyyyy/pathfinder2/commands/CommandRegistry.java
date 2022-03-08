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

import java.util.HashMap;
import java.util.Map;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>(100);
    private final Pathfinder pathfinder;

    public CommandRegistry(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    public static CommandRegistry createDefaultRegistry(Pathfinder pathfinder) {
        CommandRegistry registry = new CommandRegistry(pathfinder);
        loadDefaultCommands(registry);
        return registry;
    }

    public static void loadDefaultCommands(CommandRegistry registry) {
        SetterCommands.addSetterCommands(registry);
        MovementCommands.addMovementCommands(registry);
        TickCommands.addTickCommands(registry);
    }

    public void add(Command command) {
        commands.put(command.getCommand(), command);
    }

    public boolean isCommand(String command) {
        return commands.containsKey(command);
    }

    public CommandRegistry execute(
        Pathfinder pathfinder,
        String[] allArguments
    ) {
        int length = allArguments.length;

        if (length < 1) throw new IllegalArgumentException(
            "Too few arguments! Expected at least 1 argument but got 0."
        );

        String cmd = allArguments[0];

        if (isCommand(cmd)) {
            String[] arguments = new String[length - 1];
            System.arraycopy(allArguments, 1, arguments, 0, length - 1);
            Command command = commands.get(cmd);
            command.execute(pathfinder, arguments);
        } else {
            throw new IllegalArgumentException(
                StringUtils.format(
                    "Invalid command '%s'! In general, you should check to " +
                    "see if the command is valid BEFORE attempting to execute " +
                    "it, just to remove the cost of throwing an exception.",
                    cmd
                )
            );
        }

        return this;
    }

    public CommandRegistry execute(String... allArguments) {
        return execute(pathfinder, allArguments);
    }
}
