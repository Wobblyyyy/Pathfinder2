/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.movement;

import me.wobblyyyy.pathfinder2.TestableRobot;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Test;

/*
 * this is a really poorly written test, but to be honest, i don't have the
 * energy to go back and write a decent test so... uhh... yeah.
 */

public class TestMovementProfiler extends TestableRobot {

    @Test
    public void testVelocity() {
        /*
        Logger.setOutput(System.out::println);
        Logger.setLoggingLevel(LogLevel.TRACE);
        */

        setPosition(new PointXYZ(0, 0, 0));
        pathfinder.tick();

        /*
        try {
            Thread.sleep(1_000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        setPosition(new PointXYZ(10, 10, 45));
        pathfinder.tick();

        /*
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        setPosition(new PointXYZ(20, 20, 135));
        pathfinder.tick();

        /*
        try {
            Thread.sleep(250);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        setPosition(new PointXYZ(0, 0, 360));
        pathfinder.tick();
        /*
        StringUtils.printf(
            "(v: %s, vx: %s, vy: %s, vz: %s)",
            pathfinder.getVelocity(),
            pathfinder.getVelocityX(),
            pathfinder.getVelocityY(),
            pathfinder.getVelocityZ()
        );
        */

        /*
        Logger.setLoggingLevel(LogLevel.WARN);
        Logger.setOutput(null);
        */
    }
}
