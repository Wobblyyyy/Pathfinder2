/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;
import me.wobblyyyy.pathfinder2.wpilib.WPIAdapter;

public class TestCompareChassisSpeedsWithTranslation {
    private static final double[] MULTIPLIERS = new double[64];

    static {
        int split = MULTIPLIERS.length / 2;

        for (int i = 0; i < split; i++)
            MULTIPLIERS[i] = i / 10d;

        for (int i = split; i < MULTIPLIERS.length; i++)
            MULTIPLIERS[i] = MULTIPLIERS[i - split] * -1;
    }

    private static void compare(ChassisSpeeds speeds,
                                Translation translation) {
        ChassisSpeeds fromTranslation =
            WPIAdapter.speedsFromTranslation(translation);

        Assertions.assertEquals(
                translation,
                WPIAdapter.translationFromSpeeds(speeds)
        );

        AssertionUtils.assertSoftEquals(speeds.vxMetersPerSecond,
                fromTranslation.vxMetersPerSecond, 0.01);
        AssertionUtils.assertSoftEquals(speeds.vyMetersPerSecond,
                fromTranslation.vyMetersPerSecond, 0.01);
        AssertionUtils.assertSoftEquals(speeds.omegaRadiansPerSecond,
                fromTranslation.omegaRadiansPerSecond, 0.01);
    }

    private static void compare(ChassisSpeeds speeds,
                                Translation translation,
                                Angle angle) {
        compare(
                speeds,
                translation
        );

        compare(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                    speeds.vxMetersPerSecond,
                    speeds.vyMetersPerSecond,
                    speeds.omegaRadiansPerSecond,
                    WPIAdapter.rotationFromAngle(angle)
                ),
                translation.toRelative(angle)
        );
    }

    private static void compare(double vx,
                                double vy,
                                double vz,
                                Angle angle) {
        ValidationUtils.validate(vx, "vx");
        ValidationUtils.validate(vy, "vy");
        ValidationUtils.validate(vz, "vz");
        ValidationUtils.validate(angle, "angle");

        ChassisSpeeds speeds = new ChassisSpeeds(vx, vy, vz);
        Translation translation = new Translation(vx, vy, vz);

        compare(speeds, translation, angle);
    }

    private static void compareMultiplies(double vx,
                                          double vy,
                                          double vz,
                                          Angle angle) {
        for (double multiplier : MULTIPLIERS)
            compare(
                    vx * multiplier,
                    vy * multiplier,
                    vz * multiplier,
                    angle
            );
    }

    private static void testAbsoluteToRelativeForAngle(Angle angle) {
        compareMultiplies(1, 0, 0, angle);
        compareMultiplies(0, 1, 0, angle);
        compareMultiplies(0, 0, 1, angle);
        compareMultiplies(1, 1, 0, angle);
        compareMultiplies(1, 0, 1, angle);
        compareMultiplies(0, 1, 1, angle);
    }

    @Test
    public void testAbsoluteToRelativeForAngle() {
        testAbsoluteToRelativeForAngle(Angle.DEG_0);

        testAbsoluteToRelativeForAngle(Angle.DEG_45);
        testAbsoluteToRelativeForAngle(Angle.DEG_90);

        testAbsoluteToRelativeForAngle(Angle.DEG_135);
        testAbsoluteToRelativeForAngle(Angle.DEG_180);

        testAbsoluteToRelativeForAngle(Angle.DEG_225);
        testAbsoluteToRelativeForAngle(Angle.DEG_270);

        testAbsoluteToRelativeForAngle(Angle.DEG_315);
        testAbsoluteToRelativeForAngle(Angle.DEG_360);
    }
}
