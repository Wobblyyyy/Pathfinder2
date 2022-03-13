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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        TimeCommands.addTimeCommands(registry);
        TrajectoryCommands.addTrajectoryCommands(registry);
        TestCommands.addTestCommands(registry);
    }

    public void unsafeAdd(Command command) {
        commands.put(command.getCommand(), command);
    }

    /**
     * Add a command to the registry.
     *
     * @param command the command to add to the registry.
     */
    public void add(Command command) {
        String commandText = command.getCommand();

        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            if (entry.getKey().equals(commandText)) {
                throw new IllegalArgumentException(
                    StringUtils.format(
                        "Attempted to add a command with text '%s' but failed " +
                        "because a command with that text already exists!",
                        commandText
                    )
                );
            }
        }

        unsafeAdd(command);
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

    /**
     * Execute several {@link String}s, each of which is a "line," which is
     * a command and several arguments. The {@code lines} parameter may have
     * incomplete lines, so long as they are suffixed with a "\", indicating
     * that the line is incomplete (this functions very much like the backslash
     * character in Bash scripts).
     *
     * <p>
     * I'm going to be completely honest here - I'm really lazy and don't
     * currently have the energy to document this correctly, so if you want
     * to know more about what this method does, check out its implementation
     * in {@link Script}.
     * </p>
     *
     * @param lines the lines to parse.
     * @return {@code this}, used for method chaining.
     */
    public CommandRegistry parse(String... lines) {
        // this is a pretty bad way of doing this, i know...
        List<String> realLines = new ArrayList<>(lines.length);
        boolean shouldAppend = false;

        Logger.debug(
            CommandRegistry.class,
            "Parsing arguments <%s>",
            Arrays.toString(lines)
        );

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            int indexOfComment = line.indexOf("//");
            if (indexOfComment > -1) {
                line = line.substring(0, indexOfComment);
                Logger.debug(
                    CommandRegistry.class,
                    "trimming line <%s> at index <%s> to remove comment",
                    line,
                    indexOfComment
                );
            }

            boolean shouldAdd = true;

            Logger.debug(CommandRegistry.class, "parsing line <%s>", line);

            if (shouldAppend) {
                int lastIdx = realLines.size() - 1;
                String lastLine = realLines.get(lastIdx);
                String newLine = StringUtils.concat(lastLine, " ", line);
                realLines.set(lastIdx, newLine);
                Logger.debug(
                    CommandRegistry.class,
                    "processing linebreak (lastIdx: <%s> lastLine: <%s> " +
                    "newLine: <%s> realLines: <%s>)",
                    lastIdx,
                    lastLine,
                    newLine,
                    realLines
                );
                shouldAdd = false;
            }

            if (line.endsWith("\\")) {
                shouldAppend = true;
                line = line.substring(0, line.length() - 1);
            } else {
                shouldAppend = false;
            }

            if (shouldAdd) {
                realLines.add(line);
            }
        }

        Logger.debug(
            CommandRegistry.class,
            "Finished parsing. Lines: <%s>",
            realLines
        );

        for (String string : realLines) {
            if (string.length() == 0) continue;
            String[] arguments = string.split(" ");
            Logger.debug(
                CommandRegistry.class,
                "Processing arguments <%s>",
                Arrays.toString(arguments)
            );
            execute(arguments);
        }

        return this;
    }
}
