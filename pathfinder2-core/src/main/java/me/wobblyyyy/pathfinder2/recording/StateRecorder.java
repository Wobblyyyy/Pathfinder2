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
 * back a timed set of states. This functions by using a map containing
 * {@link Recordable}s. Each {@link Recordable}, as its name would suggest,
 * can be recorded. To add, each {@link Recordable} can play back an existing
 * recording.
 *
 * <p>
 * A {@code StateRecorder} exists in one of three states:
 * <ul>
 *     <li>Idle</li>
 *     <li>Recording</li>
 *     <li>Playing back</li>
 * </ul>
 * By default, it's in the Idle state. Recording and playback can be
 * started and stoppeed using the following methods:
 * <ul>
 *     <li>{@link #startRecording(int, int)}</li>
 *     <li>{@link #stopRecording()}</li>
 *     <li>{@link #startPlayback(StateRecording)}</li>
 *     <li>{@link #stopPlayback()}</li>
 * </ul>
 * </p>
 *
 * <p>
 * By default, {@code StateRecorder} will automatically "optimize" ongoing
 * recordings by removing redundant information. Specifically, if two adjacent
 * records have the same value for a {@code Recordable}, only the first of
 * those records will be preserved. This helps to cut down on recording output
 * size. This functionality can be configured by changing a flag with
 * {@link #setShouldOptimize(boolean)}.
 * </p>
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

    /**
     * Remove a node based on a {@code String} key.
     *
     * @param key the key of the node to remove.
     * @return the removed node. If the node was not contained in the map,
     * this method will return null.
     */
    public Recordable<?> removeNode(String key) {
        return nodes.remove(key);
    }

    /**
     * Get all of the recordable nodes in the {@code StateRecorder}.
     *
     * @return {@link #nodes}.
     */
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

    /**
     * Start a recording. Note that this will overwrite any existing recording.
     * A recording cannot be started if there is already an ongoing recording.
     * Additionally, a recording cannot be started if a recording is currently
     * being played back. Use {@link #stopRecording()} to stop recording.
     *
     * <p>
     * After finishing a recording, use {@link #stopRecording()} to stop
     * the recording from being continued. This method will return a
     * {@link StateRecording}, which you can save elsewhere. If you'd like to
     * access this recording later, use {@link #getRecording()}. Please note
     * that if you start another recording, or if you start the playback of
     * another recording, {@link #getRecording()} will return that recording
     * instead.
     * </p>
     *
     * @param recordingIntervalMs        the interval, in milliseconds,
     *                                   between each record. A higher
     *                                   interval decreases the accuracy of
     *                                   the recording, while a lower interval
     *                                   increases the size of the outputted
     *                                   recording. A good place to start
     *                                   is 100.
     * @param estimatedRecordingLengthMs how long the recording is estimated
     *                                   to last, in milliseconds. This value
     *                                   doesn't matter all that much - it's
     *                                   just used in allocating an array for
     *                                   the list of records.
     * @return {@code this}, used for method chaining.
     */
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

    /**
     * Set the {@link #isRecording} flag to false, stopping any ongoing
     * recording.
     *
     * @return the recording that was just stopped.
     */
    public StateRecording stopRecording() {
        isRecording = false;

        return recording;
    }

    /**
     * Start the playback of a given recording. This will overwrite any
     * existing recording.
     *
     * @param recording the recording to play.
     * @return {@code this}, used for method chaining.
     */
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

    /**
     * Stop any ongoing playback by setting the {@link #isPlayingBack} flag
     * to {@code false}.
     *
     * @return {@code this}, used for method chaining.
     */
    public StateRecorder stopPlayback() {
        isPlayingBack = false;

        return this;
    }

    private void cleanseRecord(StateRecord record, StateRecord lastRecord) {
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
    }

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

        if (recording == null) {
            return;
        }

        if (elapsedTimeMs >= recording.getIntervalMs()) {
            if (isRecording) {
                StateRecord record = createNewRecord();

                lastRecord = recording.getLastRecord();
                recording.addRecord(record);

                cleanseRecord(record, lastRecord);
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

    /**
     * Update the {@code StateRecorder}. If a recording is currently underway,
     * this will record the robot's current state and add it to the ongoing
     * recording. If the {@code StateRecorder} is currently playing back
     * an existing recording, this will play back the next "frame" of the
     * recording.
     */
    public void update() {
        double currentTimeMs = Time.ms();

        update(currentTimeMs);
    }

    /**
     * Get the {@code StateRecorder}'s current recording. If a recording
     * is currently underway, this method will return that recording. If a
     * recording was recorded previously and {@code startPlayback} has not
     * been called, this method will return that recording. If playback
     * has started, this will return the recording being played back.
     *
     * @return the {@code StateRecorder}'s current recording.
     */
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

    /**
     * Set if the {@code StateRecorder} should automatically optimize the
     * recording by removing redundant records. This helps to save recording
     * size by reducing the amount of records in the recording.
     *
     * <p>
     * If optimization is enabled, and a {@code Motor} was recorded having
     * the same power value for two adjacent records, the second record would
     * be removed. Because it has the same value as the first record, there's
     * no point in having another record.
     * </p>
     *
     * @param shouldOptimize true if optimization should be enabled. Otherwise,
     *                       false. Optimization is enabled by default.
     * @return {@code this}, used for method chaining.
     */
    public StateRecorder setShouldOptimize(boolean shouldOptimize) {
        this.shouldOptimize = shouldOptimize;

        return this;
    }
}
