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

import java.io.File;
import java.util.Scanner;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * @author Colin Robertson
 * @since 2.0.0
 */
public class Script {
    private final CommandRegistry registry;
    private final String script;

    public Script(CommandRegistry registry, String script) {
        ValidationUtils.validate(registry, "registry");
        ValidationUtils.validate(script, "script");

        this.registry = registry;
        this.script = script;
    }

    public static Script load(CommandRegistry registry, String path) {
        try {
            File file = new File(
                ClassLoader.getSystemClassLoader().getResource(path).getFile()
            );
            Scanner scanner = new Scanner(file);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                while (nextLine.contains(" \\")) {
                    nextLine = nextLine.replace(" \\", "\\");
                }
                builder.append(nextLine);
                builder.append('\n');
            }
            String scriptText = builder.toString();
            Logger.debug(
                Script.class,
                "Loaded script <%s> with text <%s>",
                path,
                scriptText
            );
            Script script = new Script(registry, scriptText);
            scanner.close();
            return script;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void execute() {
        String[] lines = script.split("\n");
        registry.parse(lines);
    }
}
