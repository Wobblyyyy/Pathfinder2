/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * Internal utility class for Pathfinder's logging system.
 *
 * @author Colin Robertson
 * @since 2.0.0
 */
public class InternalPathfinderLogger {
    protected static Consumer<String> output = null;
    protected static boolean shouldFilter = true;
    protected static Map<String, LogFilter> filters = new HashMap<>();

    public static void setOutput(Consumer<String> output) {
        InternalPathfinderLogger.output = output;
    }

    public static Consumer<String> getOutput() {
        return output;
    }

    public static void log(String message) {
        // don't do anything if output is null for performance reasons
        if (output == null) return;

        if (shouldFilter) {
            for (Map.Entry<String, LogFilter> entry : filters.entrySet()) {
                String string = entry.getKey();
                LogFilter mode = entry.getValue();

                switch (mode) {
                    case INCLUDES:
                        if (StringUtils.includes(message, string)) return;
                        break;
                    case EXCLUDES:
                        if (StringUtils.excludes(message, string)) return;
                        break;
                    case INCLUDES_IGNORE_CASE:
                        if (
                            StringUtils.includesIgnoreCase(message, string)
                        ) return;
                        break;
                    case EXCLUDES_IGNORE_CASE:
                        if (
                            StringUtils.excludesIgnoreCase(message, string)
                        ) return;
                        break;
                }
            }
        }

        output.accept(message);
    }
}
