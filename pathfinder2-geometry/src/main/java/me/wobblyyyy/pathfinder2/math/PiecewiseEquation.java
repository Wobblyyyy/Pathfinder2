/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

import java.util.Map;

import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * An implementation of a piecewise equation, allowing you to use multiple
 * {@code Equation}s.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public class PiecewiseEquation implements Equation {
    private static final double[] TEST_VALUES = new double[] {
        -1000000,
         -100000,
          -10000,
           -1000,
            -100,
             -10,
              -1,
               0,
               1,
              10,
             100,
            1000,
           10000,
          100000,
         1000000
    };

    private final Map<Range, Equation> equations;

    public PiecewiseEquation(Map<Range, Equation> equations) {
        this.equations = equations;

        for (double d : TEST_VALUES)
            validateX(d);
    }

    private void validateX(double x) {
        for (Map.Entry<Range, Equation> entry : equations.entrySet())
            if (entry.getKey().includes(x))
                return;

        throw new IllegalStateException(StringUtils.format(
                "Could not compute a Y value for X value <%s> because " +
                        "none of the Range elements in the Map<Range, " +
                        "Equation> include that X value! Make sure this " +
                        "map has a range of negative infinity to positive " +
                        "infinity.",
                x
        ));
    }

    @Override
    public double getY(double x) {
        for (Map.Entry<Range, Equation> entry : equations.entrySet()) {
            Range range = entry.getKey();

            if (range.includes(x)) {
                Equation equation = entry.getValue();
                return equation.getY(x);
            }
        }

        throw new IllegalStateException(StringUtils.format(
                "Could not compute a Y value for X value <%s> because " +
                        "none of the Range elements in the Map<Range, " +
                        "Equation> include that X value!",
                x
        ));
    }
}
