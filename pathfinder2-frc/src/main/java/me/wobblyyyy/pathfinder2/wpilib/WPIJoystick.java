/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.wpilib;

import me.wobblyyyy.pathfinder2.utils.Joystick;

/**
 * A {@code wpilib}-specific joystick implementation. It really can't get
 * any more simple than this.
 *
 * @author Colin Robertson
 * @since 0.15.0
 */
public class WPIJoystick extends Joystick {
    public WPIJoystick(edu.wpi.first.wpilibj.Joystick joystick) {
        super(joystick::getX, joystick::getY);
    }
}
