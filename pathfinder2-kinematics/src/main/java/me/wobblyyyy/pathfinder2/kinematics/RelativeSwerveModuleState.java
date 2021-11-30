/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * A state a swerve module can be in. Please note that this state, unlike
 * other common swerve module states, is a pair of DOUBLE values. This
 * state does NOT contain an angle and a power value - it contains two
 * power values: one for the turn, one for the drive.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class RelativeSwerveModuleState {
    /**
     * The state's turn motor power.
     */
    private final double turn;

    /**
     * The state's drive motor power.
     */
    private final double drive;

    /**
     * Create a new {@code SwerveModuleState}.
     *
     * @param turn  the swerve module's turn power.
     * @param drive the swerve module's drive power.
     */
    public RelativeSwerveModuleState(double turn,
                                     double drive) {
        this.turn = turn;
        this.drive = drive;
    }

    /**
     * Create an optimized swerve module state. If the swerve module can
     * get to the target angle faster and invert its drive power, it'll
     * respond to drive commands more quickly. It's not essential that
     * you do this, but it can help to optimize efficiency.
     *
     * @param target     the target angle.
     * @param drive      the drive motor power.
     * @param current    the module's current angle.
     * @param controller the turn controller that's used by the swerve module's
     *                   kinematics.
     * @return an optimized swerve module state.
     */
    public static RelativeSwerveModuleState optimized(Angle target,
                                                      double drive,
                                                      Angle current,
                                                      Controller controller) {
        double delta = Math.abs(target.deg() - current.deg());
        double calculated = controller.calculate(
                current.deg(),
                target.deg()
        );

        if (delta > 90) {
            return new RelativeSwerveModuleState(
                    -calculated,
                    -drive
            );
        } else {
            return new RelativeSwerveModuleState(
                    calculated,
                    drive
            );
        }
    }

    /**
     * Create an optimized swerve module state. If the swerve module can
     * get to the target angle faster and invert its drive power, it'll
     * respond to drive commands more quickly. It's not essential that
     * you do this, but it can help to optimize efficiency.
     *
     * @param target     the target angle.
     * @param drive      the drive motor power.
     * @param current    the module's current angle.
     * @param kinematics the module's kinematics.
     * @return an optimized swerve module state.
     */
    public static RelativeSwerveModuleState optimized(Angle target,
                                                      double drive,
                                                      Angle current,
                                                      RelativeSwerveModuleKinematics kinematics) {
        return optimized(
                target,
                drive,
                current,
                kinematics.getController()
        );
    }

    /**
     * Get the state's turn module power.
     *
     * @return the state's turn module power.
     */
    public double getTurn() {
        return this.turn;
    }

    /**
     * Get the state's drive module power.
     *
     * @return the state's drive module power.
     */
    public double getDrive() {
        return this.drive;
    }
}
