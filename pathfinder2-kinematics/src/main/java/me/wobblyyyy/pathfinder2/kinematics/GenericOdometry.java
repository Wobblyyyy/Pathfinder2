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
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

public class GenericOdometry<T> {
    private final Kinematics<T> kinematics;
    private PointXYZ position;
    private double previousTimeMs;

    private final Angle gyroOffset;
    private Angle previousAngle;

    public GenericOdometry(Kinematics<T> kinematics,
                           Angle gyroAngle,
                           PointXYZ initialPosition) {
        this.kinematics = kinematics;
        this.position = initialPosition;
        this.gyroOffset = position.z().subtract(gyroAngle);
        this.previousAngle = position.z();
    }

    public PointXYZ updateWithTime(double currentTimeMs,
                                   Angle gyroAngle,
                                   T state) {
        double period = previousTimeMs > 0
                ? (currentTimeMs - previousTimeMs)
                : 0.0;
        previousTimeMs = currentTimeMs;
        Angle angle = gyroAngle.add(gyroOffset);
        Translation translation = kinematics.toTranslation(state);
        double dx = translation.vx() * period;
        double dy = translation.vy() * period;
        Angle dta = angle.subtract(previousAngle);
        double dt = dta.rad();
        double sin = dta.sin();
        double cos = dta.cos();
        double s;
        double c;
        if (Math.abs(dt) < 1E-9) {
            s = 1.0 - 1.0 / 6.0 * dt * dt;
            c = 0.5 * dt;
        } else {
            s = sin / dt;
            c = (1 - cos) / dt;
        }
        PointXYZ newPosition = position.add(new PointXYZ(
                dx * s - dy * c,
                dx * c + dy * s,
                angle
        ));
        previousAngle = angle;
        position = newPosition;
        return position;
    }
}
