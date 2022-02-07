package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.Pathfinder;

public class Macros {
    public void coolMacros() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

        pathfinder.splineTo(
                new PointXYZ(10, 10, Angle.fromDeg(45)),
                new PointXYZ(30, 20, Angle.fromDeg(15)),
                new PointXYZ(19, 23, Angle.fromDeg(73)),
                new PointXYZ(101, 201, Angle.fromDeg(67))
        ).tickUntil();
    }
}
