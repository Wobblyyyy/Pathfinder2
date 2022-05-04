/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.recording.recordables;

import me.wobblyyyy.pathfinder2.recording.Recordable;

public class RecordableBoolean implements Recordable<Boolean> {
    private boolean value;

    public RecordableBoolean(boolean value) {
        this.value = value;
    }

    public RecordableBoolean() {
        this(false);
    }

    @Override
    public Boolean getRecordingValue() {
        return value;
    }

    @Override
    public void setRecordingValue(Object obj) {
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            value = b;
        }
    }
}
