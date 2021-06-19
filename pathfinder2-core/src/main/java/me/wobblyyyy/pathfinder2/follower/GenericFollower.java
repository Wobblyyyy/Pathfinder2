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
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.function.Consumer;

public class GenericFollower implements Follower {
    private final Trajectory trajectory;

    private final Controller turnController;

    public GenericFollower(Trajectory trajectory,
                           Controller turnController) {
        this.trajectory = trajectory;
        this.turnController = turnController;

        turnController.setTarget(0);
    }

    @Override
    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    @Override
    public boolean tick(PointXYZ current,
                        Consumer<Translation> consumer) {
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

        // Determine the delta between the two angles so we can calculate
        // turn in just a moment.
        double angleDelta = Angle.minimumDelta(current.z(), nextMarker.z());

        // And calculate turn. Remember, the turn controller's only job is to
        // get the delta to zero, so by feeding it the delta it should output
        // a value that will minimize that aforementioned delta.
        double turn = turnController.calculate(angleDelta);

        // Use the consumer to accept a translation we create.
        // Instead of creating an absolute translation, which would only help
        // if the robot is facing straight forwards, we have to use a relative
        // translation. The Follower interface's static method that we
        // call here (getRelativeTranslation) first generates an absolute
        // translation and then converts it to a relative one. Epic sauce.
        consumer.accept(Follower.getRelativeTranslation(
                current,
                nextMarker,
                speed,
                turn
        ));

        // We're not done yet, so return false.
        return false;
    }
}
