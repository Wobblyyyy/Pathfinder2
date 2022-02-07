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

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.exceptions.InvalidSpeedException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidTimeException;
import me.wobblyyyy.pathfinder2.exceptions.InvalidToleranceException;
import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.execution.ExecutorManager;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.follower.generators.GenericFollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.listening.Listener;
import me.wobblyyyy.pathfinder2.listening.ListenerManager;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.math.Velocity;
import me.wobblyyyy.pathfinder2.movement.MovementProfiler;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPlugin;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPluginManager;
import me.wobblyyyy.pathfinder2.plugin.bundled.PositionLocker;
import me.wobblyyyy.pathfinder2.plugin.bundled.StatTracker;
import me.wobblyyyy.pathfinder2.prebuilt.AutoRotator;
import me.wobblyyyy.pathfinder2.prebuilt.HeadingLock;
import me.wobblyyyy.pathfinder2.recording.MovementPlayback;
import me.wobblyyyy.pathfinder2.recording.MovementRecorder;
import me.wobblyyyy.pathfinder2.recording.MovementRecording;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.modifiers.Modifier;
import me.wobblyyyy.pathfinder2.robot.simulated.EmptyDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.EmptyOdometry;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.scheduler.Scheduler;
import me.wobblyyyy.pathfinder2.scheduler.Task;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import me.wobblyyyy.pathfinder2.time.Stopwatch;
import me.wobblyyyy.pathfinder2.time.Time;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.AdvancedSplineTrajectoryBuilder;
import me.wobblyyyy.pathfinder2.utils.NotNull;
import me.wobblyyyy.pathfinder2.utils.RandomString;
import me.wobblyyyy.pathfinder2.zones.Zone;
import me.wobblyyyy.pathfinder2.zones.ZoneProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The highest-level interface used for interacting with {@code Pathfinder}.
 * This class is designed to give you complete (or near complete) control
 * over your robot's movement, whether autonomous or manually controlled.
 * There are a couple of key concepts you'll need to understand in order to
 * effectively make use of this class - namely, {@link Trajectory},
 * {@link Translation}, {@link PointXY}/{@link PointXYZ}, and {@link Angle}.
 * Additionally, the {@link #tick()} method is essential to operate Pathfinder.
 * I'd encourage you to go look at some documentation for the project to get
 * a decent idea of what's going on, but hey, that's up to you. Good luck...
 * I guess? Maybe? Yeah.
 *
 * <p>
 * This is absolutely a god class, but that's completely intentional, I swear.
 * Because Pathfinder fully supports method chaining, having a single object
 * ({@code Pathfinder}, in this case) with support for just about every
 * operation you could imagine makes method chaining incredibly easy and
 * effective. You can access smaller components of Pathfinder with some
 * getter methods:
 * <ul>
 *     <li>{@link #getDataMap()}</li>
 *     <li>{@link #getOdometry()}</li>
 *     <li>{@link #getDrive()}</li>
 *     <li>{@link #getExecutorManager()}</li>
 *     <li>{@link #getPluginManager()}</li>
 *     <li>{@link #getPlayback()}</li>
 *     <li>{@link #getRecorder()}</li>
 *     <li>{@link #getListenerManager()}</li>
 * </ul>
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
@SuppressWarnings({"UnusedReturnValue", "JavaDoc"})
public class Pathfinder {
    /**
     * A static list of plugins that should be automatically loaded every
     * time an instance of Pathfinder is created.
     */
    private static final List<PathfinderPlugin> AUTO_LOAD_PLUGINS =
            new ArrayList<>();

    /**
     * The {@code Robot} (made up of {@code Drive} and {@code Odometry}) that
     * Pathfinder operates.
     */
    private final Robot robot;

    /**
     * Pathfinder's executor manager.
     */
    private final ExecutorManager executorManager;

    /**
     * A generator used in converting trajectories into {@link Follower}s.
     */
    private final FollowerGenerator generator;

    /**
     * Turn controller, used for... controlling turns. What else would
     * it be used for, huh?
     */
    private final Controller turnController;

    /**
     * A stopwatch - this is very likely to not be useful at all, but its
     * included anyways.
     */
    private final Stopwatch stopwatch = new Stopwatch();

    /**
     * A zone processor is responsible for dealing with any zones on
     * the field.
     */
    private final ZoneProcessor zoneProcessor;

    /**
     * A scheduler for executing tasks.
     */
    private final Scheduler scheduler;

    /**
     * A manager for recording Pathfinder's movement.
     */
    private final MovementRecorder recorder;

    /**
     * A manager for playing back recordings.
     */
    private final MovementPlayback playback;

    /**
     * A manager for {@link PathfinderPlugin}s.
     */
    private final PathfinderPluginManager pluginManager;

    /**
     * Used in recording information about the robot's motion.
     */
    private final MovementProfiler profiler;

    /**
     * Used in event listeners.
     */
    private final ListenerManager listenerManager;

    /**
     * The speed Pathfinder will use in creating linear trajectories.
     *
     * <p>
     * This value defaults to -1.0 if it's not set by the user.
     * </p>
     */
    private double speed = -1.0;

    /**
     * The tolerance Pathfinder will use in creating linear trajectories.
     *
     * <p>
     * This value defaults to -1.0 if it's not set by the user.
     * </p>
     */
    private double tolerance = -1.0;

    /**
     * The angle tolerance Pathfinder will use in creating linear trajectories.
     *
     * <p>
     * This value defaults to null if it's not set by the user.
     * </p>
     */
    private Angle angleTolerance = null;

    /**
     * The default tick until timeout.
     *
     * <p>
     * This value defaults to {@link Double#MAX_VALUE} if it's not set by the user.
     * </p>
     */
    private double defaultTimeout = Double.MAX_VALUE;

    /**
     * The default tick until should run supplier.
     */
    private Supplier<Boolean> defaultShouldRun = () -> true;

    /**
     * The default tick until completion consumer.
     */
    private Consumer<Pathfinder> defaultOnCompletion = pathfinder -> {
    };

    /**
     * The default tick until on tick consumer.
     */
    private BiConsumer<Pathfinder, Double> defaultOnTick = (pathfinder, aDouble) -> {
    };

    /**
     * Last tick, how many followers were there?
     */
    private int previousFollowerCount = 0;

    /**
     * Last tick, what was the currently active follower?
     */
    private Follower previousFollower = null;

    /**
     * Last tick, what was the currently active drive modifier? Or
     * something like that.
     */
    private Modifier<Translation> lastDriveModifier = null;

    /**
     * A modifiable map of operations to be run after every tick.
     */
    private final Map<String, Consumer<Pathfinder>> onTickOperations;

    /**
     * A map that can be used to communicate between classes.
     */
    private final Map<String, Object> dataMap;

    private boolean isMinimal = false;

    /**
     * Create a new {@code Pathfinder} instance. This constructor will
     * conditionally load any automatically loading plugins - if the plugin's
     * name is not in the {@code doNotLoad} collection of strings, the
     * plugin will be loaded; if the plugin's name IS in the collection of
     * strings, the plugin will not be loaded.
     *
     * <p>
     * This constructor will instantiate instances of the following:
     * <ul>
     *     <li>{@link ExecutorManager}</li>
     *     <li>{@link ZoneProcessor}</li>
     *     <li>{@link Scheduler}</li>
     *     <li>{@link MovementRecorder}</li>
     *     <li>{@link MovementPlayback}</li>
     *     <li>{@link PathfinderPluginManager}</li>
     *     <li>{@link MovementProfiler}</li>
     *     <li>{@link ListenerManager}</li>
     * </ul>
     * There's a very good chance you're not going to need some or all of those,
     * and that's okay - you simply don't have to worry about them and everything
     * will work as intended.
     * </p>
     *
     * @param robot          the {@code Pathfinder} instance's robot. This robot
     *                       should have an odometry system that can report the
     *                       position of the robot and a drive system that can
     *                       respond to drive commands. This object may not be
     *                       null or, an exception will be thrown.
     * @param generator      a generator used in creating followers. This generator
     *                       functions by accepting a {@link Trajectory} and a
     *                       {@link Robot} and returning a follower. If you're
     *                       unsure of what this means, or what you should do here,
     *                       you should probably use the "generic follower
     *                       generator," as it's the simplest. This object may not
     *                       be null, or an exception will be thrown.
     * @param turnController the controller responsible for turning the robot.
     *                       This is some bad code on my part, but basically,
     *                       this constructor assumes that the generator provided
     *                       makes use of a controller for controlling the
     *                       robot's heading.
     * @param doNotLoad      a set of {@code String}s that specify to Pathfinder
     *                       which plugins it should NOT load. If any of the
     *                       plugins that attempt to automatically load are inside
     *                       of the {@code doNotLoad} list, they won't be loaded.
     *                       This is here in case you depend on a file which
     *                       modifies the list of plugins that are automatically
     *                       loaded whenever an instance of Pathfinder is created.
     *                       If you decide you don't want that plugin to be loaded,
     *                       add it's name to this list, and you should be all good.
     */
    public Pathfinder(Robot robot,
                      FollowerGenerator generator,
                      Controller turnController,
                      String... doNotLoad) {
        if (robot == null)
            throw new NullPointerException("Robot cannot be null!");
        if (generator == null)
            throw new NullPointerException("Follower generator cannot be null!");

        this.robot = robot;
        this.generator = generator;
        this.turnController = turnController;
        this.executorManager = new ExecutorManager(robot);
        this.zoneProcessor = new ZoneProcessor();
        this.scheduler = new Scheduler(this);
        this.recorder = new MovementRecorder(this, 25);
        this.playback = new MovementPlayback(this);
        this.pluginManager = new PathfinderPluginManager();
        this.profiler = new MovementProfiler();
        this.listenerManager = new ListenerManager(this);
        this.onTickOperations = new HashMap<>();
        this.dataMap = new HashMap<>();

        for (PathfinderPlugin plugin : AUTO_LOAD_PLUGINS) {
            String pluginName = plugin.getName();

            boolean shouldLoad = true;
            for (String str : doNotLoad)
                if (str.equals(pluginName)) {
                    shouldLoad = false;
                    break;
                }

            if (shouldLoad)
                loadPlugin(plugin);
        }
    }

    /**
     * Create a new {@code Pathfinder} instance.
     *
     * @param robot          the {@code Pathfinder} instance's robot. This robot
     *                       should have an odometry system that can report the
     *                       position of the robot and a drive system that can
     *                       respond to drive commands. This object may not be
     *                       null or, an exception will be thrown.
     * @param generator      a generator used in creating followers. This generator
     *                       functions by accepting a {@link Trajectory} and a
     *                       {@link Robot} and returning a follower. If you're
     *                       unsure of what this means, or what you should do here,
     *                       you should probably use the "generic follower
     *                       generator," as it's the simplest. This object may not
     *                       be null, or an exception will be thrown.
     * @param turnController the controller responsible for turning the robot.
     *                       This is some bad code on my part, but basically,
     *                       this constructor assumes that the generator provided
     *                       makes use of a controller for controlling the
     *                       robot's heading.
     */
    public Pathfinder(Robot robot,
                      FollowerGenerator generator,
                      Controller turnController) {
        this(
                robot,
                generator,
                turnController,
                new String[0]
        );
    }

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
        this(
                robot,
                generator,
                extractController(generator),
                new String[0]
        );
    }

    /**
     * Create a new {@code Pathfinder} instance.
     *
     * <p>
     * This constructor will create a new {@link GenericFollowerGenerator}
     * by using the provided {@link Controller} as a turn controller. That's...
     * well, that's pretty much it.
     * </p>
     *
     * @param robot          the {@code Pathfinder} instance's robot. This robot
     *                       should have an odometry system that can report the
     *                       position of the robot and a drive system that can
     *                       respond to drive commands. This object may not be
     *                       null or, an exception will be thrown.
     * @param turnController the controller responsible for turning the robot.
     *                       This is some bad code on my part, but basically,
     *                       this constructor assumes that the generator provided
     *                       makes use of a controller for controlling the
     *                       robot's heading.
     */
    public Pathfinder(Robot robot,
                      Controller turnController) {
        this(
                robot,
                new GenericFollowerGenerator(turnController),
                turnController
        );
    }

    /**
     * Create a new {@code Pathfinder} instance.
     *
     * @param robot       the {@code Pathfinder} instance's robot. This robot
     *                    should have an odometry system that can report the
     *                    position of the robot and a drive system that can
     *                    respond to drive commands. This object may not be
     *                    null or, an exception will be thrown.
     * @param coefficient the coefficient used for the turn controller.
     */
    public Pathfinder(Robot robot,
                      double coefficient) {
        this(
                robot,
                new ProportionalController(coefficient)
        );
    }

    private static Controller extractController(FollowerGenerator generator) {
        if (generator instanceof GenericFollowerGenerator) {
            GenericFollowerGenerator gfg = (GenericFollowerGenerator) generator;
            return gfg.getTurnController();
        }

        return null;
    }

    /**
     * Set Pathfinder's {@code isMinimal} status.
     *
     * @param isMinimal should Pathfinder run in minimal mode, which is
     *                  designed to reduce performance impact by skipping
     *                  un-needed operations?
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder setIsMinimal(boolean isMinimal) {
        this.isMinimal = isMinimal;

        return this;
    }

    /**
     * Create a new, "simulated" instance of Pathfinder.
     *
     * <p>
     * This is pretty much only useful for debugging or testing purposes.
     * </p>
     *
     * @param coefficient the coefficient to use for the turn controller.
     * @return a new instance of Pathfinder, having a {@link Drive} of
     * {@link SimulatedDrive} and {@link Odometry} of {@link SimulatedOdometry}.
     */
    public static Pathfinder newSimulatedPathfinder(double coefficient) {
        Drive drive = new SimulatedDrive();
        Odometry odometry = new SimulatedOdometry();
        Robot robot = new Robot(drive, odometry);

        return new Pathfinder(robot, coefficient);
    }

    /**
     * Create a new, "empty" instance of Pathfinder.
     *
     * <p>
     * This is pretty much only useful for debugging or testing purposes.
     * </p>
     *
     * @param coefficient the coefficient to use for the turn controller.
     * @return a new instance of Pathfinder that makes use of both the
     * {@link EmptyDrive} and {@link EmptyOdometry} classes.
     */
    public static Pathfinder newEmptyPathfinder(double coefficient) {
        Drive drive = new EmptyDrive();
        Odometry odometry = new EmptyOdometry();
        Robot robot = new Robot(drive, odometry);

        return new Pathfinder(robot, coefficient);
    }

    public static void addAutoLoadPlugin(PathfinderPlugin plugin) {
        AUTO_LOAD_PLUGINS.add(plugin);
    }

    /**
     * Get Pathfinder's data map.
     *
     * @return Pathfinder's data map.
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    /**
     * Add data to Pathfinder's data map.
     *
     * @param key    the key for the data.j
     * @param object the data to add.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder putData(String key,
                              Object object) {
        dataMap.put(key, object);

        return this;
    }

    /**
     * Get data from Pathfinder's data map.
     *
     * @param key the key that corresponds with the data being accessed.
     * @return if the data map contains the requested key, return the
     * associated value. If the data map does not contain the requested key,
     * return null.
     */
    public Object getData(String key) {
        return dataMap.get(key);
    }

    /**
     * Get data from Pathfinder's data map and cast it to a specific
     * data type.
     *
     * @param key the key that corresponds with the data being accessed.
     * @return a casted result of a {@link #getData(String)} operation.
     */
    public PointXY getDataPointXY(String key) {
        return (PointXY) getData(key);
    }

    /**
     * Get data from Pathfinder's data map and cast it to a specific
     * data type.
     *
     * @param key the key that corresponds with the data being accessed.
     * @return a casted result of a {@link #getData(String)} operation.
     */
    public PointXYZ getDataPointXYZ(String key) {
        return (PointXYZ) getData(key);
    }

    /**
     * Get data from Pathfinder's data map and cast it to a specific
     * data type.
     *
     * @param key the key that corresponds with the data being accessed.
     * @return a casted result of a {@link #getData(String)} operation.
     */
    public Angle getDataAngle(String key) {
        return (Angle) getData(key);
    }

    /**
     * Get data from Pathfinder's data map and cast it to a specific
     * data type.
     *
     * @param key the key that corresponds with the data being accessed.
     * @return a casted result of a {@link #getData(String)} operation.
     */
    public String getDataString(String key) {
        return (String) getData(key);
    }

    /**
     * Load a {@link PathfinderPlugin}.
     *
     * @param plugin the plugin to load.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder loadPlugin(PathfinderPlugin plugin) {
        pluginManager.loadPlugin(plugin);
        plugin.onLoad(this);

        return this;
    }

    /**
     * Load all of the plugins bundled with Pathfinder.
     *
     * The plugins that will be loaded are (in order):
     * <ul>
     *     <li>{@link StatTracker}</li>
     *     <li>{@link PositionLocker}</li>
     * </ul>
     *
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder loadBundledPlugins() {
        pluginManager.loadPlugin(new StatTracker());
        pluginManager.loadPlugin(new PositionLocker());

        return this;
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
     * Get the {@code Pathfinder} instance's {@link MovementProfiler}.
     *
     * @return the {@link MovementProfiler}.
     */
    public MovementProfiler getProfiler() {
        return profiler;
    }

    /**
     * Get the {@code Pathfinder} instance's {@link ListenerManager}.
     *
     * @return the {@link ListenerManager}.
     */
    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    /**
     * Add a listener to the listener manager.
     *
     * @param name     the name of the listener. This can usually just be
     *                 completely random, unless there's a need for it to
     *                 be something specific.
     * @param listener the listener that will be added.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder addListener(String name,
                                  Listener listener) {
        listenerManager.addListener(name, listener);

        return this;
    }

    /**
     * Add a listener to the listener manager.
     *
     * @param listener the listener that will be added.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder addListener(Listener listener) {
        return addListener(
                RandomString.randomString(10),
                listener
        );
    }

    /**
     * Add a listener.
     *
     * @param condition the condition that must be true in order for the
     *                  listener to be executed.
     * @param action    a piece of functionality to be executed whenever the
     *                  condition is met.
     * @return {@code this}, used for method chaining.
     */
    @SuppressWarnings("unchecked")
    public Pathfinder addListener(Predicate<Pathfinder> condition,
                                  Runnable action) {
        Pathfinder pathfinder = this;

        return addListener(
                new Listener(
                        ListenerMode.CONDITION_IS_MET,
                        action,
                        () -> condition.test(pathfinder)
                )
        );
    }

    public Pathfinder addListener(ListenerMode mode,
                                  Runnable runnable,
                                  Supplier<Boolean>... suppliers) {
        return addListener(new Listener(mode, runnable, suppliers));
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
     * Get the default timeout.
     *
     * @return the default timeout.
     */
    public double getDefaultTimeout() {
        return this.defaultTimeout;
    }

    /**
     * Set the default timeout.
     *
     * @param defaultTimeout the default timeout.
     * @return this, used for method chaining.
     */
    public Pathfinder setDefaultTimeout(double defaultTimeout) {
        this.defaultTimeout = defaultTimeout;

        return this;
    }

    /**
     * Get the default "should run" supplier.
     *
     * @return the default "should run" supplier.
     */
    public Supplier<Boolean> getDefaultShouldRun() {
        return this.defaultShouldRun;
    }

    /**
     * Set the default "should run" supplier.
     *
     * @param defaultShouldRun the default "should run" supplier.
     * @return this, used for method chaining.
     */
    public Pathfinder setDefaultShouldRun(Supplier<Boolean> defaultShouldRun) {
        this.defaultShouldRun = defaultShouldRun;

        return this;
    }

    /**
     * Get the default "on completion" consumer.
     *
     * @return the default "on completion" consumer.
     */
    public Consumer<Pathfinder> getDefaultOnCompletion() {
        return this.defaultOnCompletion;
    }

    /**
     * Set the default "on completion" consumer.
     *
     * @param defaultOnCompletion the default "on completion" consumer.
     * @return this, used for method chaining.
     */
    public Pathfinder setDefaultOnCompletion(Consumer<Pathfinder> defaultOnCompletion) {
        this.defaultOnCompletion = defaultOnCompletion;

        return this;
    }

    /**
     * Get the default "on tick" consumer.
     *
     * @return the default "on tick" consumer.
     */
    public BiConsumer<Pathfinder, Double> getDefaultOnTick() {
        return this.defaultOnTick;
    }

    /**
     * Set the default "on tick" consumer.
     *
     * @param defaultOnTick the default "on tick" consumer.
     * @return this, used for method chaining.
     */
    public Pathfinder setDefaultOnTick(BiConsumer<Pathfinder, Double> defaultOnTick) {
        this.defaultOnTick = defaultOnTick;

        return this;
    }

    /**
     * Get {@code this} instance of Pathfinder's {@link ZoneProcessor}.
     *
     * @return a {@link ZoneProcessor}.
     */
    public ZoneProcessor getZoneProcessor() {
        return zoneProcessor;
    }

    /**
     * Add a zone to collection of zones the processor is handling.
     *
     * @param name the name of the zone. This name will be needed if you're
     *             planning on removing the zone or referencing it at some
     *             point. This is also used for getting a list of names
     *             of zones.
     * @param zone the zone.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder addZone(String name,
                              Zone zone) {
        zoneProcessor.addZone(name, zone);

        return this;
    }

    /**
     * Remove a zone.
     *
     * @param name the zone's name.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder removeZone(String name) {
        zoneProcessor.removeZone(name);

        return this;
    }

    public double ticksPerSecond() {
        Object result = dataMap.get(StatTracker.KEY_TPS);

        if (result == null)
            throw new RuntimeException("tried to get ticks per second without " +
                    "having any valid entries - make sure you load the " +
                    "StatTracker plugin!");
        else
            if (Double.isInfinite((Double) result))
                throw new RuntimeException("infinite tick rate!");
            else
                return (Double) result;
    }

    /**
     * Get Pathfinder's {@code Scheduler}.
     *
     * @return a {@link Scheduler}.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Queue a single task.
     *
     * @param task the task the scheduler should execute.
     * @see Scheduler
     */
    public Pathfinder queueTask(Task task) {
        scheduler.queueTask(task);

        return this;
    }

    /**
     * Queue an array of tasks.
     *
     * @param tasks the array of tasks the scheduler should execute.
     * @see Scheduler
     */
    public Pathfinder queueTasks(Task... tasks) {
        scheduler.queueTasks(tasks);

        return this;
    }

    /**
     * Queue a list of tasks.
     *
     * @param tasks the list of tasks the scheduler should execute.
     * @see Scheduler
     */
    public Pathfinder queueTasks(List<Task> tasks) {
        scheduler.queueTasks(tasks);

        return this;
    }

    /**
     * Stop the {@code Scheduler} from doing anything at all. If the scheduler
     * is currently automatically ticking, this will stop it from doing
     * so.
     */
    public Pathfinder clearTasks() {
        scheduler.clear();

        return this;
    }

    /**
     * Get Pathfinder's movement recorder.
     *
     * <ul>
     *     <li>
     *         Start recording by getting Pathfinder's recorder and using the
     *         {@link MovementRecorder#start()} method. This will reset the current
     *         recording (so if you recorded something and then want to start over,
     *         this is how you would do that) and set the
     *         {@code isRecording} boolean to true. While this is true, whenever
     *         you call {@link Pathfinder#tick()}, the recorder will record
     *         information on Pathfinder's current movement.
     *     </li>
     *     <li>
     *         Once you've finished recording, use the {@link MovementRecorder#stop()}
     *         method to stop recording information. You can access this recorded
     *         data by using the {@link MovementRecorder#getRecording()} method.
     *     </li>
     *     <li>
     *         Now, to play back movement, it's pretty simple. You just use the
     *         {@link MovementPlayback#startPlayback(MovementRecording)} method
     *         to start the playback, and then use {@link Pathfinder#tick()} to
     *         continue playing the movement back.
     *     </li>
     *     <li>
     *         Because everything is done on a single thread, it's quite easy
     *         to stop or start recording, and you won't have any issues with
     *         doing just that.
     *     </li>
     * </ul>
     *
     * @return Pathfinder's movement recorder.
     */
    public MovementRecorder getRecorder() {
        return recorder;
    }

    /**
     * Get Pathfinder's movement playback manager.
     *
     * <ul>
     *     <li>
     *         Start recording by getting Pathfinder's recorder and using the
     *         {@link MovementRecorder#start()} method. This will reset the current
     *         recording (so if you recorded something and then want to start over,
     *         this is how you would do that) and set the
     *         {@code isRecording} boolean to true. While this is true, whenever
     *         you call {@link Pathfinder#tick()}, the recorder will record
     *         information on Pathfinder's current movement.
     *     </li>
     *     <li>
     *         Once you've finished recording, use the {@link MovementRecorder#stop()}
     *         method to stop recording information. You can access this recorded
     *         data by using the {@link MovementRecorder#getRecording()} method.
     *     </li>
     *     <li>
     *         Now, to play back movement, it's pretty simple. You just use the
     *         {@link MovementPlayback#startPlayback(MovementRecording)} method
     *         to start the playback, and then use {@link Pathfinder#tick()} to
     *         continue playing the movement back.
     *     </li>
     *     <li>
     *         Because everything is done on a single thread, it's quite easy
     *         to stop or start recording, and you won't have any issues with
     *         doing just that.
     *     </li>
     * </ul>
     *
     * @return Pathfinder's movement playback manager.
     */
    public MovementPlayback getPlayback() {
        return playback;
    }

    /**
     * Get Pathfinder's plugin manager. This manager can be used to load
     * and unload plugins, allowing you to customize Pathfinder's inner
     * workings to your likings.
     *
     * @return Pathfinder's plugin manager.
     */
    public PathfinderPluginManager getPluginManager() {
        return pluginManager;
    }

    private void runOnTickOperations() {
        for (Map.Entry<String, Consumer<Pathfinder>> entry : onTickOperations.entrySet()) {
            Consumer<Pathfinder> consumer = entry.getValue();

            consumer.accept(this);
        }
    }

    /**
     * Bind an operation to the invocation of Pathfinder's {@link #tick()}
     * method. Anything that's bound to Pathfinder's {@link #tick()} method
     * will be called at the end of the invocation of {@link #tick()}.
     *
     * @param name   the name of the on tick operation. This mostly exists
     *               just so you can later remove on tick operations with
     *               {@link #removeOnTick(String)}.
     * @param onTick an action to be executed whenever Pathfinder ticks. This
     *               will be executed right before the plugin post-tick stuff,
     *               meaning it's after everything else. This {@link Consumer}
     *               accepts an instance of {@link Pathfinder}, which will
     *               always be the instance that was just ticked.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder onTick(String name,
                             Consumer<Pathfinder> onTick) {
        onTickOperations.put(name, onTick);

        return this;
    }

    /**
     * Bind an operation to the invocation of Pathfinder's {@link #tick()}
     * method. Anything that's bound to Pathfinder's {@link #tick()} method
     * will be called at the end of the invocation of {@link #tick()}.
     *
     * @param onTick an action to be executed whenever Pathfinder ticks. This
     *               will be executed right before the plugin post-tick stuff,
     *               meaning it's after everything else. This {@link Consumer}
     *               accepts an instance of {@link Pathfinder}, which will
     *               always be the instance that was just ticked.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder onTick(Consumer<Pathfinder> onTick) {
        return onTick(RandomString.randomString(10), onTick);
    }

    /**
     * Remove an on tick operation.
     *
     * @param name the name of the operation to remove.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder removeOnTick(String name) {
        onTickOperations.remove(name);

        return this;
    }

    /**
     * "Tick" Pathfinder once. This will tell Pathfinder's execution manager
     * to check to see what Pathfinder should be doing right now, and based
     * on that, move your robot. This method is required to operate Pathfinder
     * and should be run as frequently as possible. Not executing this method
     * will cause the library to not function at all.
     *
     * <p>
     * If the {@code tick()} method causes the invocation of an odometry
     * system's {@link Odometry#getPosition()} method, and that method updates
     * the robot's position based on the amount of elapsed time since the
     * last update, there can be issues. If {@code tick()} is called too
     * frequently, there may be inaccuracy in positional tracking if the
     * {@link Odometry#getPosition()} method is called too frequently.
     * </p>
     *
     * <p>
     * The order everything is ticked in is as follows:
     * <ol>
     *     <li>Plugin pre-tick ({@link PathfinderPluginManager#preTick(Pathfinder)})</li>
     *     <li>Scheduler ({@link #getScheduler()})</li>
     *     <li>Zone processor ({@link #getZoneProcessor()})</li>
     *     <li>Executor manager ({@link #getExecutorManager()})</li>
     *     <li>Playback manager ({@link #getPlayback()})</li>
     *     <li>Motion profiler ({@link #getProfiler()})</li>
     *     <li>Recording manager ({@link #getRecorder()})</li>
     *     <li>On tick operations ({@link #onTickOperations})</li>
     *     <li>Plugin post-tick ({@link PathfinderPluginManager#postTick(Pathfinder)})</li>
     * </ol>
     * </p>
     *
     * <p>
     * If you're curious about how many times the tick method is being called
     * per second, check out {@link #ticksPerSecond()}. This does require you
     * to use the {@link StatTracker} plugin, which can be loaded two ways:
     * <ul>
     *     <li>{@code pathfinder.loadPlugin(new StatTracker());}</li>
     *     <li>{@code pathfinder.loadBundledPlugins();}</li>
     * </ul>
     * </p>
     *
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tick() {
        pluginManager.preTick(this);
        if (!isMinimal) {
            scheduler.tick();
            zoneProcessor.update(this);
        }
        executorManager.tick();
        if (!isMinimal) {
            if (executorManager.howManyExecutors() > 0) {
                int followerCount = executorManager.howManyFollowers();
                Follower follower =
                        executorManager.getCurrentExecutor().getCurrentFollower();

                if (followerCount > previousFollowerCount)
                    pluginManager.onStartFollower(
                            this,
                            executorManager.getCurrentExecutor().getCurrentFollower()
                    );
                else if (followerCount < previousFollowerCount)
                    pluginManager.onFinishFollower(
                            this,
                            previousFollower
                    );

                previousFollowerCount = followerCount;
                previousFollower = follower;
            }
        }
        pluginManager.onTick(this);
        if (!isMinimal) {
            playback.tick();
            profiler.capture(getPosition());
            recorder.tick();
            listenerManager.tick(this);
            runOnTickOperations();
            pluginManager.postTick(this);
        }
        pluginManager.postTick(this);

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
     * Tick Pathfinder while the provided supplier returns true.
     *
     * @param shouldContinueRunning a supplier, indicating whether Pathfinder
     *                              should still continue running.
     * @param onTick                a {@link Consumer} that will be executed
     *                              after every successful tick.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder tickUntil(Supplier<Boolean> shouldContinueRunning,
                                Consumer<Pathfinder> onTick) {
        return tickUntil(
                Double.MAX_VALUE,
                shouldContinueRunning,
                onTick
        );
    }

    /**
     * Tick Pathfinder while the provided supplier returns true.
     *
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
    public Pathfinder tickUntil(Supplier<Boolean> shouldContinueRunning,
                                BiConsumer<Pathfinder, Double> onTick) {
        return tickUntil(
                Double.MAX_VALUE,
                shouldContinueRunning,
                onTick
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
        if (timeoutMs < 0)
            throw new InvalidTimeException(
                    "Attempted to use an invalid timeout time in in a call to " +
                            "the tickUntil method - make sure this time value " +
                            "is greater than or equal to 0."
            );

        if (shouldContinueRunning == null)
            throw new NullPointerException(
                    "Attempted to use a null supplier with the tickUntil " +
                            "method - this can't be null, nerd."
            );

        if (onTick == null)
            throw new NullPointerException(
                    "Attempted to use a null consumer with the tickUntil " +
                            "method. This also can't be null, nerd."
            );

        double start = Time.ms();

        while (isActive() && shouldContinueRunning.get()) {
            double current = Time.ms();
            double elapsed = current - start;

            if (elapsed >= timeoutMs)
                break;

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
                shouldContinueRunning,
                onTick
        );

        double start = Time.ms();

        while (isActive() && shouldContinueRunning.get()) {
            double current = Time.ms();
            double elapsed = current - start;

            if (elapsed >= timeoutMs)
                break;

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

            if (elapsed >= timeoutMs)
                break;

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

            if (elapsed >= timeoutMs)
                break;

            tick();
        }

        return this;
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil() {
        return tickUntil(
                defaultTimeout,
                defaultShouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param timeoutMs the maximum amount of time, in milliseconds, that
     *                  this can run for. After this amount of time has
     *                  elapsed, this method will finish its execution,
     *                  regardless of whether the path has been completed.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(double timeoutMs) {
        return tickUntil(
                timeoutMs,
                defaultShouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param shouldRun a supplier that indicates whether the method should
     *                  continue executing. If this supplier returns false,
     *                  the method will finish executing.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(Supplier<Boolean> shouldRun) {
        return tickUntil(
                defaultTimeout,
                shouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param onTick code that should be run once per tick. This consumer
     *               accepts {@code this} instance of Pathfinder, as well
     *               as a {@code Double} value representing the elapsed
     *               time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(BiConsumer<Pathfinder, Double> onTick) {
        return tickUntil(
                defaultTimeout,
                defaultShouldRun,
                onTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param timeoutMs the maximum amount of time, in milliseconds, that
     *                  this can run for. After this amount of time has
     *                  elapsed, this method will finish its execution,
     *                  regardless of whether the path has been completed.
     * @param shouldRun a supplier that indicates whether the method should
     *                  continue executing. If this supplier returns false,
     *                  the method will finish executing.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(double timeoutMs,
                                       Supplier<Boolean> shouldRun) {
        return tickUntil(
                timeoutMs,
                shouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param timeoutMs the maximum amount of time, in milliseconds, that
     *                  this can run for. After this amount of time has
     *                  elapsed, this method will finish its execution,
     *                  regardless of whether the path has been completed.
     * @param onTick    code that should be run once per tick. This consumer
     *                  accepts {@code this} instance of Pathfinder, as well
     *                  as a {@code Double} value representing the elapsed
     *                  time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(double timeoutMs,
                                       BiConsumer<Pathfinder, Double> onTick) {
        return tickUntil(
                timeoutMs,
                defaultShouldRun,
                onTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param shouldRun a supplier that indicates whether the method should
     *                  continue executing. If this supplier returns false,
     *                  the method will finish executing.
     * @param onTick    code that should be run once per tick. This consumer
     *                  accepts {@code this} instance of Pathfinder, as well
     *                  as a {@code Double} value representing the elapsed
     *                  time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(Supplier<Boolean> shouldRun,
                                       BiConsumer<Pathfinder, Double> onTick) {
        return tickUntil(
                defaultTimeout,
                shouldRun,
                onTick
        );
    }

    /**
     * Use the {@link #tickUntil(double, Supplier, BiConsumer)} method with
     * default values.
     *
     * @param timeoutMs the maximum amount of time, in milliseconds, that
     *                  this can run for. After this amount of time has
     *                  elapsed, this method will finish its execution,
     *                  regardless of whether the path has been completed.
     * @param shouldRun a supplier that indicates whether the method should
     *                  continue executing. If this supplier returns false,
     *                  the method will finish executing.
     * @param onTick    code that should be run once per tick. This consumer
     *                  accepts {@code this} instance of Pathfinder, as well
     *                  as a {@code Double} value representing the elapsed
     *                  time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultTickUntil(double timeoutMs,
                                       Supplier<Boolean> shouldRun,
                                       BiConsumer<Pathfinder, Double> onTick) {
        return tickUntil(
                timeoutMs,
                shouldRun,
                onTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen() {
        return andThen(
                defaultOnCompletion,
                defaultTimeout,
                defaultShouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param timeoutMs the maximum amount of time, in milliseconds, that
     *                  this can run for. After this amount of time has
     *                  elapsed, this method will finish its execution,
     *                  regardless of whether the path has been completed.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(double timeoutMs) {
        return andThen(
                defaultOnCompletion,
                timeoutMs,
                defaultShouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param onCompletion code that should be executed upon the completion.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(Consumer<Pathfinder> onCompletion) {
        return andThen(
                onCompletion,
                defaultTimeout,
                defaultShouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param shouldRun a supplier that indicates whether the method should
     *                  continue executing. If this supplier returns false,
     *                  the method will finish executing.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(Supplier<Boolean> shouldRun) {
        return andThen(
                defaultOnCompletion,
                defaultTimeout,
                shouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param onTick code that should be run once per tick. This consumer
     *               accepts {@code this} instance of Pathfinder, as well
     *               as a {@code Double} value representing the elapsed
     *               time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(BiConsumer<Pathfinder, Double> onTick) {
        return andThen(
                defaultOnCompletion,
                defaultTimeout,
                defaultShouldRun,
                onTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param onCompletion code that should be executed upon the completion.
     * @param timeoutMs    the maximum amount of time, in milliseconds, that
     *                     this can run for. After this amount of time has
     *                     elapsed, this method will finish its execution,
     *                     regardless of whether the path has been completed.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(Consumer<Pathfinder> onCompletion,
                                     double timeoutMs) {
        return andThen(
                onCompletion,
                timeoutMs,
                defaultShouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param onCompletion code that should be executed upon the completion.
     * @param shouldRun    a supplier that indicates whether the method should
     *                     continue executing. If this supplier returns false,
     *                     the method will finish executing.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(Consumer<Pathfinder> onCompletion,
                                     Supplier<Boolean> shouldRun) {
        return andThen(
                onCompletion,
                defaultTimeout,
                shouldRun,
                defaultOnTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param onCompletion code that should be executed upon the completion.
     * @param onTick       code that should be run once per tick. This consumer
     *                     accepts {@code this} instance of Pathfinder, as well
     *                     as a {@code Double} value representing the elapsed
     *                     time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(Consumer<Pathfinder> onCompletion,
                                     BiConsumer<Pathfinder, Double> onTick) {
        return andThen(
                onCompletion,
                defaultTimeout,
                defaultShouldRun,
                onTick
        );
    }

    /**
     * Use the {@link #andThen(Consumer, double, Supplier, BiConsumer)} with
     * default values.
     *
     * @param onCompletion code that should be executed upon the completion.
     * @param timeoutMs    the maximum amount of time, in milliseconds, that
     *                     this can run for. After this amount of time has
     *                     elapsed, this method will finish its execution,
     *                     regardless of whether the path has been completed.
     * @param shouldRun    a supplier that indicates whether the method should
     *                     continue executing. If this supplier returns false,
     *                     the method will finish executing.
     * @param onTick       code that should be run once per tick. This consumer
     *                     accepts {@code this} instance of Pathfinder, as well
     *                     as a {@code Double} value representing the elapsed
     *                     time, in milliseconds.
     * @return {@code this}, used for method chaining.
     * @see #setDefaultOnCompletion(Consumer)
     * @see #setDefaultTimeout(double)
     * @see #setDefaultShouldRun(Supplier)
     * @see #setDefaultOnTick(BiConsumer)
     */
    public Pathfinder defaultAndThen(Consumer<Pathfinder> onCompletion,
                                     double timeoutMs,
                                     Supplier<Boolean> shouldRun,
                                     BiConsumer<Pathfinder, Double> onTick) {
        return andThen(
                onCompletion,
                timeoutMs,
                shouldRun,
                onTick
        );
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
                              BiConsumer<Pathfinder, Double> onTick) {
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
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder waitUntil(Supplier<Boolean> condition) {
        return waitUntil(condition, Double.MAX_VALUE);
    }

    /**
     * Pause for a certain amount of time.
     *
     * @param timeoutMs how long it should wait.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder waitUntil(double timeoutMs) {
        return waitUntil(() -> true, timeoutMs);
    }

    /**
     * Pause until a certain condition is met.
     *
     * @param condition the condition that must be met before continuing.
     * @param maxTimeMs   the maximum length of the pause. If the amount of
     *                  elapsed time exceeds this length, the condition will
     *                  break and Pathfinder will be unpaused, regardless of
     *                  whether the condition has been met.
     * @return this instance of Pathfinder, used for method chaining.
     */
    @SuppressWarnings("BusyWait")
    public Pathfinder waitUntil(Supplier<Boolean> condition,
                                double maxTimeMs) {
        if (condition == null)
            throw new NullPointerException(
                    "Attempted to use the waitUntil method with a null " +
                            "condition supplier!"
            );

        if (maxTimeMs < 0)
            throw new InvalidTimeException(
                    "Attempted to use an invalid time value! Make sure the " +
                            "time value you're supplying is 0 or greater."
            );

        ElapsedTimer timer = new ElapsedTimer(true);

        try {
            while (!condition.get() && timer.isElapsedLessThan(maxTimeMs)) {
                tick();
                Thread.sleep(10);
            }
        } catch (InterruptedException ignored) {
        }

        return this;
    }

    /**
     * Pause as long as a certain condition is met. This method requires
     * doing just that the use of multiple threads, as this method has a
     * busy wait that will block the calling thread until {@code condition}'s
     * {@code get} returns false.
     *
     * @param condition the condition that must be met in order to continue.
     *                  If this condition returns false, this method will
     *                  finish its execution and will unpause.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder waitAsLongAs(Supplier<Boolean> condition) {
        return waitAsLongAs(condition, Double.MAX_VALUE);
    }

    /**
     * Pause as long as a certain condition is met. This method requires
     * doing just that the use of multiple threads, as this method has a
     * busy wait that will block the calling thread until {@code condition}'s
     * {@code get} returns false.
     *
     * @param condition the condition that must be met in order to continue.
     *                  If this condition returns false, this method will
     *                  finish its execution and will unpause.
     * @param maxTime   the maximum length of the pause. If the amount of
     *                  elapsed time exceeds this length, the condition will
     *                  break and Pathfinder will be unpaused, regardless of
     *                  whether the condition has been met.
     * @return {@code this}, used for method chaining.
     */
    @SuppressWarnings("BusyWait")
    public Pathfinder waitAsLongAs(Supplier<Boolean> condition,
                                   double maxTime) {
        if (condition == null)
            throw new NullPointerException(
                    "Attempted to use the waitAsLongAs method with a null " +
                            "condition supplier!"
            );

        if (maxTime < 0)
            throw new InvalidTimeException(
                    "Attempted to use an invalid time value! Make sure the " +
                            "time value you're supplying is 0 or greater."
            );

        ElapsedTimer timer = new ElapsedTimer(true);

        try {
            while (condition.get() && timer.isElapsedLessThan(maxTime))
                Thread.sleep(10);
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
    public Pathfinder followTrajectories(Trajectory... trajectories) {
        return followTrajectories(Arrays.asList(trajectories));
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

        for (Trajectory trajectory : trajectories)
            followers.add(generator.generate(
                    robot,
                    trajectory
            ));

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
        if (follower == null)
            throw new NullPointerException(
                    "Attempted to follow a null Follower object - make sure " +
                            "this object is not null."
            );

        executorManager.addExecutor(follower);

        return this;
    }

    /**
     * Follow a list of followers.
     *
     * @param followers a list of followers.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder follow(List<Follower> followers) {
        if (followers == null)
            throw new NullPointerException(
                    "Attempted to follow a null list of Follower objects - " +
                            "make sure the list you supply is not null."
            );

        executorManager.addExecutor(followers);

        return this;
    }

    /**
     * Follow a list of followers.
     *
     * @param followers a list of followers.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder follow(Follower... followers) {
        if (followers == null)
            throw new NullPointerException(
                    "Attempted to follow a null list of Follower objects - " +
                            "make sure the list you supply is not null."
            );

        executorManager.addExecutor(Arrays.asList(followers));

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

    private void checkForMissingDefaultValues() {
        // perform some wonderful exception checking...
        // this was a pretty big pain for me personally because I entirely
        // forgot that you actually need to set these values, so these lovely
        // and very handy reminders should definitely help... I think...
        // side note, I'm really craving some vanilla ice cream right now,
        // but I don't think I have any :(

        if (speed < 0 && tolerance < 0 && angleTolerance == null)
            throw new RuntimeException(
                    "Attempted to use the goTo method without having set " +
                            "Pathfinder's default speed, tolerance, and angle " +
                            "tolerance. Use the setSpeed(double), " +
                            "setTolerance(double), and setAngleTolerance(Angle) " +
                            "methods to set these values before using any  " +
                            "variation of the goTo method."
            );

        if (speed < 0)
            throw new InvalidSpeedException(
                    "Attempted to use the goTo method without having set the " +
                            "speed of Pathfinder first! Use the setSpeed(double) " +
                            "method to set a speed value."
            );

        if (tolerance < 0)
            throw new InvalidToleranceException(
                    "Attempted to use the goTo method without having set the " +
                            "tolerance of Pathfinder first! Use the setTolerance(double) " +
                            "method to set a tolerance value."
            );

        if (angleTolerance == null)
            throw new NullAngleException(
                    "Attempted to use the goTo method without having set the " +
                            "angle tolerance of Pathfinder first! Use the" +
                            "setAngleTolerance(Angle) method to set a " +
                            "tolerance value."
            );
    }

    /**
     * Create a spline trajectory to a certain target point, and then follow
     * that aforementioned trajectory. This will use the default speed,
     * tolerance, and angle tolerance values.
     *
     * @param points a set of control points for the spline. This
     *               will automatically insert the robot's current
     *               position into this array. This array must have
     *               AT LEAST two points.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder splineTo(PointXYZ... points) {
        return splineTo(
                speed,
                tolerance,
                angleTolerance,
                points
        );
    }

    /**
     * Create a spline trajectory to a certain target point, and then follow
     * that aforementioned trajectory.
     *
     * @param speed          the speed at which the robot should move. This
     *                       is a constant value.
     * @param tolerance      the tolerance used for determining whether the
     *                       robot is at the target point.
     * @param angleTolerance same thing as {@code tolerance}, but for the
     *                       robot's angle.
     * @param points         a set of control points for the spline. This
     *                       will automatically insert the robot's current
     *                       position into this array. This array must have
     *                       AT LEAST two points.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder splineTo(double speed,
                               double tolerance,
                               Angle angleTolerance,
                               PointXYZ... points) {
        if (points.length < 2) throw new IllegalArgumentException(
                "At least two control points are required to use the " +
                        "splineTo method."
        );
        checkForMissingDefaultValues();

        double length = PointXY.distance(
                points[0],
                points[points.length - 1]
        );
        double step = length / 20;

        AdvancedSplineTrajectoryBuilder builder =
                new AdvancedSplineTrajectoryBuilder()
                        .setSpeed(speed)
                        .setTolerance(tolerance)
                        .setAngleTolerance(angleTolerance)
                        .setStep(step)
                        .add(getPosition());

        for (PointXYZ point : points) {
            if (point == null) throw new NullPointException(
                    "Cannot use the splineTo method with a null " +
                            "control point!"
            );

            builder.add(point);
        }

        Trajectory trajectory = builder.build();

        return followTrajectory(trajectory);
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

        checkForMissingDefaultValues();

        followTrajectory(new LinearTrajectory(
                point,
                speed,
                tolerance,
                angleTolerance
        ));

        return this;
    }

    /**
     * Move the robot in a certain direction for a certain amount of time.
     * This is a blocking method call, meaning it will block the current thread
     * until its execution has finished (whatever {@code timeoutMs} is).
     *
     * @param translation the translation that will be set to the robot.
     *                    This value may not be null.
     * @param timeoutMs   how long the robot should move for. This value is
     *                    represented in milliseconds and must be greater
     *                    than 0. This value may also not be infinite.
     * @return {@code this, used for method chaining}
     */
    @SuppressWarnings("BusyWait")
    public Pathfinder moveFor(Translation translation,
                              double timeoutMs) {
        if (translation == null)
            throw new NullPointerException(
                    "Cannot use a null translation!"
            );

        if (timeoutMs <= 0 || Double.isInfinite(timeoutMs) || timeoutMs == Double.MAX_VALUE)
            throw new IllegalArgumentException(
                    "Invalid timeout!"
            );

        ElapsedTimer timer = new ElapsedTimer(true);
        setTranslation(translation);

        try {
            while (timer.isElapsedLessThan(timeoutMs))
                Thread.sleep(10);
        } catch (Exception ignored) {
        }

        return this;
    }

    /**
     * Move the robot in a certain direction (specified by a translation)
     * for a specified amount of time, in milliseconds. This is a blocking
     * method call, meaning it will block the current thread until its
     * execution has finished (whatever {@code timeoutMs} is).
     *
     * @param vx        the vx component of the robot's translation.
     * @param vy        the vy component of the robot's translation.
     * @param vz        the vz component of the robot's translation.
     * @param timeoutMs how long the robot should move for, in milliseconds.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder moveFor(double vx,
                              double vy,
                              double vz,
                              double timeoutMs) {
        return moveFor(
                new Translation(vx, vy, vz),
                timeoutMs
        );
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
     * Is Pathfinder currently active? Pathfinder is considered to be active
     * if there is at least 1 active follower in Pathfinder's 
     * {@link ExecutorManager}. If there is 1 or more followers, this method
     * will return true. If there are not any followers, this method will
     * return false, indicating that Pathfinder is currently idle and is not
     * in the process of following a {@link Follower}.
     *
     * @return is Pathfinder currently active? This method will return true
     * if Pathfinder is active (meaning its currently following a path) and
     * will return false if Pathfinder is not active (meaning its idle).
     */
    public boolean isActive() {
        return executorManager.isActive();
    }

    /**
     * Get this instance of {@code Pathfinder}'s {@link ExecutorManager}.
     *
     * @return Pathfinder's executor manager.
     */
    public ExecutorManager getExecutorManager() {
        return executorManager;
    }

    /**
     * Clear the {@link ExecutorManager}, resetting just about everything.
     *
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder clear() {
        this.pluginManager.preClear(this);
        this.executorManager.clearExecutors();
        this.pluginManager.onClear(this);

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
        Translation translation = getDrive().getTranslation();

        return translation != null
                ? translation
                : Translation.ZERO;
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
     * no effect. As soon as the follower/trajectory/executor is ticked again
     * with the {@link #tick()} method, the translation will be set to whatever
     * the {@link Follower} says the translation should be.
     * </p>
     *
     * @param translation the translation to set to the robot. This translation
     *                    should be RELATIVE, meaning forwards is forwards for
     *                    the robot, not forwards relative to you.
     * @return this instance of Pathfinder, used for method chaining.
     */
    public Pathfinder setTranslation(Translation translation) {
        if (translation == null)
            throw new NullPointerException(
                    "Attempted to use the setTranslation method, but provided " +
                            "a null translation - make sure this translation " +
                            "isn't null next time, alright? Cool."
            );

        getDrive().setTranslation(translation);

        return this;
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

    /**
     * Get the robot's velocity according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's velocity, in units per second.
     */
    public Velocity getVelocity() {
        return profiler.getLastSnapshot().getVelocity();
    }

    /**
     * Get the robot's velocity according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's velocity, in units per second.
     */
    public double getVelocityXY() {
        return profiler.getLastSnapshot().getVelocityXY();
    }

    /**
     * Get the robot's X velocity according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's X velocity, in units per second.
     */
    public double getVelocityX() {
        return profiler.getLastSnapshot().getVelocityX();
    }

    /**
     * Get the robot's Y velocity according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's Y velocity, in units per second.
     */
    public double getVelocityY() {
        return profiler.getLastSnapshot().getVelocityY();
    }

    /**
     * Get the robot's Z velocity according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's Z velocity, in units per second. Because this is
     * an angle and not a degree or radian measure, I can't think of a better
     * term, but it's basically just angle per second.
     */
    public Angle getVelocityZ() {
        return profiler.getLastSnapshot().getVelocityZ();
    }

    /**
     * Get the robot's acceleration according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's acceleration, in units per second squared.
     */
    public double getAccelerationXY() {
        return profiler.getLastSnapshot().getAccelerationXY();
    }

    /**
     * Get the robot's X acceleration according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's X acceleration, in units per second squared.
     */
    public double getAccelerationX() {
        return profiler.getLastSnapshot().getAccelerationX();
    }

    /**
     * Get the robot's Y acceleration according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's Y acceleration, in units per second squared.
     */
    public double getAccelerationY() {
        return profiler.getLastSnapshot().getAccelerationY();
    }

    /**
     * Get the robot's Z acceleration according to the last motion snapshot. If
     * no motion snapshots have been taken, invoking this method will cause
     * a {@link NullPointerException}.
     *
     * <p>
     * Along with all other profiler-related methods, the return value of
     * this method is units per second. Because units are not specified
     * anywhere and are thus left up to the user, these units can be anything
     * at all - but the time will always remain in seconds.
     * </p>
     *
     * @return the robot's Z acceleration, in units per second squared.
     */
    public Angle getAccelerationZ() {
        return profiler.getLastSnapshot().getAccelerationZ();
    }

    /**
     * Lock Pathfinder's heading by using a modifier. Every time a translation
     * is set to the drive train, it will be modified so that it will turn
     * the robot towards the provided heading value.
     *
     * <p>
     * In order to reverse this effect, use {@link #unlockHeading()}.
     * </p>
     *
     * <p>
     * The following three methods CAN NOT be combined or you will encounter
     * some issues: (combined meaning nested)
     * <ul>
     *     <li>{@link #lockHeading(Angle)}</li>
     *     <li>{@link #lockHeading(PointXY)}</li>
     *     <li>{@link #lockHeading(PointXY, Angle)}</li>
     * </ul>
     * Basically, this is a technical limitation because of my laziness and
     * poorly written code. This might get fixed at some point in the near
     * future, but it's not a high priority.
     * </p>
     *
     * @param heading the heading Pathfinder should remain at.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder lockHeading(Angle heading) {
        Modifier<Translation> modifier = new HeadingLock(
                heading,
                turnController,
                this.getPosition()::z
        );

        lastDriveModifier = (Modifier<Translation>) getDrive().getModifier();

        getDrive().addModifier(modifier);

        return this;
    }

    /**
     * Lock Pathfinder's heading by making the robot face a given point. The
     * robot will always face directly towards the provided point.
     *
     * <p>
     * In order to reverse this effect, use {@link #unlockHeading()}.
     * </p>
     *
     * <p>
     * The following three methods CAN NOT be combined or you will encounter
     * some issues: (combined meaning nested)
     * <ul>
     *     <li>{@link #lockHeading(Angle)}</li>
     *     <li>{@link #lockHeading(PointXY)}</li>
     *     <li>{@link #lockHeading(PointXY, Angle)}</li>
     * </ul>
     * Basically, this is a technical limitation because of my laziness and
     * poorly written code. This might get fixed at some point in the near
     * future, but it's not a high priority.
     * </p>
     *
     * @param point the point to lock heading around.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder lockHeading(PointXY point) {
        Modifier<Translation> modifier = new AutoRotator(
                this,
                turnController,
                point,
                Angle.DEG_0,
                true
        );

        lastDriveModifier = (Modifier<Translation>) getDrive().getModifier();

        getDrive().addModifier(modifier);

        return this;
    }

    /**
     * Lock Pathfinder's heading by making the robot face a given point. The
     * robot will always face directly towards the provided point, plus
     * the provided angle offset.
     *
     * <p>
     * In order to reverse this effect, use {@link #unlockHeading()}.
     * </p>
     *
     * <p>
     * The following three methods CAN NOT be combined or you will encounter
     * some issues: (combined meaning nested)
     * <ul>
     *     <li>{@link #lockHeading(Angle)}</li>
     *     <li>{@link #lockHeading(PointXY)}</li>
     *     <li>{@link #lockHeading(PointXY, Angle)}</li>
     * </ul>
     * Basically, this is a technical limitation because of my laziness and
     * poorly written code. This might get fixed at some point in the near
     * future, but it's not a high priority.
     * </p>
     *
     * @param point       the point to lock heading around.
     * @param angleOffset the offset to be applied to the angle Pathfinder
     *                    determines it needs to face.
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder lockHeading(PointXY point,
                                  Angle angleOffset) {
        Modifier<Translation> modifier = new AutoRotator(
                this,
                turnController,
                point,
                angleOffset,
                true
        );

        lastDriveModifier = (Modifier<Translation>) getDrive().getModifier();

        getDrive().addModifier(modifier);

        return this;
    }

    /**
     * Unlock Pathfinder's heading.
     *
     * @return {@code this}, used for method chaining.
     */
    public Pathfinder unlockHeading() {
        getDrive().setModifier(lastDriveModifier);

        return this;
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
     * Really simply, this just return's the current position. To be
     * completely honest, I have absolutely no idea why anybody would ever
     * even want to do this, but I figured I'd include it anyways. No harm
     * in that, I guess... right?
     *
     * @return the current position, as a string.
     */
    @Override
    public String toString() {
        return getPosition().toString();
    }
}
