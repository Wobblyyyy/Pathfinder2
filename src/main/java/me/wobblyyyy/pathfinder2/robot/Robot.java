package me.wobblyyyy.pathfinder2.robot;

public class Robot {
    private final Drive drive;
    private final Odometry odometry;

    public Robot(Drive drive,
                 Odometry odometry) {
        this.drive = drive;
        this.odometry = odometry;
    }

    public Drive drive() {
        return this.drive;
    }

    public Odometry odometry() {
        return this.odometry;
    }
}
