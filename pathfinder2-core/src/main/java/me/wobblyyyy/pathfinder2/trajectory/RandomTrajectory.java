/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Completely useless. That's all there is to say. There is absolutely no
 * reason to use this, ever. Like. Not even ironically. Are you mad that
 * there's just a random class you'll never use sitting on your computer
 * now? Does that make you angry? Sorry.
 *
 * @author Colin Robertson
 * @since 1.0.1
 */
public class RandomTrajectory implements Trajectory {

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return current.add(
            new PointXYZ(
                Math.random() * 2,
                Math.random() * 2,
                Math.random() * 2
            )
        );
    }

    @Override
    public boolean isDone(PointXYZ current) {
        return false;
    }

    @Override
    public double speed(PointXYZ current) {
        return Math.random();
    }
}
