/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

/**
 * An entirely empty motor, mostly only useful for testing purposes.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class EmptyMotor extends AbstractMotor {

    public EmptyMotor() {
        super(aDouble -> {}, () -> null);
    }
}
