/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.time.Time;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A scheduler is an easy way to automate Pathfinder's operation.
 *
 * <p>
 * A scheduler is responsible for managing a single {@code Pathfinder}
 * instance. The scheduler can primarily be managed with a couple of
 * methods...
 * <ul>
 *     <li>
 *         {@link #tick()}<br>
 *         Tick the scheduler once. This will activate the next task if it
 *         can be activated. If there is already an activated task, this will
 *         check to see if the task has "expired" (if it shouldn't continue
 *         its execution) and if it has, it'll be removed from the task
 *         queue, meaning the next {@link #tick()} method call will
 *         activate a new task.
 *     </li>
 *     <li>
 *         {@link #queueTask(Task)}, {@link #queueTasks(Task...)}, {@link #queueTasks(List)}<br>
 *         Queue one or more tasks. These tasks can be controlled via the
 *         {@link #tick()} method.
 *     </li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Scheduler {
    /**
     * The {@link Pathfinder} instance the scheduler is controlling.
     */
    private final Pathfinder pathfinder;

    /**
     * The list of tasks the scheduler should execute.
     */
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Is the scheduler currently in automatic ticking mode?
     */
    private boolean isAutomaticallyTicking = false;

    /**
     * Create a new {@code Scheduler}.
     *
     * @param pathfinder the {@link Pathfinder} instance that the scheduler
     *                   will act upon.
     */
    public Scheduler(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    /**
     * Queue a single task.
     *
     * @param task the task the scheduler should execute.
     * @see Scheduler
     */
    public void queueTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Queue a list of tasks.
     *
     * @param tasks the list of tasks the scheduler should execute.
     * @see Scheduler
     */
    public void queueTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Queue an array of tasks.
     *
     * @param tasks the array of tasks the scheduler should execute.
     * @see Scheduler
     */
    public void queueTasks(Task... tasks) {
        queueTasks(Arrays.asList(tasks));
    }

    private void dequeueTask(Task task) {
        this.tasks.remove(task);
        this.pathfinder.clear();
    }

    /**
     * Tick the scheduler once.
     *
     * @see Scheduler
     */
    public void tick() {
        if (this.tasks.size() == 0) return;

        Task currentTask = this.tasks.get(0);

        double currentTimeMilliseconds = Time.ms();

        if (!currentTask.hasStarted()) {
            currentTask.start(currentTimeMilliseconds);

            this.pathfinder.followTrajectory(currentTask.getTrajectory());
        } else {
            if (!(Math.abs(currentTask.getMinTimeMilliseconds()) > 1_000_000)) {
                if (
                    !currentTask.isMinimumTimeLimitValid(
                        currentTimeMilliseconds
                    )
                ) {
                    this.pathfinder.tick();
                    return;
                }

                Trajectory currentTrajectory = currentTask.getTrajectory();
                PointXYZ currentRobotPosition =
                    this.pathfinder.getOdometry().getPosition();

                boolean isMaximumTimeInvalid = !currentTask.isMaximumTimeLimitValid(
                    currentTimeMilliseconds
                );
                boolean isTrajectoryCompleted = currentTrajectory.isDone(
                    currentRobotPosition
                );

                if (isMaximumTimeInvalid || isTrajectoryCompleted) {
                    this.dequeueTask(currentTask);
                } else {
                    this.pathfinder.tick();
                }
            }
        }
    }

    /**
     * Enable automatic ticking until the {@link #disableAutomaticTicking()}
     * method is called.
     *
     * @see #enableAutomaticTicking(Supplier)
     * @see #disableAutomaticTicking()
     */
    public void enableAutomaticTicking() {
        this.enableAutomaticTicking(() -> true);
    }

    /**
     * Enable automatic ticking until either the {@code shouldContinueTicking}
     * {@link Supplier} returns {@code false}, or the
     * {@link #disableAutomaticTicking()} method is called.
     *
     * <p>
     * <em>THE USE OF THIS METHOD IS STRONGLY DISCOURAGED.</em> Using another
     * thread in an application like this doesn't provide any benefits. Because
     * Pathfinder shouldn't be performing incredibly intense calculations,
     * there's nothing to worry about. Additionally, CPU-bound operations
     * are not sped up by multithreading, they're just made to be non-blocking.
     * </p>
     *
     * <p>
     * Automatic ticking is starting a new thread on which the {@link #tick()}
     * method will be called repeatedly. This means you can focus on other
     * parts of your robot in your main thread. It's worth nothing that I
     * personally don't recommend using automatic ticking, but it's a
     * fairly personal choice.
     * </p>
     *
     * @param shouldContinueTicking a supplier to indicate whether the
     *                              thread should continue ticking. If this
     *                              supplier returns false at any point,
     *                              the thread will automatically be stopped,
     *                              and thus, automatic ticking will stop.
     * @see #enableAutomaticTicking()
     * @see #disableAutomaticTicking()
     */
    public void enableAutomaticTicking(
        Supplier<Boolean> shouldContinueTicking
    ) {
        this.isAutomaticallyTicking = true;

        Thread automaticTickingThread = new Thread(
            () -> {
                while (
                    isAutomaticallyTicking() && shouldContinueTicking.get()
                ) {
                    // originally pathfinder was written with jdk11
                    // but because FIRST doesn't like updating their tech
                    // we have to use jdk8 instead... so if this ever does get
                    // updated to jdk11 again, uncomment the next line:
                    // Thread.onSpinWait();

                    this.tick();
                }
            }
        );

        automaticTickingThread.start();
    }

    /**
     * Disable automatic ticking.
     *
     * @see #enableAutomaticTicking()
     * @see #enableAutomaticTicking(Supplier)
     */
    public void disableAutomaticTicking() {
        this.isAutomaticallyTicking = false;
    }

    /**
     * Is the {@code Scheduler} currently in automatic ticking mode?
     * This value will return false until the {@link #enableAutomaticTicking()}
     * or {@link #enableAutomaticTicking(Supplier)} method is called, and
     * will then return true until {@link #disableAutomaticTicking()} is called.
     *
     * @return is the {@code Scheduler} currently in automatic ticking?
     */
    public boolean isAutomaticallyTicking() {
        return this.isAutomaticallyTicking;
    }

    /**
     * Stop the {@code Scheduler} from doing anything at all. If the scheduler
     * is currently automatically ticking, this will stop it from doing
     * so.
     */
    public void clear() {
        if (isAutomaticallyTicking) disableAutomaticTicking();

        this.tasks.clear();
    }
}
