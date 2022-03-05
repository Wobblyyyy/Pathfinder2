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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * Utilities for serializing and deserializing different types from Pathfinder
 * into and from {@link String}s.
 *
 * @author Colin Robertson
 * @since 0.10.7
 */
public class StringSerializer {

    private StringSerializer() {}

    private static double[] deserialize(String string, int expectedValues) {
        String[] split = string.split(",");
        double[] values = new double[split.length];

        for (int i = 0; i < split.length; i++) values[i] =
            Double.parseDouble(split[i]);

        if (values.length != expectedValues) throw new IllegalArgumentException(
            String.format(
                "Tried to deserialize string, expected %s " +
                "values but found %s",
                expectedValues,
                values.length
            )
        );

        return values;
    }

    private static String fastFormat(double... values) {
        if (values.length == 0) throw new IllegalArgumentException(
            "Cannot format a string with 0 values!"
        );

        StringBuilder builder = new StringBuilder(50);

        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);

            if (i != values.length - 1) builder.append(',');
        }

        return builder.toString();
    }

    public static String serializePointXY(PointXY point) {
        return fastFormat(point.x(), point.y());
    }

    public static PointXY deserializePointXY(String string) {
        double[] values = deserialize(string, 2);

        return new PointXY(values[0], values[1]);
    }

    public static String serializePointXYZ(PointXYZ point) {
        return fastFormat(point.x(), point.y(), point.z().deg());
    }

    public static PointXYZ deserializePointXYZ(String string) {
        double[] values = deserialize(string, 3);

        return new PointXYZ(values[0], values[1], values[2]);
    }

    public static String serializeAngle(Angle angle) {
        return fastFormat(angle.deg());
    }

    public static Angle deserializeAngle(String string) {
        double[] values = deserialize(string, 1);

        return Angle.fixedDeg(values[0]);
    }

    public static String serializeTranslation(Translation t) {
        return fastFormat(t.vx(), t.vy(), t.vz());
    }

    public static Translation deserializeTranslation(String string) {
        double[] values = deserialize(string, 3);

        return new Translation(values[0], values[1], values[2]);
    }
}
