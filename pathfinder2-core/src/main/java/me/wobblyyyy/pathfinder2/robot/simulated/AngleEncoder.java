/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * An encoder that's capable of supplying the angle the encoder is at.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public interface AngleEncoder {
    /**
     * Get the angle the encoder is at.
     *
     * @return the angle the encoder is at.
     */
    Angle getAngle();
}
