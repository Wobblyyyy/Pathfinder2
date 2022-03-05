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

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.robot.sensors.AbstractGyroscope;

/**
 * Wrapper for {@link AHRS} gyroscope.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class AHRSGyro extends AbstractGyroscope {
    private final AHRS ahrs;
    private final AHRSGyroMode mode;

    /**
     * Create a new {@code AHRSGyro}.
     *
     * @param ahrs the gyroscope to use.
     * @param mode the mode the gyroscope should operate in.
     */
    public AHRSGyro(AHRS ahrs, AHRSGyroMode mode) {
        this.ahrs = ahrs;
        this.mode = mode;
    }

    /**
     * Create a new {@code AHRSGyro}.
     *
     * @param port the gyroscope's port. This is usually MXP.
     */
    public AHRSGyro(SerialPort.Port port) {
        this(new AHRS(port), AHRSGyroMode.NORMAL);
    }

    /**
     * Create a new {@code AHRSGyro}, using the MXP port.
     */
    public AHRSGyro() {
        this(SerialPort.Port.kMXP);
    }

    @Override
    public Angle getRawAngle() {
        switch (mode) {
            case NORMAL:
                return Angle.fixedDeg(ahrs.getAngle());
            case YAW:
                return Angle.fixedDeg(ahrs.getYaw());
            case PITCH:
                return Angle.fixedDeg(ahrs.getPitch());
            case ROLL:
                return Angle.fixedDeg(ahrs.getRoll());
            default:
                throw new RuntimeException();
        }
    }
}
