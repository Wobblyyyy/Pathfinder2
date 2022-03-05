/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import java.util.function.Function;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * One of the two major components of your robot - firstly, there's odometry,
 * and secondly, there's the drive train!
 *
 * <p>
 * The Drive interface works quite simply. Let's imagine that your robot
 * has a constant state it's in - a translation. This translation has three
 * values:
 * <ul>
 *     <li>forwards/backwards movement (Y)</li>
 *     <li>right/left movement (X)</li>
 *     <li>turning (around the center of the robot) (Z)</li>
 * </ul>
 * At any given point, your robot has one of these translations as its "state."
 * If the robot is stopped, your translation has X, Y, and Z values of
 * 0, 0, and 0 respectively. If your robot is moving 1 forwards, you'd have
 * 1, 0, and 0. If your robot is moving at a 45 degree angle, you'd have
 * 1, 1, and 0 - you're still not turning, but now you're moving along both
 * the X and Y axes.
 * </p>
 *
 * <p>
 * In order for Pathfinder to control your robot, it needs to be able to
 * apply translations to the robot. So every Pathfinder instance requires a
 * Drive instance as well - the drive interface provides a way for Pathfinder
 * to tell your robot which direction to move in and how fast.
 * </p>
 *
 * <p>
 * It doesn't matter HOW your drive interface is implemented. There are
 * several prebuilt options, such as...
 * <ul>
 *     <li>{@link me.wobblyyyy.pathfinder2.drive.MecanumDrive}</li>
 *     <li>{@link me.wobblyyyy.pathfinder2.drive.SwerveDrive}</li>
 * </ul>
 * ... but it doesn't exactly matter how your drivetrain is implemented, so
 * long as it works. The drivetrain must actually set power to motors - if
 * it doesn't, Pathfinder won't think it's moving, and it thus won't be able
 * to determine where it is.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Drive {
    /**
     * Get the drive train's current translation. This shouldn't be determined
     * based on what the robot's actually doing, but rather, whatever the last
     * translation that was set to the drive train. This value should only
     * ever change whenever the {@link #setTranslation(Translation)} method
     * is called.
     *
     * @return whatever translation was last set to the drive train.
     */
    Translation getTranslation();

    /**
     * Set a translation to the drive train. This should, in fact, make the
     * robot move! Crazy. Anyways. Whenever this method is invoked, the
     * value the invocation of {@link #getTranslation()} will be changed
     * to the value of the {@link Translation} passed as a parameter to
     * this method call.
     *
     * <p>
     * This translation should be relative to the robot, not relative to the
     * field. If you have an absolute translation (NOT relative to the robot),
     * you can use {@link Translation#absoluteToRelative(Translation, Angle)}
     * or {@link Translation#toRelative(Angle)} to convert that translation
     * into a relative translation the robot can then use.
     * </p>
     *
     * @param translation a translation the robot should act upon. This
     *                    translation should always be <em>relative</em>,
     *                    meaning whatever the translation says should make
     *                    the robot act accordingly according to the robot's
     *                    position and the robot's current heading. I'm
     *                    currently exhausted and just about entirely unable
     *                    to type, so this isn't coherent, but guess what -
     *                    that really sucks for you, doesn't it?
     */
    void setTranslation(Translation translation);

    default Function<Translation, Translation> getDriveModifier() {
        throw new UnsupportedOperationException(
            "The implementation of " +
            "the Drive interface you're using does not support " +
            "modifiers!"
        );
    }

    default void setDriveModifier(Function<Translation, Translation> modifier) {
        throw new UnsupportedOperationException(
            "The implementation of " +
            "the Drive interface you're using does not support " +
            "modifiers!"
        );
    }
}
