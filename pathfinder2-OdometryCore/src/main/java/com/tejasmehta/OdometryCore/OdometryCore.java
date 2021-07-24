// Copyright 2020 Tejas Mehta <tmthecoder@gmail.com>
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
package com.tejasmehta.OdometryCore;

import com.tejasmehta.OdometryCore.localization.EncoderPositions;
import com.tejasmehta.OdometryCore.localization.HeadingUnit;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import com.tejasmehta.OdometryCore.math.CoreMath;

/**
 * The main class to wrap over all odometry calculations and providing an
 * easy-to-use abstraction for all odometry operations, calculations, and units
 * @author Tejas Mehta
 * Made on Wednesday, November 04, 2020
 * File Name: Odometrycore
 */
public class OdometryCore {

    private final double wheelDiameter;
    private final double cpr;
    private final double leftOffset;
    private final double rightOffset;
    private final double frontBackOffset;
    private static OdometryCore instance;
    private double leftInches = 0;
    private double rightInches = 0;
    private double frontBackInches = 0;
    private OdometryPosition position = new OdometryPosition(0, 0, 0, HeadingUnit.RADIANS);

    /**
     * Private constructor for local instance construction
     * @param cpr - The counts per rotation for the encoders
     * @param wheelDiameter - The encoder wheel's diameter (in inches)
     * @param leftOffset - The left encoder's offset from the center (in inches)
     * @param rightOffset - The right wheel's offset from the center (in inches)
     * @param frontBackOffset - The front or back wheel's offset from the center (in inches)
     */
    private OdometryCore(double cpr, double wheelDiameter, double leftOffset, double rightOffset, double frontBackOffset) {
        this.wheelDiameter = wheelDiameter;
        this.cpr = cpr;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.frontBackOffset = frontBackOffset;
    }

    /**
     * The static instance getter
     * @throws IllegalArgumentException if there is no instance made
     * @return - The current instance of OdometryCore
     */
    public static OdometryCore getInstance() {
        if (instance == null) throw new IllegalArgumentException("Please initialize OdometryCore with OdometryCore.initialize(cpr, wheelDiameter, leftOffset, rightOffset, frontBackOffset)");
        return instance;
    }

    /**
     * A method to initialize the static OdometryCore instance
     * @param cpr - The counts per rotation for the encoders
     * @param wheelDiameter - The encoder wheel's diameter (in inches)
     * @param leftOffset - The left encoder's offset from the center (in inches)
     * @param rightOffset - The right wheel's offset from the center (in inches)
     * @param frontBackOffset - The front or back wheel's offset from the center (in inches)
     */
    public static void initialize(double cpr, double wheelDiameter, double leftOffset, double rightOffset, double frontBackOffset) {
        instance = new OdometryCore(cpr, wheelDiameter, leftOffset, rightOffset, frontBackOffset);
    }

    /**
     * A checker to see whether the core is initialized
     * @return - A boolean signifying whether the core is initialized
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * A method to get the current position using three wheel odometry
     * @param positions - The robot's three encoder's positions
     * @return - The Position of the robot with an x, y, and heading
     */
    public OdometryPosition getCurrentPosition(EncoderPositions positions) {
        double newLeftInches = CoreMath.ticksToInches(positions.getLeftPosition(), cpr, wheelDiameter);
        double newRightInches = CoreMath.ticksToInches(positions.getRightPosition(), cpr, wheelDiameter);
        double newFrontBackInches = CoreMath.ticksToInches(positions.getFrontBackPosition(), cpr, wheelDiameter);

        double leftChange = newLeftInches - leftInches;
        double rightChange = newRightInches - rightInches;
        double frontBackChange = newFrontBackInches - frontBackInches;

        double previousHeading = CoreMath.getHeading(leftInches, rightInches, leftOffset, rightOffset);

        OdometryPosition posChange = CoreMath.getOdometryPosition(leftChange, rightChange, frontBackChange, leftOffset, rightOffset, frontBackOffset, previousHeading);

        double absHeading = CoreMath.getHeading(newLeftInches, newRightInches, leftOffset, rightOffset);

        leftInches += leftChange;
        rightInches += rightChange;
        frontBackInches += frontBackChange;
        position = new OdometryPosition(position.getX() + posChange.getX(), position.getY() + posChange.getY(), absHeading, HeadingUnit.RADIANS);

        return position;
    }
}
