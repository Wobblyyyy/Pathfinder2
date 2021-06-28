/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import java.util.stream.DoubleStream;

public class MeccanumState {
    private final double fl;

    private final double fr;

    private final double bl;

    private final double br;

    public MeccanumState(double fl,
                         double fr,
                         double bl,
                         double br) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
    }

    public double fl() {
        return this.fl;
    }

    public double fr() {
        return this.fr;
    }

    public double bl() {
        return this.bl;
    }

    public double br() {
        return this.br;
    }

    public double maxPower() {
        double[] values = new double[]{
                fl,
                fr,
                bl,
                br
        };

        double[] newValues = new double[4];

        for (int i = 0; i < values.length; i++) {
            newValues[i] = Math.abs(values[i]);
        }

        return DoubleStream.of(newValues).max().getAsDouble();
    }

    public MeccanumState normalize(double max) {
        double realMax = maxPower();

        double _fl = fl;
        double _fr = fr;
        double _bl = bl;
        double _br = br;

        if (realMax > max) {
            _fl = fl / realMax * max;
            _fr = fr / realMax * max;
            _bl = bl / realMax * max;
            _br = br / realMax * max;
        }

        return new MeccanumState(_fl, _fr, _bl, _br);
    }

    public MeccanumState normalizeFromMaxUnderOne() {
        double max = maxPower();

        return normalize(max > 1 ? 1.0 : max);
    }

    public MeccanumState add(MeccanumState state) {
        return new MeccanumState(
                fl + state.fl,
                fr + state.fr,
                bl + state.bl,
                br + state.br
        );
    }

    public MeccanumState multiply(MeccanumState state) {
        return new MeccanumState(
                fl * state.fl,
                fr * state.fr,
                bl * state.bl,
                br * state.br
        );
    }

    public MeccanumState multiply(double multiplier) {
        return multiply(new MeccanumState(
                multiplier,
                multiplier,
                multiplier,
                multiplier
        ));
    }
}
