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

/**
 * Different types of units.
 *
 * <p>
 * Please note that units are in just about no way relevant to Pathfinder's
 * operation. They are included in the event you need to make some conversions.
 * However, this is NOT integral to using the library.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public enum Unit {
    /**
     * Inches.
     */
    INCH(1),

    /**
     * Centimeters.
     */
    CM(1 / 2.54),

    /**
     * Meters.
     */
    M(39.37),

    /**
     * Kilometers.
     */
    KM(39370),

    /**
     * Millimeters.
     */
    MM(1 / 25.4),

    /**
     * Miles.
     */
    MILE(63360),

    /**
     * Yards.
     */
    YARD(36),

    /**
     * Feet.
     */
    FOOT(12),

    /**
     * Nautical miles. Because obviously, you need to use these. Right?
     */
    NAUTICAL_MILE(72913);

    /**
     * The unit's value in inches.
     */
    private final double valueInInches;

    Unit(double valueInInches) {
        this.valueInInches = valueInInches;
    }

    /**
     * Get the unit's value, in inches.
     *
     * @return the unit's value in inches.
     */
    public double getValueInInches() {
        return valueInInches;
    }

    /**
     * Convert a provided value of whatever unit you're using into inches.
     *
     * @param a the value to convert.
     * @return the value converted into inches.
     */
    public double inches(double a) {
        return valueInInches * a;
    }

    /**
     * Honestly, I don't care enough to write useful documentation here. My
     * head hurts pretty badly right now, so you're just about out of luck.
     * I don't know. Figure it out yourself, nerd.
     *
     * @param a a parameter that does mysterious things.
     * @return who knows? certainly not me.
     */
    public double reciprocal(double a) {
        return valueInInches * (1 / a);
    }
}
