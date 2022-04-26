/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.components;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.math.MinMax;

/**
 * A two-axis gimbal that uses two servos.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public class Gimbal {
    private final Servo yawServo;
    private final Servo pitchServo;

    private final Controller yawController;
    private final Controller pitchController;

    public Gimbal(
        Servo yawServo,
        Servo pitchServo,
        Controller yawController,
        Controller pitchController
    ) {
        this.yawServo = yawServo;
        this.pitchServo = pitchServo;
        this.yawController = yawController;
        this.pitchController = pitchController;
    }

    public void adjustGimbal(PointXY target, PointXY current) {
        double dx = current.x() - target.x();
        double dy = current.y() - target.y();

        double yawAdjustment = yawController.calculate(dx, 0);
        double pitchAdjustment = pitchController.calculate(dy, 0);

        double currentYaw = yawServo.getPosition();
        double currentPitch = pitchServo.getPosition();

        double nextYaw = MinMax.clip(currentYaw + yawAdjustment, 0, 1);
        double nextPitch = MinMax.clip(currentPitch + pitchAdjustment, 0, 1);

        yawServo.setPosition(nextYaw);
        pitchServo.setPercent(nextPitch);
    }
}
