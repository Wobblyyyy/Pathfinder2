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

public enum LogLevel {
    FATAL(5),
    ERROR(4),
    WARN(3),
    INFO(2),
    DEBUG(1),
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
