/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

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

    public static Translation absoluteToRelative(Translation translation,
                                                 Angle heading) {
        Angle newAngle = translation.angle().add(heading);

        return new Translation(
                newAngle.cos() * translation.magnitude(),
                newAngle.sin() * translation.magnitude(),
                translation.vz()
        );
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

    public double magnitude() {
        return Math.hypot(this.vx, this.vy);
    }

    public PointXY point() {
        return new PointXY(this.vx, this.vy);
    }

    public Angle angle() {
        return PointXY.zero().angleTo(point());
    }

    public Translation toRelative(Angle heading) {
        return absoluteToRelative(this, heading);
    }
}
