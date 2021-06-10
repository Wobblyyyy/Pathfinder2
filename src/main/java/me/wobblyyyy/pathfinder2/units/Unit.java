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

public enum Unit {
    INCH(1),
    CM(1 / 2.54),
    M(39.37),
    KM(39370),
    MM(1 / 25.4),
    MILE(63360),
    YARD(36),
    FOOT(12),
    NAUTICAL_MILE(72913);

    private final double valueInInches;

    Unit(double valueInInches) {
        this.valueInInches = valueInInches;
    }

    public double getValueInInches() {
        return valueInInches;
    }

    public double inches(double a) {
        return valueInInches * a;
    }

    public double reciprocal(double a) {
        return valueInInches * (1 / a);
    }
}
