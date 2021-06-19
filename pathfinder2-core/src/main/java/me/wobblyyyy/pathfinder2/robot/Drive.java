/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * One of the two major components of your robot - firstly, there's odometry,
 * and secondly, there's the drive train!
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Drive {
    /**
     * Get the drive train's current translation. This shouldn't be determined
     * based on what the robot's actually doing, but rather, whatever the last
     * translation that was set to the drive train.
     *
     * @return whatever translation was last set to the drive train.
     */
    Translation getTranslation();

    /**
     * Set a translation to the drive train. This should, in fact, make the
     * robot move! Crazy. Anyways.
     *
     * @param translation a translation the robot should act upon. This
     *                    translation should always be <em>relative</em>,
     *                    meaning whatever the translation says should make
     *                    the robot act accordingly according to the robot's
     *                    position and the robot's current heading. I'm
     *                    currently really tired and just about entirely unable
     *                    to type, so this isn't coherent, but guess what -
     *                    that really sucks for you, doesn't it?
     */
    void setTranslation(Translation translation);
}
