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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.time.Time;

/**
 * Used for recording Pathfinder's movement.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class MovementRecorder {
    private MovementRecording recording;
    private final Pathfinder pathfinder;
    private boolean isRecording = false;
    private double minDelayMs;
    private double lastRecordMs = 0;

    /**
     * Create a new {@code MovementRecorder}.
     *
     * @param pathfinder the instance of Pathfinder that's being utilized.
     *                   This is needed so that the recorder has access
     *                   to Pathfinder's data.
     * @param minDelayMs the minimum delay, in milliseconds, between recordings.
     *                   Having a higher minimum delay means this will
     *                   record fewer "snapshots" of Pathfinder's movement,
     *                   but will in turn decrease outputted file size.
     *                   Having a lower minimum delay increases how many
     *                   "snapshots" this recorder takes, at the price of
     *                   a larger file size.
     */
    public MovementRecorder(Pathfinder pathfinder,
                            double minDelayMs) {
        this.pathfinder = pathfinder;
        this.minDelayMs = minDelayMs;
    }

    /**
     * Start a new recording. This will reset the existing recording (if
     * there is one) and then begin recording a new recording.
     */
    public void start() {
        if (recording != null) recording.clear();
        else recording = new MovementRecording();
        isRecording = true;
        lastRecordMs = Time.ms();
    }

    /**
     * Stop the recording by setting the internal {@code isRecording} flag
     * to false, meaning ticking this recorder won't actually do anything.
     */
    public void stop() {
        isRecording = false;
    }

    public MovementRecording getRecording() {
        return recording;
    }

    /**
     * Tick, or update, the {@code MovementRecorder}. If the {@code isRecording}
     * flag has not been set via the {@link #start()} method, this will do
     * absolutely nothing. If the flag has been set to false using the
     * {@link #stop()} method, this method will do nothing.
     */
    public void tick() {
        if (!isRecording) return;

        double current = Time.ms();
        double elapsed = current - lastRecordMs;

        if (elapsed >= minDelayMs) {
            MovementRecord record = new MovementRecord(
                    pathfinder.getPosition(),
                    pathfinder.getVelocityXY(),
                    elapsed,
                    pathfinder.getTranslation()
            );

            recording.record(record);
            lastRecordMs = current;
        }
    }

    public double getMinDelayMs() {
        return minDelayMs;
    }

    public void setMinDelayMs(double minDelayMs) {
        this.minDelayMs = minDelayMs;
    }
}
