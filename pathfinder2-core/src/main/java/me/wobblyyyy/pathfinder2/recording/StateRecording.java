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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A simple container for several {@link StateRecord}s.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public class StateRecording implements Serializable {
    private final int intervalMs;
    private final List<StateRecord> records;

    public StateRecording(int intervalMs, List<StateRecord> records) {
        this.intervalMs = intervalMs;
        this.records = records;
    }

    public StateRecording(int intervalMs) {
        this(intervalMs, new ArrayList<>());
    }

    public int getIntervalMs() {
        return intervalMs;
    }

    public List<StateRecord> getRecords() {
        return records;
    }

    public StateRecord getLastRecord() {
        int index = records.size() - 1;

        if (index < 0) {
            return null;
        }

        return records.get(index);
    }

    public StateRecording addRecord(StateRecord record) {
        ValidationUtils.validate(record, "record");

        records.add(record);

        return this;
    }
}
