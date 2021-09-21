/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.easy;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import me.wobblyyyy.pathfinder2.robot.Odometry;

import java.util.function.Supplier;

/**
 * Static methods to create {@link Odometry} instances.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class EasyOdometry {
    private EasyOdometry() {

    }

    public static Odometry buildOdometry(Supplier<Double> xPos,
                                         Supplier<Double> yPos,
                                         Supplier<Angle> zPos) {
        return new AbstractOdometry() {
            @Override
            public PointXYZ getRawPosition() {
                double x = xPos.get();
                double y = yPos.get();
                Angle z = zPos.get();

                return new PointXYZ(x, y, z);
            }
        };
    }

    public static Odometry buildOdometry(Supplier<PointXY> pointSupplier,
                                         Supplier<Angle> angleSupplier) {
        return new AbstractOdometry() {
            @Override
            public PointXYZ getRawPosition() {
                return pointSupplier.get().withHeading(angleSupplier.get());
            }
        };
    }
}
