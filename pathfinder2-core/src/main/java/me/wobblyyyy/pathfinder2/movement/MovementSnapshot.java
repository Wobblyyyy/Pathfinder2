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

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public double getVelocityXY() {
        return velocityXY;
    }

    public void setVelocityXY(double velocityXY) {
        this.velocityXY = velocityXY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public Angle getVelocityZ() {
        return velocityZ;
    }

    public void setVelocityZ(Angle velocityZ) {
        this.velocityZ = velocityZ;
    }

    public double getAccelerationXY() {
        return accelerationXY;
    }

    public void setAccelerationXY(double accelerationXY) {
        this.accelerationXY = accelerationXY;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(double accelerationY) {
        this.accelerationY = accelerationY;
    }

    public Angle getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(Angle accelerationZ) {
        this.accelerationZ = accelerationZ;
    }
}
