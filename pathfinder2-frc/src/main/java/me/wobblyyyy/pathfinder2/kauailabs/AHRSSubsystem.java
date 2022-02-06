/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kauailabs;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * A subsystem wrapper for an {@code AHRSGyro}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class AHRSSubsystem extends SubsystemBase {
    private final AHRSGyro gyro;
    private Angle angle;

    /**
     * Create a new {@code AHRSGyro}.
     *
     * @param gyro the gyroscope.
     */
    public AHRSSubsystem(AHRSGyro gyro) {
        this.gyro = gyro;
    }

    /**
     * Get the gyroscope's angle.j
     *
     * @return the gyroscope's angle.
     */
    public Angle getAngle() {
        return angle;
    }

    /**
     * Occasionally update the gyroscope's angle.
     */
    @Override
    public void periodic() {
        angle = gyro.getAngle();
    }
}
