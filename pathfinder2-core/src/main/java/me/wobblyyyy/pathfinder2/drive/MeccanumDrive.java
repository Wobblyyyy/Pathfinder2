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
import me.wobblyyyy.pathfinder2.robot.modifiers.DriveModifier;

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
 * <p>
 * If, no matter what you do, you can't get your chassis to drive in the
 * right direction, try using a modifier - this will change the inputted
 * {@link Translation}s so your robot can correctly follow them.
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
    private Function<Translation, Translation> modifier;

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
        this(fr, fl, br, bl, Angle.zero());
    }

    /**
     * Create a new meccanum drive. Crazy!
     *
     * @param fr          the front-right motor.
     * @param fl          the front-left motor.
     * @param br          the back-right motor.
     * @param bl          the back-left motor.
     * @param angleOffset the angle offset to use for applying translations.
     */
    public MeccanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl,
                         Angle angleOffset) {
        this(fr, fl, br, bl, angleOffset, false, false, false);
    }

    /**
     * Create a new {@code MeccanumDrive} with a modifier.
     *
     * @param fr          the front-right motor.
     * @param fl          the front-left motor.
     * @param br          the back-right motor.
     * @param bl          the back-left motor.
     * @param angleOffset the angle offset to apply to any inputted translations.
     * @param swapXY      should input translations have their X and Y
     *                    values swapped? This is the very first modifier that
     *                    gets applied - if X and Y values are swapped, and
     *                    you set reflectX to true, you'll be reflecting what
     *                    were originally Y values.
     * @param reflectX    should X values be reflected across the Y axis?
     *                    Try changing this if your robot isn't moving in
     *                    the right direction.
     * @param reflectY    should Y values be reflected across the X axis?
     *                    Try changing this if your robot isn't moving in
     *                    the right direction.
     */
    public MeccanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl,
                         Angle angleOffset,
                         boolean swapXY,
                         boolean reflectX,
                         boolean reflectY) {
        DriveModifier modBuilder = new DriveModifier();
        modBuilder.swapXY(swapXY).invertX(reflectX).invertY(reflectY);
        this.modifier = modBuilder::modify;

        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;

        kinematics = new RelativeMeccanumKinematics(0, 1, angleOffset);
    }

    /**
     * Get the last translation that was set to the drivetrain.
     *
     * @return the last translation that was set to the drivetrain.
     */
    @Override
    public Translation getTranslation() {
        return this.translation;
    }

    /**
     * Set a translation to the drivetrain. This translation should
     * always be relative to the robot, not relative to the field,
     * yourself, or any other stationary object.
     *
     * @param translation a translation the robot should act upon. This
     *                    translation should always be <em>relative</em>,
     *                    meaning whatever the translation says should make
     *                    the robot act accordingly according to the robot's
     *                    position and the robot's current heading.
     */
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
