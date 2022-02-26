/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.odometrycore;

import com.tejasmehta.OdometryCore.OdometryCore;
import com.tejasmehta.OdometryCore.localization.EncoderPositions;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;

import java.util.function.Supplier;

/**
 * A generic three wheel odometry implementation. This odometry depends on
 * the {@link OdometryCore} core class, a part of the {@code OdometryCore}
 * library.
 *
 * @author Colin Robertson
 * @see EncoderProfile
 * @see ThreeWheelOdometryProfile
 * @since 0.0.0
 */
@SuppressWarnings("FieldCanBeLocal")
public class ThreeWheelOdometry extends AbstractOdometry {
    /**
     * The {@code ThreeWheelOdometryProfile}. It's really that simple.
     */
    private final ThreeWheelOdometryProfile profile;

    /**
     * A set of encoders used for the three wheel odometry.
     */
    private final EncoderProfile encoders;

    /**
     * An instance of the {@link OdometryCore} class.
     */
    private final OdometryCore odometryCore;

    /**
     * Create a new {@code ThreeWheelOdometry} instance and initialize
     * {@link OdometryCore}.
     *
     * @param profile  a set of parameters necessary for OdometryCore to
     *                 properly calculate the robot's position.
     * @param encoders a group of suppliers, representing physical encoders
     *                 on the robot.
     */
    public ThreeWheelOdometry(ThreeWheelOdometryProfile profile,
                              EncoderProfile encoders) {
        this.profile = profile;
        this.encoders = encoders;

        OdometryCore.initialize(
                profile.getCpr(),
                profile.getWheelDiameter(),
                profile.getOffsetLeft(),
                profile.getOffsetRight(),
                profile.getOffsetCenter()
        );

        odometryCore = OdometryCore.getInstance();
    }

    /**
     * Get an {@link EncoderPositions} object based on the provided
     * encoder profile's values.
     *
     * @return a set of encoder positions.
     */
    private EncoderPositions getEncoderPositions() {
        return new EncoderPositions(
                encoders.left(),
                encoders.right(),
                encoders.center()
        );
    }

    /**
     * Get the robot's raw position from the odometry system.
     *
     * @return the robot's position.
     */
    @Override
    public PointXYZ getRawPosition() {
        OdometryPosition odometryPosition = odometryCore.getCurrentPosition(
                getEncoderPositions()
        );

        return OdometryCoreUtils.fromOdometryPosition(odometryPosition);
    }

    /**
     * A group of three {@link Supplier}s, each of which should supply a
     * number representing an encoder's position/count.
     */
    public static class EncoderProfile {
        private final Supplier<Double> encoderLeft;

        private final Supplier<Double> encoderRight;

        private final Supplier<Double> encoderCenter;

        /**
         * Create a new {@code EncoderProfile}.
         *
         * @param encoderLeft   a method to access the left encoder's count.
         * @param encoderRight  a method to access the right encoder's count.
         * @param encoderCenter a method to access the center encoder's count.
         */
        public EncoderProfile(Supplier<Double> encoderLeft,
                              Supplier<Double> encoderRight,
                              Supplier<Double> encoderCenter) {
            this.encoderLeft = encoderLeft;
            this.encoderRight = encoderRight;
            this.encoderCenter = encoderCenter;
        }

        public double left() {
            return encoderLeft.get();
        }

        public double right() {
            return encoderRight.get();
        }

        public double center() {
            return encoderCenter.get();
        }
    }

    /**
     * A group of parameters used by {@code OdometryCore} to calculate the
     * position of a robot over time.
     */
    public static class ThreeWheelOdometryProfile {
        private final double cpr;

        private final double wheelDiameter;

        private final double offsetLeft;

        private final double offsetRight;

        private final double offsetCenter;

        /**
         * Create a new odometry profile, which can later be used in an
         * instance of the {@link ThreeWheelOdometry} class.
         *
         * @param cpr           the encoder's CPR, or counts per revolution.
         *                      This number should be the same across each
         *                      of your three encoders.
         * @param wheelDiameter the diameter of the odometry wheels. This
         *                      unit doesn't matter, but it should be the
         *                      same as the three offset values.
         * @param offsetLeft    the left odometry wheel's offset from the
         *                      center of the robot. Only horizontal offset
         *                      matters.
         * @param offsetRight   the right odometry wheel's offset from the
         *                      center of the robot. Only horizontal offset
         *                      matters.
         * @param offsetCenter  the front/back odometry wheel's offset from
         *                      the center of the robot. Only vertical
         *                      offset from the center matters.
         */
        public ThreeWheelOdometryProfile(double cpr,
                                         double wheelDiameter,
                                         double offsetLeft,
                                         double offsetRight,
                                         double offsetCenter) {
            this.cpr = cpr;
            this.wheelDiameter = wheelDiameter;

            this.offsetLeft = offsetLeft;
            this.offsetRight = offsetRight;
            this.offsetCenter = offsetCenter;
        }

        public double getCpr() {
            return cpr;
        }

        public double getWheelDiameter() {
            return wheelDiameter;
        }

        public double getOffsetLeft() {
            return offsetLeft;
        }

        public double getOffsetRight() {
            return offsetRight;
        }

        public double getOffsetCenter() {
            return offsetCenter;
        }
    }
}
