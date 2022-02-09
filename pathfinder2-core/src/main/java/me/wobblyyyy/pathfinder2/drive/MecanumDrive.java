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
import me.wobblyyyy.pathfinder2.kinematics.MecanumState;
import me.wobblyyyy.pathfinder2.kinematics.RelativeMecanumKinematics;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.components.Motor;
import me.wobblyyyy.pathfinder2.robot.modifiers.DriveModifier;
import me.wobblyyyy.pathfinder2.utils.NotNull;

import java.util.function.Function;

/**
 * Simple mecanum drive implementation. This implementation makes use of the
 * {@link RelativeMecanumKinematics} class to determine specific motor powers
 * for each of the individual motors - front right, front left, back right,
 * and back left. Because the {@link RelativeMecanumKinematics} class is...
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
public class MecanumDrive implements Drive {
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
    private final RelativeMecanumKinematics kinematics;

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
     * Create a new mecanum drive. Crazy!
     *
     * @param fr the front-right motor.
     * @param fl the front-left motor.
     * @param br the back-right motor.
     * @param bl the back-left motor.
     */
    public MecanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl) {
        this(fr, fl, br, bl, Angle.zero());
    }

    /**
     * Create a new mecanum drive. Crazy!
     *
     * @param fr          the front-right motor.
     * @param fl          the front-left motor.
     * @param br          the back-right motor.
     * @param bl          the back-left motor.
     * @param angleOffset the angle offset to use for applying translations.
     */
    public MecanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl,
                         Angle angleOffset) {
        this(fr, fl, br, bl, angleOffset, false, false, false);
    }

    /**
     * Create a new {@code MecanumDrive} with a modifier.
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
    public MecanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl,
                         Angle angleOffset,
                         boolean swapXY,
                         boolean reflectX,
                         boolean reflectY) {
        this(
                fr,
                fl,
                br,
                bl,
                angleOffset,
                0.0,
                1.0,
                0.5,
                swapXY,
                reflectX,
                reflectY
        );
    }

    /**
     * Create a new {@code MecanumDrive} with a modifier.
     *
     * @param fr            the front-right motor.
     * @param fl            the front-left motor.
     * @param br            the back-right motor.
     * @param bl            the back-left motor.
     * @param angleOffset   the angle offset to apply to any inputted translations.
     * @param swapXY        should input translations have their X and Y
     *                      values swapped? This is the very first modifier that
     *                      gets applied - if X and Y values are swapped, and
     *                      you set reflectX to true, you'll be reflecting what
     *                      were originally Y values.
     * @param invertX       should X values be reflected across the Y axis?
     *                      Try changing this if your robot isn't moving in
     *                      the right direction.
     * @param invertY       should Y values be reflected across the X axis?
     *                      Try changing this if your robot isn't moving in
     *                      the right direction.
     * @param turnMagnitude the multiplier for magnitude calculation during
     *                      zero-point turns. This value should typically be
     *                      very low. To get a rough estimate of this value,
     *                      take the maximum angle delta (unless you changed
     *                      it, it will be 180 degrees) and multiply it so that
     *                      it fits within the bounds of 0.0 to 1.0. A good
     *                      starting place is 0.001.
     */
    public MecanumDrive(Motor fr,
                         Motor fl,
                         Motor br,
                         Motor bl,
                         Angle angleOffset,
                         double minMagnitude,
                         double maxMagnitude,
                         double turnMagnitude,
                         boolean swapXY,
                         boolean invertX,
                         boolean invertY) {
        NotNull.throwExceptionIfNull(
                "Attempted to create an instance of the MecanumDrive " +
                        "class with one or more null Motor objects.",
                fr,
                fl,
                br,
                bl
        );

        this.modifier = new DriveModifier()
                .swapXY(swapXY)
                .invertX(invertX)
                .invertY(invertY)::modify;

        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;

        kinematics = new RelativeMecanumKinematics(
                0,
                1,
                turnMagnitude,
                angleOffset
        );
    }

    /**
     * Create a new instance of the {@link MecanumDriveBuilder} class.
     *
     * @return a new instance of the {@link MecanumDriveBuilder} class.
     */
    public static MecanumDriveBuilder builder() {
        return new MecanumDriveBuilder();
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
        this.translation = getModifier().apply(translation);

        MecanumState state = kinematics.calculate(this.translation);

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

    /**
     * A builder/helper class to simplify the creation of an instance of
     * a {@link MecanumDrive}.
     *
     * <p>
     * There are several parameters that you can customize:
     * <ul>
     *     <li>Front right motor</li>
     *     <li>Front left motor</li>
     *     <li>Back right motor</li>
     *     <li>Back left motor</li>
     *     <li>Should X and Y be swapped?</li>
     *     <li>Should X be inverted?</li>
     *     <li>Should Y be inverted?</li>
     *     <li>The kinematics' minimum magnitude</li>
     *     <li>The kinematics' maximum magnitude</li>
     *     <li>The kinematics' turn magnitude</li>
     *     <li>The kinematics' angle offset</li>
     * </ul>
     * </p>
     */
    public static class MecanumDriveBuilder {
        private Motor frontRight;
        private Motor frontLeft;
        private Motor backRight;
        private Motor backLeft;
        private boolean swapXY = false;
        private boolean invertX = false;
        private boolean invertY = false;
        private double minMagnitude = 0.0;
        private double maxMagnitude = 1.0;
        private double turnMagnitude = 0.001;
        private Angle angleOffset = Angle.DEG_0;

        public MecanumDriveBuilder() {

        }

        public MecanumDriveBuilder setFrontRight(Motor frontRight) {
            this.frontRight = frontRight;

            return this;
        }

        public MecanumDriveBuilder setFrontLeft(Motor frontLeft) {
            this.frontLeft = frontLeft;

            return this;
        }

        public MecanumDriveBuilder setBackRight(Motor backRight) {
            this.backRight = backRight;

            return this;
        }

        public MecanumDriveBuilder setBackLeft(Motor backLeft) {
            this.backLeft = backLeft;

            return this;
        }

        public MecanumDriveBuilder setMinMagnitude(double minMagnitude) {
            this.minMagnitude = minMagnitude;

            return this;
        }

        public MecanumDriveBuilder setMaxMagnitude(double maxMagnitude) {
            this.maxMagnitude = maxMagnitude;

            return this;
        }

        public MecanumDriveBuilder setTurnMagnitude(double turnMagnitude) {
            this.turnMagnitude = turnMagnitude;

            return this;
        }

        public MecanumDriveBuilder setAngleOffset(Angle angleOffset) {
            this.angleOffset = angleOffset;

            return this;
        }

        public MecanumDriveBuilder setMotors(Motor frontRight,
                                              Motor frontLeft,
                                              Motor backRight,
                                              Motor backLeft) {
            this.frontRight = frontRight;
            this.frontLeft = frontLeft;
            this.backRight = backRight;
            this.backLeft = backLeft;

            return this;
        }

        public MecanumDriveBuilder swapXY(boolean swapXY) {
            this.swapXY = swapXY;

            return this;
        }

        public MecanumDriveBuilder invertX(boolean invertX) {
            this.invertX = invertX;

            return this;
        }

        public MecanumDriveBuilder invertY(boolean invertY) {
            this.invertY = invertY;

            return this;
        }

        public MecanumDriveBuilder setSwapXY(boolean swapXY) {
            this.swapXY = swapXY;

            return this;
        }

        public MecanumDriveBuilder setInvertX(boolean invertX) {
            this.invertX = invertX;

            return this;
        }

        public MecanumDriveBuilder setInvertY(boolean invertY) {
            this.invertY = invertY;

            return this;
        }

        public MecanumDriveBuilder setMagnitudes(double minMagnitude,
                                                  double maxMagnitude,
                                                  double turnMagnitude) {
            this.minMagnitude = minMagnitude;
            this.maxMagnitude = maxMagnitude;
            this.turnMagnitude = turnMagnitude;

            return this;
        }

        public MecanumDriveBuilder setModifiers(boolean swapXY,
                                                 boolean invertX,
                                                 boolean invertY) {
            this.swapXY = swapXY;
            this.invertX = invertX;
            this.invertY = invertY;

            return this;
        }

        public MecanumDrive build() {
            boolean areAnyMotorsNull = NotNull.isAnythingNull(
                    frontRight,
                    frontLeft,
                    backRight,
                    backLeft
            );

            if (areAnyMotorsNull) {
                throw new NullPointerException(
                        "Attempted to create an instance of the mecanum drive " +
                                "class using the MecanumDriveBuilder without " +
                                "setting 1 or more of the motors to a non-null " +
                                "value! Each of the motors (frontRight, frontLeft," +
                                "backRight, and backLeft) need to have a non-null " +
                                "value in order to build a MecanumDrive."
                );
            }

            return new MecanumDrive(
                    frontRight,
                    frontLeft,
                    backRight,
                    backLeft,
                    angleOffset,
                    minMagnitude,
                    maxMagnitude,
                    turnMagnitude,
                    swapXY,
                    invertX,
                    invertY
            );
        }
    }
}
