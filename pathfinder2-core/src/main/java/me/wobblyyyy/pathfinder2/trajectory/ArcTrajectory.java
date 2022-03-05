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
import me.wobblyyyy.pathfinder2.geometry.Circle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * @author Colin Robertson
 * @since 1.0.0
 * @deprecated it doesn't really work, and to be honest, I'm really not in
 * the mood to refactor code right now, so I'm just going to suggest that
 * you don't use it.
 */
@SuppressWarnings("UnusedAssignment")
@Deprecated
public class ArcTrajectory implements Trajectory {
    private final Circle circle;
    private final double radius;
    private final double speed;
    private final Angle angleStep;
    private final Angle desiredHeading;
    private final Angle start;
    private final Angle stop;
    private final boolean isInverted;

    public ArcTrajectory(
        PointXY center,
        double radius,
        double speed,
        Angle angleStep,
        Angle desiredHeading,
        Angle start,
        Angle stop
    ) {
        circle = new Circle(center, radius);
        this.radius = radius;
        this.speed = speed;
        this.angleStep = angleStep;
        this.desiredHeading = desiredHeading;
        this.start = start.fix();
        this.stop = stop.fix();
        this.isInverted = this.start.deg() > this.stop.deg();
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        double deg = current.z().fix().deg();

        if (isInverted) {
            if (deg > start.deg()) deg = start.deg(); else if (
                deg < stop.deg()
            ) deg = stop.deg();
        } else {
            if (deg < start.deg()) deg = start.deg(); else if (
                deg > stop.deg()
            ) deg = stop.deg();
        }

        return circle
            .getCenter()
            .inDirection(
                radius,
                circle.getCenter().angleTo(current).add(angleStep)
            )
            .withHeading(desiredHeading);
    }

    @Override
    public double speed(PointXYZ current) {
        return speed;
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return false;
    }
}
