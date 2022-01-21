/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.utils.Gamepad;

/**
 * @deprecated use the listener system instead!
 */
@Deprecated
public class ButtonMapper {
    private final Pathfinder pathfinder;
    private final Gamepad gamepad;

    public ButtonMapper(Pathfinder pathfinder,
                        Gamepad gamepad) {
        this.pathfinder = pathfinder;
        this.gamepad = gamepad;
    }

    public Trajectory getTrajectory() {
        if (!gamepad.areAnyButtonsPressed())
            throw new RuntimeException(
                    "Cannot get a trajectory without any buttons being pressed!"
            );

        PointXYZ point = null;
        if (gamepad.a()) point = gamepad.getMappedPoint(Gamepad.InputButton.BUTTON_A);
        else if (gamepad.b()) point = gamepad.getMappedPoint(Gamepad.InputButton.BUTTON_B);
        else if (gamepad.x()) point = gamepad.getMappedPoint(Gamepad.InputButton.BUTTON_X);
        else if (gamepad.y()) point = gamepad.getMappedPoint(Gamepad.InputButton.BUTTON_Y);
        if (point != null) {
            double speed = pathfinder.getSpeed();
            double tolerance = pathfinder.getTolerance();
            Angle angleTolerance = pathfinder.getAngleTolerance();

            return new LinearTrajectory(
                    point,
                    speed,
                    tolerance,
                    angleTolerance
            );
        }

        return null;
    }

    public void update() {
        if (gamepad.areAnyButtonsPressed()) {
            Trajectory trajectory = getTrajectory();

            pathfinder.followTrajectory(trajectory);
        }

        if (gamepad.start()) {
            pathfinder.clear();
        }
    }
}
