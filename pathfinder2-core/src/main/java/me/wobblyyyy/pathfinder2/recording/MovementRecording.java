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

public class MovementRecording implements Serializable {
    private final List<MovementRecord> recording = new ArrayList<>(100);

    public void record(MovementRecord record) {
        recording.add(record);
    }

    public List<MovementRecord> getRecording() {
        return recording;
    }

    public void clear() {
        recording.clear();
    }
}
