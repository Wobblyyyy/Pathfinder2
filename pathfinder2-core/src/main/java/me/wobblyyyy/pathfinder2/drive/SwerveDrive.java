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
 * <p>
 * A swerve drive (or at least this type of swerve drive) is a type of
 * drive system that has eight motors in total. The swerve chassis is
 * composed of four {@link SwerveModule}s, each of which contain two
 * motors. One of these motors controls the direction the module is facing,
 * and the other motor controls the robot's actual movement on the ground.
 * Most simply, a swerve chassis works by rotating all of the swerve
 * modules to the angle that corresponds with the direction it should move
 * in and then moving in that direction.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SwerveDrive implements Drive {
    /**
     * The chassis' front right module.
     */
    private final SwerveModule frontRightModule;

    /**
     * The chassis' front left module.
     */
    private final SwerveModule frontLeftModule;

    /**
     * The chassis' back right module.
     */
    private final SwerveModule backRightModule;

    /**
     * The chassis' back left module.
     */
    private final SwerveModule backLeftModule;

    /**
     * The translation the chassis is moving according to.
     */
    private Translation translation = Translation.zero();

    /**
     * The chassis' modifier - basically, a function that modifies any
     * inputted translations.
     */
    private Function<Translation, Translation> modifier = s -> s;

    /**
     * The chassis' kinematics.
     */
    private final RelativeSwerveDriveKinematics kinematics;

    /**
     * Create a new swerve drive.
     *
     * @param frontRightModule front-right swerve module.
     * @param frontLeftModule  front-left swerve module.
     * @param backRightModule  back-right swerve module.
     * @param backLeftModule   back-left swerve module.
     * @param moduleController the turn controller used to control the swerve
     *                         module's turn angle. This controller accepts
     *                         degrees as input and as the target.
     * @param turnCoefficient  the coefficient used in calculating how fast
     *                         the chassis should turn. Higher values make
     *                         the robot turn faster, and lower values make
     *                         the robot turn slower. This value should usually
     *                         be around 0.1, but it'll take some testing to
     *                         figure out what number works best for you.
     */
    public SwerveDrive(SwerveModule frontRightModule,
                       SwerveModule frontLeftModule,
                       SwerveModule backRightModule,
                       SwerveModule backLeftModule,
                       Controller moduleController,
                       double turnCoefficient) {
        this.frontRightModule = frontRightModule;
        this.frontLeftModule = frontLeftModule;
        this.backRightModule = backRightModule;
        this.backLeftModule = backLeftModule;

        this.kinematics = new RelativeSwerveDriveKinematics(
                new SwerveModuleKinematics(moduleController),
                frontRightModule::getAngle,
                frontLeftModule::getAngle,
                backRightModule::getAngle,
                backLeftModule::getAngle,
                turnCoefficient
        );
    }

    /**
     * Get the last translation that was set to the robot.
     *
     * @return the last translation that was set to the robot.
     */
    @Override
    public Translation getTranslation() {
        return translation;
    }

    /**
     * Set a translation to the robot. In the case of this swerve chassis,
     * this does a couple of things. Firstly, it applies the modifier to
     * the inputted translation. Secondly, it calculates a swerve chassis
     * state based on the translation. Thirdly, it determines the swerve
     * module states of each individual module. And finally, it sets the
     * state to each of these modules, making the robot move.
     *
     * @param translation a translation the robot should act upon. This
     *                    translation should always be <em>relative</em>,
     *                    meaning whatever the translation says should make
     *                    the robot act accordingly according to the robot's
     *                    position and the robot's current heading. I'm
     *                    currently exhausted and just about entirely unable
     *                    to type, so this isn't coherent, but guess what -
     */
    @Override
    public void setTranslation(Translation translation) {
        translation = modifier.apply(translation);

        this.translation = translation;

        RelativeSwerveState state = kinematics.calculate(translation);

        SwerveModuleState frState = state.fr();
        SwerveModuleState flState = state.fl();
        SwerveModuleState brState = state.br();
        SwerveModuleState blState = state.bl();

        this.frontRightModule.set(frState);
        this.frontLeftModule.set(flState);
        this.backRightModule.set(brState);
        this.backLeftModule.set(blState);
    }

    /**
     * Get the modifier.
     *
     * @return the modifier.
     */
    @Override
    public Function<Translation, Translation> getModifier() {
        return this.modifier;
    }

    /**
     * Set the modifier.
     *
     * @param modifier the modifier. This modifier... well, it literally just
     *                 modifies any translations that the robot is given.
     *                 For example, if you want to invert the X and Y values
     *                 of any given translation, you could do so by using
     *                 a modifier.
     */
    @Override
    public void setModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }
}
