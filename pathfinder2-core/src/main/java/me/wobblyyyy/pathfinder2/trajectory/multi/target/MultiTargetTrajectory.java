/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.multi.target;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.math.Magnitude;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.NotNull;

/**
 * A multi-stage trajectory with multiple target points. This trajectory
 * allows each point to have individual values - speed, tolerance, angle
 * tolerance, and even whether it should act more like a {@code FastTrajectory}
 * or a {@code LinearTrajectory}. This is one of the few mutable types of
 * trajectory. Every time the {@link #nextMarker(PointXYZ)} method is called,
 * the {@code MultiTargetTrajectory} determines if it's reached its target
 * point. If it has reached the target point, it'll begin moving to the next
 * target point. If it has not reached the target point, it'll continue going
 * towards that point. After all of the target points have been reached,
 * the trajectory is marked as finished, and the {@link #isDone(PointXYZ)}
 * method will return true, terminating the trajectory's execution.
 *
 * <p>
 * This class provides a streamlined way of creating trajectories that have
 * non-linear movement without dealing with splines and spline interpolation.
 * Using several target points with the {@link TargetPrecision#FAST} precision
 * type allows you to follow a loosely-defined set of points rather quickly,
 * while still maintaining somewhat respectable accuracy.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class MultiTargetTrajectory implements Trajectory {
    private final TrajectoryTarget[] targets;
    private int index = 0;
    private double lastStartX = 0;
    private double lastStartY = 0;
    private Angle lastStartZ = Angle.DEG_0;
    private double requiredTranslationX = 0;
    private double requiredTranslationY = 0;
    private double requiredTranslationZ = 0;
    private boolean hasFinished = false;
    private boolean hasRan = false;

    /**
     * Create a new {@code MultiTargetTrajectory}.
     *
     * @param targets an array of targets for the {@code Trajectory} to
     *                follow.
     */
    public MultiTargetTrajectory(TrajectoryTarget[] targets) {
        NotNull.throwExceptionIfNull(
                "Cannot create a multi-target trajectory with " +
                        "any null targets!",
                (Object[]) targets
        );

        this.targets = targets;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        // if this is the first time the trajectory is running,
        // set up some values
        if (!hasRan) {
            lastStartX = current.x();
            lastStartY = current.y();
            lastStartZ = current.z();

            PointXYZ next = targets[index].target();

            requiredTranslationX = next.x() - current.x();
            requiredTranslationY = next.y() - current.y();
            requiredTranslationZ = next.z().subtract(current.z()).fix().deg();

            hasRan = true;
        }

        TrajectoryTarget target = targets[index];

        // current values
        double currentX = current.x();
        double currentY = current.y();
        Angle currentAngle = current.z();

        // sort of "delta" values - difference between current and start
        double translationX = currentX - lastStartX;
        double translationY = currentY - lastStartY;
        double translationZ = currentAngle.subtract(lastStartZ).fix().deg();

        boolean hasCompletedX =
                Magnitude.higherMagnitude(translationX, requiredTranslationX);
        boolean hasCompletedY =
                Magnitude.higherMagnitude(translationY, requiredTranslationY);
        boolean hasCompletedZ =
                Magnitude.higherMagnitude(translationZ, requiredTranslationZ);

        if (hasCompletedX && hasCompletedY && hasCompletedZ) {
            // precision targets need to be treated differently - we have
            // to handle tolerance values
            if (target.precision() == TargetPrecision.PRECISE) {
                PointXYZ targetPoint = target.target();
                double distance = current.absDistance(targetPoint);
                Angle angleDistance = Angle.angleDelta(
                        current.z(),
                        targetPoint.z()
                );

                boolean validDistance = distance < target.tolerance();
                boolean validAngle = angleDistance.lessThanOrEqualTo(
                        target.angleTolerance());

                if (!(validDistance && validAngle)) {
                    return targetPoint;
                }
            }

            // non-precision targets are treated normally - we don't really
            // care about tolerance
            if (index == targets.length - 1) {
                // if it's the last target point, the entire trajectory is done
                hasFinished = true;
                return current;
            } else {
                lastStartX = currentX;
                lastStartY = currentY;
                lastStartZ = currentAngle;

                index++;

                PointXYZ next = targets[index].target();

                requiredTranslationX = next.x() - currentX;
                requiredTranslationY = next.y() - currentY;
                requiredTranslationZ = next.z().subtract(currentAngle)
                        .fix().deg();
            }
        }

        return targets[index].target();
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return hasFinished;
    }

    @Override
    public double speed(PointXYZ current) {
        return targets[index].speed();
    }
}
