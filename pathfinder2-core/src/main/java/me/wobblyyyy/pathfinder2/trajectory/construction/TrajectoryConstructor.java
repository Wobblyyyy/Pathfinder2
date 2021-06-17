/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.construction;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Easy-to-use constructor for creating simple trajectories.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class TrajectoryConstructor {
    private static final Angle FORWARDS = Angle.fromDeg(90);

    private static final Angle BACKWARDS = Angle.fromDeg(270);

    private static final Angle RIGHTWARDS = Angle.fromDeg(0);

    private static final Angle LEFTWARDS = Angle.fromDeg(180);

    private final List<Trajectory> trajectories;

    private PointXYZ current;

    public TrajectoryConstructor() {
        this.trajectories = new ArrayList<>();
    }

    public void move(Angle direction,
                     double distance) {
        PointXYZ target = current.inDirection(distance, direction);
        trajectories.add(new LinearTrajectory(current, target));
        current = target;
    }

    public void moveForwards(double distance) {
        move(FORWARDS.add(current.z()), distance);
    }

    public void moveBackwards(double distance) {
        move(BACKWARDS.add(current.z()), distance);
    }

    public void moveRight(double distance) {
        move(RIGHTWARDS.add(current.z()), distance);
    }

    public void moveLeft(double distance) {
        move(LEFTWARDS.add(current.z()), distance);
    }

    public void rotate(Angle angle) {
        PointXYZ target = new PointXYZ(
                current.x(),
                current.y(),
                current.z().add(angle)
        );
        trajectories.add(new LinearTrajectory(current, target));
        current = target;
    }

    public void goTo(PointXYZ target) {
        trajectories.add(new LinearTrajectory(current, target));
        current = target;
    }

    public List<Trajectory> trajectories() {
        return this.trajectories;
    }
}
