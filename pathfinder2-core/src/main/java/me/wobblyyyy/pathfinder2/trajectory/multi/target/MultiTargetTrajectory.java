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

    public MultiTargetTrajectory(TrajectoryTarget[] targets) {
        this.targets = targets;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
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

        double currentX = current.x();
        double currentY = current.y();
        Angle currentAngle = current.z();

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

            if (index == targets.length - 1) {
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
