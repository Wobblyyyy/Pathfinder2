/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.multi.segment;

import java.util.Arrays;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A {@code Trajectory} made up of multiple trajectories. Pretty simply, it's
 * a trajectory with multiple segments - that's a crazy coincidence, right?
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class MultiSegmentTrajectory implements Trajectory {
    /**
     * A pseudo-queue data structure.
     */
    private final List<Trajectory> trajectories;

    public MultiSegmentTrajectory(List<Trajectory> trajectories) {
        this.trajectories = trajectories;
    }

    public MultiSegmentTrajectory(Trajectory... trajectories) {
        this(Arrays.asList(trajectories));
    }

    /**
     * If there's still at least one trajectory in the
     * {@code MultiSegmentTrajectory}, "skip" over it by removing it from
     * the queue, moving on to the next trajectory.
     */
    public void skip() {
        if (trajectories.size() > 0) {
            trajectories.remove(0);
        }
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        if (trajectories.size() > 0) {
            Trajectory currentTrajectory = trajectories.get(0);
            return currentTrajectory.nextMarker(current);
        }

        return current;
    }

    /**
     * Has the {@code MultiSegmentTrajectory} finished executing all of
     * its segments?
     *
     * <p>
     * This method will also advance the queue if the current segment has
     * finished. If the current segment has in fact finished, and it's the
     * last segment, then there are no more segments, and it returns true.
     * It's just like magic!
     * </p>
     *
     * @param current the robot's current position.
     * @return true if ALL of the trajectories have finished execution,
     * otherwise, false.
     */
    @Override
    public boolean isDone(PointXYZ current) {
        if (trajectories.size() > 0) {
            Trajectory currentTrajectory = trajectories.get(0);

            if (currentTrajectory.isDone(current)) trajectories.remove(0);
        }

        return trajectories.size() == 0;
    }

    @Override
    public double speed(PointXYZ current) {
        if (trajectories.size() > 0) {
            Trajectory currentTrajectory = trajectories.get(0);
            return currentTrajectory.speed(current);
        }

        return 0.0;
    }
}
