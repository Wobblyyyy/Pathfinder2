package me.wobblyyyy.pathfinder2.utils;

public class StringUtils {
    private static void appendNext(StringBuilder builder,
            char type,
            Object nextSource) {
        switch (type) {
            case 's':
                builder.append(nextSource.toString());
                return;
            case 'i':
                builder.append(Integer.parseInt(nextSource.toString()));
                return;
            case 'd':
                builder.append(Double.parseDouble(nextSource.toString()));
                return;
        }
    }

    /**
     * Faster alternative to {@link String#format(String, Object...)} that
     * utilizes a {@link StringBuilder} to quickly format a string.
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
    public static String format(String template,
            int bufferSize,
            Object... sources) {
        StringBuilder buffer = new StringBuilder(bufferSize);

        int sourceIndex = 0;

        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);

            if (c == '%') {
                char type = template.charAt(++i);

                if (sourceIndex > sources.length - 1)
                    throw new IndexOutOfBoundsException(
                            "ran out of sources while formatting string: " +
                            "expected at least 1 additional source " +
                            "parameter but did not find any for " +
                            "type " + type
                            );

                appendNext(buffer, type, sources[sourceIndex++]);
            } else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * Faster alternative to {@link String#format(String, Object...)} that
     * utilizes a {@link StringBuilder} to quickly format a string.
     *
     * @param template   the template string to use. Any character preceded
     *                   by a {@code %} (percent) sign will be used to determine
     *                   a formatting type.
     * @param sources    an array of sources to use for {@code %}. If this
     *                   array is shorter than the amount of {@code %} in the
     *                   {@code template} string, an exception will be thrown.
     * @return a freshly formatted string.
     * @see #format(String, int, Object...)
     */
    public static String format(String template,
            Object... sources) {
        return format(template, template.length() * sources.length, sources);
    }
}

