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
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.listening.Listener;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.time.ElapsedTimer;
import me.wobblyyyy.pathfinder2.utils.SupplierFilter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("BusyWait")
public class TestStatTracker {
    @SuppressWarnings("UnusedAssignment")
    @Test
    @Disabled
    public void testTicksPerSecond() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01)
                .setSpeed(0.75)
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
        AtomicInteger i = new AtomicInteger(0);

        pathfinder.getListenerManager()
                .bind(
                        ListenerMode.CONDITION_ALWAYS_MET,
                        () -> Math.random() > 0.5,
                        (b) -> {
                            double x;

                            if (b) x = 10;
                            else x = 1;

                            x *= x;
                        }
                )
                .bind(
                        ListenerMode.CONDITION_IS_MET,
                        () -> SupplierFilter.trueThenAllFalse(
                                () -> true,
                                () -> false,
                                () -> Math.random() > 0.5,
                                () -> Math.random() > 0.5
                        ),
                        (b) -> {
                            i.incrementAndGet();
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                )
                .addListener(new Listener(
                        10,
                        ListenerMode.CONDITION_IS_MET,
                        () -> {
                            double a = Math.sqrt(Math.cos(10 * Math.PI));
                            a *= Math.cbrt(a);
                        },
                        () -> true
                ))
                .addListener(new Listener(
                        5,
                        ListenerMode.CONDITION_IS_MET,
                        () -> {
                            double a = Math.sqrt(Math.cos(10 * Math.PI));
                            a /= Math.cbrt(a);
                        },
                        () -> true
                ))
                .addListener(new Listener(
                        100,
                        ListenerMode.CONDITION_IS_MET,
                        () -> {
                            double a = Math.sqrt(Math.cos(10 * Math.PI));
                            a += Math.cbrt(a);
                        },
                        () -> true
                ))
                .addListener(new Listener(
                        0,
                        ListenerMode.CONDITION_IS_MET,
                        () -> {
                            double a = Math.sqrt(Math.cos(10 * Math.PI));
                            a -= Math.cbrt(a);
                        },
                        () -> true
                ));

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
        System.out.println("condition met: " + i.get());
        System.out.println("total PointXY: " + PointXY.COUNT);
        System.out.println("total PointXYZ: " + PointXYZ.COUNT);
        System.out.println("total Angle: " + Angle.COUNT);
    }
}
