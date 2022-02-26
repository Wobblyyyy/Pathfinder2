/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Robot;

/**
 * A wrapper for instances of {@link SimulatedDrive} and
 * {@link SimulatedOdometry}.
 *
 * @author Colin Robertson
 * @since 0.15.0
 */
public class SimulatedWrapper {
    private final SimulatedDrive drive;
    private final SimulatedOdometry odometry;

    public SimulatedWrapper(SimulatedDrive drive,
                            SimulatedOdometry odometry) {
        this.drive = drive;
        this.odometry = odometry;

        this.drive.setModifier(
                (translation) -> {
                    PointXYZ pos = odometry.getPosition();

                    odometry.setRawPosition(
                            pos.inDirection(
                                    translation.magnitude(),
                                    translation.angle()
                            ).withHeading(pos.z().add(Angle.fromDeg(
                                    translation.vz()
                            )))
                    );

                    return translation;
                }
        );
    }

    private static double roundNumber(double value) {
        return Math.round(value * 32) / 32.0;
    }

    public SimulatedDrive getDrive() {
        return drive;
    }

    public SimulatedOdometry getOdometry() {
        return odometry;
    }

    public Robot getRobot() {
        return new Robot(drive, odometry);
    }
}
