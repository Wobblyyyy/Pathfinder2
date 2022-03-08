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

import java.util.function.BiConsumer;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

public class Command {
    private final String command;
    private final BiConsumer<Pathfinder, String[]> executor;
    private final int minimumArguments;
    private final int maximumArguments;

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

        int argumentCount = arguments.length;

        boolean tooFewArgs = argumentCount < minimumArguments;
        boolean tooManyArgs = argumentCount > maximumArguments;

        if (tooFewArgs || tooManyArgs) throw new IllegalArgumentException(
            StringUtils.format(
                "Invalid argument count! Expected " +
                "at least %s and at most %s, but got %s!",
                minimumArguments,
                maximumArguments
            )
        );

        executor.accept(pathfinder, arguments);
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
