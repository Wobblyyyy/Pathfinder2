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
 * A measurement of velocity - motion in a given direction.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Velocity {
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
    public Velocity(double speed,
                    Angle direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public static Velocity blend(Velocity a,
                                 Velocity b) {
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

    public double getSpeed() {
        return this.speed;
    }

    public Angle getDirection() {
        return this.direction;
    }

    public double getAbsoluteSpeed() {
        return Math.abs(speed);
    }

    public PointXY convertFromElapsedMilliseconds(double elapsedMilliseconds) {
        return convertFromElapsedSeconds(elapsedMilliseconds / 1_000);
    }

    public PointXY convertFromElapsedSeconds(double elapsedSeconds) {
        return applyVelocitySeconds(PointXY.zero(), elapsedSeconds);
    }

    public PointXY applyVelocityMilliseconds(PointXY original,
                                             double elapsedMilliseconds) {
        return applyVelocitySeconds(original, elapsedMilliseconds / 1_000);
    }

    public PointXY applyVelocitySeconds(PointXY original,
                                        double elapsedSeconds) {
        return original.inDirection(
                speed * elapsedSeconds,
                direction
        );
    }

    public Velocity blend(Velocity velocity) {
        return Velocity.blend(this, velocity);
    }
}
