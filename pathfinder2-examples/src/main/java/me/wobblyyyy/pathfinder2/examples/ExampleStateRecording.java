/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.recording.StateRecording;
import me.wobblyyyy.pathfinder2.robot.components.Motor;
import me.wobblyyyy.pathfinder2.utils.Gamepad;

public class ExampleStateRecording {
    private Pathfinder pathfinder;
    private Gamepad gamepad;
    private Motor rightMotor;
    private Motor leftMotor;
    private StateRecording recording;

    public void init() {
        pathfinder = Pathfinder.newSimulatedPathfinder(-0.05);
        gamepad = new Gamepad();

        // just pretend these aren't null lol
        rightMotor = null;
        leftMotor = null;

        pathfinder.getRecorder().putNode("rightMotor", rightMotor);
        pathfinder.getRecorder().putNode("leftMotor", leftMotor);

        pathfinder
            .getListenerManager()
            .bindButtonPress(
                gamepad.buttonA::isPressed,
                () -> {
                    pathfinder.getRecorder().startRecording(100, 10_000);
                }
            )
            .bindButtonPress(
                gamepad.buttonB::isPressed,
                () -> {
                    recording = pathfinder.getRecorder().stopRecording();
                }
            )
            .bindButtonPress(
                gamepad.buttonX::isPressed,
                () -> {
                    pathfinder.getRecorder().startPlayback(recording);
                }
            )
            .bindButtonPress(
                gamepad.buttonY::isPressed,
                () -> {
                    pathfinder.getRecorder().stopPlayback();
                }
            );
    }

    public void run() {
        while (true) {
            pathfinder.tick();
        }
    }
}
