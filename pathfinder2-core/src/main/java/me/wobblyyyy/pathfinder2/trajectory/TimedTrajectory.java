/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.time.Time;

/**
 * A trajectory that will go in a certain direction for a certain amount
 * of time.
 *
 * @author Colin Robertson
 * @since 0.2.3
 */
public class TimedTrajectory implements Trajectory {
    private final Translation translation;
    private double startTime = 0;
    private double elapsedTime = 0;
    private final double timeout;
    private final double speed;

    /**
     * Create a new {@code TimedTrajectory} with a default speed value of 1.0.
     *
     * @param translation the translation the robot should follow.
     * @param timeout     how long the trajectory should last.
     */
    public TimedTrajectory(Translation translation,
                           double timeout) {
        this(
                translation,
                timeout,
                1.0
        );
    }

    /**
     * Create a new {@code TimedTrajectory}.
     *
     * @param translation the translation the robot should follow.
     * @param timeout     how long the trajectory should last.
     * @param speed       how fast the robot should move (should usually be
     *                    a value within 0.0 to 1.0)
     */
    public TimedTrajectory(Translation translation,
                           double timeout,
                           double speed) {
        this.translation = translation;
        this.timeout = timeout;
        this.speed = speed;
    }

    private void updateTime() {
        double currentTime = Time.ms();

        if (startTime == 0) {
            startTime = currentTime;
        }

        elapsedTime = currentTime - startTime;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return current.inDirection(
                speed,
                // go forward for 10 seconds - matt
                // right foot right stomp - alec
                current.z().add(Angle.atan2(
                        translation.vy(),
                        translation.vx()
                ))
        ).rotate(
                current,
                Angle.fixedRad(translation.vz())
        );
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return elapsedTime >= timeout;
    }

    @Override
    public double speed(PointXYZ current) {
        return speed;
    }
}
