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
 * An executable bit of code stored in a text form. This can be parsed by
 * using an instance of {@link CommandRegistry}, allowing you to manage
 * Pathfinder's operations without writing any actual Java code. Note that
 * the scripting system provided in Pathfinder is rudimentary at best and
 * you'll never be able to replicate all of the capabilities of the Java
 * implementation of Pathfinder using scripts, but they can be pretty neat.
 *
 * <p>
 * Scripts are basically just a series of {@link Command}s. In fact, that's
 * all they are.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.0.0
 */
public class Script {
    private final CommandRegistry registry;
    private final String[] lines;

    public Script(CommandRegistry registry, String script) {
        ValidationUtils.validate(registry, "registry");
        ValidationUtils.validate(script, "script");

        this.registry = registry;
        this.lines = script.split("\n");
    }

    /**
     * Load a script from a given path. If the script is not located at
     * the given path, or if there's an issue loading the script, an
     * exception will be thrown. This will NOT execute the script - it'll
     * simply load the script.
     *
     * @param registry the command registry to use when executing the script.
     * @param path     the path to the script. If you're using Gradle as a
     *                 build system, this will be relative to the
     *                 {@code resources} directory. For example, if your
     *                 script was located in {@code resources/pf/abc.pf}, this
     *                 parameter would be {@code "pf/abc.pf"}.
     * @return the script that's been loaded.
     */
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

    /**
     * Execute the script using the {@link CommandRegistry} supplied to this
     * {@code Script} in the {@link Script#Script(CommandRegistry, String)}
     * constructor.
     */
    public void execute() {
        registry.parse(lines);
    }
}
