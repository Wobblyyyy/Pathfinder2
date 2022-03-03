/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.follower;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Every rose has its thorns. And every trajectory has its follower. That
 * doesn't sound as catchy, but I'm sure you get the idea.
 *
 * <p>
 * Each trajectory that you'd like the robot to follow needs to have a
 * follower responsible for... well, actually following the trajectory.
 * </p>
 *
 * <p>
 * A follower is responsible for manipulating the robot's motors in a
 * manner that allows the robot to follow some sort of predetermined path.
 * Each {@code Follower} should be responsible for a single action.
 * </p>
 *
 * <p>
 * For almost all use cases, the {@link GenericFollower} is the best
 * implementation of the {@code Follower} interface.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Follower {
    /**
     * Get an absolute translation based on a couple of parameters.
     *
     * <p>
     * This method functions as follows.
     * <ul>
     *     <li>
     *         Determine the angle from the robot's current position to
     *         the robot's target position.
     *     </li>
     *     <li>
     *         Create an imaginary point using the lovely
     *         {@link PointXYZ#inDirection(double, Angle)} method. This point
     *         will always be drawn at a distance of 1. The point's angle
     *         is equal to the angle we just calculated.
     *     </li>
     *     <li>
     *         Convert that point into a translation by getting the X and Y
     *         values of the point. The generated translation simply
     *         re-uses the {@code turn} parameter that's passed into
     *         this method.
     *     </li>
     * </ul>
     * </p>
     *
     * @param current the robot's current position.
     * @param target  the robot's target point / marker.
     * @param speed   the speed at which the robot should move.
     * @param turn    how much the robot should turn.
     * @return an absolute translation. This translation will always have X
     * and Y values that fit within the bounds (-1.0, 1.0). This translation
     * is also absolute, NOT relative.
     */
    static Translation getAbsoluteTranslation(PointXYZ current,
                                              PointXYZ target,
                                              double speed,
                                              double turn) {
        // this is messy, i know, but i don't know how else to do it so uh...
        PointXY currentXY = new PointXY(current.x(), current.y());
        PointXY targetXY = new PointXY(target.x(), target.y());

        // need to instantiate new PointXY instead of just casting the existing
        // PointXYZ to PointXY - if you casted it, it would use the PointXYZ
        // equals method, which also checks if they have the same angle. if
        // we just use PointXY instead, it just checks for matching X and Y
        if (currentXY.equals(targetXY))
            return Translation.ZERO.withVz(turn);

        Angle angle = current.angleTo(target).fix().subtract(current.z());

        PointXYZ targetPoint = PointXYZ.ZERO.inDirection(speed, angle);

        return new Translation(targetPoint.x(), targetPoint.y(), turn);
    }

    /**
     * Get a relative translation by first generating an absolute translation
     * and then converting it into a relative one.
     *
     * @param current the robot's current position.
     * @param target  the robot's target point / marker.
     * @param speed   the speed at which the robot should move.
     * @param turn    how much the robot should turn.
     * @return a relative translation. This translation, like any absolute
     * translation, will always have X and Y values that fit within the bounds
     * (-1.0, 1.0). The Z aspect of the translation will always be equal to
     * whatever turn value is provided.
     */
    static Translation getRelativeTranslation(PointXYZ current,
                                              PointXYZ target,
                                              double speed,
                                              double turn) {
        return Translation.absoluteToRelative(
                // We start by creating an absolute translation using the
                // "getAbsoluteTranslation" method. From there, we convert it to
                // a relative one. Complicated stuff, right?
                getAbsoluteTranslation(current, target, speed, turn),

                // Translation.absoluteToRelative requires a heading value that
                // represents the robot's current heading to generate the
                // new relative translation - that's exactly what this is.
                current.z()
        );
    }

    /**
     * Get the trajectory the follower is following.
     *
     * <p>
     * This method is mostly just here to enforce that any classes implementing
     * the {@code Follower} interface require a trajectory in the constructor.
     * </p>
     *
     * @return the trajectory the follower is following.
     */
    Trajectory getTrajectory();

    /**
     * "Tick" the follower once.
     *
     * <p>
     * Each time the follower is ticked, a new translation should be generated.
     * The {@code Consumer<Translation>} that's supplied to this method will
     * accept that translation, and in doing so, drive the robot accordingly.
     * This translation will always be relative, meaning relative to whatever
     * direction the robot is currently facing.
     * </p>
     *
     * @param current  the robot's current position.
     * @param consumer a consumer that will accept a generated translation.
     *                 This translation should never have X or Y values
     *                 outside the bounds (-1.0, 1.0).
     * @return if the follower has finished, return true, indicating that
     * the follower can be de-queued and the next follower can begin execution.
     * If the follower has not finished, however, return false, indicating
     * that we need to keep going at least a little while longer.
     */
    boolean tick(PointXYZ current, Consumer<Translation> consumer);

    /**
     * Tick a follower once.
     *
     * @param follower       the follower to tick.
     * @param getPosition    a supplier that returns the robot's position.
     * @param setTranslation a consumer that accepts a translation.
     * @return true if the follower is finished. Otherwise, false.
     */
    static boolean tickFollower(Follower follower,
                                Supplier<PointXYZ> getPosition,
                                Consumer<Translation> setTranslation) {
        return follower.tick(
                getPosition.get(),
                setTranslation
        );
    }
}
