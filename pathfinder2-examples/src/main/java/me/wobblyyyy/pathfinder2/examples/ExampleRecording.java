/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.examples;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.recording.MovementPlayback;
import me.wobblyyyy.pathfinder2.recording.MovementRecorder;
import me.wobblyyyy.pathfinder2.recording.MovementRecording;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedDrive;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;

/**
 * A lovely example demonstrating the usage of Pathfinder's built-in
 * recording and playback platform. It's pretty simple, quite honestly,
 * and it shouldn't be too hard to understand. Basically, here's the
 * plan.
 *
 * <ul>
 *     <li>
 *         Start recording by getting Pathfinder's recorder and using the
 *         {@link MovementRecorder#start()} method. This will reset the current
 *         recording (so if you recorded something and then want to start over,
 *         this is how you would do that) and set the
 *         {@code isRecording} boolean to true. While this is true, whenever
 *         you call {@link Pathfinder#tick()}, the recorder will record
 *         information on Pathfinder's current movement.
 *     </li>
 *     <li>
 *         Once you've finished recording, use the {@link MovementRecorder#stop()}
 *         method to stop recording information. You can access this recorded
 *         data by using the {@link MovementRecorder#getRecording()} method.
 *     </li>
 *     <li>
 *         Now, to play back movement, it's pretty simple. You just use the
 *         {@link MovementPlayback#startPlayback(MovementRecording)} method
 *         to start the playback, and then use {@link Pathfinder#tick()} to
 *         continue playing the movement back.
 *     </li>
 *     <li>
 *         Because everything is done on a single thread, it's quite easy
 *         to stop or start recording, and you won't have any issues with
 *         doing just that.
 *     </li>
 * </ul>
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
@SuppressWarnings(
    {
        "FieldCanBeLocal",
        "ConstantConditions",
        "LoopConditionNotUpdatedInsideLoop"
    }
)
public class ExampleRecording {
    private Drive drive;
    private Odometry odometry;
    private Robot robot;
    private Pathfinder pathfinder;

    private MovementRecording recording;

    public void init() {
        drive = new SimulatedDrive();
        odometry = new SimulatedOdometry();
        robot = new Robot(drive, odometry);
        pathfinder = new Pathfinder(robot, 0.02);
    }

    public void record() {
        boolean condition = true;

        pathfinder.getMovementRecorder().start();

        while (condition) {
            pathfinder.tick();
        }

        recording = pathfinder.getMovementRecorder().getRecording();
    }

    public void playback() {
        boolean condition = true;

        pathfinder.getPlayback().startPlayback(recording);

        while (condition) {
            pathfinder.tick();
        }
    }
}
