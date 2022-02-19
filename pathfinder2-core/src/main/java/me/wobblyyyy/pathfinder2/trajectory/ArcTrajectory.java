package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Circle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class ArcTrajectory implements Trajectory {
    private final Circle circle;
    private final double radius;
    private final double speed;
    private final Angle angleStep;
    private final Angle desiredHeading;
    private final Angle start;
    private final Angle stop;

    public ArcTrajectory(PointXY center,
                         double radius,
                         double speed,
                         Angle angleStep,
                         Angle desiredHeading,
                         Angle start,
                         Angle stop) {
        circle = new Circle(center, radius);
        this.radius = radius;
        this.speed = speed;
        this.angleStep = angleStep;
        this.desiredHeading = desiredHeading;
        this.start = start;
        this.stop = stop;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return circle.getCenter().inDirection(
                radius,
                circle.getCenter().angleTo(current).add(angleStep)
        ).withHeading(desiredHeading);
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
