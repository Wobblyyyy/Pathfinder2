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

import me.wobblyyyy.pathfinder2.exceptions.*;
import me.wobblyyyy.pathfinder2.execution.ExecutorManager;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import me.wobblyyyy.pathfinder2.time.Stopwatch;
import me.wobblyyyy.pathfinder2.time.Time;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The highest-level interface used for interacting with {@code Pathfinder}.
 *
 * <p>
 * This object is the main method of interacting with the Pathfinder library.
 * With it, you'll be able to control all of your robot's movement from a
 * single object - both autonomous and teleop movement included. For advanced
 * use of the library, you'll need to become familiar with the concept behind
 * {@link Trajectory}. For simple use, however, you can simply use the
 * {@link #goTo(PointXY)} or {@link #goTo(PointXYZ)} methods to get the job
 * done. This isn't all Pathfinder can do, however - trajectories provide
 * a lot of flexibility and allow you to control your robot's movement
 * quite precisely and quite easily.
 * </p>
 *
 * <p>
 * At any given point, you should only be using one of these objects. I have
 * absolutely no idea why you would want to use more than one of them, but
 * I'd put this here anyways.
 * </p>
 *
 * @author Colin Robertson
 * @see #goTo(PointXY)
 * @see #goTo(PointXYZ)
 * @see #setTranslation(Translation)
 * @see #followTrajectory(Trajectory)
 * @see #followTrajectories(List)
 * @see #follow(Follower)
 * @see #follow(List)
 * @see #tickUntil(double, Supplier)
 * @see #andThen(Consumer, double, Supplier)
 * @since 0.0.0
 */
@SuppressWarnings("UnusedReturnValue")
public class Pathfinder {
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
    private double speed = -1.0;

    /**
     * The tolerance Pathfinder will use in creating linear trajectories.
     */
    private double tolerance = -1.0;

    /**
     * The angle tolerance Pathfinder will use in creating linear trajectories.
     */
    private Angle angleTolerance = null;

    /**
     * Create a new {@code Pathfinder} instance.
     *
     * @param robot     the {@code Pathfinder} instance's robot. This robot
     *                  should have an odometry system that can report the
     *                  position of the robot and a drive system that can
     *                  respond to drive commands. This object may not be
     *                  null or, an exception will be thrown.
     * @param generator a generator used in creating followers. This generator
     *                  functions by accepting a {@link Trajectory} and a
     *                  {@link Robot} and returning a follower. If you're
     *                  unsure of what this means, or what you should do here,
     *                  you should probably use the "generic follower
     *                  generator," as it's the simplest. This object may not
     *                  be null, or an exception will be thrown.
     */
    public Pathfinder(Robot robot,
                      FollowerGenerator generator) {
        if (robot == null)
            throw new NullPointerException("Robot cannot be null!");
        if (generator == null)
            throw new NullPointerException("Follower generator cannot be null!");

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
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder setSpeed(double speed) {
        InvalidSpeedException.throwIfInvalid(
                "Attempted to set speed to an invalid value - speed " +
                        "values must be within the range of 0.0 to 1.0.",
                speed
        );

        this.speed = speed;

        return this;
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
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder setTolerance(double tolerance) {
        InvalidToleranceException.throwIfInvalid(
                "Attempted to set an invalid tolerance - all " +
                        "tolerance values must be above 0.",
                tolerance
        );

        this.tolerance = tolerance;

        return this;
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
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder setAngleTolerance(Angle angleTolerance) {
        InvalidToleranceException.throwIfInvalid(
                "Attempted to set an invalid angle tolerance - all " +
                        "tolerance values must be above 0.",
                angleTolerance.deg()
        );

        this.angleTolerance = angleTolerance;

        return this;
    }

    /**
     * "Tick" Pathfinder once. This will tell Pathfinder's execution manager
     * to check to see what Pathfinder should be doing right now, and based
     * on that, move your robot.
     *
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tick() {
        this.getExecutorManager().tick();

        return this;
    }

    /**
     * Tick Pathfinder until it finishes whatever path is currently being
     * executed.
     *
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil() {
        return tickUntil(
                Double.MAX_VALUE,
                () -> true
        );
    }

    /**
     * Tick Pathfinder until either the path it was following is finished or
     * the timeout time (in milliseconds) is reached.
     *
     * @param timeoutMs how long, in milliseconds, Pathfinder will
     *                  continue ticking (as a maximum). If the
     *                  path finishes before this time is reached,
     *                  it'll stop as normal.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(double timeoutMs) {
        return tickUntil(
                timeoutMs,
                () -> true
        );
    }

    /**
     * Tick Pathfinder while the provided supplier returns true.
     *
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(Supplier<Boolean> shouldContinueRunning) {
        return tickUntil(
                Double.MAX_VALUE,
                shouldContinueRunning
        );
    }

    /**
     * Tick Pathfinder while the provided supplier returns true and the
     * elapsed time is less than the timeout time.
     *
     * @param timeoutMs             how long, in milliseconds, Pathfinder will
     *                              continue ticking (as a maximum). If the
     *                              path finishes before this time is reached,
     *                              it'll stop as normal.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(double timeoutMs,
                                Supplier<Boolean> shouldContinueRunning) {
        return tickUntil(
                timeoutMs,
                shouldContinueRunning,
                pathfinder -> {
                }
        );
    }

    /**
     * Tick Pathfinder while the provided supplier returns true and the
     * elapsed time is less than the timeout time.
     *
     * @param timeoutMs             how long, in milliseconds, Pathfinder will
     *                              continue ticking (as a maximum). If the
     *                              path finishes before this time is reached,
     *                              it'll stop as normal.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @param onTick                a {@link Consumer} that will be executed
     *                              after every successful tick.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(double timeoutMs,
                                Supplier<Boolean> shouldContinueRunning,
                                Consumer<Pathfinder> onTick) {
        if (timeoutMs < 0) {
            throw new InvalidTimeException(
                    "Attempted to use an invalid timeout time in in a call to " +
                            "the tickUntil method - make sure this time value " +
                            "is greater than or equal to 0."
            );
        }

        if (shouldContinueRunning == null) {
            throw new NullPointerException(
                    "Attempted to use a null supplier with the tickUntil " +
                            "method - this can't be null, nerd."
            );
        }

        if (onTick == null) {
            throw new NullPointerException(
                    "Attempted to use a null consumer with the tickUntil " +
                            "method. This also can't be null, nerd."
            );
        }

        double start = Time.ms();

        while (isActive() && shouldContinueRunning.get()) {
            double current = Time.ms();
            double elapsed = current - start;

            if (elapsed >= timeoutMs) {
                break;
            }

            tick();
            onTick.accept(this);
        }

        return this;
    }

    /**
     * Tick Pathfinder while the provided supplier returns true and the
     * elapsed time is less than the timeout time.
     *
     * @param timeoutMs             how long, in milliseconds, Pathfinder will
     *                              continue ticking (as a maximum). If the
     *                              path finishes before this time is reached,
     *                              it'll stop as normal.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @param onTick                a {@link Consumer} that will be executed
     *                              after every successful tick. This consumer
     *                              accepts two parameters - first the instance
     *                              of Pathfinder that is running. Second, a
     *                              double value representing the total elapsed
     *                              time (in milliseconds) that the tick
     *                              until method has been running for.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(double timeoutMs,
                                Supplier<Boolean> shouldContinueRunning,
                                BiConsumer<Pathfinder, Double> onTick) {
        NotNull.throwExceptionIfNull(
                "A null value was passed to the tickUntil method! " +
                        "Please make sure you don't pass any null values.",
                shouldContinueRunning
        );

        double start = Time.ms();

        while (isActive() && shouldContinueRunning.get()) {
            double current = Time.ms();
            double elapsed = current - start;

            if (elapsed >= timeoutMs) {
                break;
            }

            tick();
            onTick.accept(this, elapsed);
        }

        return this;
    }

    /**
     * Continually tick Pathfinder for as long as it needs to be ticked to
     * finish executing the current path. This method accepts a
     * {@link BiConsumer} parameter that in turn accepts two parameters -
     * first, {@code this} instance of Pathfinder, and second, the elapsed
     * time (in milliseconds). If you'd like to exit out of the ticking, you
     * can simply use {@link #clear()} inside of the {@link BiConsumer}.
     *
     * @param onTick a {@link Consumer} that will be executed
     *               after every successful tick. This consumer
     *               accepts two parameters - first the instance
     *               of Pathfinder that is running. Second, a
     *               double value representing the total elapsed
     *               time (in milliseconds) that the tick
     *               until method has been running for.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(BiConsumer<Pathfinder, Double> onTick) {
        double start = Time.ms();

        while (isActive()) {
            double current = Time.ms();
            double elapsed = current - start;

            onTick.accept(this, elapsed);
        }

        return this;
    }

    /**
     * Tick Pathfinder while the elapsed time is less than the timeout (in
     * milliseconds) and the {@link Predicate} (accepting {@code this} as
     * a parameter) returns true.
     *
     * @param timeoutMs how long, in milliseconds, Pathfinder will
     *                  continue ticking (as a maximum). If the
     *                  path finishes before this time is reached,
     *                  it'll stop as normal.
     * @param isValid   a predicate, accepting {@code this}
     *                  instance of Pathfinder as a parameter.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(double timeoutMs,
                                Predicate<Pathfinder> isValid) {
        NotNull.throwExceptionIfNull(
                "A null value was passed to the tickUntil method! " +
                        "Please make sure you don't pass any null values.",
                isValid
        );

        double start = Time.ms();

        while (isActive() && isValid.test(this)) {
            double current = Time.ms();
            double elapsed = current - start;

            if (elapsed >= timeoutMs) {
                break;
            }

            tick();
        }

        return this;
    }

    /**
     * Tick Pathfinder while the elapsed time is less than the timeout (in
     * milliseconds), the {@link Predicate} (accepting {@code this} as
     * a parameter) returns true, and the provided {@link Supplier} also
     * returns true.
     *
     * @param timeoutMs             how long, in milliseconds, Pathfinder will
     *                              continue ticking (as a maximum). If the
     *                              path finishes before this time is reached,
     *                              it'll stop as normal.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @param isValid               a predicate, accepting {@code this}
     *                              instance of Pathfinder as a parameter.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(double timeoutMs,
                                Supplier<Boolean> shouldContinueRunning,
                                Predicate<Pathfinder> isValid) {
        NotNull.throwExceptionIfNull(
                "A null value was passed to the tickUntil method! " +
                        "Please make sure you don't pass any null values.",
                shouldContinueRunning,
                isValid
        );

        double start = Time.ms();

        while (
                isActive() &&
                        shouldContinueRunning.get() &&
                        isValid.test(this)
        ) {
            double current = Time.ms();
            double elapsed = current - start;

            if (elapsed >= timeoutMs) {
                break;
            }

            tick();
        }

        return this;
    }

    /**
     * Use the {@code tickUntil} method to tick Pathfinder until the path
     * it's executing is finished.
     *
     * @param onCompletion a callback to be executed after Pathfinder finishes
     *                     whatever it's doing. This consumer accepts the
     *                     instance of Pathfinder that this method was
     *                     called from.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder andThen(Consumer<Pathfinder> onCompletion) {
        return andThen(
                onCompletion,
                Double.MAX_VALUE,
                () -> true
        );
    }

    /**
     * Use the {@code tickUntil} method to tick Pathfinder until the path
     * it's executing is finished (or a timeout is reached).
     *
     * @param onCompletion a callback to be executed after Pathfinder finishes
     *                     whatever it's doing. This consumer accepts the
     *                     instance of Pathfinder that this method was
     *                     called from.
     * @param timeoutMs    how long, in milliseconds, Pathfinder will continue
     *                     ticking (as a maximum). If the path finishes before
     *                     this time is reached, it'll stop as normal.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder andThen(Consumer<Pathfinder> onCompletion,
                              double timeoutMs) {
        return andThen(
                onCompletion,
                timeoutMs,
                () -> true
        );
    }

    /**
     * Use the {@code tickUntil} method to tick Pathfinder until the path
     * it's executing is finished (or the {@code shouldContinueRunning}
     * {@link Supplier} returns false).
     *
     * @param onCompletion          a callback to be executed after Pathfinder finishes
     *                              whatever it's doing. This consumer accepts the
     *                              instance of Pathfinder that this method was
     *                              called from.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder andThen(Consumer<Pathfinder> onCompletion,
                              Supplier<Boolean> shouldContinueRunning) {
        return andThen(
                onCompletion,
                Double.MAX_VALUE,
                shouldContinueRunning
        );
    }

    /**
     * Use the {@code tickUntil} method to tick Pathfinder until the path
     * it's executing is finished (or a timeout is reached, or the
     * {@code shouldContinueRunning} {@link Supplier} returns false).
     *
     * @param onCompletion          a callback to be executed after Pathfinder finishes
     *                              whatever it's doing. This consumer accepts the
     *                              instance of Pathfinder that this method was
     *                              called from.
     * @param timeoutMs             how long, in milliseconds, Pathfinder will continue
     *                              ticking (as a maximum). If the path finishes before
     *                              this time is reached, it'll stop as normal.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder andThen(Consumer<Pathfinder> onCompletion,
                              double timeoutMs,
                              Supplier<Boolean> shouldContinueRunning) {
        tickUntil(
                timeoutMs,
                shouldContinueRunning
        );

        onCompletion.accept(this);

        return this;
    }

    /**
     * Use the {@code tickUntil} method to tick Pathfinder until the path
     * it's executing is finished (or a timeout is reached, or the
     * {@code shouldContinueRunning} {@link Supplier} returns false).
     *
     * @param onCompletion          a callback to be executed after Pathfinder finishes
     *                              whatever it's doing. This consumer accepts the
     *                              instance of Pathfinder that this method was
     *                              called from.
     * @param timeoutMs             how long, in milliseconds, Pathfinder will continue
     *                              ticking (as a maximum). If the path finishes before
     *                              this time is reached, it'll stop as normal.
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @param onTick                a {@link Consumer} that will be called
     *                              once per tick.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder andThen(Consumer<Pathfinder> onCompletion,
                              double timeoutMs,
                              Supplier<Boolean> shouldContinueRunning,
                              Consumer<Pathfinder> onTick) {
        tickUntil(
                timeoutMs,
                shouldContinueRunning,
                onTick
        );

        onCompletion.accept(this);

        return this;
    }

    /*
     * the waitUntil and waitAsLongAs methods have to use busy waiting
     * because JDK8 doesn't support the Thread#onSpinWait method, which
     * is really obnoxious, but oh well... I guess...
     */

    /**
     * Pause until a certain condition is met.
     *
     * @param condition the condition that must be met before continuing.
     * @param maxTime   the maximum length of the pause. If the amount of
     *                  elapsed time exceeds this length, the condition will
     *                  break and Pathfinder will be unpaused, regardless of
     *                  whether the condition has been met.
     * @return this instance of Pathfinder, used for method chaining.
     */
    @SuppressWarnings("BusyWait")
    public Pathfinder waitUntil(Supplier<Boolean> condition,
                                double maxTime) {
        if (condition == null) {
            throw new NullPointerException(
                    "Attempted to use the waitUntil method with a null " +
                            "condition supplier!"
            );
        }

        if (maxTime < 0) {
            throw new InvalidTimeException(
                    "Attempted to use an invalid time value! Make sure the " +
                            "time value you're supplying is 0 or greater."
            );
        }

        ElapsedTimer timer = new ElapsedTimer(true);

        try {
            while (!condition.get() && timer.isElapsedLessThan(maxTime)) {
                Thread.sleep(10);
            }
        } catch (InterruptedException ignored) {
        }

        return this;
    }

    /**
     * Pause as long as a certain condition is met.
     *
     * @param condition the condition that must be met in order to continue.
     *                  If this condition returns false, this method will
     *                  finish its execution and will unpause.
     * @param maxTime   the maximum length of the pause. If the amount of
     *                  elapsed time exceeds this length, the condition will
     *                  break and Pathfinder will be unpaused, regardless of
     *                  whether the condition has been met.
     * @return this instance of Pathfinder, used for method chaining.
     */
    @SuppressWarnings("BusyWait")
    public Pathfinder waitAsLongAs(Supplier<Boolean> condition,
                                   double maxTime) {
        if (condition == null) {
            throw new NullPointerException(
                    "Attempted to use the waitAsLongAs method with a null " +
                            "condition supplier!"
            );
        }

        if (maxTime < 0) {
            throw new InvalidTimeException(
                    "Attempted to use an invalid time value! Make sure the " +
                            "time value you're supplying is 0 or greater."
            );
        }

        ElapsedTimer timer = new ElapsedTimer(true);

        try {
            while (condition.get() && timer.isElapsedLessThan(maxTime)) {
                Thread.sleep(10);
            }
        } catch (InterruptedException ignored) {
        }

        return this;
    }

    /**
     * Follow a single trajectory.
     *
     * @param trajectory the trajectory to follow.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder followTrajectory(Trajectory trajectory) {
        if (trajectory == null)
            throw new NullPointerException("Cannot follow a null trajectory!");

        List<Trajectory> list = new ArrayList<Trajectory>(1) {{
            add(trajectory);
        }};

        followTrajectories(list);

        return this;
    }

    /**
     * Follow multiple trajectories.
     *
     * @param trajectories a list of trajectories to follow.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder followTrajectories(List<Trajectory> trajectories) {
        if (trajectories == null)
            throw new NullPointerException("Cannot follow null trajectories!");

        List<Follower> followers = new ArrayList<>();

        for (Trajectory trajectory : trajectories) {
            followers.add(generator.generate(
                    robot,
                    trajectory
            ));
        }

        follow(followers);

        return this;
    }

    /**
     * Follow a single follower.
     *
     * @param follower a single follower to follow.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder follow(Follower follower) {
        if (follower == null) {
            throw new NullPointerException(
                    "Attempted to follow a null Follower object - make sure " +
                            "this object is not null."
            );
        }

        manager.addExecutor(follower);

        return this;
    }

    /**
     * Follow a list of followers.
     *
     * @param followers a list of followers.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder follow(List<Follower> followers) {
        if (followers == null) {
            throw new NullPointerException(
                    "Attempted to follow a null list of Follower objects - " +
                            "make sure the list you supply is not null."
            );
        }

        manager.addExecutor(followers);

        return this;
    }

    /**
     * Go to a specific point. This method will create a new linear trajectory.
     *
     * @param point the target point to go to.
     * @return this instance of Pathfinder, used for method chaining.
     * @see #setSpeed(double)
     * @see #setTolerance(double)
     * @see #setAngleTolerance(Angle)
     */
    public Pathfinder goTo(PointXY point) {
        goTo(
                point.withHeading(
                        getOdometry().getZ()
                )
        );

        return this;
    }

    /**
     * Go to a specific point. This method will create a new linear trajectory.
     *
     * @param point the target point to go to.
     * @return this instance of Pathfinder, used for method chaining.
     * @see #setSpeed(double)
     * @see #setTolerance(double)
     * @see #setAngleTolerance(Angle)
     */
    public Pathfinder goTo(PointXYZ point) {
        NullPointException.throwIfInvalid(
                "Attempted to navigate to a null point.",
                point
        );

        // perform some wonderful exception checking...
        // this was a pretty big pain for me personally because I entirely
        // forgot that you actually need to set these values, so these lovely
        // and very handy reminders should definitely help... I think...
        // side note, I'm really craving some vanilla ice cream right now,
        // but I don't think I have any :(

        if (speed < 0 && tolerance < 0 && angleTolerance == null) {
            throw new RuntimeException(
                    "Attempted to use the goTo method without having set " +
                            "Pathfinder's default speed, tolerance, and angle " +
                            "tolerance. Use the setSpeed(double), " +
                            "setTolerance(double), and setAngleTolerance(Angle) " +
                            "methods to set these values before using any  " +
                            "variation of the goTo method."
            );
        }

        if (speed < 0) {
            throw new InvalidSpeedException(
                    "Attempted to use the goTo method without having set the " +
                            "speed of Pathfinder first! Use the setSpeed(double) " +
                            "method to set a speed value."
            );
        }

        if (tolerance < 0) {
            throw new InvalidToleranceException(
                    "Attempted to use the goTo method without having set the " +
                            "tolerance of Pathfinder first! Use the setTolerance(double) " +
                            "method to set a tolerance value."
            );
        }

        if (angleTolerance == null) {
            throw new NullAngleException(
                    "Attempted to use the goTo method without having set the " +
                            "angle tolerance of Pathfinder first! Use the" +
                            "setAngleTolerance(Angle) method to set a " +
                            "tolerance value."
            );
        }

        followTrajectory(new LinearTrajectory(
                point,
                speed,
                tolerance,
                angleTolerance
        ));

        return this;
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
     *
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder clear() {
        manager.clearExecutors();

        return this;
    }

    /**
     * Use the {@link #getOdometry()} method in combination with the
     * odometry class' {@link Odometry#getPosition()} to access the
     * robot's current position.
     *
     * @return the robot's current position.
     */
    public PointXYZ getPosition() {
        return getOdometry().getPosition();
    }

    /**
     * Get the robot's raw position.
     *
     * @return the robot's raw position.
     */
    public PointXYZ getRawPosition() {
        return getOdometry().getRawPosition();
    }

    /**
     * Get the robot's current translation.
     *
     * @return the robot's current translation.
     */
    public Translation getTranslation() {
        return getDrive().getTranslation();
    }

    /**
     * Get the X component of the robot's translation.
     *
     * @return the X component of the robot's translation.
     */
    public double getVx() {
        return getTranslation().vx();
    }

    /**
     * Set the X component of the robot's translation.
     *
     * <p>
     * This will update the robot's translation by copying over unchanged
     * values and reassigning the changed value.
     * </p>
     *
     * @param vx the X component of the robot's translation.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder setVx(double vx) {
        return setTranslation(
                new Translation(
                        vx,
                        getVy(),
                        getVz()
                )
        );
    }

    /**
     * Get the Y component of the robot's translation.
     *
     * @return the Y component of the robot's translation.
     */
    public double getVy() {
        return getTranslation().vy();
    }

    /**
     * Set the Y component of the robot's translation.
     *
     * <p>
     * This will update the robot's translation by copying over unchanged
     * values and reassigning the changed value.
     * </p>
     *
     * @param vy the Y component of the robot's translation.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder setVy(double vy) {
        return setTranslation(
                new Translation(
                        getVx(),
                        vy,
                        getVz()
                )
        );
    }

    /**
     * Get the Z component of the robot's translation.
     *
     * @return the Z component of the robot's translation.
     */
    public double getVz() {
        return getTranslation().vz();
    }

    /**
     * Set the Z component of the robot's translation.
     *
     * <p>
     * This will update the robot's translation by copying over unchanged
     * values and reassigning the changed value.
     * </p>
     *
     * @param vz the Z component of the robot's translation.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder setVz(double vz) {
        return setTranslation(
                new Translation(
                        getVx(),
                        getVy(),
                        vz
                )
        );
    }

    /**
     * Set a translation to the robot. This is how to manually move your robot.
     * If, for example, you're in TeleOp, and you'd like to drive your robot
     * according to some joystick inputs, this is the method you should use.
     *
     * <p>
     * Calling this method will immediately update the robot's translation.
     * However, if the robot is still under the control of a trajectory
     * or follower or executor, the translation you set will have next to
     * no effect.
     * </p>
     *
     * @param translation the translation to set to the robot. This translation
     *                    should be RELATIVE, meaning forwards is forwards for
     *                    the robot, not forwards relative to you.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder setTranslation(Translation translation) {
        if (translation == null) {
            throw new NullPointerException(
                    "Attempted to use the setTranslation method, but provided " +
                            "a null translation - make sure this translation " +
                            "isn't null next time, alright? Cool."
            );
        }

        getDrive().setTranslation(translation);

        return this;
    }

    /**
     * Get how long the current follower has been executing. If no followers
     * have executed, this will return 0. If no followers are active, but
     * a follower has been active in the past, this will return the execution
     * time of the last follower.
     *
     * @return the execution time of the current follower.
     */
    public double getExecutionTime() {
        return getExecutorManager().getExecutionTime();
    }

    /*
     * I'm not entirely sure why you'd ever need any of these methods (even
     * the toString method is rather pointless) but I'm putting them here
     * anyways. so epic, right? so epic.
     */

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Convert this instance of {@code Pathfinder} into a {@code String}.
     * Really simply, this just return's the current position.
     *
     * @return the current position, as a string.
     */
    @Override
    public String toString() {
        return getPosition().toString();
    }
}
