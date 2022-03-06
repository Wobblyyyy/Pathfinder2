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
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * Pathfinder's main logging API. The logging level Pathfinder uses can be
 * configured using {@Link #setLoggingLevel(LogLevel)}. By default, Pathfinder
 * will use the logging level {@link LogLevel#WARN}, which will only output
 * warnings.
 *
 * <p>
 * All of the log outputs included in Pathfinder's source code can be
 * disabled by changing {@link #arePathfinderLogsEnabled}. You can do that
 * using the following methods:
 * <ul>
 *     <li>{@link #setArePathfinderLogsEnabled(boolean)}</li>
 *     <li>{@link #enablePathfinderLogging()}</li>
 *     <li>{@link #disablePathfinderLogging()}</li>
 * </ul>
 * If you're using the default logging level ({@link LogLevel#WARN}), you
 * likely won't need to change this setting, as Pathfinder only emits logs
 * with severity levels of {@code WARN} or above ({@code ERROR} and
 * {@code FATAL}) if something has gone wrong enough that you should probably
 * know about it.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.0.0
 */
public class Logger {
    private static LogLevel loggingLevel = LogLevel.WARN;
    private static Map<Object, String> map = new HashMap<>();
    private static boolean arePathfinderLogsEnabled = true;

    /**
     * Get the current logging level.
     *
     * @return the current logging level.
     */
    public static LogLevel getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * Set Pathfinder's logging level.
     *
     * @param loggingLevel the logging level to use.
     */
    public static void setLoggingLevel(LogLevel loggingLevel) {
        Logger.loggingLevel = loggingLevel;
    }

    public static void addFilter(Object key, String filter, LogFilter mode) {
        map.put(key, filter);
        InternalPathfinderLogger.filters.put(filter, mode);
    }

    public static void removeFilter(Object key) {
        InternalPathfinderLogger.filters.remove(map.get(key));
        map.remove(key);
    }

    /**
     * Set whether Pathfinder's built-in logs should be outputted.
     *
     * @param arePathfinderLogsEnabled true if Pathfinder's default logs should
     *                                 be outputted. False if Pathfinder's
     *                                 default logs should not be outputted.
     */
    public static void setArePathfinderLogsEnabled(
        boolean arePathfinderLogsEnabled
    ) {
        Logger.arePathfinderLogsEnabled = arePathfinderLogsEnabled;
    }

    /**
     * Enable Pathfinder's built-in logs.
     */
    public static void enablePathfinderLogging() {
        setArePathfinderLogsEnabled(true);
    }

    /**
     * Disable Pathfinder's built-in logs.
     */
    public static void disablePathfinderLogging() {
        setArePathfinderLogsEnabled(false);
    }

    private static void internalLog(
        LogLevel level,
        String tag,
        String message
    ) {
        // don't log if the logging level is too low
        if (!loggingLevel.shouldLog(level)) return;

        // (hopefully) over-allocate the StringBuilder used inside of
        // sizedFormat so that there's no need to re-allocate the builder's
        // buffer during formatting
        InternalPathfinderLogger.log(
            StringUtils.sizedFormat(
                "[%s] [%s]: %s%n",
                10 + tag.length() + (message.length() * 2),
                level,
                tag,
                message
            )
        );
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param level            the log's level. Pathfinder's logger can be
     *                         configured to use one of six log levels:
     *                         {@link LogLevel#FATAL}, {@link LogLevel#ERROR},
     *                         {@link LogLevel#WARN}, {@link LogLevel#INFO},
     *                         {@link LogLevel#DEBUG}, or
     *                         {@link LogLevel#TRACE}. Using a higher log level
     *                         will output less messages to the log. It's
     *                         encouraged you use the lowest log level possible
     *                         while still providing enough debugging info.
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void log(
        LogLevel level,
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        if (InternalPathfinderLogger.output == null) return;

        if (formatSpecifiers.length == 0) {
            internalLog(level, tag, messageFormat);
        } else {
            internalLog(
                level,
                tag,
                StringUtils.format(messageFormat, formatSpecifiers)
            );
        }
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void log(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.DEBUG, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void fatal(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.FATAL, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void error(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.ERROR, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void warn(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.WARN, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void info(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.INFO, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void debug(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.DEBUG, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void trace(
        String tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.TRACE, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void log(
        LogLevel level,
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        // if pathfinder's logs are disabled, check to see if the source
        // of the log is within pathfinder
        if (!arePathfinderLogsEnabled) {
            if (InternalPathfinderLogger.output == null) return;

            String className = tag.getName();

            if (className.contains("me.wobblyyyy.pathfinder2")) return;
        }

        log(level, tag.getSimpleName(), messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void log(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.DEBUG, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void fatal(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.FATAL, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void error(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.ERROR, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void warn(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.WARN, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void info(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.INFO, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void debug(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.DEBUG, tag, messageFormat, formatSpecifiers);
    }

    /**
     * Log a message to Pathfinder's logger system. If the logger is disabled
     * (if the logger as set by
     * {@link InternalPathfinderLogger#setOutput(java.util.function.Consumer)}
     * is null), this method will do nothing. If the logger is enabled, this
     * will create a new formatted log message and output it to the logger.
     *
     * @param tag              the tag to use for the log message. This tag
     *                         can be anything. Generally, this tag is the
     *                         name of the class that logged the message.
     * @param messageFormat    the format of the message. If no format
     *                         specifiers are provided, this parameter will
     *                         be outputted as the log's message without being
     *                         formatted. If format specifiers are provided,
     *                         this message will be formatted with
     *                         {@link StringUtils#format(String, Object...)}
     *                         before being logged.
     * @param formatSpecifiers a varargs parameter of format specifiers that
     *                         will be provided to the {@code format} method
     *                         from {@link StringUtils}. If this has a length
     *                         of 0, nothing will be formatted.
     */
    public static void trace(
        Class<?> tag,
        String messageFormat,
        Object... formatSpecifiers
    ) {
        log(LogLevel.TRACE, tag, messageFormat, formatSpecifiers);
    }
}
