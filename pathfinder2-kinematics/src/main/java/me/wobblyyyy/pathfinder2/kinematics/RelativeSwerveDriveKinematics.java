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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;

import java.util.function.Supplier;

public class RelativeSwerveDriveKinematics implements ForwardsKinematics<RelativeSwerveState> {
    private final SwerveModuleKinematics frk;
    private final SwerveModuleKinematics flk;
    private final SwerveModuleKinematics brk;
    private final SwerveModuleKinematics blk;

    private final Supplier<Angle> frModuleAngle;
    private final Supplier<Angle> flModuleAngle;
    private final Supplier<Angle> brModuleAngle;
    private final Supplier<Angle> blModuleAngle;

    private final double turnMultiplier;

    public RelativeSwerveDriveKinematics(SwerveModuleKinematics frk,
                                         SwerveModuleKinematics flk,
                                         SwerveModuleKinematics brk,
                                         SwerveModuleKinematics blk,
                                         Supplier<Angle> frModuleAngle,
                                         Supplier<Angle> flModuleAngle,
                                         Supplier<Angle> brModuleAngle,
                                         Supplier<Angle> blModuleAngle,
                                         double turnMultiplier) {
        this.frk = frk;
        this.flk = flk;
        this.brk = brk;
        this.blk = blk;

        this.frModuleAngle = frModuleAngle;
        this.flModuleAngle = flModuleAngle;
        this.brModuleAngle = brModuleAngle;
        this.blModuleAngle = blModuleAngle;

        this.turnMultiplier = turnMultiplier;
    }

    @Override
    public RelativeSwerveState calculate(Translation translation) {
        Angle angle = translation.angle();

        double frTurn = frk.calculate(frModuleAngle.get(), angle);
        double flTurn = flk.calculate(flModuleAngle.get(), angle);
        double brTurn = brk.calculate(brModuleAngle.get(), angle);
        double blTurn = brk.calculate(blModuleAngle.get(), angle);

        double vz = translation.vz() * turnMultiplier;
        double frDrive = translation.magnitude() + vz;
        double flDrive = translation.magnitude() - vz;
        double brDrive = translation.magnitude() + vz;
        double blDrive = translation.magnitude() - vz;

        SwerveModuleState frState = new SwerveModuleState(frTurn, frDrive);
        SwerveModuleState flState = new SwerveModuleState(flTurn, flDrive);
        SwerveModuleState brState = new SwerveModuleState(brTurn, brDrive);
        SwerveModuleState blState = new SwerveModuleState(blTurn, blDrive);

        return new RelativeSwerveState(
                frState,
                flState,
                brState,
                blState
        );
    }
}
