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

/**
 * Generic odometry implementation that can use an instance of
 * {@link Kinematics} to determine a robot's position.
 *
 * @author Colin Robertson
 * @since 2.0.0
 */
public class GenericOdometry<T> {
    private final Kinematics<T> kinematics;
    private final Angle gyroOffset;
    private final double updateIntervalMs;
    private PointXYZ position;
    private double previousTimeMs = -1;
    private Angle previousAngle;

    public GenericOdometry(
        Kinematics<T> kinematics,
        Angle gyroAngle,
        PointXYZ initialPosition
    ) {
        this(kinematics, gyroAngle, initialPosition, 0);
    }

    public GenericOdometry(
        Kinematics<T> kinematics,
        Angle gyroAngle,
        PointXYZ initialPosition,
        double updateIntervalMs
    ) {
        this.kinematics = kinematics;
        this.position = initialPosition;
        this.gyroOffset = position.z().subtract(gyroAngle);
        this.previousAngle = position.z();
        this.updateIntervalMs = updateIntervalMs;
    }

    /**
     * Update the position based on the system's current time (as dictated
     * by {@code Time#ms()} or {@code System#currentTimeMillis()}), the
     * angle of the gyroscope on the robot, and the current state of the robot.
     *
     * <p>
     * This method integrates the velocity of the robot (as determined by
     * the parameter of type {@code T}) over time to determine the position
     * of the robot.
     * </p>
     *
     * @param currentTimeMs the system's current time, in milliseconds.
     *                      Use either {@code Time#ms()} or
     *                      {@code System#currentTimeMillis()} to get this
     *                      value.
     * @param gyroAngle     the angle of the robot's gyroscope.
     * @param state         the current state of the robot. This value
     *                      should usually be determined by using encoders to
     *                      determine the robot's velocity.
     * @return the robot's updated position.
     */
    public PointXYZ updateWithTime(
        double currentTimeMs,
        Angle gyroAngle,
        T state
    ) {
        double period = previousTimeMs > -1
            ? (currentTimeMs - previousTimeMs)
            : 0.0;

        // if not enough time has elapsed, return the robot's position
        // instead of updating the position
        if (period < updateIntervalMs) {
            return position;
        }

        previousTimeMs = currentTimeMs;

        period /= 1_000;

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

        PointXYZ newPosition = position.add(
            new PointXYZ(dx * s - dy * c, dx * c + dy * s, angle)
        );

        previousAngle = angle;
        position = newPosition;

        return position;
    }
}
