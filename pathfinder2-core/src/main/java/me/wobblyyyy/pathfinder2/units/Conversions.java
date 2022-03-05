/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.units;

import static me.wobblyyyy.pathfinder2.units.Unit.*;

/**
 * Unit conversions based on the {@link Unit} enum.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Conversions {

    private Conversions() {}

    /**
     * Convert a measurement from {@code current} unit into {@code target}
     * unit. Pretty darn cool, right?
     *
     * @param current the base unit.
     * @param target  the target unit.
     * @param value   the value to convert.
     * @return the converted value.
     */
    public static double convert(Unit current, Unit target, double value) {
        return target.reciprocal(current.inches(value));
    }

    public static double inchesToCm(double value) {
        return convert(INCH, CM, value);
    }

    public static double cmToInches(double value) {
        return convert(CM, INCH, value);
    }

    public static double inchesToM(double value) {
        return convert(INCH, M, value);
    }

    public static double mToInches(double value) {
        return convert(M, INCH, value);
    }

    public static double inchesToKm(double value) {
        return convert(INCH, KM, value);
    }

    public static double kmToInches(double value) {
        return convert(KM, INCH, value);
    }

    public static double inchesToMm(double value) {
        return convert(INCH, MM, value);
    }

    public static double mmToInches(double value) {
        return convert(MM, INCH, value);
    }

    public static double inchesToMiles(double value) {
        return convert(INCH, MILE, value);
    }

    public static double milesToInches(double value) {
        return convert(MILE, INCH, value);
    }

    public static double inchesToYards(double value) {
        return convert(INCH, YARD, value);
    }

    public static double yardsToInches(double value) {
        return convert(YARD, INCH, value);
    }

    public static double inchesToFeet(double value) {
        return convert(INCH, FOOT, value);
    }

    public static double feetToInches(double value) {
        return convert(FOOT, INCH, value);
    }

    public static double inchesToNauticalMiles(double value) {
        return convert(INCH, NAUTICAL_MILE, value);
    }

    public static double nauticalMilesToInches(double value) {
        return convert(NAUTICAL_MILE, INCH, value);
    }
}
