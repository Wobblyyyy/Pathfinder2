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

/**
 * String-related utilities.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class StringUtils {
    private StringUtils() {

    }

    private static void appendNext(StringBuilder builder,
                                   char type,
                                   int maxLength,
                                   Object nextSource) {
        int previousLength = builder.length();

        if (nextSource == null) {
            builder.append("null");
            return;
        }

        switch (type) {
            case 's':
                builder.append(nextSource.toString());
                break;
            case 'i':
                builder.append(Integer.parseInt(nextSource.toString()));
                break;
            case 'd':
                builder.append(Double.parseDouble(nextSource.toString()));
                break;
            case 'f':
                builder.append(Float.parseFloat(nextSource.toString()));
                break;
            default:
                throw new IllegalArgumentException(format("Tried to use " +
                        "format type <%s> but did not find it, valid types " +
                        "are (percent sign)s, (percent sign)i, (percent " +
                        "sign)d, and (percent sign)f.", type));
        }

        if (maxLength > 0)
            if (builder.length() - previousLength > maxLength)
                builder.setLength(previousLength + maxLength);
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
    public static String sizedFormat(String template,
                                     int bufferSize,
                                     Object... sources) {
        if (bufferSize < 0)
            throw new IllegalArgumentException("Buffer size must " +
                    "be a positive integer (or zero) but it was " + bufferSize);

        ValidationUtils.validate(template, "template");

        for (Object source : sources)
            ValidationUtils.validate(source);

        StringBuilder buffer = new StringBuilder(bufferSize);

        if (sources.length == 0)
            throw new IllegalArgumentException(
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

                if (sourceIndex > sources.length - 1)
                    throw new IndexOutOfBoundsException(
                            "ran out of sources while formatting string: " +
                                    "expected at least 1 additional source " +
                                    "parameter but did not find any for " +
                                    "type " + type
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
    public static String format(String template,
                                Object... sources) {
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
    public static String formatArray(String separator,
                                     Object[] objects) {
        int length = objects.length;
        if (length == 0) return "";
        else if (length == 1) return objects[0].toString();

        String firstString = objects[0].toString();
        StringBuilder builder = new StringBuilder(firstString.length() * length);

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
    public static String formatArray(String separator,
                                     String startString,
                                     String endString,
                                     Object... objects) {
        return startString + formatArray(separator, objects) + endString;
    }
}

