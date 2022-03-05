/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

/**
 * Utility to generate random strings.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class RandomString {
    public static final char[] LETTERS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final char[] NUMBERS = "123456789".toCharArray();

    public static char randomLetter() {
        return LETTERS[(int) Math.floor(Math.random() * LETTERS.length)];
    }

    public static char randomNumber() {
        return NUMBERS[(int) Math.floor(Math.random() * NUMBERS.length)];
    }

    public static char randomChar() {
        return Math.random() > 0.5 ? randomLetter() : randomNumber();
    }

    /**
     * Generate a random string.
     *
     * <p>
     * This string will consist exclusively of alphanumeric characters.
     * </p>
     *
     * @param length the length of the string to generate.
     * @return a random string of length length.
     */
    public static String randomString(int length) {
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) builder.append(randomChar());

        return builder.toString();
    }
}
