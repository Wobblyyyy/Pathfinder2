/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

import java.util.HashMap;
import java.util.Map;

public class Gamepad {
    public Button buttonA;
    public Button buttonB;
    public Button buttonX;
    public Button buttonY;

    public Button buttonStart;
    public Button buttonSelect;

    public DualJoystick joysticks;

    private final Map<InputButton, PointXYZ> pointMap = new HashMap<>();

    public boolean areAnyButtonsPressed() {
        return areLetterButtonsPressed() ||
                buttonStart.isPressed() ||
                buttonSelect.isPressed();
    }

    public boolean areLetterButtonsPressed() {
        return buttonA.isPressed() ||
                buttonB.isPressed() ||
                buttonX.isPressed() ||
                buttonY.isPressed();
    }

    public boolean areJoysticksInUse() {
        return joysticks.left().isNonZero() ||
                joysticks.right().isNonZero();
    }

    public PointXYZ getMappedPoint(InputButton button) {
        return pointMap.get(button);
    }

    public void mapPoint(InputButton input,
                         PointXYZ point) {
        pointMap.remove(input);
        pointMap.put(input, point);
    }

    public enum InputButton {
        BUTTON_A,
        BUTTON_B,
        BUTTON_X,
        BUTTON_Y,
        BUTTON_START,
        BUTTON_SELECT
    }

    public boolean a() {
        return buttonA.isPressed();
    }

    public boolean b() {
        return buttonB.isPressed();
    }

    public boolean x() {
        return buttonX.isPressed();
    }

    public boolean y() {
        return buttonY.isPressed();
    }

    public boolean wasPressedA() {
        return buttonA.wasPressed();
    }

    public boolean wasPressedB() {
        return buttonB.wasPressed();
    }

    public boolean wasPressedX() {
        return buttonX.wasPressed();
    }

    public boolean wasPressedY() {
        return buttonY.wasPressed();
    }

    public boolean wasReleasedA() {
        return buttonA.wasReleased();
    }

    public boolean wasReleasedB() {
        return buttonB.wasReleased();
    }

    public boolean wasReleasedX() {
        return buttonX.wasReleased();
    }

    public boolean wasReleasedY() {
        return buttonY.wasReleased();
    }

    public boolean start() {
        return buttonStart.isPressed();
    }

    public boolean select() {
        return buttonSelect.isPressed();
    }

    public boolean wasStartPressed() {
        return buttonStart.wasPressed();
    }

    public boolean wasSelectPressed() {
        return buttonSelect.wasPressed();
    }

    public boolean wasStartReleased() {
        return buttonStart.wasReleased();
    }

    public boolean wasSelectReleased() {
        return buttonSelect.wasReleased();
    }
}
