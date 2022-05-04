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

import me.wobblyyyy.pathfinder2.recording.recordables.RecordableBoolean;
import me.wobblyyyy.pathfinder2.recording.recordables.RecordableDouble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStateRecorder {

    @Test
    public void testRecordSingleBoolean() {
        StateRecorder recorder = new StateRecorder();

        RecordableBoolean b = new RecordableBoolean(false);

        recorder.putNode("b", b);
        recorder.startRecording(50, 50);

        b.setRecordingValue(true);
        recorder.update(50);

        b.setRecordingValue(false);
        recorder.update(100);

        b.setRecordingValue(true);
        recorder.update(150);

        b.setRecordingValue(false);

        StateRecording recording = recorder.stopRecording();

        Assertions.assertEquals(false, b.getRecordingValue());
        recorder.startPlayback(recording);

        recorder.update(200);
        Assertions.assertEquals(true, b.getRecordingValue());

        recorder.update(250);
        Assertions.assertEquals(false, b.getRecordingValue());

        recorder.update(300);
        Assertions.assertEquals(true, b.getRecordingValue());

        recorder.stopPlayback();
    }

    @Test
    public void testRecordSingleDoublej() {
        StateRecorder recorder = new StateRecorder();

        RecordableDouble d = new RecordableDouble(0.0);

        recorder.putNode("d", d);
        recorder.startRecording(50, 50);

        d.setRecordingValue(0.125);
        recorder.update(50);

        d.setRecordingValue(0.25);
        recorder.update(100);

        d.setRecordingValue(0.5);
        recorder.update(150);

        d.setRecordingValue(0.0);

        StateRecording recording = recorder.stopRecording();

        Assertions.assertEquals(0.0, d.getRecordingValue());
        recorder.startPlayback(recording);

        recorder.update(200);
        Assertions.assertEquals(0.125, d.getRecordingValue());

        recorder.update(250);
        Assertions.assertEquals(0.25, d.getRecordingValue());

        recorder.update(300);
        Assertions.assertEquals(0.5, d.getRecordingValue());

        recorder.stopPlayback();
    }
}
