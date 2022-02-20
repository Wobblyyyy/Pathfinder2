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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import me.wobblyyyy.pathfinder2.trajectory.multi.segment.MultiSegmentTrajectory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A path-like thing that your robot can follow. Look, I know all this
 * documentation is supposed to be official and what not, but I can't really
 * define it any better than that.
 *
 * <p>
 * A trajectory works pretty simply, actually. There's a method called
 * {@link #nextMarker(PointXYZ)} that gets the next marker point the robot
 * should attempt to drive to based on the current point. And... that's about
 * it. Yeah. There's really not much more to it.
 * </p>
 *
 * <p>
 * Each {@code Trajectory} is executed via a {@link me.wobblyyyy.pathfinder2.follower.Follower}.
 * A follower is responsible for interpreting a trajectory, as well as the
 * robot's current position, to determine how the robot should move. Long
 * story short, {@code Trajectory} instances cannot directly control how
 * Pathfinder operates the robot - rather, you'll have to encapsulate each
 * trajectory in an individual follower. These followers are then executed
 * via a {@link me.wobblyyyy.pathfinder2.execution.FollowerExecutor}, which,
 * in turn, are managed via a {@link me.wobblyyyy.pathfinder2.execution.ExecutorManager}.
 * </p>
 *
 * <p>
 * As of Pathfinder2 v1.1.0, you can also add primitive listeners to a
 * trajectory using the following methods:
 * <ul>
 *     <li>{@link #onStart(Consumer)}</li>
 *     <li>{@link #onFinish(Consumer)}</li>
 *     <li>{@link #withModifiers(Function, Function, Function)}</li>
 *     <li>{@link #addListeners(Consumer, Consumer)}</li>
 * </ul>
 * These listeners are relatively ineffective, as the addition of each layer
 * requires the instantiation of a new trajectory. Although this should not
 * pose a performance problem in any cases, it's worth mentioning. These
 * listeners are bound to the trajectory itself and are based on the state
 * of the trajectory; therefore, you do not need to use Pathfinder's
 * {@code listening} package in order to use these listeners.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Trajectory extends Serializable {
    /**
     * Get the next marker that the robot should attempt to navigate to. This
     * should always be a point along the trajectory.
     *
     * <p>
     * A marker position should provide the robot with a "target." The robot
     * should then calculate how it should move to reach that target point.
     * In essence, a holonomic robot should calculate the angle between its
     * own position and the next target point, and, from there, determine
     * how to move in that angle.
     * </p>
     *
     * <p>
     * A {@code Trajectory} is, in essence, a long sequence of points the
     * robot should navigate to. These points are typically generated
     * dynamically. Simple trajectories, such as linear trajectories, only need
     * a single marker point - the target point.
     * </p>
     *
     * @param current the robot's current position. This position is used in
     *                determining the next marker position. Some trajectories
     *                may use the robot's current position to keep track of
     *                progress through a stateful trajectory, such as a
     *                trajectory that passes through the same point twice.
     * @return the next marker the robot should attempt to navigate to. The
     * robot should calculate, based on the current and marker positions,
     * how to move in order to reach the target position.
     */
    PointXYZ nextMarker(PointXYZ current);

    /**
     * Has the robot finished executing this trajectory? This method should
     * return true if the robot has finished executing the trajectory and
     * can move on to the next trajectory or stop. If the robot hasn't finished
     * it's trajectory, meaning it should still continue executing it, this
     * method should return false.
     *
     * <p>
     * Each trajectory follower can only be marked as "completed" if this
     * method returns true. Also note that as soon as this method returns
     * true, the follower responsible for executing that trajectory will
     * be de-queued, meaning that this trajectory will no longer be
     * executed. Scary.
     * </p>
     *
     * @param current the robot's current position.
     * @return true if the robot has finished executing the trajectory, false
     * if it has not. Frequently, this is defined by a target position: after
     * the robot reaches a certain position, the trajectory will end. However,
     * this can also have other requirements - for example, the robot must be
     * below a certain velocity, or the robot must not be accelerating, etc.
     */
    boolean isDone(PointXYZ current);

    /**
     * Determine the speed at which the robot should be moving while executing
     * this section of the trajectory. Most simple forms of trajectory, such
     * as the {@link LinearTrajectory}, have a linear speed, meaning the
     * robot will move at the same speed throughout the duration of its
     * {@code Trajectory}. Contrarily, more advanced trajectories, such as
     * spline trajectories, can have variable speeds which change based on
     * the robot's position.
     *
     * <p>
     * Trajectories can have different speeds at different points. This method
     * CAN return the same value constantly, regardless of the position of
     * the robot. However, if you'd like to have some more control over the
     * speed of your robot at different points throughout the trajectory,
     * you're more than free to do so.
     * </p>
     *
     * <p>
     * If you'd like to control the speed of a trajectory, I'd suggest you
     * make note of the following class:
     * {@link me.wobblyyyy.pathfinder2.control.SplineController}
     * There's absolutely no need to, and you can control the speed using
     * other methods, but the {@code SplineController} gives you the most
     * control over what speed your robot will have at what point.
     * </p>
     *
     * @param current the robot's current position. This position is used in
     *                determining how fast the robot should move from here.
     * @return the speed at which the robot should be moving. This value should
     * always be positive. A value of 0 corresponds to not moving at all, and
     * a value of 1 corresponds to moving at full speed.
     */
    double speed(PointXYZ current);

    /**
     * Convert this trajectory to a {@link MultiSegmentTrajectory}, adding
     * all of the provided additional trajectories.
     *
     * @param additionalTrajectories a variable length array containing
     *                               trajectories that should be conjoined
     *                               with {@code this} trajectory.
     * @return a new {@link MultiSegmentTrajectory}. This new trajectory will
     * have {@code this} trajectory as the first segment, and then any of the
     * trajectories in the variable argument will be appended afterwards.
     */
    default Trajectory toMultiSegmentTrajectory(Trajectory... additionalTrajectories) {
        Trajectory[] trajectories = new Trajectory[additionalTrajectories.length + 1];
        System.arraycopy(
                additionalTrajectories, 0,
                trajectories, 1,
                additionalTrajectories.length
        );
        trajectories[0] = this;
        return new MultiSegmentTrajectory(Arrays.asList(trajectories));
    }

    /**
     * Add a listener for the trajectory's first execution.
     *
     * @param onStart  code to be executed whenever the trajectory begins
     *                 its execution. This code will be executed once: the
     *                 very first time the {@link #nextMarker(PointXYZ)} is
     *                 called. After the first time the {@link #nextMarker(PointXYZ)}
     *                 is called, this code will not be executed again.
     * @return a new {@code Trajectory} that has several event listeners
     * attached. If the listener is empty, it'll simply do nothing - otherwise,
     * it'll activate whenever a certain condition is met.
     */
    default Trajectory onStart(Consumer<PointXYZ> onStart) {
        return addListeners(onStart, (point) -> {});
    }

    /**
     * Add a listener for the first time the trajectory's
     * {@link #isDone(PointXYZ)} method returns true.
     *
     * @param onFinish code to be executed whenever the trajectory has finished
     *                 its execution. This is a {@code Consumer} that accepts
     *                 a {@code PointXYZ}, which is the robot's position at
     *                 the time of finish. This condition will only be
     *                 triggered the first time the {@code Trajectory}'s
     *                 {@link #isDone(PointXYZ)} returns true.
     * @return a new {@code Trajectory} that has several event listeners
     * attached. If the listener is empty, it'll simply do nothing - otherwise,
     * it'll activate whenever a certain condition is met.
     */
    default Trajectory onFinish(Consumer<PointXYZ> onFinish) {
        return addListeners((point) -> {}, onFinish);
    }

    /**
     * Add listeners to a trajectory by creating a wrapper trajectory and
     * monitoring for certain conditions. Each of listeners responds to
     * a specific condition - the most useful of these are {@code onStart}
     * and {@code onFinish}, as they can be used to do some pretty neat
     * things without having to make use of a {@code PathfinderPlugin}.
     *
     * @param onStart  code to be executed whenever the trajectory begins
     *                 its execution. This code will be executed once: the
     *                 very first time the {@link #nextMarker(PointXYZ)} is
     *                 called. After the first time the {@link #nextMarker(PointXYZ)}
     *                 is called, this code will not be executed again.
     * @param onFinish code to be executed whenever the trajectory has finished
     *                 its execution. This is a {@code Consumer} that accepts
     *                 a {@code PointXYZ}, which is the robot's position at
     *                 the time of finish. This condition will only be
     *                 triggered the first time the {@code Trajectory}'s
     *                 {@link #isDone(PointXYZ)} returns true.
     * @return a new {@code Trajectory} that has several event listeners
     * attached. If the listener is empty, it'll simply do nothing - otherwise,
     * it'll activate whenever a certain condition is met.
     */
    default Trajectory addListeners(Consumer<PointXYZ> onStart,
                                    Consumer<PointXYZ> onFinish) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            boolean hasStarted = false;
            boolean hasFinished = false;

            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                if (!hasStarted) {
                    onStart.accept(current);
                    hasStarted = true;
                }

                return nextMarkerFunction.apply(current);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                boolean isDone = isDoneFunction.apply(current);

                if (isDone)
                    if (!hasFinished) {
                        onFinish.accept(current);
                        hasFinished = true;
                    }

                return isDone;
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(current);
            }
        };
    }

    /**
     * Add listeners to a trajectory by creating a wrapper trajectory and
     * monitoring for certain conditions. Each of listeners responds to
     * a specific condition - the most useful of these are {@code onStart}
     * and {@code onFinish}, as they can be used to do some pretty neat
     * things without having to make use of a {@code PathfinderPlugin}.
     *
     * @param onStart  code to be executed whenever the trajectory begins
     *                 its execution. This code will be executed once: the
     *                 very first time the {@link #nextMarker(PointXYZ)} is
     *                 called. After the first time the {@link #nextMarker(PointXYZ)}
     *                 is called, this code will not be executed again.
     * @param onIsDone code to be executed whenever the {@link #isDone(PointXYZ)}
     *                 method is called. This code will be executed every
     *                 time the {@link #isDone(PointXYZ)} method is called,
     *                 regardless of whether the trajectory is done or not.
     * @param onSpeed  code to be executed whenever the {@link #speed(PointXYZ)}
     *                 method is called. This code will be executed every
     *                 time the speed method is called.
     * @param onFinish code to be executed whenever the trajectory has finished
     *                 its execution. This is a {@code Consumer} that accepts
     *                 a {@code PointXYZ}, which is the robot's position at
     *                 the time of finish. This condition will only be
     *                 triggered the first time the {@code Trajectory}'s
     *                 {@link #isDone(PointXYZ)} returns true.
     * @return a new {@code Trajectory} that has several event listeners
     * attached. If the listener is empty, it'll simply do nothing - otherwise,
     * it'll activate whenever a certain condition is met.
     */
    default Trajectory addListeners(Consumer<PointXYZ> onStart,
                                    Consumer<Boolean> onIsDone,
                                    Consumer<Double> onSpeed,
                                    Consumer<PointXYZ> onFinish) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            boolean hasStarted = false;
            boolean hasFinished = false;

            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                if (!hasStarted) {
                    onStart.accept(current);
                    hasStarted = true;
                }

                return nextMarkerFunction.apply(current);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                boolean isDone = isDoneFunction.apply(current);

                if (isDone)
                    if (!hasFinished) {
                        onFinish.accept(current);
                        hasFinished = true;
                    }

                onIsDone.accept(isDone);

                return isDone;
            }

            @Override
            public double speed(PointXYZ current) {
                double speed = speedFunction.apply(current);

                onSpeed.accept(speed);

                return speed;
            }
        };
    }

    default Trajectory withModifiers(Function<PointXYZ, PointXYZ> nextMarkerModifier,
                                     Function<Boolean, Boolean> isDoneModifier,
                                     Function<Double, Double> speedModifier) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerModifier.apply(nextMarkerFunction.apply(current));
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneModifier.apply(isDoneFunction.apply(current));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedModifier.apply(speedFunction.apply(current));
            }
        };
    }

    /**
     * Create a wrapper trajectory around this trajectory that behaves exactly
     * the same as {@code this} trajectory, but with time limits. The
     * trajectory will only execute if it has not exceeded
     * {@code maximumTimeMs}. At the very least, this will execute until
     * {@code minimumTimeMs} has elapsed.
     *
     * @param minimumTimeMs the minimum amount of time the trajectory may
     *                      execute for, in milliseconds.
     * @param maximumTimeMs the maximum amount of time the trajectory may
     *                      execute for, in milliseconds.
     * @return a wrapper for {@code this} trajectory.
     */
    default Trajectory withTimeLimits(double minimumTimeMs,
                                      double maximumTimeMs) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            ElapsedTimer timer = new ElapsedTimer();

            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                if (!timer.hasStarted())
                    timer.start();

                return nextMarkerFunction.apply(current);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                double elapsedMs = timer.elapsedMs();

                if (elapsedMs < minimumTimeMs)
                    return true;
                else if (elapsedMs < maximumTimeMs)
                    return isDoneFunction.apply(current);
                else if (elapsedMs > maximumTimeMs)
                    return true;
                else
                    return false;
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(current);
            }
        };
    }

    /**
     * Reflect the entire trajectory over a specified axis.
     *
     * @param xReflectionAxis the axis to reflect the trajectory over.
     * @return a reflected trajectory. This will not modify any of the
     * values of the base trajectory - rather, it'll simply reflect all
     * inputted points over a specified axis.
     */
    default Trajectory reflectX(double xReflectionAxis) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.reflectOverX(xReflectionAxis))
                    .reflectOverX(xReflectionAxis);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.reflectOverX(xReflectionAxis));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.reflectOverX(xReflectionAxis));
            }
        };
    }

    /**
     * Reflect the entire trajectory over a specified axis.
     *
     * @param xReflectionAxis the axis to reflect the trajectory over.
     * @return a reflected trajectory. This will not modify any of the
     * values of the base trajectory - rather, it'll simply reflect all
     * inputted points over a specified axis.
     */
    default Trajectory reflectY(double yReflectionAxis) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.reflectOverY(yReflectionAxis))
                    .reflectOverY(yReflectionAxis);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.reflectOverY(yReflectionAxis));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.reflectOverY(yReflectionAxis));
            }
        };
    }

    /**
     * Offset the entire trajectory.
     *
     * @param offset the trajectory's offset.
     * @return a trajectory with an applied offset.
     */
    default Trajectory offset(PointXYZ offset) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.add(offset)).add(offset);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.add(offset));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.add(offset));
            }
        };
    }

    /**
     * Rotate an entire trajectory around a point by a given angle.
     *
     * @param center the center of rotation for the trajectory.
     * @param angle  how far to rotate the trajectory.
     * @return the rotated trajectory.
     */
    default Trajectory rotateAround(PointXY center,
                                    Angle angle) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.rotate(center, angle)).rotate(center, angle);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.rotate(center, angle));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.rotate(center, angle));
            }
        };
    }

    /**
     * Shift a trajectory.
     *
     * @param origin the trajectory's base.
     * @param target the trajectory's target.
     * @return a shifted trajectory.
     */
    default Trajectory shift(PointXY origin,
                             PointXY target) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        PointXYZ difference = origin.subtract(target).withHeading(Angle.fromDeg(0));

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.add(difference)).add(difference);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.add(difference));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.add(difference));
            }
        };
    }

    default Trajectory multiply(PointXYZ multiplier) {
        return multiply(multiplier.x(), multiplier.y(), multiplier.z().deg());
    }

    default Trajectory multiplyX(double multiplier) {
        return multiply(multiplier, 1, 1);
    }

    default Trajectory multiplyY(double multiplier) {
        return multiply(1, multiplier, 1);
    }

    default Trajectory multiplyZ(Angle multiplier) {
        return multiply(1, 1, multiplier.deg());
    }

    default Trajectory multiply(double multiplier) {
        return multiply(multiplier, multiplier, multiplier);
    }
    
    default Trajectory multiplyXY(double multiplier) {
        return multiply(multiplier, multiplier, 1);
    }


    default Trajectory multiply(double xMultiplier,
                                double yMultiplier,
                                double zMultiplier) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        PointXYZ mult = new PointXYZ(xMultiplier,
                yMultiplier, Angle.fromDeg(zMultiplier));

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.multiply(mult)).multiply(mult);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.multiply(mult));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.multiply(mult));
            }
        };
    }

    default Trajectory add(double xMultiplier,
                           double yMultiplier,
                           double zMultiplier) {
        Function<PointXYZ, PointXYZ> nextMarkerFunction =
            InternalTrajectoryUtils.nextMarkerFunction(this);
        Function<PointXYZ, Boolean> isDoneFunction =
            InternalTrajectoryUtils.isDoneFunction(this);
        Function<PointXYZ, Double> speedFunction =
            InternalTrajectoryUtils.speedFunction(this);

        PointXYZ mult = new PointXYZ(xMultiplier,
                yMultiplier, Angle.fromDeg(zMultiplier));

        return new Trajectory() {
            @Override
            public PointXYZ nextMarker(PointXYZ current) {
                return nextMarkerFunction.apply(
                        current.add(mult)).add(mult);
            }

            @Override
            public boolean isDone(PointXYZ current) {
                return isDoneFunction.apply(
                        current.add(mult));
            }

            @Override
            public double speed(PointXYZ current) {
                return speedFunction.apply(
                        current.add(mult));
            }
        };
    }

    default Trajectory shiftToRobot(PointXYZ origin,
                                    PointXYZ target) {
        return shift(origin, target)
            .rotateAround(target, target.z().subtract(origin.z()));
    }

    /**
     * Utilities intended exclusively for internal use within Pathfinder. This
     * class only exists because you can't make private methods in interfaces,
     * so... I had to create a static class to avoid cluttering the main
     * {@code Trajectory} interface with useless default methods.
     */
    static class InternalTrajectoryUtils {
        static Function<PointXYZ, PointXYZ> nextMarkerFunction(Trajectory trajectory) {
            return trajectory::nextMarker;
        }

        static Function<PointXYZ, Boolean> isDoneFunction(Trajectory trajectory) {
            return trajectory::isDone;
        }

        static Function<PointXYZ, Double> speedFunction(Trajectory trajectory) {
            return trajectory::speed;
        }
    }
}
