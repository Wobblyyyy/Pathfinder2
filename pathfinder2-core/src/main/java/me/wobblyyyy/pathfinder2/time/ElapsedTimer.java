/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.time;

/**
 * A very simple timer that keeps track of how much time has been elapsed.
 *
 * @author Colin Robertson
 * @since 0.2.4
 */
public class ElapsedTimer {
    private long start = 0;

    public ElapsedTimer() {
        this(false);
    }

    public ElapsedTimer(boolean shouldStart) {
        if (shouldStart) {
            start();
        }
    }

    public void start() {
        start = Time.longMs();
    }

    public long getElapsed() {
        long current = Time.longMs();

        return current - start;
    }

    public boolean isElapsedMoreThan(long time) {
        return time >= getElapsed();
    }

    public boolean isElapsedLessThan(long time) {
        return time <= getElapsed();
    }

    public boolean isElapsedMoreThan(double time) {
        return time >= getElapsed();
    }

    public boolean isElapsedLessThan(double time) {
        return time <= getElapsed();
    }
}
