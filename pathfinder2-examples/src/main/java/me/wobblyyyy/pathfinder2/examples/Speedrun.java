package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;

public class Speedrun {
    private final Controller controller;
    private final Drive drive;
    private final Odometry odometry;
    private final Robot robot;
    private final Pathfinder pathfinder;

    public Speedrun() {
        this.controller = new ProportionalController(0.01);
        this.drive = new SimulatedDrive();
        this.odometry = new SimulatedOdometry();
        this.robot = new Robot(drive, odometry);
        this.pathfinder = new Pathfinder(robot, controller);
    }

    public void run() {
        pathfinder.goTo(new PointXY(10, 10)).tickUntil();
        pathfinder.splineTo(
                new PointXYZ(10, 10, 45),
                new PointXYZ(20, 15, 90),
                new PointXYZ(30, 20, 180)
        ).tickUntil();
        pathfinder.splineTo(
                new PointXYZ(20, 20, Angle.fromDeg(45)),
                new PointXYZ(30, 25, Angle.fromDeg(50)),
                new PointXYZ(40, 30, Angle.fromRad(0.5 * Math.PI))
        ).tickUntil();
    }
}
