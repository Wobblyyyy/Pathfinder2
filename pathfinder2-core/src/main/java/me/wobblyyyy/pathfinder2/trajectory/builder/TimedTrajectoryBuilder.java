/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.builder;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.trajectory.TimedTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating a {@link List} of {@link TimedTrajectory}s.
 *
 * @author Colin Robertson
 * @since 0.2.3
 */
public class TimedTrajectoryBuilder {
    private final double defaultSpeed;
    private final double defaultTurnMultiplier;
    private final List<Trajectory> trajectories = new ArrayList<Trajectory>();

    public TimedTrajectoryBuilder() {
        this(0);
    }

    public TimedTrajectoryBuilder(double defaultSpeed) {
        this(defaultSpeed, 0);
    }

    public TimedTrajectoryBuilder(double defaultSpeed,
                                  double defaultTurnMultiplier) {
        this.defaultSpeed = defaultSpeed;
        this.defaultTurnMultiplier = defaultTurnMultiplier;
    }

    public TimedTrajectoryBuilder add(Translation translation,
                                      double timeMs) {
        return add(
                translation,
                timeMs,
                defaultSpeed,
                defaultTurnMultiplier
        );
    }

    public TimedTrajectoryBuilder add(Translation translation,
                                      double timeMs,
                                      double speed) {
        return add(
                translation,
                timeMs,
                speed,
                defaultTurnMultiplier
        );
    }

    public TimedTrajectoryBuilder add(Translation translation,
                                      double timeMs,
                                      double speed,
                                      double turnMultiplier) {
        trajectories.add(new TimedTrajectory(
                translation,
                timeMs,
                speed,
                turnMultiplier
        ));

        return this;
    }

    public TimedTrajectoryBuilder add(Angle direction,
                                      double timeMs) {

        return add(
                direction,
                timeMs,
                defaultSpeed,
                defaultTurnMultiplier
        );
    }

    public TimedTrajectoryBuilder add(Angle direction,
                                      double timeMs,
                                      double speed) {
        return add(
                direction,
                timeMs,
                speed,
                defaultTurnMultiplier
        );
    }

    public TimedTrajectoryBuilder add(Angle direction,
                                      double timeMs,
                                      double speed,
                                      double turnMultiplier) {
        return add(
                direction.toTranslation(),
                timeMs,
                speed,
                turnMultiplier
        );
    }

    public List<Trajectory> getTrajectories() {
        return trajectories;
    }
}
