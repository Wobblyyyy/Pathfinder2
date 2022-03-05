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

import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPlugin;

/**
 * @author Colin Robertson
 * @since 0.10.3
 */
public class GyroOdometry extends PathfinderPlugin {
    private static final String NAME = "GyroOdometry";

    private final Supplier<Double> getGyroVelocity;
    private final double minCombinedVelocity;
    private PointXYZ lastPosition;

    public GyroOdometry(
        Supplier<Double> getGyroVelocity,
        double minCombinedVelocity
    ) {
        this.getGyroVelocity = getGyroVelocity;
        this.minCombinedVelocity = minCombinedVelocity;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void preTick(Pathfinder pathfinder) {
        lastPosition = pathfinder.getPosition();
    }

    @Override
    public void postTick(Pathfinder pathfinder) {
        if (getGyroVelocity.get() < minCombinedVelocity) pathfinder
            .getOdometry()
            .offsetSoPositionIs(lastPosition);
    }
}
