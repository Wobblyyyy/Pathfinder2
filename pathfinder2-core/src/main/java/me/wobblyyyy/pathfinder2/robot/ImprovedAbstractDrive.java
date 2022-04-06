/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import java.util.function.Function;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * An improved implementation of {@link Drive}. All you need to do to create
 * a drive system is implement the {@link #abstractSetTranslation(Translation)}
 * method.
 *
 * <p>
 * Here's an example implementation of this class.
 * <code><pre>
 * public class ExampleDrive extends ImprovedAbstractDrive {
 *     private final RelativeMecanumKinematics kinematics;
 *
 *     private final Motor frontRight;
 *     private final Motor frontLeft;
 *     private final Motor backRight;
 *     private final Motor backLeft;
 *
 *     public ExampleDrive(
 *         Motor frontRight,
 *         Motor frontLeft,
 *         Motor backRight,
 *         Motor backLeft
 *     ) {
 *         kinematics = new RelativeMecanumKinematics(0, 1, Angle.fromDeg(0));
 *
 *         this.frontRight = frontRight;
 *         this.frontLeft = frontLeft;
 *         this.backRight = backRight;
 *         this.backLeft = backLeft;
 *     }
 *
 *     @Override
 *     public void abstractSetTranslation(Translation translation) {
 *         // code to set power to the motors would go here
 *         MecanumState state = kinematics.calculate(translation);
 *
 *         frontRight.setPower(state.fr());
 *         frontLeft.setPower(state.fl());
 *         backRight.setPower(state.br());
 *         backLeft.setPower(state.bl());
 *     }
 * }
 * </pre></code>
 * </p>
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public abstract class ImprovedAbstractDrive implements Drive {
    private Function<Translation, Translation> modifier = t -> t;
    private Translation translation;

    public Translation abstractGetTranslation() {
        return translation;
    }

    public abstract void abstractSetTranslation(Translation translation);

    @Override
    public Translation getTranslation() {
        return abstractGetTranslation();
    }

    @Override
    public void setTranslation(Translation translation) {
        translation = modifier.apply(translation);

        this.translation = translation;

        abstractSetTranslation(translation);
    }

    @Override
    public Function<Translation, Translation> getDriveModifier() {
        return modifier;
    }

    @Override
    public void setDriveModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }
}
