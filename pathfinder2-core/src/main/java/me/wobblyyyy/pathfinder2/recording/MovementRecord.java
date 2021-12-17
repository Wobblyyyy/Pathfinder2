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

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

import java.io.Serializable;

public class MovementRecord implements Serializable {
    private PointXYZ position;
    private double velocity;
    private double elapsedMs;
    private Translation translation;

    public MovementRecord() {

    }

    public MovementRecord(PointXYZ position,
                          double velocity,
                          double elapsedMs,
                          Translation translation) {
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
