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

import me.wobblyyyy.pathfinder2.Core;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.time.Time;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

/**
 * A trajectory that will go in a certain direction for a certain amount
 * of time. This type of trajectory is most useful when making a timed
 * autonomous routine - for example, if you have a robot that doesn't have
 * great positional tracking abilities, you can precisely time your
 * autonomous using several timed trajectories so that your robot follows a
 * predictable pattern of motion.
 *
 * @author Colin Robertson
 * @since 0.2.3
 */
public class TimedTrajectory implements Trajectory {
    private final Translation translation;
    private final double timeoutMs;
    private final double speed;
    private final double turnMultiplier;
    private double startTime = 0;
    private double elapsedTime = 0;

    /**
     * Create a new {@code TimedTrajectory}.
     *
     * @param translation    the translation the robot should follow. This
     *                       translation will have vx and vy values of how far
     *                       the robot should move in those respective directions
     *                       (remember, always relative to the robot). The vz
     *                       value of this translation will be how fast the
     *                       robot will turn, in radians.
     * @param timeoutMs      how long the trajectory should last. This time
     *                       is measured in milliseconds. The trajectory is
     *                       considered finished after the elapsed time (ms) is
     *                       greater than this value.
     * @param speed          how fast the robot should move (should usually be
     *                       a value within 0.0 to 1.0)
     * @param turnMultiplier the value that all vz values will be multiplied
     *                       by. Having a higher turn multiplier means your
     *                       robot will attempt to turn more quickly, while
     *                       having a lower turn multiplier means your robot
     *                       will attempt to turn more slowly.
     */
    public TimedTrajectory(
        Translation translation,
        double timeoutMs,
        double speed,
        double turnMultiplier
    ) {
        this.translation = translation;
        this.timeoutMs = timeoutMs;
        this.speed = speed;
        this.turnMultiplier = turnMultiplier;
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
        // my friend and my boyfriend (matt and alec respectively) decided
        // they wanted to write some code (they don't know how to code), so
        // i let them type their own comments. here they are!
        // go forward for 10 seconds - matt
        // right foot right stomp - alec

        return current
            .inDirection(
                speed,
                current.z().add(Angle.atan2(translation.vy(), translation.vx()))
            )
            .rotate(current, Angle.fixedRad(translation.vz() * turnMultiplier));
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return elapsedTime >= timeoutMs;
    }

    @Override
    public double speed(PointXYZ current) {
        return speed;
    }

    @Override
    public String toString() {
        return StringUtils.format(
            Core.timedTrajectoryFormat,
            translation,
            timeoutMs
        );
    }
}
