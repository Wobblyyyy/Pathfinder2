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
import java.util.Set;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A {@code CommandRegistry} is responsible for parsing {@link String}
 * expressions into {@link Command}s. Each {@code CommandRegistry} has a
 * {@link Map} containing {@link Command}s. It's suggested you use
 * {@link #createDefaultRegistry(Pathfinder)} to create a new instance of
 * {@code CommandRegistry}, as this will automatically load the default
 * commands that are bundled with Pathfinder.
 *
 * @author Colin Robertson
 * @since 2.0.0
 * @see #createDefaultRegistry(Pathfinder)
 */
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
        ScriptingCommands.addScriptingCommands(registry);
    }

    /**
     * Add a command to the registry.
     *
     * @param command the command to add to the registry.
     */
    public void add(Command command) {
        commands.put(command.getCommand(), command);
    }

    public boolean isCommand(String command) {
        return commands.containsKey(command);
    }

    /**
     * Execute (or at least try to execute) a command.
     *
     * @param pathfinder   the instance of Pathfinder the command registry
     *                     is responsible for controlling.
     * @param allArguments all of the arguments to pass to the command.
     *                     The first argument should be the actual command,
     *                     and any other arguments should be arguments to
     *                     pass to the command.
     * @return {@code this}, used for method chaining.
     */
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
            Set<Map.Entry<String, Object>> entries = pathfinder
                .getDataMap()
                .entrySet();
            for (int i = 0; i < arguments.length; i++) {
                Logger.trace(
                    CommandRegistry.class,
                    "Checking for variables in <%s> (idx %s)",
                    arguments[i],
                    i
                );
                for (Map.Entry<String, Object> e : entries) {
                    String str = e.getKey();
                    if (str.startsWith(ScriptingCommands.DEF_PREFIX)) {
                        String name = str.replaceFirst(
                            ScriptingCommands.DEF_PREFIX,
                            ""
                        );
                        if (arguments[i].startsWith('$' + name)) {
                            arguments[i] = (String) e.getValue();
                        }
                    }
                }
            }
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

    /**
     * Execute (or at least try to execute) a command.
     *
     * @param allArguments all of the arguments to pass to the command.
     *                     The first argument should be the actual command,
     *                     and any other arguments should be arguments to
     *                     pass to the command.
     * @return {@code this}, used for method chaining.
     */
    public CommandRegistry execute(String... allArguments) {
        return execute(pathfinder, allArguments);
    }
}
