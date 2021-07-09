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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.kinematics.MeccanumState;
import me.wobblyyyy.pathfinder2.kinematics.RelativeMeccanumKinematics;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.components.Motor;

import java.util.function.Function;

/**
 * Simple meccanum drive implementation. This implementation makes use of the
 * {@link RelativeMeccanumKinematics} class to determine specific motor powers
 * for each of the individual motors - front right, front left, back right,
 * and back left. Because the {@link RelativeMeccanumKinematics} class is...
 * well... relative, you don't need to input any measurements or anything.
 *
 * <p>
 * This class has a minimum power of 0.0 and a maximum power of 1.0.
 * Additionally, motor powers are all normalized before being multiplied
 * by a magnitude - this means if you want to drive straight forwards at
 * full speed, your robot will only travel at ~0.71 speed.
 * </p>
 *
 * @author Colin Robetson
 * @since 0.0.0
 */
public class MeccanumDrive implements Drive {
    /**
     * Front-right motor.
     */
    private final Motor fr;

    /**
     * Front-left motor.
     */
    private final Motor fl;

    /**
     * Back-right motor.
     */
    private final Motor br;

    /**
     * Back-left motor.
     */
    private final Motor bl;

    /**
     * Kinematics! Woot-woot!
     */
    private final RelativeMeccanumKinematics kinematics;

    /**
     * The drive's modifier - you probably don't need to touch this.
     */
    private Function<Translation, Translation> modifier = t -> t;

    /**
     * The drive's translation - this is only used for the
     * {@link #getTranslation()} method. The translation is actually
     * applied during the {@link #setTranslation(Translation)} method.
     */
    private Translation translation = Translation.zero();

    /**
     * Create a new meccanum drive. Crazy!
     *
     * @param fr the front-right motor.
     * @param fl the front-left motor.
     * @param br the back-right motor.
     * @param bl the back-left motor.
     */
    public MeccanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl) {
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;

        kinematics = new RelativeMeccanumKinematics(0, 1, Angle.zero());
    }

    @Override
    public Translation getTranslation() {
        return this.translation;
    }

    @Override
    public void setTranslation(Translation translation) {
        this.translation = translation;

        MeccanumState state = kinematics.calculate(translation);

        fr.setPower(state.fr());
        fl.setPower(state.fl());
        br.setPower(state.br());
        bl.setPower(state.bl());
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
