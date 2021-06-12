/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.recorder;

public class Record {
    private final float x;
    private final float y;
    private final float z;

    public Record(final double x,
                  final double y,
                  final double z) {
        this(
                (float) x,
                (float) y,
                (float) z
        );
    }

    public Record(final float x,
                  final float y,
                  final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public float z() {
        return this.z;
    }
}
