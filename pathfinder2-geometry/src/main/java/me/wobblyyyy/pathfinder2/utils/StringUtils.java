/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import java.util.function.Consumer;
import me.wobblyyyy.pathfinder2.math.Rounding;

/**
 * String-related utilities.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class StringUtils {

    private StringUtils() {}

    private static void appendNext(
        StringBuilder builder,
        char type,
        int maxLength,
        Object nextSource
    ) {
        int previousLength = builder.length();

        if (nextSource == null) {
            builder.append("null");
            return;
        }

        switch (type) {
            case 's':
                builder.append(nextSource);
                break;
            case 'i':
                builder.append(Integer.parseInt(nextSource.toString()));
                break;
            case 'd':
                double number = Double.parseDouble(nextSource.toString());
                double rounded = Rounding.fastRound(number);
                builder.append(formatNumber(rounded));
                break;
            case 'f':
                builder.append(Float.parseFloat(nextSource.toString()));
                break;
            default:
                throw new IllegalArgumentException(
                    format(
                        "Tried to use " +
                        "format type <%s> but did not find it, valid types " +
                        "are (percent sign)s, (percent sign)i, (percent " +
                        "sign)d, and (percent sign)f.",
                        type
                    )
                );
        }

        if (maxLength > 0) if (
            builder.length() - previousLength > maxLength
        ) builder.setLength(previousLength + maxLength);
    }

    /**
     * Format a number so that it has commas in it. This will NOT round the
     * number.
     *
     * @param number the number to format.
     * @return the formatted number.
     */
    public static String formatNumber(double number) {
        String str = String.valueOf(number);
        int decimalIndex = str.indexOf('.');
        int length = str.length();
        boolean invalidDecimalIdx = decimalIndex < 4;

        if (length < 5 || invalidDecimalIdx) {
            return str;
        }

        if (invalidDecimalIdx) {
            decimalIndex = length;
        }

        StringBuilder builder = new StringBuilder(16);
        int offset = (decimalIndex % 3) - 1;
        int counter = 1;

        for (int i = 0; i < offset; i++) {
            builder.append(str.charAt(i));
        }

        for (int i = offset; i < length; i++) {
            if (i < decimalIndex) {
                if (counter-- == 0) {
                    builder.append(',');
                    counter = 2;
                }
            }

            if (offset > -1) {
                builder.append(str.charAt(i));
            }
        }

        return builder.toString();
    }

    /**
     * Round a number, then apply {@link #formatNumber(double)}.
     *
     * @param number the number to round and format.
     * @param places how many places to round the number to.
     * @return the rounded and formatted number.
     */
    public static String roundAndFormatNumber(double number, int places) {
        double rounded = Rounding.fastRound(number, places);

        return formatNumber(rounded);
    }

    /**
     * Round a number, then apply {@link #formatNumber(double)}.
     *
     * @param number the number to round and format.
     * @return the rounded and formatted number.
     */
    public static String roundAndFormatNumber(double number) {
        return roundAndFormatNumber(number, 3);
    }

    /**
     * Faster alternative to {@link String#format(String, Object...)} that
     * utilizes a {@link StringBuilder} to quickly format a string.
     *
     * <p>
     * <ul>
     *     <li>%s: any string</li>
     *     <li>%i: parse as integer</li>
     *     <li>%d: parse as double</li>
     *     <li>%s: parse as float</li>
     * </ul>
     * </p>
     *
     * <p>
     * Any {@code percent} identifier can have a number to limit the length
     * of the parameter. Example: {@code "%10s"} will limit the length of
     * the value that gets added to 10 characters maximum.
     * </p>
     *
     * @param template   the template string to use. Any character preceded
     *                   by a {@code %} (percent) sign will be used to determine
     *                   a formatting type.
     * @param bufferSize the size of the buffer. This should be at least the
     *                   size of the template string.
     * @param sources    an array of sources to use for {@code %}. If this
     *                   array is shorter than the amount of {@code %} in the
     *                   {@code template} string, an exception will be thrown.
     * @return a freshly formatted string.
     */
    public static String sizedFormat(
        String template,
        int bufferSize,
        Object... sources
    ) {
        if (sources.length == 0) return template;

        if (bufferSize < 0) throw new IllegalArgumentException(
            "Buffer size must " +
            "be a positive integer (or zero) but it was " +
            bufferSize
        );

        ValidationUtils.validate(template, "template");

        StringBuilder buffer = new StringBuilder(bufferSize);

        if (sources.length == 0) throw new IllegalArgumentException(
            "tried to format a string without providing any " +
            "source parameters - make sure you have " +
            "at least one source parameter"
        );

        int length = template.length();
        int sourceIndex = 0;

        for (int i = 0; i < length; i++) {
            char c = template.charAt(i);

            if (c == '%') {
                char type = template.charAt(++i);
                int maxLength = -1;

                if (type == 'n') {
                    buffer.append('\n');
                    continue;
                }

                if (Character.isDigit(type)) {
                    StringBuilder numberBuilder = new StringBuilder(5);
                    numberBuilder.append(type);

                    for (int j = i + 1; j < length; j++) {
                        char character = template.charAt(j);

                        if (Character.isDigit(character)) {
                            numberBuilder.append(character);
                        } else {
                            i = j;
                            break;
                        }
                    }

                    maxLength = Integer.parseInt(numberBuilder.toString());
                    type = template.charAt(i);
                }

                if (
                    sourceIndex > sources.length - 1
                ) throw new IndexOutOfBoundsException(
                    "ran out of sources while formatting string: " +
                    "expected at least 1 additional source " +
                    "parameter but did not find any for " +
                    "type " +
                    type
                );

                appendNext(buffer, type, maxLength, sources[sourceIndex++]);
            } else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * Faster alternative to {@link String#format(String, Object...)} that
     * utilizes a {@link StringBuilder} to quickly format a string. This is
     * up to 6 times faster than {@link String#format(String, Object...)}.
     * By default, this will create a {@link StringBuilder} with an internal
     * character buffer with a size equal to the length of {@code template}
     * times the amount of sources in the {@code sources} parameter. This can
     * frequently result in oversized arrays, which may have a negative impact
     * on memory usage. To resolve this, you can use
     * {@link #sizedFormat(String, int, Object...)}, which allows you to
     * specify the size of the byte buffer to prevent overallocation.
     *
     * <p>
     * <ul>
     *     <li>%s: any string</li>
     *     <li>%i: parse as integer</li>
     *     <li>%d: parse as double</li>
     *     <li>%s: parse as float</li>
     * </ul>
     * </p>
     *
     * <p>
     * Any {@code percent} identifier can have a number to limit the length
     * of the parameter. Example: {@code "%10s"} will limit the length of
     * the value that gets added to 10 characters maximum.
     * </p>
     *
     * @param template the template string to use. Any character preceded
     *                 by a {@code %} (percent) sign will be used to determine
     *                 a formatting type.
     * @param sources  an array of sources to use for {@code %}. If this
     *                 array is shorter than the amount of {@code %} in the
     *                 {@code template} string, an exception will be thrown.
     * @return a freshly formatted string.
     */
    public static String format(String template, Object... sources) {
        return sizedFormat(
            template,
            template.length() * sources.length,
            sources
        );
    }

    /**
     * Quickly format an array of objects.
     *
     * @param separator a string to separate objects.
     * @param objects   all of the objects that will be formatted.
     * @return a formatted array of objects.
     */
    public static String formatArray(String separator, Object[] objects) {
        int length = objects.length;
        if (length == 0) return ""; else if (
            length == 1
        ) return objects[0].toString();

        String firstString = objects[0].toString();
        StringBuilder builder = new StringBuilder(
            firstString.length() * length
        );

        for (int i = 1; i < objects.length; i++) {
            builder.append(objects[i]);
            builder.append('\n');
        }

        builder.setLength(builder.length() - separator.length());

        return builder.toString();
    }

    /**
     * Quickly format an array of objects.
     *
     * @param separator a string to separate objects.
     * @param objects   all of the objects that will be formatted.
     * @return a formatted array of objects.
     */
    public static String formatArray(
        String separator,
        String startString,
        String endString,
        Object... objects
    ) {
        return startString + formatArray(separator, objects) + endString;
    }

    /**
     * Format and print a string.
     *
     * @param consumer the consumer that will accept the formatted string.
     * @param template the template to use for the string.
     * @param sources  sources to use for formatting the string.
     */
    public static void printf(
        Consumer<String> consumer,
        String template,
        Object... sources
    ) {
        consumer.accept(format(template, sources));
    }

    /**
     * Format and print a string.
     *
     * @param template the template to use for the string.
     * @param sources  sources to use for formatting the string.
     */
    public static void printf(String template, Object... sources) {
        printf(System.out::println, template, sources);
    }

    public static boolean includes(String base, String search) {
        return base.contains(search);
    }

    public static boolean includesIgnoreCase(String base, String search) {
        return includes(base.toLowerCase(), search.toLowerCase());
    }

    public static boolean excludes(String base, String search) {
        return !includes(base, search);
    }

    public static boolean excludesIgnoreCase(String base, String search) {
        return !includesIgnoreCase(base, search);
    }

    /**
     * Count how many instances of {@code search} are contained in the
     * provided {@code string} parameter.
     *
     * @param string the string to search.
     * @param search the characters to search for.
     * @return the amount of instances of {@code search} in {@code string}.
     */
    public static int count(String string, String search) {
        int count = 0;

        while (string.contains(search)) {
            string = string.replaceFirst(search, "");
            count++;
        }

        return count;
    }

    /**
     * Count how many instances of {@code search} are contained in the
     * provided {@code string} parameter.
     *
     * @param string the string to search.
     * @param search the characters to search for.
     * @return the amount of instances of {@code search} in {@code string}.
     */
    public static int count(String string, char search) {
        return count(string, String.valueOf(search));
    }

    /**
     * Count how many instances of {@code search} are contained in the
     * provided {@code string} parameter.
     *
     * @param string the string to search.
     * @param search the characters to search for.
     * @return the amount of instances of {@code search} in {@code string}.
     */
    public static int count(String string, int search) {
        return count(string, String.valueOf(search));
    }

    /**
     * Count how many instances of {@code search} are contained in the
     * provided {@code string} parameter.
     *
     * @param string the string to search.
     * @param search the characters to search for.
     * @return the amount of instances of {@code search} in {@code string}.
     */
    public static int count(String string, float search) {
        return count(string, String.valueOf(search));
    }

    /**
     * Count how many instances of {@code search} are contained in the
     * provided {@code string} parameter.
     *
     * @param string the string to search.
     * @param search the characters to search for.
     * @return the amount of instances of {@code search} in {@code string}.
     */
    public static int count(String string, double search) {
        return count(string, String.valueOf(search));
    }

    /**
     * Count how many instances of {@code search} are contained in the
     * provided {@code string} parameter.
     *
     * @param string the string to search.
     * @param search the characters to search for.
     * @return the amount of instances of {@code search} in {@code string}.
     */
    public static int count(String string, byte search) {
        return count(string, String.valueOf(search));
    }

    public static String concat(String... strings) {
        int length = 0;
        for (String string : strings) length += string.length();

        StringBuilder builder = new StringBuilder(length);
        for (String string : strings) builder.append(string);

        return builder.toString();
    }

    public static String wrap(String prefix, String suffix, String string) {
        return concat(prefix, suffix, string);
    }

    public static String wrap(char prefix, char suffix, Object obj) {
        String string = obj.toString();

        String builder = prefix + string + suffix;

        return builder;
    }

    public static String wrapWithBraces(Object obj) {
        return wrap('(', ')', obj);
    }

    public static String wrapWithSquareBraces(Object obj) {
        return wrap('[', ']', obj);
    }

    public static String wrapWithCurlyBraces(Object obj) {
        return wrap('{', '}', obj);
    }

    public static String wrapWithDiamondBraces(Object obj) {
        return wrap('<', '>', obj);
    }

    public static String wrap(Object obj) {
        return wrapWithDiamondBraces(obj);
    }
}
