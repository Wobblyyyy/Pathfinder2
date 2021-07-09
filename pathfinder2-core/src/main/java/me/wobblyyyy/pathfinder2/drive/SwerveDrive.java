/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.drive;

import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.kinematics.RelativeSwerveDriveKinematics;
import me.wobblyyyy.pathfinder2.kinematics.RelativeSwerveState;
import me.wobblyyyy.pathfinder2.kinematics.SwerveModuleKinematics;
import me.wobblyyyy.pathfinder2.kinematics.SwerveModuleState;
import me.wobblyyyy.pathfinder2.robot.Drive;

import java.util.function.Function;

/**
 * A very simple (and very lovely) swerve drive implementation.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SwerveDrive implements Drive {
    private final SwerveModule fr;
    private final SwerveModule fl;
    private final SwerveModule br;
    private final SwerveModule bl;

    private final SwerveModuleKinematics frk;
    private final SwerveModuleKinematics flk;
    private final SwerveModuleKinematics brk;
    private final SwerveModuleKinematics blk;

    private final Controller moduleController;

    private Translation translation = Translation.zero();
    private Function<Translation, Translation> modifier = s -> s;

    private RelativeSwerveDriveKinematics kinematics;

    /**
     * Create a new swerve drive.
     *
     * @param fr               front-right swerve module.
     * @param fl               front-left swerve module.
     * @param br               back-right swerve module.
     * @param bl               back-left swerve module.
     * @param moduleController the turn controller used to control the swerve
     *                         module's turn angle. This controller accepts
     *                         degrees as input and as the target.
     */
    public SwerveDrive(SwerveModule fr,
                       SwerveModule fl,
                       SwerveModule br,
                       SwerveModule bl,
                       Controller moduleController) {
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;

        this.moduleController = moduleController;

        this.frk = new SwerveModuleKinematics(moduleController);
        this.flk = new SwerveModuleKinematics(moduleController);
        this.brk = new SwerveModuleKinematics(moduleController);
        this.blk = new SwerveModuleKinematics(moduleController);

        this.kinematics = new RelativeSwerveDriveKinematics(
                this.frk,
                this.flk,
                this.brk,
                this.blk,
                this.fr::getAngle,
                this.fl::getAngle,
                this.br::getAngle,
                this.bl::getAngle,
                1.0
        );
    }

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public void setTranslation(Translation translation) {
        this.translation = translation;

        RelativeSwerveState state = kinematics.calculate(translation);

        SwerveModuleState frState = state.fr();
        SwerveModuleState flState = state.fl();
        SwerveModuleState brState = state.br();
        SwerveModuleState blState = state.bl();

        this.fr.set(frState);
        this.fl.set(flState);
        this.br.set(brState);
        this.bl.set(blState);
    }

    @Override
    public Function<Translation, Translation> getModifier() {
        return this.modifier;
    }

    @Override
    public void setModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }
}
