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
import me.wobblyyyy.pathfinder2.math.Velocity;

import java.io.Serializable;

/**
 * A snapshot of a robot's movement. Primarily, the robot's velocity and
 * acceleration, in units per second.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class MovementSnapshot implements Serializable {
    private Velocity velocity = new Velocity(0, Angle.DEG_0);
    private double velocityXY;
    private double velocityX;
    private double velocityY;
    private Angle velocityZ = Angle.DEG_0;

    private double accelerationXY;
    private double accelerationX;
    private double accelerationY;
    private Angle accelerationZ = Angle.DEG_0;

    public Velocity getVelocity() {
        return velocity;
    }

    public MovementSnapshot setVelocity(Velocity velocity) {
        this.velocity = velocity;
        return this;
    }

    public double getVelocityXY() {
        return velocityXY;
    }

    public MovementSnapshot setVelocityXY(double velocityXY) {
        this.velocityXY = velocityXY;
        return this;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public MovementSnapshot setVelocityX(double velocityX) {
        this.velocityX = velocityX;
        return this;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public MovementSnapshot setVelocityY(double velocityY) {
        this.velocityY = velocityY;
        return this;
    }

    public Angle getVelocityZ() {
        return velocityZ;
    }

    public MovementSnapshot setVelocityZ(Angle velocityZ) {
        this.velocityZ = velocityZ;
        return this;
    }

    public double getAccelerationXY() {
        return accelerationXY;
    }

    public MovementSnapshot setAccelerationXY(double accelerationXY) {
        this.accelerationXY = accelerationXY;
        return this;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public MovementSnapshot setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
        return this;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public MovementSnapshot setAccelerationY(double accelerationY) {
        this.accelerationY = accelerationY;
        return this;
    }

    public Angle getAccelerationZ() {
        return accelerationZ;
    }

    public MovementSnapshot setAccelerationZ(Angle accelerationZ) {
        this.accelerationZ = accelerationZ;
        return this;
    }
}
