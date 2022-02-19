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
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple trajectory builder used in creating trajectory groupings made solely
 * from linear trajectories.
 *
 * <p>
 * Some of the more useful features of this constructor include the
 * {@link #setSpeed(double)} method. If you want to execute one route at
 * a slow speed, and then suddenly go a lot faster for the next route, you
 * can call this method to do exactly that.
 * </p>
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

    private final PointXYZ start;

    /**
     * A list of all the trajectories.
     */
    private List<Trajectory> trajectories = new ArrayList<>();

    /**
     * The speed at which new followers should be created. This is a mutable
     * value.
     */
    private double speed;

    /**
     * Positional tolerance. Once again - mutable.
     */
    private double tolerance;

    /**
     * Angle tolerance. You guessed it - mutable!
     */
    private Angle angleTolerance;

    /**
     * Last recorded target point. Used for calculating new trajectories.
     */
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
        this.start = start;
    }

    public LinearTrajectoryBuilder() {
        this(0, 0, Angle.fromDeg(0), PointXYZ.zero());
    }

    public LinearTrajectoryBuilder(LinearTrajectoryBuilder builder) {
        this.speed = builder.speed;
        this.tolerance = builder.tolerance;
        this.angleTolerance = builder.angleTolerance;
        this.last = builder.start;
        this.start = builder.start;
        this.trajectories = new ArrayList<>(builder.trajectories);
    }

    public double getSpeed() {
        return this.speed;
    }

    public LinearTrajectoryBuilder setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public LinearTrajectoryBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    public Angle getAngleTolerance() {
        return this.angleTolerance;
    }

    public LinearTrajectoryBuilder setAngleTolerance(Angle angleTolerance) {
        this.angleTolerance = angleTolerance;

        return this;
    }

    /**
     * Move in a line and rotate to a specified heading.
     *
     * @param distance    the distance from the current point that the new
     *                    point should be drawn at.
     * @param targetAngle the angle the trajectory should attempt to turn to.
     * @param angle       the angle at which the line should be drawn.
     */
    public LinearTrajectoryBuilder rotateLine(double distance,
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

        return this;
    }

    /**
     * Move in a line. Epic, right? Of course, it's epic.
     *
     * @param distance how long the line should be.
     * @param angle    the direction the line should be drawn in.
     */
    public LinearTrajectoryBuilder line(double distance,
                                        Angle angle) {
        rotateLine(distance, last.z(), angle);

        return this;
    }

    /**
     * Relative to the robot, move forwards.
     *
     * @param distance how far to move.
     */
    public LinearTrajectoryBuilder forwards(double distance) {
        this.line(distance, FORWARDS.add(last.z()));

        return this;
    }

    /**
     * Relative to the robot, move rightwards.
     *
     * @param distance how far to move.
     */
    public LinearTrajectoryBuilder rightwards(double distance) {
        this.line(distance, RIGHTWARDS.add(last.z()));

        return this;
    }

    /**
     * Relative to the robot, move leftwards.
     *
     * @param distance how far to move.
     */
    public LinearTrajectoryBuilder leftwards(double distance) {
        this.line(distance, LEFTWARDS.add(last.z()));

        return this;
    }

    /**
     * Relative to the robot, move backwards.
     *
     * @param distance how far to move.
     */
    public LinearTrajectoryBuilder backwards(double distance) {
        this.line(distance, BACKWARDS.add(last.z()));

        return this;
    }

    /**
     * Relative to the robot, move forwards and rotate by a specified amount.
     *
     * @param distance   how far to move.
     * @param toRotateBy how much to rotate.
     */
    public LinearTrajectoryBuilder rotateForwards(double distance,
                                                  Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                FORWARDS.add(last.z())
        );

        return this;
    }

    /**
     * Relative to the robot, move rightwards and rotate by a specified amount.
     *
     * @param distance   how far to move.
     * @param toRotateBy how much to rotate.
     */
    public LinearTrajectoryBuilder rotateRightwards(double distance,
                                                    Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                RIGHTWARDS.add(last.z())
        );

        return this;
    }

    /**
     * Relative to the robot, move leftwards and rotate by a specified amount.
     *
     * @param distance   how far to move.
     * @param toRotateBy how much to rotate.
     */
    public LinearTrajectoryBuilder rotateLeftwards(double distance,
                                                   Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                LEFTWARDS.add(last.z())
        );

        return this;
    }

    /**
     * Relative to the robot, move backwards and rotate by a specified amount.
     *
     * @param distance   how far to move.
     * @param toRotateBy how much to rotate.
     */
    public LinearTrajectoryBuilder rotateBackwards(double distance,
                                                   Angle toRotateBy) {
        rotateLine(
                distance,
                last.z().add(toRotateBy),
                BACKWARDS.add(last.z())
        );

        return this;
    }

    /**
     * Go to a specific point.
     *
     * @param target the target point.
     */
    public LinearTrajectoryBuilder goTo(PointXYZ target) {
        trajectories.add(new LinearTrajectory(
                target,
                speed,
                tolerance,
                angleTolerance
        ));

        last = target;

        return this;
    }

    /**
     * Go to a specified X value.
     *
     * @param x the X value.
     */
    public LinearTrajectoryBuilder goToX(double x) {
        goTo(last.withX(x));

        return this;
    }

    /**
     * Go to a specified Y value.
     *
     * @param y the Y value.
     */
    public LinearTrajectoryBuilder goToY(double y) {
        goTo(last.withY(y));

        return this;
    }

    /**
     * Go to a specified X and Y coordinate without paying any attention
     * to the heading of the robot.
     *
     * @param x target X coordinate.
     * @param y target Y coordinate.
     */
    public LinearTrajectoryBuilder goTo(double x,
                                        double y) {
        goTo(new PointXYZ(x, y, last.z()));

        return this;
    }

    /**
     * Get a list of all the trajectories that have been generated.
     *
     * @return a list of all the generated trajectories.
     */
    public List<Trajectory> getTrajectories() {
        return this.trajectories;
    }
}
