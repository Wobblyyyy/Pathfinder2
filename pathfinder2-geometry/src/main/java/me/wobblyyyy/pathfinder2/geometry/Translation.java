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

/**
 * A two-dimensional translation. Robots should be capable of receiving
 * these translations and moving appropriately.
 *
 * <p>
 * Translations are defined by three components.
 * <ul>
 *     <li>X translation (translation along the X axis)</li>
 *     <li>Y translation (translation along the Y axis)</li>
 *     <li>Z translation (rotation around the center of the robot)</li>
 * </ul>
 * Please note, however, that these X and Y axes are relative to the robot.
 * A translation with a Y value of 1 would make the robot go "forwards,"
 * forwards, of course, meaning whatever direction the robot "thinks"
 * is forwards - not absolute positioning.
 * </p>
 *
 * <p>
 * Absolute translations can be converted to relative translations using the
 * following methods:
 * <ul>
 *     <li>
 *         {@link #absoluteToRelative(Translation, Angle)}:
 *         Convert an absolute translation into a relative translation
 *         by using the robot's current heading.
 *     </li>
 *     <li>
 *         {@link #toRelative(Angle)}:
 *         Convert the calling translation into a relative translation
 *         by using the robot's current heading.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * {@code Translation}s are the core of Pathfinder's movement. Robots operate
 * based exclusively on {@code Translation} instances. Such, translations
 * aren't confined to being used in autonomous navigation. If you'd like
 * to operate the robot, say, in a manual control mode, you can utilize
 * translations (and the {@link #absoluteToRelative(Translation, Angle)} method)
 * to do exactly that.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class Translation {
    /**
     * The robot's translation along its X axis.
     */
    private final double vx;

    /**
     * The robot's translation along its Y axis.
     */
    private final double vy;

    /**
     * The robot's rotation around its center of rotation.
     */
    private final double vz;

    /**
     * Create a new {@code Translation} using two translational values.
     *
     * @param vx the robot's translation along its X axis.
     * @param vy the robot's translation along its Y axis.
     */
    public Translation(double vx,
                       double vy) {
        this(vx, vy, 0);
    }

    /**
     * Create a new {@code Translation} using three translational values.
     *
     * @param vx the robot's translation along its X axis.
     * @param vy the robot's translation along its Y axis.
     * @param vz the robot's rotation around its center of rotation.
     */
    public Translation(double vx,
                       double vy,
                       double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    /**
     * Convert an absolute translation into a relative translation.
     *
     * <p>
     * You may be more familiar with this concept if we use the term
     * "field-relative" and "robot-relative". This method converts a field
     * relative translation into a robot relative one.
     * </p>
     *
     * @param translation the original (absolute) {@code Translation}.
     * @param heading     the heading the robot is currently facing. This value
     *                    should almost always come directly from the robot's
     *                    odometry system.
     * @return a relative translation.
     * @see <a href="https://pdocs.kauailabs.com/navx-mxp/examples/field-oriented-drive/">Field-oriented drive</a>
     */
    public static Translation absoluteToRelative(Translation translation,
                                                 Angle heading) {
        // Create a new angle by adding the translation's angle to the
        // robot's heading. Once again - pretty swaggy.
        Angle newAngle = translation.angle().add(heading);

        // Return a new translation by using some fancy trig or something.
        // Remember: COS = X, SIN = Y.
        // Also, this code roughly corresponds with the stuff in the
        // PointXY class' rotation method. Just as a heads-up.
        return new Translation(
                newAngle.cos() * translation.magnitude(),
                newAngle.sin() * translation.magnitude(),
                translation.vz()
        );
    }

    /**
     * Get the translation's X component.
     *
     * @return the translation's X component.
     */
    public double vx() {
        return this.vx;
    }

    /**
     * Get the translation's Y component.
     *
     * @return the translation's Y component.
     */
    public double vy() {
        return this.vy;
    }

    /**
     * Get the translation's Z component.
     *
     * @return the translation's Z component.
     */
    public double vz() {
        return this.vz;
    }

    /**
     * Get the translation's magnitude.
     *
     * <p>
     * This value is calculated by using the {@link Math#hypot(double, double)}
     * method. It assumes the {@code vx} and {@code vy} values are legs in
     * a right triangle and calculates a theoretical hypotenuse based on that.
     * This is the distance from the origin, or the magnitude at which
     * the translation operates.
     * </p>
     *
     * @return the {@code Translation}'s magnitude, or total distance from
     * zero. This method uses the {@link Math#hypot(double, double)} method.
     */
    public double magnitude() {
        return Math.hypot(this.vx, this.vy);
    }

    /**
     * Create a point using the component vx and vy translation values.
     *
     * @return a point located at ({@link #vx()}, {@link #vy()}).
     */
    public PointXY point() {
        return new PointXY(this.vx, this.vy);
    }

    /**
     * Create an imaginary point at (vx, vy) and determine the angle from
     * an origin (0, 0) to that point.
     *
     * @return the angle between an imaginary point and an imaginary origin.
     * That doesn't make sense, does it? No. Probably not.
     */
    public Angle angle() {
        return PointXY.zero().angleTo(point());
    }

    /**
     * Convert the calling translation into a relative translation.
     *
     * @param heading the heading of the robot. This should come directly
     *                from the odometry system the robot uses.
     * @return a relative translation generated by converting the calling
     * translation into a relative translation. Swag.
     * @see #absoluteToRelative(Translation, Angle)
     */
    public Translation toRelative(Angle heading) {
        return absoluteToRelative(this, heading);
    }
}
