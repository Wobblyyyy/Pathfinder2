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
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedRobot;

public class ExampleFieldNavigation {
    private Robot robot = new SimulatedRobot();
    private double coefficient = -0.05;
    private Pathfinder pathfinder = new Pathfinder(robot, coefficient);

    public void moveAroundSquare() {
        pathfinder.goTo(new PointXYZ(0, 0, Angle.fromDeg(0)));
        pathfinder.tickUntil();

        pathfinder.goTo(new PointXYZ(10, 0, Angle.fromDeg(0)));
        pathfinder.tickUntil();

        pathfinder.goTo(new PointXYZ(10, 10, Angle.fromDeg(0)));
        pathfinder.tickUntil();

        pathfinder.goTo(new PointXYZ(0, 10, Angle.fromDeg(0)));
        pathfinder.tickUntil();

        pathfinder.goTo(new PointXYZ(0, 0, Angle.fromDeg(0)));
        pathfinder.tickUntil();
    }
}
