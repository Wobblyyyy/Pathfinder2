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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Get the average of things.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Average {
    public static double of(double... numbers) {
        double sum = 0;

        for (double d : numbers) {
            sum += d;
        }

        return sum / numbers.length;
    }

    public static float of(float... numbers) {
        float sum = 0;

        for (float d : numbers) {
            sum += d;
        }

        return sum / numbers.length;
    }

    public static int of(int... numbers) {
        int sum = 0;

        for (int d : numbers) {
            sum += d;
        }

        return sum / numbers.length;
    }

    public static long of(long... numbers) {
        long sum = 0;

        for (long d : numbers) {
            sum += d;
        }

        return sum / numbers.length;
    }

    public static double ofX(PointXY... points) {
        double x = 0;

        for (PointXY point : points) {
            x += point.x();
        }

        return x;
    }

    public static double ofY(PointXY... points) {
        double y = 0;

        for (PointXY point : points) {
            y += point.y();
        }

        return y;
    }

    public static Angle ofZ(PointXYZ... points) {
        Angle z = Angle.fromDeg(0);

        for (PointXYZ point : points) {
            z = z.add(point.z());
        }

        return z.fix();
    }

    public static PointXY of(PointXY... points) {
        double x = 0;
        double y = 0;

        for (PointXY point : points) {
            x += point.x();
            y += point.y();
        }

        return new PointXY(x, y);
    }

    public static PointXYZ of(PointXYZ... points) {
        double x = 0;
        double y = 0;
        Angle z = Angle.fromDeg(0);

        for (PointXYZ point : points) {
            x += point.x();
            y += point.y();
            z = z.add(point.z());
        }

        return new PointXYZ(x, y, z);
    }
}
