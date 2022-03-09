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
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

public class ScriptingCommands {
    public static final String DEF_PREFIX = "var_";

    private ScriptingCommands() {}

    public static Command DEF_COMMAND = new Command(
        "def",
        (pathfinder, args) -> {
            String name = args[0];
            String[] valueArray = new String[args.length - 1];
            System.arraycopy(args, 1, valueArray, 0, valueArray.length);
            String value = StringUtils.concat(valueArray);
            pathfinder.putData(DEF_PREFIX + name, value);
            Logger.debug(
                ScriptingCommands.class,
                "Defined variable <%s> with value <%s> (args: <%s>)",
                DEF_PREFIX + name,
                value,
                Arrays.toString(args)
            );
        },
        2,
        Integer.MAX_VALUE
    );

    public static void addScriptingCommands(CommandRegistry registry) {
        registry.unsafeAdd(DEF_COMMAND);
    }
}
