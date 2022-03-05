/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.recording;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;

public class TestMovementRecorder {

    public void testMovementRecorder() {
        Pathfinder pf = Pathfinder.newSimulatedPathfinder(0.01);
        SimulatedOdometry odometry = (SimulatedOdometry) pf.getOdometry();

        Translation translation = new Translation(0.51, 0.51, 0);
        odometry.setVelocity(Angle.DEG_45, 0.5);
        odometry.setTranslation(translation);
        pf.setTranslation(translation);

        pf.getRecorder().start();
        pf.tick();

        ElapsedTimer timer = new ElapsedTimer(true);

        while (timer.elapsedSeconds() < 2) pf.tick();
    }
}
