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

public class TankKinematics implements Kinematics<TankState> {
    private final double turnCoefficient;
    private final double trackWidth;

    public TankKinematics(double turnCoefficient, double trackWidth) {
        this.turnCoefficient = turnCoefficient;
        this.trackWidth = trackWidth;
    }

    @Override
    public TankState calculate(Translation translation) {
        Angle translationAngle = translation.angle();

        double turnDistance = Min.magnitude(
            Angle.minimumDelta(Angle.DEG_90, translationAngle),
            Angle.minimumDelta(Angle.DEG_270, translationAngle)
        ) * translation.magnitude();
        double turn = translation.vz() + (turnDistance * turnCoefficient);

        Logger.trace(
            TankKinematics.class,
            "coeff: %s%nangle: %s%nturn: %s%ndeg: %s%n%n",
            turnCoefficient,
            translation.angle(),
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

        return new Translation(
            0,
            Average.of(right, left),
            (right - left) / trackWidth
        );
    }
}
