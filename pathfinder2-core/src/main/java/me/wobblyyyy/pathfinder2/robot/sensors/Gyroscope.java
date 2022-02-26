/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * An interface for a gyroscope.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public interface Gyroscope {
    /**
     * Get the angle the gyroscope is currently facing.
     *
     * @return the angle the gyroscope is currently facing.
     */
    Angle getAngle();

    /**
     * Set the current angle of the gyroscope.
     *
     * @param angle the angle to set to the gyroscope.
     */
    void setAngle(Angle angle);

    /**
     * Set the angle of the gyroscope to 0 degrees.
     */
    void reset();
}
