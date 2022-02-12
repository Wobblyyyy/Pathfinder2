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
