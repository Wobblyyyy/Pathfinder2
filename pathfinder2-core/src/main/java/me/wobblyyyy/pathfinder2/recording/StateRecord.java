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
import java.util.HashMap;
import java.util.Map;

/**
 * A record for the state of an entire robot (or at least all of the parts of
 * the robot that are being recorded).
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public class StateRecord implements Serializable {
    private final Map<String, Object> map;

    public StateRecord(Map<String, Object> map) {
        this.map = map;
    }

    public StateRecord(StateRecorder recorder) {
        this(new HashMap<>(recorder.getNodes().size()));
    }

    public StateRecord() {
        this(new HashMap<>());
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public StateRecord put(String key, Object value) {
        map.put(key, value);

        return this;
    }
}
