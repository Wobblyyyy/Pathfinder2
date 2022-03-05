/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.builder;

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.FastTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A builder class used for creating simple sequences of trajectories.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class TrajectoryBuilder {
    private final List<Trajectory> trajectories = new ArrayList<>();
    private PointXYZ lastPoint = null;
    private double speed = 0.5;
    private double tolerance = 2;
    private Angle angleTolerance = Angle.fromDeg(5);

    public TrajectoryBuilder setStartPos(PointXYZ pos) {
        lastPoint = pos;

        return this;
    }

    public TrajectoryBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public TrajectoryBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public TrajectoryBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    public TrajectoryBuilder linearTo(PointXYZ target) {
        return linearTo(target, speed);
    }

    public TrajectoryBuilder linearTo(PointXYZ target, double speed) {
        trajectories.add(
            new LinearTrajectory(target, speed, tolerance, angleTolerance)
        );

        lastPoint = target;

        return this;
    }

    public TrajectoryBuilder fastTo(PointXYZ target) {
        return fastTo(target, speed);
    }

    public TrajectoryBuilder fastTo(PointXYZ target, double speed) {
        if (lastPoint == null) {
            throw new NullPointException(
                "Must use the setStartPos method before creating a fast " +
                "trajectory to a target! This position should be " +
                "the robot's starting position."
            );
        }

        trajectories.add(new FastTrajectory(lastPoint, target, speed));

        lastPoint = target;

        return this;
    }

    public List<Trajectory> build() {
        return trajectories;
    }
}
