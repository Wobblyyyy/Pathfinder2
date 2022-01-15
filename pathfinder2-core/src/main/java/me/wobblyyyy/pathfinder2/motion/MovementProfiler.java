/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.motion;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Helper class for recording {@link MovementSnapshot}s for a robot.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class MovementProfiler {
    private PointXYZ position;
    private double unitsPerSec;
    private double xUnitsPerSec;
    private double yUnitsPerSec;
    private double zDegreesPerSec;
    private double timeMs;

    public MovementSnapshot capture(PointXYZ position,
                                    double timeMs) {
        if (this.timeMs == 0) this.timeMs = timeMs;

        double deltaMs = timeMs - this.timeMs;
        double deltaSec = deltaMs / 1_000;
        double dx = position.distanceX(this.position);
        double dy = position.distanceY(this.position);
        double dxy = position.distance(this.position);
        double dzDeg = position.z().deg() - this.position.z().deg();
        double dxPs = dx / deltaSec;
        double dyPs = dy / deltaSec;
        double dzDegPs = dzDeg / deltaSec;
        double d = dxy / deltaSec;

        double dXp = dxPs - xUnitsPerSec;
        double dYp = dxPs - xUnitsPerSec;
        double dZp = dzDegPs - zDegreesPerSec;
        double dp = d - unitsPerSec;

        double dXps = dXp / deltaSec;
        double dYps = dYp / deltaSec;
        double dZps = dZp / deltaSec;
        double dps = d / deltaSec;

        this.position = position;
        this.unitsPerSec = dps;
        this.xUnitsPerSec = dxPs;
        this.yUnitsPerSec = dyPs;
        this.zDegreesPerSec = dyPs;
        this.timeMs = timeMs;

        MovementSnapshot snapshot = new MovementSnapshot();
        snapshot.setAcceleration(dps);
        snapshot.setAccelerationX(dXps);
        snapshot.setAccelerationY(dYps);
        snapshot.setAccelerationZ(Angle.fromDeg(dZps));
        snapshot.setVelocity(d);
        snapshot.setVelocityX(dxPs);
        snapshot.setVelocityY(dyPs);
        snapshot.setVelocityZ(Angle.fromDeg(dzDegPs));
        return snapshot;
    }
}
