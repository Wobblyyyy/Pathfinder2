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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple trajectory builder used in creating trajectory groupings made solely
 * from linear trajectories.
 *
 * @author Colin Robertson
 * @see LinearTrajectory
 * @since 0.0.0
 */
public class LinearTrajectoryBuilder {
    private static final Angle FORWARDS = Angle.DEG_90;

    private static final Angle RIGHTWARDS = Angle.DEG_0;

    private static final Angle LEFTWARDS = Angle.DEG_180;

    private static final Angle BACKWARDS = Angle.DEG_270;

    private final List<Trajectory> trajectories = new ArrayList<>();

    private double speed;

    private double tolerance;

    private Angle angleTolerance;

    private PointXYZ last;

    /**
     * Create a new {@code LinearTrajectoryBuilder}.
     *
     * @param speed          the speed at which the robot should move to the
     *                       target point. Note that this speed value can not
     *                       be changed while the trajectory is being followed.
     * @param tolerance      the tolerance used in determining whether the
     *                       robot's X and Y coordinates match up with those
     *                       of the target point.
     * @param angleTolerance the tolerance used in determining whether the
     *                       robot's heading matches up with whatever heading
     *                       the robot is supposed to be facing.
     * @param start          the trajectory builder's starting point. This
     *                       doesn't really matter all that much - you'll
     *                       be fine if you just set it to (0, 0, 0).
     */
    public LinearTrajectoryBuilder(double speed,
                                   double tolerance,
                                   Angle angleTolerance,
                                   PointXYZ start) {
        this.speed = speed;
        this.tolerance = tolerance;
        this.angleTolerance = angleTolerance;
        this.last = start;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public void setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;
    }

    public Angle getAngleTolerance() {
        return this.angleTolerance;
    }

    /**
     * Move in a line and rotate to a specified heading.
     *
     * @param distance    the distance from the current point that the new
     *                    point should be drawn at.
     * @param targetAngle the angle the trajectory should attempt to turn to.
     * @param angle       the angle at which the line should be drawn.
     */
    public void rotateLine(double distance,
                           Angle targetAngle,
                           Angle angle) {
        PointXYZ next = last.inDirection(distance, angle);

        trajectories.add(new LinearTrajectory(
                next.withHeading(targetAngle),
                speed,
                tolerance,
                angleTolerance
        ));

        last = next;
    }

    /**
     * Move in a line. Epic, right? Of course it's epic.
     *
     * @param distance how long the line should be.
     * @param angle    the direction the line should be drawn in.
     */
    public void line(double distance,
                     Angle angle) {
        rotateLine(distance, last.z(), angle);
    }

    public void forwards(double distance) {
        this.line(distance, FORWARDS.add(last.z()));
    }

    public void rightwards(double distance) {
        this.line(distance, RIGHTWARDS.add(last.z()));
    }

    public void leftwards(double distance) {
        this.line(distance, LEFTWARDS.add(last.z()));
    }

    public void backwards(double distance) {
        this.line(distance, BACKWARDS.add(last.z()));
    }

    public void rotateForwards(double distance,
                               Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                FORWARDS.add(last.z())
        );
    }

    public void rotateRightwards(double distance,
                                 Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                RIGHTWARDS.add(last.z())
        );
    }

    public void rotateLeftwards(double distance,
                                Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                LEFTWARDS.add(last.z())
        );
    }

    public void rotateBackwards(double distance,
                                Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                BACKWARDS.add(last.z())
        );
    }

    public void goTo(PointXYZ target) {
        trajectories.add(new LinearTrajectory(
                target,
                speed,
                tolerance,
                angleTolerance
        ));

        last = target;
    }

    public void goToX(double x) {
        goTo(last.withX(x));
    }

    public void goToY(double y) {
        goTo(last.withY(y));
    }

    public void goTo(double x,
                     double y) {
        goTo(new PointXYZ(x, y, last.z()));
    }

    public List<Trajectory> getTrajectories() {
        return this.trajectories;
    }
}
