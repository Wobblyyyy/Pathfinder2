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

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.exceptions.NullControllerException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.exceptions.NullTrajectoryException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.function.Consumer;

/**
 * A generic implementation of the {@link Follower} interface. For almost all
 * use cases, this implementation should work fairly well. This implementation
 * of the {@link Follower} interface depends on a "turn controller" - in
 * essence, a {@link Controller} that determines the speed at which the robot
 * is turning.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class GenericFollower implements Follower {
    /**
     * The follower's trajectory.
     */
    private final Trajectory trajectory;

    /**
     * The follower's turn controller.
     */
    private final Controller turnController;

    /**
     * Create a new {@code GenericFollower}.
     *
     * <p>
     * In addition to creating a {@code GenericFollower}, this constructor will
     * set the turn controller's target to 0.
     * </p>
     *
     * @param trajectory     the trajectory the follower should follow.
     * @param turnController a turn controller, responsible for determining
     *                       turn values.
     */
    public GenericFollower(Trajectory trajectory,
                           Controller turnController) {
        if (trajectory == null) {
            throw new NullTrajectoryException(
                    "Can't create a generic follower with a null trajectory!"
            );
        }

        if (turnController == null) {
            throw new NullControllerException(
                    "Can't create a generic follower with a null turn controller!"
            );
        }

        this.trajectory = trajectory;
        this.turnController = turnController;

        turnController.setTarget(0);
    }

    /**
     * Get the follower's trajectory.
     *
     * @return the follower's trajectory.
     */
    @Override
    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    @Override
    public boolean tick(PointXYZ current,
                        Consumer<Translation> consumer) {
        if (current == null) {
            throw new NullPointException(
                    "Attempted to tick a generic follower with a NULL " +
                            "current point; make sure your position is never " +
                            "null!"
            );
        }

        // If the trajectory is done, we should stop executing this method
        // right here.
        if (trajectory.isDone(current)) {
            // Because the trajectory has finished, we give the robot a
            // translation of zero - there's no point in moving anymore, right?
            consumer.accept(Translation.zero());

            // And because the translation is finished, the tick method should
            // return true. Putting a return statement here ensures that the
            // rest of the method won't get executed.
            return true;
        }

        // Assuming the follower hasn't finished executing its trajectory,
        // we continue by finding a couple values and generating a shiny
        // new translation for the robot to follow.

        // Get the next marker by calling the trajectory's nextMarker
        // method. This method should always return a target point we can
        // use to generate a new translation.
        PointXYZ nextMarker = trajectory.nextMarker(current);

        // And of course, the speed, because some trajectories can control
        // speed differently.
        double speed = trajectory.speed(current);

        // Determine the delta between the two angles, so we can calculate
        // turn in just a moment.
        double angleDelta = Angle.minimumDelta(current.z(), nextMarker.z());

        // And calculate turn. Remember, the turn controller's only job is to
        // get the delta to zero, so by feeding it the delta it should output
        // a value that will minimize that aforementioned delta.
        double turn = turnController.calculate(angleDelta);

        Translation translation = Follower.getRelativeTranslation(
                current,
                nextMarker,
                speed,
                turn
        );

        // Use the consumer to accept a translation we create.
        // Instead of creating an absolute translation, which would only help
        // if the robot is facing straight forwards, we have to use a relative
        // translation. The Follower interface's static method that we
        // call here (getRelativeTranslation) first generates an absolute
        // translation and then converts it to a relative one. Epic sauce.
        consumer.accept(translation);

        // We're not done yet, so return false.
        return false;
    }
}
