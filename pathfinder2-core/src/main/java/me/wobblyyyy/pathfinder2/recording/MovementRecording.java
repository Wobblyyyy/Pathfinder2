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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A recording of Pathfinder's movement. A {@code MovementRecording} is little
 * more than a wrapper for a {@link List} that stores many instances of
 * {@link MovementRecord}. {@code MovementRecording}s can be serialized so
 * that they can be imported or exported, but it's generally suggested you
 * make use of a library like GSON instead of Java's built-in serialization
 * tooling so that your recordings are more accessible to non-JDK-based
 * environments.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class MovementRecording implements Serializable {
    private final List<MovementRecord> recording = new ArrayList<>(100);

    /**
     * Record a single {@link MovementRecording}. This will add the supplied
     * recording to the internal {@link List} of {@link MovementRecord}s.
     *
     * @param record the record to add.
     */
    public void record(MovementRecord record) {
        recording.add(record);
    }

    /**
     * Get a list of all of the recorded {@link MovementRecord}s.
     *
     * @return a {@code List}, containing all of the {@link MovementRecord}s
     * that have been recorded.
     */
    public List<MovementRecord> getRecording() {
        return recording;
    }

    /**
     * Clear the entire list of {@link MovementRecord}s.
     */
    public void clear() {
        recording.clear();
    }
}
