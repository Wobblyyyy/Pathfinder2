package me.wobblyyyy.pathfinder2.units;

import static me.wobblyyyy.pathfinder2.units.Unit.*;

public class Conversions {
    private Conversions() {

    }

    public static double convert(Unit current,
                                 Unit target,
                                 double value) {
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
