/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.recording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.wobblyyyy.pathfinder2.exceptions.IllegalStateRecordException;
import me.wobblyyyy.pathfinder2.exceptions.StateRecorderException;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.time.Time;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * Used in recording the state of a robot. If you've already recorded a
 * state, you can then play that state back. Additionally, you can play
 * back a timed set of states.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public class StateRecorder {
    private final Map<String, Recordable<?>> nodes = new HashMap<>();

    private boolean shouldOptimize = true;
    private StateRecord lastRecord = null;
    private int index = 0;
    private double lastTimeMs = 0;
    private StateRecording recording = null;
    private boolean isRecording = false;
    private boolean isPlayingBack = false;

    public StateRecorder() {}

    /**
     * Add a node to the {@code StateRecorder}.
     *
     * @param key        the node's key.
     * @param recordable the node's {@code Recordable}.
     * @return {@code this}, used for method chaining.
     */
    public StateRecorder putNode(String key, Recordable<?> recordable) {
        if (nodes.containsKey(key)) throw new IllegalArgumentException(
            "Could not add node with key " +
            key +
            " because there already " +
            "exists a node with that key!"
        );

        if (recordable == null) throw new NullPointerException(
            "Could not add a node with key " +
            key +
            " because the provided " +
            "Recordable was null!"
        );

        nodes.put(key, recordable);

        return this;
    }

    public Recordable<?> removeNode(String key) {
        return nodes.remove(key);
    }

    public Map<String, Recordable<?>> getNodes() {
        return nodes;
    }

    /**
     * Create and return a new {@code StateRecord}. This new record will
     * represent the current state of the robot (or at least all of the
     * parts of the robot that are being recorded).
     *
     * @return a new {@code StateRecord}, created by calling the
     * {@link Recordable#getRecordingValue()} value on each of the
     * {@link Recordable}s in the {@link #nodes} map.
     */
    public StateRecord createNewRecord() {
        StateRecord record = new StateRecord(this);

        Logger.trace(
            StateRecorder.class,
            "Creating record <%s>",
            record.toString()
        );

        for (Map.Entry<String, Recordable<?>> entry : nodes.entrySet()) {
            String name = entry.getKey();
            Recordable<?> recordable = entry.getValue();
            Object value = recordable.getRecordingValue();

            Logger.trace(
                StateRecorder.class,
                "Creating record with value <%s> for node <%s>",
                value,
                name
            );

            record.put(name, value);
        }

        return record;
    }

    private void ensureNodeNameIsValid(String name) {
        if (!nodes.containsKey(name)) throw new IllegalStateRecordException(
            "Could not find a Recordable with name <" +
            name +
            ">. Make " +
            "sure you used the putNode(String, Recordable) method before " +
            "trying to play back a recording."
        );
    }

    /**
     * Apply a single record to an entire robot. This will iterate over
     * the map in the provided {@code StateRecord} and use the
     * {@link Recordable#setRecordingValue(Object)}. Because the
     * {@code StateRecord} contains a {@code Map<String, Object>}, each
     * {@link Recordable} is referenced by {@link String} name. If the name
     * is not contained in {@link #nodes}, an exception will be thrown.
     * Specifically, a {@link IllegalStateRecordException}.
     *
     * @param record the record to apply.
     * @return {@code this}, used for method chaining.
     */
    public StateRecorder applyRecord(StateRecord record) {
        ValidationUtils.validate(record, "record");

        Logger.trace(
            StateRecorder.class,
            "Applying record <%s>",
            record.toString()
        );

        for (Map.Entry<String, Object> entry : record.getMap().entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            ensureNodeNameIsValid(name);

            Logger.trace(
                StateRecorder.class,
                "Setting value of <%s> to node with name <%s>",
                value,
                name
            );

            Recordable<?> recordable = nodes.get(name);
            recordable.setRecordingValue(value);
        }

        return this;
    }

    public StateRecorder startRecording(
        int recordingIntervalMs,
        int estimatedRecordingLengthMs
    ) {
        if (isRecording) throw new StateRecorderException(
            "Cannot start a new recording because the StateRecorder is " +
            "already recording! Use stopRecording() first!"
        );

        if (isPlayingBack) throw new StateRecorderException(
            "Cannot start a new recording because the StateRecorder is " +
            "currently playing back an existing recording!"
        );

        int size = (estimatedRecordingLengthMs + 1_000) / recordingIntervalMs;

        recording = new StateRecording(size);

        isRecording = true;
        lastTimeMs = 0;
        index = 0;

        return this;
    }

    public StateRecording stopRecording() {
        isRecording = false;

        return recording;
    }

    public StateRecorder startPlayback(StateRecording recording) {
        ValidationUtils.validate(recording, "recording");

        if (isPlayingBack) throw new StateRecorderException(
            "Cannot start playback because the StateRecorder is already " +
            "playing back a recording! Use stopPlayback() first."
        );

        if (isRecording) throw new StateRecorderException(
            "Cannot start playback because the StateRecorder is currently " +
            "recording! Use stopRecording() first."
        );

        this.recording = recording;

        isPlayingBack = true;
        lastTimeMs = 0;
        index = 0;

        return this;
    }

    public StateRecorder stopPlayback() {
        isPlayingBack = false;

        return this;
    }

    private void cleanseRecord(StateRecord record) {}

    /**
     * Update the {@code StateRecorder}. If a recording is currently underway,
     * this will record the robot's current state and add it to the ongoing
     * recording. If the {@code StateRecorder} is currently playing back
     * an existing recording, this will play back the next "frame" of the
     * recording.
     *
     * @param currentTimeMs the current timestamp. This is used in calculating
     * the amount of elapsed time, so that the recording is processed as
     * evenly as possible.
     */
    public void update(double currentTimeMs) {
        double elapsedTimeMs = currentTimeMs - lastTimeMs;

        if (elapsedTimeMs >= recording.getIntervalMs()) {
            if (isRecording) {
                StateRecord record = createNewRecord();

                lastRecord = recording.getLastRecord();
                recording.addRecord(record);

                if (shouldOptimize) {
                    if (lastRecord != null) {
                        Map<String, Object> lastMap = lastRecord.getMap();
                        Map<String, Object> map = record.getMap();

                        List<String> toRemove = new ArrayList<>(map.size());
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            if (!lastMap.containsKey(key)) {
                                continue;
                            }

                            Object lastValue = lastMap.get(key);

                            if (lastValue.equals(value)) {
                                toRemove.add(key);
                            }
                        }

                        for (String str : toRemove) {
                            map.remove(str);
                        }
                    }
                }
            } else if (isPlayingBack) {
                int recordingSize = recording.getRecords().size();

                if (index >= recordingSize) {
                    isPlayingBack = false;
                    return;
                }

                StateRecord record = recording.getRecords().get(index);
                applyRecord(record);

                index++;
            }

            lastTimeMs = currentTimeMs;
        }
    }

    public void update() {
        double currentTimeMs = Time.ms();

        update(currentTimeMs);
    }

    public StateRecording getRecording() {
        return recording;
    }

    /**
     * Is the {@code StateRecorder} currently recording?
     *
     * @return true if the {@code StateRecorder} is currently recording.
     * Otherwise, false.
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Is the {@code StateRecorder} currently playing back a recording?
     *
     * @return true if the {@code StateRecorder} is currently playing back
     * a recording. Otherwise, false.
     */
    public boolean isPlayingBack() {
        return isPlayingBack;
    }

    public StateRecorder setShouldOptimize(boolean shouldOptimize) {
        this.shouldOptimize = shouldOptimize;

        return this;
    }
}
