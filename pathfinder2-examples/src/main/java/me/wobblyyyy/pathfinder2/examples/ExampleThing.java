package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;

public class ExampleThing {
    private final PointXY point;
    private final Angle angle;

    public ExampleThing(PointXY point,
                        Angle angle) {
        this.point = point;
        this.angle = angle;
    }

    public PointXY getPoint() {
        return point;
    }

    public Angle getAngle() {
        return angle;
    }
}
