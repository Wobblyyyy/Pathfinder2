/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.recording;

import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

import java.io.Serializable;

/**
 * A snapshot of Pathfinder's movement.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class MovementRecord implements Serializable {
    private PointXYZ position;
    private double velocity;
    private double elapsedMs;
    private Translation translation;

    /**
     * Create a new {@code MovementRecord} without any data.
     */
    public MovementRecord() {

    }

    /**
     * Create a new {@code MovementRecord} with data.
     *
     * @param position    the current position of the robot.
     * @param velocity    the current velocity of the robot.
     * @param elapsedMs   the amount of time, in milliseconds, that have
     *                    elapsed since the last record.
     * @param translation the current translation of the robot.
     */
    public MovementRecord(PointXYZ position,
                          double velocity,
                          double elapsedMs,
                          Translation translation) {
        if (position == null)
            throw new NullPointException(
                    "attempted to create a MovementRecord with a null " +
                            "position, make sure this position isn't null"
            );
        if (translation == null)
            throw new NullPointerException(
                    "attempted to create a MovementRecord with a null " +
                            "translation, make sure this translation isn't null"
            );

        this.position = position;
        this.velocity = velocity;
        this.elapsedMs = elapsedMs;
        this.translation = translation;
    }

    public PointXYZ getPosition() {
        return position;
    }

    public void setPosition(PointXYZ position) {
        this.position = position;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getElapsedMs() {
        return elapsedMs;
    }

    public void setElapsedMs(double elapsedMs) {
        this.elapsedMs = elapsedMs;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}
