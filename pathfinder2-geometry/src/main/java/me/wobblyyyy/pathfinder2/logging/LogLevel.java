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

/**
 * Different logging severity levels. I'd encourage you to use the highest
 * logging level you can while still providing useful information. The
 * default logging level is {@link WARN}.
 *
 * @author Colin Robertson
 * @since 2.0.0
 */
public enum LogLevel {
    /**
     * Silence all logs. Nothing (including {@link FATAL}) will be logged.
     */
    NONE(6),

    /**
     * Used when there's a fatal {@link Exception} or {@link Error} during
     * program execution.
     */
    FATAL(5),

    /**
     * Used when there's a serious or severe error or exception during the
     * program's execution. These exceptions or errors are not fatal.
     */
    ERROR(4),

    /**
     * Used for warnings. This is the default logging level, and I'd suggest
     * you leave it at this unless you have a reason to change it. Pathfinder
     * uses logging levels above this level rarely, so your logs shouldn't
     * get too cluttered.
     */
    WARN(3),

    /**
     * Used for general-purpose logging information. Examples include logging
     * for every plugin that's loaded, logging for every instance of
     * {@code Pathfinder} that's created, etc.
     */
    INFO(2),

    /**
     * Used for debugging purposes. Using this log level will output a lot of
     * logs. Such, I'd encourage you to only use it when needed.
     */
    DEBUG(1),

    /**
     * The lowest log level, used for logging just about every event that
     * happens during program execution. This will absolutely flood your
     * output, so I'd encourage you to only use it when you've exhausted
     * all other options. Combing through this... well, let's just say it's
     * not going to be fun.
     */
    TRACE(0);

    int value;

    LogLevel(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public boolean shouldLog(LogLevel level) {
        return level.value >= value;
    }
}
