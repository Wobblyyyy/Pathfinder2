/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.movement;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.math.Rounding;
import me.wobblyyyy.pathfinder2.math.Velocity;
import me.wobblyyyy.pathfinder2.time.Time;

/**
 * Helper class for recording {@link MovementSnapshot}s for a robot.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class MovementProfiler {
    private PointXYZ position = PointXYZ.ZERO;
    private double xUnitsPerSec;
    private double yUnitsPerSec;
    private double zDegreesPerSec;
    private double timeMs;
    private MovementSnapshot lastSnapshot = new MovementSnapshot();

    /**
     * Get the most recently-recorded snapshot. If no snapshots have been
     * recorded yet, this will return null.
     *
     * @return if at least one snapshot has been captured/recorded, this
     * will return the most recently captured/recorded snapshot. If no
     * snapshots have been taken, this will return null.
     */
    public MovementSnapshot getLastSnapshot() {
        return lastSnapshot;
    }

    /**
     * Capture a single {@link MovementSnapshot}.
     *
     * @param position the robot's current position.
     * @return a new {@link MovementSnapshot}.
     */
    public MovementSnapshot capture(PointXYZ position) {
        return capture(position, Time.ms());
    }

    private static double fixValue(double value) {
        if (
            Double.isNaN(value) || Double.isInfinite(value)
        ) return 0; else return value;
    }

    /**
     * Capture a single {@link MovementSnapshot}.
     *
     * @param position the robot's current position.
     * @param timeMs   the current time, in milliseconds.
     * @return a new {@link MovementSnapshot}.
     */
    public MovementSnapshot capture(PointXYZ position, double timeMs) {
        if (position == null) return new MovementSnapshot();

        // look... i'm sorry to whoever is reading this.
        // this isn't good code. this isn't even decent code. this is
        // just genuinely awful, and i'm sorry for everything...

        if (this.timeMs == 0) this.timeMs = timeMs;

        Logger.trace(
            MovementProfiler.class,
            "Current position: %s, previous position: %s",
            position,
            this.position
        );

        double deltaMs = fixValue(timeMs - this.timeMs);
        double deltaSec = fixValue(deltaMs / 1_000);
        double dx = fixValue(this.position.distanceX(position));
        double dy = fixValue(this.position.distanceY(position));
        double dxy = fixValue(this.position.distance(position));
        double dzDeg = fixValue(position.z().deg() - this.position.z().deg());
        double dxPs = fixValue(dx / deltaSec);
        double dyPs = fixValue(dy / deltaSec);
        double dzDegPs = fixValue(dzDeg / deltaSec);
        double d = fixValue(dxy / deltaSec);

        double dXp = fixValue(dxPs - xUnitsPerSec);
        double dYp = fixValue(dxPs - yUnitsPerSec);
        double dZp = fixValue(dzDegPs - zDegreesPerSec);

        double dXps = fixValue(dXp / deltaSec);
        double dYps = fixValue(dYp / deltaSec);
        double dZps = fixValue(dZp / deltaSec);
        double dps = fixValue(d / deltaSec);

        Angle angle = this.position.angleTo(position);

        this.position = position;
        this.xUnitsPerSec = dxPs;
        this.yUnitsPerSec = dyPs;
        this.zDegreesPerSec = dyPs;
        this.timeMs = timeMs;

        Logger.trace(
            MovementProfiler.class,
            "delta ms: %s, delta sec: %s, dx: %s, dy: %s, dxy: %s, dzDeg: %s" +
            ", dxPs: %s, dyPs: %s, dzDegPs: %s, d: %s, dXp: %s, dYp: %s, dZp" +
            ": %s, dXps: %s, dYps: %s, dZps: %s, dps: %s, angle: %s, " +
            "position: %s, xUnitsPerSec: %s, yUnitsPerSec: %s, zDegreesPer" +
            "Sec: %s, timeMs: %s",
            Rounding.fastRound(deltaMs),
            Rounding.fastRound(deltaSec),
            Rounding.fastRound(dx),
            Rounding.fastRound(dy),
            Rounding.fastRound(dxy),
            Rounding.fastRound(dzDeg),
            Rounding.fastRound(dxPs),
            Rounding.fastRound(dyPs),
            Rounding.fastRound(dzDegPs),
            Rounding.fastRound(d),
            Rounding.fastRound(dXp),
            Rounding.fastRound(dYp),
            Rounding.fastRound(dZp),
            Rounding.fastRound(dXps),
            Rounding.fastRound(dYps),
            Rounding.fastRound(dZps),
            Rounding.fastRound(dps),
            angle,
            position,
            Rounding.fastRound(xUnitsPerSec),
            Rounding.fastRound(yUnitsPerSec),
            Rounding.fastRound(zDegreesPerSec),
            Rounding.fastRound(timeMs)
        );

        MovementSnapshot snapshot = new MovementSnapshot()
            .setAccelerationXY(dps)
            .setAccelerationX(dXps)
            .setAccelerationY(dYps)
            .setAccelerationZ(Angle.fromDeg(dZps))
            .setVelocity(new Velocity(d, angle))
            .setVelocityXY(d)
            .setVelocityX(dxPs)
            .setVelocityY(dyPs)
            .setVelocityZ(Angle.fromDeg(dzDegPs));

        lastSnapshot = snapshot;
        return snapshot;
    }
}
