/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.multi.target;

/**
 * Types of target precision for the {@link MultiTargetTrajectory}.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public enum TargetPrecision {
    /**
     * The target should be precise. The robot will make sure it's within
     * the provided tolerance values whenever approaching a precise target. This
     * can cause a sort of jitter effect, an effect often responsible for
     * slowing the robot down. It may pause in order to compensate for any
     * overcorrection.
     */
    PRECISE,

    /**
     * The target does not need to be precise. The robot will not make sure
     * it's within the provided tolerance values when approaching a fast
     * target. This means the robot doesn't have to adjust and compensate
     * for any movement error, making the trajectory able to move more quickly.
     */
    FAST,
}
