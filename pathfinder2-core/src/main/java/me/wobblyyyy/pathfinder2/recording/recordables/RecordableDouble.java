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

public class RecordableDouble implements Recordable<Double> {
    private double value;

    public RecordableDouble(double value) {
        this.value = value;
    }

    public RecordableDouble() {
        this(0.0);
    }

    @Override
    public Double getRecordingValue() {
        return value;
    }

    @Override
    public void setRecordingValue(Object obj) {
        if (obj instanceof Double) {
            Double d = (Double) obj;
            value = d;
        }
    }
}
