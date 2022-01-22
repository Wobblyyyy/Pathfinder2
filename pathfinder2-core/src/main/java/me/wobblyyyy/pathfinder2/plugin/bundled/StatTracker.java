/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.plugin.bundled;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPlugin;

/**
 * @author Colin Robertson
 * @since 0.10.3
 */
public class StatTracker extends PathfinderPlugin {
    private static final String NAME = "StatTracker";

    private long ticks = 0;
    private long followersFinished = 0;
    private double totalDistance = 0;
    private PointXY lastPoint = null;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onTick(Pathfinder pathfinder) {
        ticks++;

        PointXYZ position = pathfinder.getPosition();

        if (lastPoint == null) lastPoint = position;

        totalDistance += lastPoint.absDistance(position);
    }

    @Override
    public void onFinishFollower(Pathfinder pathfinder,
                                 Follower follower) {
        followersFinished++;
    }
}
