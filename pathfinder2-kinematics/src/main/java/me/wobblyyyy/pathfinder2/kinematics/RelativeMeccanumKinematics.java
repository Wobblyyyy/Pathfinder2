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

/**
 * The most simple implementation of meccanum kinematics. For most use
 * cases, this should suffice. If you need more advanced meccanum kinematics,
 * this class isn't for you.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class RelativeMeccanumKinematics implements ForwardsKinematics<MeccanumState> {
    /**
     * The angles of each of the wheels.
     */
    private static final Angle[] WHEEL_ANGLES = new Angle[]{
            Angle.DEG_45,                  // FRONT LEFT
            Angle.DEG_315,                 // FRONT RIGHT
            Angle.DEG_315,                 // BACK LEFT
            Angle.DEG_45                   // BACK RIGHT
    };

    /**
     * The kinematics' minimum magnitude.
     */
    private final double minMagnitude;

    /**
     * The kinematics' maximum magnitude.
     */
    private final double maxMagnitude;

    /**
     * The kinematic's turn magnitude.
     */
    private final double turnMagnitude;

    /**
     * The kinematic's angle offset.
     */
    private final Angle angleOffset;

    /**
     * Create a new instance of the {@code RelativeMeccanumKinematics} class.
     *
     * @param minMagnitude the minimum magnitude.
     * @param maxMagnitude the maximum magnitude.
     * @param angleOffset  the angle offset for the kinematics.
     */
    public RelativeMeccanumKinematics(double minMagnitude,
                                      double maxMagnitude,
                                      Angle angleOffset) {
        this(
                minMagnitude,
                maxMagnitude,
                1.0,
                angleOffset
        );
    }

    /**
     * Create a new instance of the {@code RelativeMeccanumKinematics} class.
     *
     * @param minMagnitude the minimum magnitude.
     * @param maxMagnitude the maximum magnitude.
     * @param turnMagnitude the turn magnitude.
     * @param angleOffset  the angle offset for the kinematics.
     */
    public RelativeMeccanumKinematics(double minMagnitude,
                                      double maxMagnitude,
                                      double turnMagnitude,
                                      Angle angleOffset) {
        this.minMagnitude = minMagnitude;
        this.maxMagnitude = maxMagnitude;
        this.turnMagnitude = turnMagnitude;
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
        double magnitude = Math.abs(Math.max(Math.min(
                Math.hypot(
                        translation.vx(),
                        translation.vy()
                ) + Math.abs(turn * turnMagnitude),
                maxMagnitude
        ), minMagnitude));

        double frontLeft = calculatePower(angle, WHEEL_ANGLES[0]) + turn;
        double frontRight = calculatePower(angle, WHEEL_ANGLES[1]) - turn;
        double backLeft = calculatePower(angle, WHEEL_ANGLES[2]) + turn;
        double backRight = calculatePower(angle, WHEEL_ANGLES[3]) - turn;

        return new MeccanumState(
                frontLeft,
                frontRight,
                backLeft,
                backRight
        )
                .normalizeFromMaxUnderOne()
                .multiply(magnitude);
    }
}
