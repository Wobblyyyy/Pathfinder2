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
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.math.Average;
import me.wobblyyyy.pathfinder2.math.Min;

/**
 * Kinematics for a tank drive (also commonly known as a "differential drive").
 * A tank drive, in the (hopefully unlikely) event you were unaware, is a type
 * of chassis that applies power along two different axes to move the robot.
 *
 * <p>
 * By default, this value is 1 / 90. Because a tank drive can only move along
 * one axis - forwards and backwards - you need a way to convert translations
 * with multiple axes into a translation that can be applied to a differential
 * drive chassis. If a translation has a horizontal component (say (1, 0, 0)),
 * this value will be used in calculating a {@link TankState} that can be
 * applied to the chassis.
 * </p>
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class TankKinematics implements Kinematics<TankState> {
    private final double turnCoefficient;
    private final double trackWidth;

    /**
     * Create a new instance of {@code TankKinematics}.
     *
     * @param trackWidth      the track width of the chassis. This should be
     *                        in whatever unit you're using for the rest of
     *                        Pathfinder. "Track width" is the measurement
     *                        between the two sides of the robot.
     */
    public TankKinematics(double trackWidth) {
        this(1.0 / 90.0, trackWidth);
    }

    /**
     * Create a new instance of {@code TankKinematics}.
     *
     * @param turnCoefficient the turn coefficient to use for the kinematics.
     *                        By default, this value is 1 / 90. Because a
     *                        tank drive can only move along one axis -
     *                        forwards and backwards - you need a way to
     *                        convert translations with multiple axes into
     *                        a translation that can be applied to a
     *                        differential drive chassis. If a translation
     *                        has a horizontal component (say (1, 0, 0)), this
     *                        value will be used in calculating a
     *                        {@link TankState} that can be applied to the
     *                        chassis.
     * @param trackWidth      the track width of the chassis. This should be
     *                        in whatever unit you're using for the rest of
     *                        Pathfinder. "Track width" is the measurement
     *                        between the two sides of the robot.
     */
    public TankKinematics(double turnCoefficient, double trackWidth) {
        this.turnCoefficient = turnCoefficient;
        this.trackWidth = trackWidth;
    }

    @Override
    public TankState calculate(Translation translation) {
        Angle translationAngle = translation.angle();

        double turnDistance =
            Min.magnitude(
                Angle.minimumDelta(Angle.DEG_90, translationAngle),
                Angle.minimumDelta(Angle.DEG_270, translationAngle)
            ) *
            translation.magnitude();
        double turn = translation.vz() + (turnDistance * turnCoefficient);

        Logger.trace(
            TankKinematics.class,
            "coeff: <%s> angle: <%s> turn: <%s> turn distance: <%s>",
            turnCoefficient,
            translationAngle,
            turn,
            turnDistance
        );

        return new TankState(
            translation.vy() + (trackWidth / 2 * turn),
            translation.vy() - (trackWidth / 2 * turn)
        );
    }

    @Override
    public Translation toTranslation(TankState state) {
        double right = state.right();
        double left = state.left();

        double vy = Average.of(right, left);
        double vz = (right - left) / trackWidth;

        Logger.trace(
            TankKinematics.class,
            "r: <%s> l: <%s> vy: <%s> vz: <%s>",
            right,
            left,
            vy,
            vz
        );

        return new Translation(0, vy, vz);
    }
}
