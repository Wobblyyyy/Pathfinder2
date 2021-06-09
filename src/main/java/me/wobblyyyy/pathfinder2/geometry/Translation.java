package me.wobblyyyy.pathfinder2.geometry;

public class Translation {
    private final double vx;
    private final double vy;
    private final double vz;

    public Translation(double vx,
                       double vy,
                       double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public double vx() {
        return this.vx;
    }

    public double vy() {
        return this.vy;
    }

    public double vz() {
        return this.vz;
    }
}
