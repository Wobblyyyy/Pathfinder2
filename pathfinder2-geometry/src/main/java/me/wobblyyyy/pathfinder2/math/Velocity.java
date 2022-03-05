/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * A measurement of velocity - motion in a given direction. This velocity can
 * have positive or negative speed values and a direction relative to the
 * absolute coordinate plane Pathfinder uses.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Velocity implements Serializable {
    /**
     * The velocity's speed.
     */
    private final double speed;

    /**
     * The velocity's direction.
     */
    private final Angle direction;

    /**
     * Create a new {@code Velocity}.
     *
     * @param speed     the speed the object is moving at. This can be either
     *                  a negative or positive number.
     * @param direction the direction the object is moving in.
     */
    public Velocity(double speed, Angle direction) {
        this.speed = speed;
        this.direction = direction;
    }

    /**
     * Create a new {@code Velocity} by blending together two velocities.
     * This averages the speed and direction values together.
     *
     * @param a one of two velocities to blend.
     * @param b one of two velocities to blend.
     * @return a blended velocity.
     */
    public static Velocity blend(Velocity a, Velocity b) {
        double speedA = a.getSpeed();
        double speedB = b.getSpeed();

        double speedBlended = (speedA + speedB) / 2d;

        Angle directionA = a.getDirection();
        Angle directionB = b.getDirection();

        double aDeg = directionA.fix().deg();
        double bDeg = directionB.fix().deg();

        Angle directionBlended = Angle.fromDeg((aDeg + bDeg) / 2);

        return new Velocity(speedBlended, directionBlended);
    }

    /**
     * Get the velocity's speed component.
     *
     * @return the velocity's speed.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Get the velocity's direction component.
     *
     * @return the velocity's direction.
     */
    public Angle getDirection() {
        return this.direction;
    }

    /**
     * Get the absolute value of the velocity.
     *
     * @return the absolute value of the velocity.
     */
    public double getAbsoluteSpeed() {
        return Math.abs(speed);
    }

    public PointXY convertFromElapsedMilliseconds(double elapsedMilliseconds) {
        return convertFromElapsedSeconds(elapsedMilliseconds / 1_000);
    }

    public PointXY convertFromElapsedSeconds(double elapsedSeconds) {
        return applyVelocitySeconds(PointXY.zero(), elapsedSeconds);
    }

    public PointXY applyVelocityMilliseconds(
        PointXY original,
        double elapsedMilliseconds
    ) {
        return applyVelocitySeconds(original, elapsedMilliseconds / 1_000);
    }

    public PointXY applyVelocitySeconds(
        PointXY original,
        double elapsedSeconds
    ) {
        return original.inDirection(speed * elapsedSeconds, direction);
    }

    /**
     * Blend this velocity with another velocity.
     *
     * @param velocity the velocity to blend with.
     * @return the blended velocity.
     * @see Velocity#blend(Velocity, Velocity)
     */
    public Velocity blend(Velocity velocity) {
        return Velocity.blend(this, velocity);
    }

    /**
     * Convert the {@code Velocity} into a point by using speed as a distance
     * value and the direction as a direction to draw the point upon.
     *
     * @return convert the {@code Velocity} into a PointXY.
     */
    public PointXY asPointXY() {
        return PointXY.zero().inDirection(speed, direction);
    }

    /**
     * Convert the {@code Velocity} into a point by using speed as a distance
     * value and the direction as a direction to draw the point upon.
     *
     * @return convert the {@code Velocity} into a PointXYZ. Z is equal
     * to the direction value.
     */
    public PointXYZ asPointXYZ() {
        return PointXYZ
            .zero()
            .inDirection(speed, direction)
            .withHeading(direction);
    }

    public Velocity nearestVelocity(
        List<Velocity> velocities,
        Velocity velocity
    ) {
        return nearestVelocities(velocities, velocity, 1).get(0);
    }

    public List<Velocity> nearestVelocities(
        List<Velocity> velocities,
        Velocity velocity,
        int howMany
    ) {
        if (howMany < velocities.size()) throw new IllegalArgumentException(
            "Cannot return more velocities than were provided!"
        );

        PointXY velocityPoint = velocity.asPointXY();

        velocities.sort(
            (v1, v2) -> {
                PointXY point1 = v1.asPointXY();
                PointXY point2 = v2.asPointXY();

                double distance1 = PointXY.distance(velocityPoint, point1);
                double distance2 = PointXY.distance(velocityPoint, point2);

                return Double.compare(distance1, distance2);
            }
        );

        List<Velocity> nearestVelocities = new ArrayList<>(howMany);

        for (int i = 0; i < howMany; i++) {
            nearestVelocities.set(i, velocities.get(0));
        }

        return nearestVelocities;
    }
}
