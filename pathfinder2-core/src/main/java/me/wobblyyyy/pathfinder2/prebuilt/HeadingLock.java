/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import java.util.function.Function;
import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * A modifier that controls the heading of the robot by using a turn controller
 * to ensure the heading remains constant.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class HeadingLock implements Function<Translation, Translation> {
    private final Angle target;
    private final Controller turnController;
    private final Supplier<Angle> getCurrentAngle;

    /**
     * Create a new {@code HeadingLock} modifier.
     *
     * @param target          the heading the robot should maintain.
     * @param turnController  a controller used for calculating the robot's
     *                        vz values.
     * @param getCurrentAngle a supplier that returns the robot's current
     *                        heading.
     */
    public HeadingLock(
        Angle target,
        Controller turnController,
        Supplier<Angle> getCurrentAngle
    ) {
        this.target = target;
        this.turnController = turnController;
        this.getCurrentAngle = getCurrentAngle;
    }

    @Override
    public Translation apply(Translation translation) {
        return new Translation(
            translation.vx(),
            translation.vz(),
            turnController.calculate(getCurrentAngle.get().deg(), target.deg())
        );
    }
}
