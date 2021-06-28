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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;

public class RelativeMeccanumKinematics implements ForwardsKinematics<MeccanumState> {
    private static final Angle[] WHEEL_ANGLES = new Angle[]{
            Angle.DEG_45,                  // FRONT LEFT
            Angle.DEG_315,                 // FRONT RIGHT
            Angle.DEG_315,                 // BACK LEFT
            Angle.DEG_45                   // BACK RIGHT
    };

    private final double minMagnitude;

    private final double maxMagnitude;

    private final Angle angleOffset;

    public RelativeMeccanumKinematics(double minMagnitude,
                                      double maxMagnitude,
                                      Angle angleOffset) {
        this.minMagnitude = minMagnitude;
        this.maxMagnitude = maxMagnitude;
        this.angleOffset = angleOffset;
    }

    private static double transformCos(Angle movement,
                                       Angle wheel) {
        return movement.cos() * wheel.cos() * 1.0;
    }

    private static double transformSin(Angle movement,
                                       Angle wheel) {
        return movement.sin() * wheel.sin() * 1.0;
    }

    private static double calculatePower(Angle angleMovement,
                                         Angle angleWheel) {
        double transformX = transformSin(angleMovement, angleWheel);
        double transformY = transformCos(angleMovement, angleWheel);

        return transformX + transformY;
    }

    @Override
    public MeccanumState calculate(Translation translation) {
        double turn = translation.vz();
        Angle angle = Angle.atan2(
                translation.vy(),
                translation.vx()
        ).add(angleOffset);
        double magnitude = Math.max(Math.min(
                Math.hypot(translation.vx(), translation.vy()),
                maxMagnitude
        ), minMagnitude);

        double fl = calculatePower(angle, WHEEL_ANGLES[0]) + turn;
        double fr = calculatePower(angle, WHEEL_ANGLES[1]) - turn;
        double bl = calculatePower(angle, WHEEL_ANGLES[2]) + turn;
        double br = calculatePower(angle, WHEEL_ANGLES[3]) - turn;

        return new MeccanumState(fl, fr, bl, br)
                .normalizeFromMaxUnderOne()
                .multiply(magnitude);
    }
}
