package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Circle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public class ArcTrajectory implements Trajectory {
    private final Circle circle;
    private final double speed;
    private final Angle start;
    private final Angle stop;

    public ArcTrajectory(PointXY center,
                         double radius,
                         double speed,
                         Angle start,
                         Angle stop) {
        circle = new Circle(center, radius);
        this.speed = speed;
        this.start = start;
        this.stop = stop;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        // Angle currentAngle = circle.getCenter().angleTo(current);

        return current;
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
