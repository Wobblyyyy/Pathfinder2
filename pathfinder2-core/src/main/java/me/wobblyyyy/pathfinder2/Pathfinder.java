/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2;

import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.execution.ExecutorManager;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.time.Stopwatch;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * The highest-level interface used for interacting with {@code Pathfinder}.
 *
 * <p>
 * Because it's rather challenging to document such an expansive library, I
 * would suggest you read up on some documentation before attempting to dive
 * into everything that's going on here.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Pathfinder {
    /*#########################################################################
     * PRIVATE FIELDS
     *
     * These fields are very thinly documented because they're not visible
     * to users - they're private, they're internal. If you'd like to learn
     * a bit more about these fields, you can probably go look at the getters
     * and setters section. Better yet - official documentation.
     *#######################################################################*/

    /**
     * The {@code Robot} (made up of {@code Drive} and {@code Odometry}) that
     * Pathfinder operates.
     */
    private final Robot robot;

    /**
     * Pathfinder's executor manager.
     */
    private final ExecutorManager manager;

    /**
     * A generator used in converting trajectories into {@link Follower}s.
     */
    private final FollowerGenerator generator;

    /**
     * A stopwatch - this is very likely to not be useful at all, but its
     * included anyways.
     */
    private final Stopwatch stopwatch = new Stopwatch();

    /**
     * The speed Pathfinder will use in creating linear trajectories.
     */
    private double speed;

    /**
     * The tolerance Pathfinder will use in creating linear trajectories.
     */
    private double tolerance;

    /**
     * The angle tolerance Pathfinder will use in creating linear trajectories.
     */
    private Angle angleTolerance;

    /*#########################################################################
     * CONSTRUCTOR
     *
     * There's not much I can say here... sorry...
     *
     * As a future idea - should there be more constructors? Or is one
     * good enough?
     *#######################################################################*/

    /**
     * Create a new {@code Pathfinder} instance.
     *
     * @param robot     the {@code Pathfinder} instance's robot. This robot
     *                  should have an odometry system that can report the
     *                  position of the robot and a drive system that can
     *                  respond to drive commands.
     * @param generator a generator used in creating followers. This generator
     *                  functions by accepting a {@link Trajectory} and a
     *                  {@link Robot} and returning a follower. If you're
     *                  unsure of what this means, or what you should do here,
     *                  you should probably use the "generic follower
     *                  generator," as it's the simplest.
     */
    public Pathfinder(Robot robot,
                      FollowerGenerator generator) {
        this.robot = robot;
        this.generator = generator;

        // Create a new manager using the robot's odometry and drive systems.
        // I don't know why I'm using odometry and drive independently of the
        // Robot container class here, but that's for a later day.
        manager = new ExecutorManager(
                robot.odometry(),
                robot.drive()
        );
    }

    /*#########################################################################
     * GETTERS & SETTERS
     *
     * Some fields do not have setters:
     * - Robot
     * - Odometry
     * - Drive
     *
     * Most other fields have both a getter and setter. If the documentation
     * for one of these is changed, the other should be changed as well - not
     * keeping both of them up to date can make the code rather confusing.
     *#######################################################################*/

    /**
     * Get the {@code Pathfinder} instance's {@link Robot}. This is a final
     * field that's initialized upon construction.
     *
     * @return the {@code Pathfinder} instance's {@link Robot}.
     */
    public Robot getRobot() {
        return robot;
    }

    /**
     * Get the {@code Pathfinder} instance's {@link Odometry} system.
     *
     * @return the odometry system.
     */
    public Odometry getOdometry() {
        return robot.odometry();
    }

    /**
     * Get the {@code Pathfinder} instance's {@link Drive} system.
     *
     * @return the drive system.
     */
    public Drive getDrive() {
        return robot.drive();
    }

    /**
     * Get the speed at which Pathfinder will generate new linear followers.
     * This speed value is entirely irrelevant if you only generate custom
     * trajectories. It only applies to the {@link #goTo(PointXY)} and the
     * {@link #goTo(PointXYZ)} methods.
     *
     * @return the speed at which new linear followers will be generated.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Set the speed Pathfinder will use to generate new linear trajectories.
     *
     * <p>
     * Note that this speed value only applies to two methods:
     * <ul>
     *     <li>{@link #goTo(PointXY)}</li>
     *     <li>{@link #goTo(PointXYZ)}</li>
     * </ul>
     * If you don't plan on using either of those methods, you can entirely
     * ignore the speed value.
     * </p>
     *
     * @param speed the speed at which Pathfinder should generate new linear
     *              trajectories when either of the two "goTo" methods
     *              are called.
     */
    public void setSpeed(double speed) {
        InvalidSpeedException.throwIfInvalid(
                "Attempted to set speed to an invalid value - speed " +
                        "values must be within the range of 0.0 to 1.0.",
                speed
        );

        this.speed = speed;
    }

    /**
     * Get the tolerance Pathfinder will use for generating new linear
     * trajectories.
     *
     * @return the tolerance Pathfinder will use for generating new linear
     * trajectories.
     */
    public double getTolerance() {
        return this.tolerance;
    }

    /**
     * Set the tolerance Pathfinder will use for generating new linear
     * trajectories.
     *
     * <p>
     * This value only applies to two methods:
     * <ul>
     *     <li>{@link #goTo(PointXY)}</li>
     *     <li>{@link #goTo(PointXYZ)}</li>
     * </ul>
     * </p>
     *
     * @param tolerance the tolerance Pathfinder will use in generating
     *                  new linear trajectories.
     */
    public void setTolerance(double tolerance) {
        InvalidToleranceException.throwIfInvalid(
                "Attempted to set an invalid tolerance - all " +
                        "tolerance values must be above 0.",
                tolerance
        );

        this.tolerance = tolerance;
    }

    /**
     * Get the angle tolerance Pathfinder will use in generating new linear
     * trajectories.
     *
     * @return the angle tolerance Pathfinder will use in generating new
     * linear trajectories.
     */
    public Angle getAngleTolerance() {
        return this.angleTolerance;
    }

    /**
     * Set the angle tolerance Pathfinder will use in generating new linear
     * trajectories.
     *
     * <p>
     * This value only applies to two methods:
     * <ul>
     *     <li>{@link #goTo(PointXY)}</li>
     *     <li>{@link #goTo(PointXYZ)}</li>
     * </ul>
     * </p>
     *
     * @param angleTolerance the tolerance Pathfinder should use in generating
     *                       new linear trajectories.
     */
    public void setAngleTolerance(Angle angleTolerance) {
        InvalidToleranceException.throwIfInvalid(
                "Attempted to set an invalid angle tolerance - all " +
                        "tolerance values must be above 0.",
                angleTolerance.deg()
        );

        this.angleTolerance = angleTolerance;
    }

    /*#########################################################################
     * "COMMAND" METHODS (TELL PATHFINDER WHAT TO DO)
     *
     * These methods are the main method of operation for Pathfinder. Without
     * these methods, you can't really do much... anything, actually. Most
     * of these methods funnel into another and are different "options."
     *
     * If you have a suggestion for more "command" methods that can be
     * added, you're more than welcome to go ahead. So long as it functions
     * in a similar manner to all of the other "command" methods, you're good!
     *#######################################################################*/

    /**
     * "Tick" Pathfinder once. This will tell Pathfinder's execution manager
     * to check to see what Pathfinder should be doing right now, and based
     * on that, move your robot.
     */
    public void tick() {
        this.getExecutorManager().tick();
    }

    /**
     * Follow a single trajectory.
     *
     * @param trajectory the trajectory to follow.
     */
    public void followTrajectory(Trajectory trajectory) {
        List<Trajectory> list = new ArrayList<>() {{
            add(trajectory);
        }};

        followTrajectories(list);
    }

    /**
     * Follow multiple trajectories.
     *
     * @param trajectories a list of trajectories to follow.
     */
    public void followTrajectories(List<Trajectory> trajectories) {
        List<Follower> followers = new ArrayList<>();

        for (Trajectory trajectory : trajectories) {
            followers.add(generator.generate(
                    robot,
                    trajectory
            ));
        }

        follow(followers);
    }

    /**
     * Follow a single follower.
     *
     * @param follower a single follower to follow.
     */
    public void follow(Follower follower) {
        List<Follower> list = new ArrayList<>() {{
            add(follower);
        }};

        follow(list);
    }

    /**
     * Follow a list of followers.
     *
     * @param followers a list of followers.
     */
    public void follow(List<Follower> followers) {
        manager.addExecutor(followers);
    }

    /**
     * Go to a specific point. This method will create a new linear trajectory.
     *
     * @param point the target point to go to.
     * @see #setSpeed(double)
     * @see #setTolerance(double)
     * @see #setAngleTolerance(Angle)
     */
    public void goTo(PointXY point) {
        NullPointException.throwIfInvalid(
                "Attempted to navigate to a null point.",
                point
        );

        goTo(
                point.withHeading(
                        getOdometry().getZ()
                )
        );
    }

    /**
     * Go to a specific point. This method will create a new linear trajectory.
     *
     * @param point the target point to go to.
     * @see #setSpeed(double)
     * @see #setTolerance(double)
     * @see #setAngleTolerance(Angle)
     */
    public void goTo(PointXYZ point) {
        NullPointException.throwIfInvalid(
                "Attempted to navigate to a null point.",
                point
        );

        followTrajectory(new LinearTrajectory(
                point,
                speed,
                tolerance,
                angleTolerance
        ));
    }

    /**
     * Get Pathfinder's {@code Stopwatch}.
     *
     * @return Pathfinder's {@code Stopwatch} instance.
     */
    public Stopwatch stopwatch() {
        return this.stopwatch;
    }

    /**
     * Is Pathfinder currently active?
     *
     * @return is Pathfinder currently active? This method will return true
     * if Pathfinder is active (meaning its currently following a path) and
     * will return false if Pathfinder is not active (meaning its idle).
     */
    public boolean isActive() {
        return manager.isActive();
    }

    /*#########################################################################
     * MISC. STUFF
     *
     * That's a good description, I know.
     *#######################################################################*/

    /**
     * Get this instance of {@code Pathfinder}'s {@link ExecutorManager}.
     *
     * @return Pathfinder's executor manager.
     */
    public ExecutorManager getExecutorManager() {
        return manager;
    }

    /**
     * Clear the {@link ExecutorManager}, resetting just about everything.
     */
    public void clear() {
        manager.clearExecutors();
    }
}
