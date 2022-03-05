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

import java.util.function.Consumer;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.exceptions.NullControllerException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.exceptions.NullTrajectoryException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

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
     * @param trajectory     the trajectory the follower should follow. The
     *                       follower is responsible for operating the robot
     *                       such that the robot moves in a way dictated by
     *                       this {@code Trajectory}. After the trajectory has
     *                       finished, the follower should also finish.
     * @param turnController a turn controller, responsible for determining
     *                       turn values. This controller's target will be
     *                       set to 0. The controller will receive the current
     *                       distance from the target angle, in degrees, and
     *                       should output a value to minimize that distance
     *                       and bring it as close to 0 as possible.
     */
    public GenericFollower(Trajectory trajectory, Controller turnController) {
        ValidationUtils.validate(trajectory, "trajectory");
        ValidationUtils.validate(turnController, "turnController");

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
    public boolean tick(PointXYZ current, Consumer<Translation> consumer) {
        // alright - i'm writing this comment maybe... say... four months?
        // after writing this initial bit of code, and i would just like to
        // say - if you're here because you're trying to figure out what makes
        // the robot move, congrats! you've found it! enjoy some stupidly
        // thoroughly commented and insanely simple code!
        // with love,
        // - colin <3
        // Tue Jan 18 22:34:58
        // p.s. i also made all of the comments in this method lowercase now
        // so it matches the vibe better. you know what i mean?

        ValidationUtils.validate(current, "current");
        ValidationUtils.validate(consumer, "consumer");

        // if the trajectory is done, we should stop executing this method
        // right here.
        if (trajectory.isDone(current)) {
            // because the trajectory has finished, we give the robot a
            // translation of zero - there's no point in moving anymore, right?
            consumer.accept(Translation.ZERO);

            // and because the translation is finished, the tick method should
            // return true. putting a return statement here ensures that the
            // rest of the method won't get executed.
            return true;
        }

        // assuming the follower hasn't finished executing its trajectory,
        // we continue by finding a couple values and generating a shiny
        // new translation for the robot to follow.

        // get the next marker by calling the trajectory's nextMarker
        // method. this method should always return a target point we can
        // use to generate a new translation.
        PointXYZ nextMarker = trajectory.nextMarker(current);

        // and of course, the speed, because some trajectories can control
        // speed differently.
        double speed = trajectory.speed(current);

        // determine the delta between the two angles, so we can calculate
        // turn in just a moment.
        double angleDelta = Angle.minimumDelta(current.z(), nextMarker.z());

        // and calculate turn. remember, the turn controller's only job is to
        // get the delta to zero, so by feeding it the delta it should output
        // a value that will minimize that aforementioned delta.
        double turn = turnController.calculate(angleDelta);

        // getRelativeTranslation returns a RELATIVE translation (meaning it
        // can be applied to the robot)
        Translation translation = Follower.getRelativeTranslation(
            current,
            nextMarker,
            speed,
            turn
        );

        // use the consumer to accept a translation we create.
        // instead of creating an absolute translation, which would only help
        // if the robot is facing straight forwards, we have to use a relative
        // translation. the follower interface's static method that we
        // call here (getRelativeTranslation) first generates an absolute
        // translation and then converts it to a relative one. epic sauce.
        consumer.accept(translation);

        // we're not done yet, so return false.
        return false;
    }
}
