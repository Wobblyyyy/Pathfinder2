/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.components;

import me.wobblyyyy.pathfinder2.time.Time;

/**
 * An abstract implementation of {@code Servo}.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public abstract class AbstractServo implements Servo {
    private double lastPosMs = 0.0;
    private double lastPosition = 0.0;

    private double currentPosMs = 0.0;
    private double currentPosition = 0.0;

    public abstract void abstractSetPosition(double position);

    public double abstractGetPosition() {
        return currentPosition;
    }

    public void setPositionWithTime(double position, double currentTimeMs) {
        abstractSetPosition(position);
        lastPosition = currentPosition;
        lastPosMs = currentPosMs;
        currentPosition = position;
        currentPosMs = currentTimeMs;
    }

    /**
     * Set the position of the servo.
     *
     * @param position the position of the servo. This position value should
     *                 be greater than or equal to 0 and less than or equal
     *                 to 1.
     */
    @Override
    public void setPosition(double position) {
        setPositionWithTime(position, Time.ms());
    }

    /**
     * Get the last position that the servo was commanded to move to.
     *
     * @return the last position the servo was commanded to move to.
     */
    @Override
    public double getPosition() {
        return abstractGetPosition();
    }

    @Override
    public double getVelocity() {
        double elapsedDistance = currentPosition - lastPosition;
        double elapsedTimeMs = currentPosMs - lastPosMs;
        double elapsedSeconds = elapsedTimeMs / 1_000;

        return elapsedDistance / elapsedSeconds;
    }
}
