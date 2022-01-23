/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.plugin.bundled;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import org.junit.jupiter.api.Test;

@SuppressWarnings("BusyWait")
public class TestStatTracker {
    @Test
    public void testTicksPerSecond() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01)
                .setSpeed(0.5)
                .setTolerance(2)
                .setAngleTolerance(Angle.fixedDeg(10));

        pathfinder.loadPlugin(new StatTracker());
        pathfinder.goTo(new PointXYZ(10, 10, 0));
        pathfinder.splineTo(
                new PointXYZ(10, 10, 90),
                new PointXYZ(12, 11, 45),
                new PointXYZ(13, 15, 180)
        );
        ElapsedTimer timer = new ElapsedTimer(true);
        SimulatedOdometry odometry = (SimulatedOdometry) pathfinder.getOdometry();

        try {
            while (timer.elapsedSeconds() < 5) {
                if (Math.random() > 0.5) Thread.sleep(2);
                pathfinder.tick();
                Translation translation = pathfinder.getTranslation();
                odometry.setRawPosition(
                        odometry.getRawPosition().add(new PointXYZ(
                                translation.vx() / 50,
                                translation.vy() / 50,
                                Angle.fixedRad(translation.vz() / 50)
                        ))
                );
            }
        } catch (Exception ignored) {
        }

        pathfinder.tick();

        System.out.println("tps: " + pathfinder.ticksPerSecond());
        System.out.println("ticks: " + pathfinder.getData("pf_ticks"));
        System.out.println("position: " + pathfinder.getPosition());
    }
}
