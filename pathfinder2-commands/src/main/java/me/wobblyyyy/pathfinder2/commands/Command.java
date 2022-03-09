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

import java.util.Arrays;
import java.util.function.BiConsumer;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A {@code Command} should recieve a set of {@link String} arguments and an
 * instance of {@link Pathfinder} and perform a certain action based on the
 * provided arguments. The {@code Command} system allows you to operate
 * Pathfinder by using strings, allowing you to create what are, in essence,
 * executable scripts. Each command has a minimum and maximum amount of
 * arguments. Commands may have anywhere from 0 to {@link Integer#MAX_VALUE}
 * arguments, although I cannot fathom why you'd ever need to have thousands
 * of parameters.
 *
 * <p>
 * Please note that this is nothing like the {@code Command} system in
 * {@code wpilib}!
 * </p>
 *
 * @author Colin Robertson
 * @since 2.0.0
 * @see CommandRegistry
 */
public class Command {
    private final String command;
    private final BiConsumer<Pathfinder, String[]> executor;
    private final int minimumArguments;
    private final int maximumArguments;

    public Command(String command, BiConsumer<Pathfinder, String[]> executor) {
        this(command, executor, 0, 1);
    }

    public Command(
        String command,
        BiConsumer<Pathfinder, String[]> executor,
        int arguments
    ) {
        this(command, executor, arguments, arguments);
    }

    /**
     * Create a new {@code Command}. If the command is executed with too many
     * or too few arguments, an {@link IllegalArgumentException} will be
     * thrown.
     *
     * @param command          the {@link String} representation of the command.
     * @param executor         a {@code BiConsumer} responsible for handling
     *                         the execution of the command.
     * @param minimumArguments the minimum amount of arguments.
     * @param maximumArguments the maximum amount of arguments.
     * @see #validArgumentCount(int)
     */
    public Command(
        String command,
        BiConsumer<Pathfinder, String[]> executor,
        int minimumArguments,
        int maximumArguments
    ) {
        this.command = command;
        this.executor = executor;
        this.minimumArguments = minimumArguments;
        this.maximumArguments = maximumArguments;
    }

    public void execute(Pathfinder pathfinder, String[] arguments) {
        ValidationUtils.validate(arguments, "arguments");

        int count = arguments.length;

        if (!validArgumentCount(count)) throw new IllegalArgumentException(
            StringUtils.format(
                "Invalid argument count! Expected " +
                "at least %s and at most %s, but got %s! The arguments " +
                "you provided was/were: %s",
                minimumArguments,
                maximumArguments,
                count,
                Arrays.toString(arguments)
            )
        );

        executor.accept(pathfinder, arguments);
    }

    public boolean validArgumentCount(int argumentCount) {
        return (
            argumentCount >= minimumArguments &&
            maximumArguments >= argumentCount
        );
    }

    public String getCommand() {
        return command;
    }

    public BiConsumer<Pathfinder, String[]> getExecutor() {
        return executor;
    }

    public int getMinimumArguments() {
        return minimumArguments;
    }

    public int getMaximumArguments() {
        return maximumArguments;
    }
}
