// Copyright 2020 Tejas Mehta <tmthecoder@gmail.com>
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
package com.tejasmehta.OdometryCore.math;

import com.tejasmehta.OdometryCore.localization.HeadingUnit;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;

/**
 * A class to house and handle all the math methods for three wheel odometry
 *
 * @author Tejas Mehta
 * Made on Wednesday, November 04, 2020
 * File Name: CoreMath
 */
public class CoreMath {

    /**
     * A method to simplify a radian count into a count between 0 and 2π by counting the excess multiplicity
     * and removing it
     *
     * @param radians - The count in radians (may be above or below 0 - 2π)
     * @return - The simplified radian count (will be between 0 and 2π)
     */
    public static double simplifyRadians(double radians) {
        int extraCount = (int) (radians / (2.0 * Math.PI));
        double simplified = radians - (2.0 * Math.PI * extraCount);
        if (simplified < 0) {
            simplified += 2 * Math.PI;
        }
        return simplified;
    }

    /**
     * A method to convert encoder ticks to their respective inch measurement given a cpr and diameter
     *
     * @param ticks    - The amount of elapsed ticks
     * @param cpr      - The amount of counts per rotation for the encoder
     * @param diameter - The encoder wheel's diameter
     * @return - The inch measurement of the given ticks
     */
    public static double ticksToInches(
        double ticks,
        double cpr,
        double diameter
    ) {
        return ticks * (Math.PI * diameter) / cpr;
    }

    /**
     * A method to get the current heading in radians of the robot given the following parameters
     * Sourced from the formula: Δθ = (ΔR - ΔL)/(leftOffset + rightOffset)
     *
     * @param leftChange  - The change in the left odometry wheel's movement (in inches)
     * @param rightChange - The change in the right odometry wheel's movement (in inches)
     * @param leftOffset  - The horizontal offset of the left wheel from the robot's center (in inches)
     * @param rightOffset - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @return - The radian value of the current heading
     */
    public static double getHeading(
        double leftChange,
        double rightChange,
        double leftOffset,
        double rightOffset
    ) {
        double angleRads =
            (rightChange - leftChange) / (leftOffset + rightOffset);
        double normalized = angleRads % (2 * Math.PI);
        if (normalized < 0) {
            normalized += 2 * Math.PI;
        }
        return normalized;
    }

    /**
     * A method to get the average heading to rotate the localized vector by (in radians)
     * Sourced from the formula AverageHeading = PreviousHeading + HeadingChange/2
     *
     * @param previousHeading - The previous heading of the robot (in radians)
     * @param headingChange   - The change in heading from the previous heading to the current one (in radians)
     * @return - The average heading to rotate the local vector by (in radians)
     */
    public static double getAverageHeading(
        double previousHeading,
        double headingChange
    ) {
        return previousHeading + headingChange / 2.0;
    }

    /**
     * A method to get the raw X coordinate (no conversion to the local plane yet) by utilizing the change in the front/back odometry wheel and its offset
     * Sourced from the formula 2sin(θ/2) * (ΔS/Δθ + forwardBackwardOffset)
     *
     * @param frontBackChange - The change in the front/back odometey wheel's movement (in inches)
     * @param frontBackOffset - The horizontal offset of the front/back odometry wheel from the robot's center (in inches)
     * @param headingChange   - The change in the heading from the previous measurement (in radians)
     * @return - The raw x coordinate (in inches) of the robot without the conversion to the local coordinate plane (relative to the robot)
     */
    public static double getRawXCoordinate(
        double frontBackChange,
        double frontBackOffset,
        double headingChange
    ) {
        if (headingChange == 0) return frontBackChange;
        return (
            2 *
            Math.sin(headingChange / 2) *
            (frontBackChange / headingChange + frontBackOffset)
        );
    }

    /**
     * A method to get the raw Y coordinate (no conversion to the local plane yet) via averaging the X coordinate results from both the right and left coordinate methods,
     * mainly for accuracy and consistency between both sides in case one side is a little off
     *
     * @param leftChange    - The change in the left odometry wheel's movement (in inches)
     * @param rightChange   - The change in the right odometry wheel's movement (in inches)
     * @param leftOffset    - The horizontal offset of the left odometry wheel from the robot's center (in inches)
     * @param rightOffset   - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @param headingChange - The change in the heading from the previous measurement (in radians)
     * @return - The average of the results of both the `getRawYCoordinateWithRightOnly()` and `getRawYCoordinateWithLeftOnly()` methods (in inches)
     */
    public static double getRawYCoordinate(
        double leftChange,
        double rightChange,
        double leftOffset,
        double rightOffset,
        double headingChange
    ) {
        return (
            (
                getRawYCoordinateWithRightOnly(
                    rightChange,
                    rightOffset,
                    headingChange
                ) +
                getRawYCoordinateWithLeftOnly(
                    leftChange,
                    leftOffset,
                    headingChange
                )
            ) /
            2
        );
    }

    /**
     * A method to get the raw Y coordinate (no conversion to the local plane yet) by utilizing only the change and offset of the right wheel
     * Sourced from the formula: 2sin(θ/2) * (ΔR/Δθ + rightOffset)
     *
     * @param rightChange   - The change in the right odometry wheel's movement (in inches)
     * @param rightOffset   - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @param headingChange - The change in heading from the previous measurement (in radians)
     * @return - The raw y coordinate (in inches) of the robot without conversion to the local coordinate plane (relative to the robot)
     */
    public static double getRawYCoordinateWithRightOnly(
        double rightChange,
        double rightOffset,
        double headingChange
    ) {
        if (headingChange == 0) return rightChange;
        return (
            2 *
            Math.sin(headingChange / 2) *
            (rightChange / headingChange + rightOffset)
        );
    }

    /**
     * A method to get the raw Y coordinate (no conversion to the local plane yet) by utilizing only the change and offset of the left wheel
     * Sourced from the formula: 2sin(θ/2) * (ΔL/Δθ - leftOffset)
     *
     * @param leftChange    - The change in the left odometry wheel's movement (in inches)
     * @param leftOffset    - The horizontal offset of the left odometry wheel from the robot's center (in inches)
     * @param headingChange - The change in heading from the previous measurement (in radians)
     * @return - The raw y coordinate (in inches) of the robot without conversion to the local coordinate plane (relative to the robot)
     */
    public static double getRawYCoordinateWithLeftOnly(
        double leftChange,
        double leftOffset,
        double headingChange
    ) {
        if (headingChange == 0) return leftChange;
        return (
            2 *
            Math.sin(headingChange / 2) *
            (leftChange / headingChange - leftOffset)
        );
    }

    /**
     * A method to take the changes in the left, right, and front/back odometry wheels, along with their respective offsets to create a polar coordinate
     * after getting their raw x and y coordinates (still not localized)
     *
     * @param leftChange      - The change in the left odometry wheel's movement (in inches)
     * @param rightChange     - The change in the right odometry wheel's movement (in inches)
     * @param frontBackChange - The change in the front/back odometey wheel's movement (in inches)
     * @param leftOffset      - The horizontal offset of the left odometry wheel from the robot's center (in inches)
     * @param rightOffset     - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @param frontBackOffset - The horizontal offset of the front/back odometry wheel from the robot's center (in inches)
     * @return - The Polar coordinate that coincides with the robot's relative coordinate
     */
    public static PolarCoordinate getPolarCoordinate(
        double leftChange,
        double rightChange,
        double frontBackChange,
        double leftOffset,
        double rightOffset,
        double frontBackOffset
    ) {
        double heading = getHeading(
            leftChange,
            rightChange,
            leftOffset,
            rightOffset
        );
        double rawXCoordinate = getRawXCoordinate(
            frontBackChange,
            frontBackOffset,
            heading
        );
        double rawYCoordinate = getRawYCoordinate(
            leftChange,
            rightChange,
            leftOffset,
            rightOffset,
            heading
        );
        double rawMovement = rightChange / heading + rightOffset;
        return getPolarCoordinate(rawXCoordinate, rawYCoordinate);
    }

    /**
     * A method to take both the raw X and Y coordinates and convert them to an equivalent polar coordinate
     *
     * @param rawXCoordinate - The raw x coordinate that is still relative to the robot's position and not the local coordinate plane
     * @param rawYCoordinate - The raw y coordinate that is still relative to the robot's position and not the local coordinate plane
     * @return - The polar coordinate that is equivalent to the raw cartesian coordinates given
     */
    public static PolarCoordinate getPolarCoordinate(
        double rawXCoordinate,
        double rawYCoordinate
    ) {
        return PolarCoordinate.fromCartesian(
            new CartesianCoordinate(rawXCoordinate, rawYCoordinate)
        );
    }

    /**
     * A method to take the left and right encoders' new values, old values, as well as the change of the front/back encoder paired with
     * the offset of all 3 to calculate a localized Cartesian Coordinate after performing relative calculations
     *
     * @param currentLeftPosition       - The current reported left encoder;s position (in inches)
     * @param currentRightPosition      - The current reported right encoder's position (in inches)
     * @param currentFrontBackPosition  - The current reported front/back encoder's position (in inches)
     * @param previousLeftPosition      - The previous reported position of the left encoder (in inches)
     * @param previousRightPosition     - The previous reported position of the right encoder (in inches)
     * @param previousFrontBackPosition - The previous reported position of the front/back encoder (in inches)
     * @param leftOffset                - The horizontal offset of the left odometry wheel from the robot's center (in inches)
     * @param rightOffset               - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @param frontBackOffset           - The horizontal offset of the front/back odometry wheel from the robot's center (in inches)
     * @return - The converted Cartesian Coordinate on the local plane of the field
     */
    public static CartesianCoordinate getConvertedCoordinate(
        double currentLeftPosition,
        double currentRightPosition,
        double currentFrontBackPosition,
        double previousLeftPosition,
        double previousRightPosition,
        double previousFrontBackPosition,
        double leftOffset,
        double rightOffset,
        double frontBackOffset
    ) {
        double leftChange = currentLeftPosition - previousLeftPosition;
        double rightChange = currentRightPosition - previousRightPosition;
        double frontBackChange =
            currentFrontBackPosition - previousFrontBackPosition;
        PolarCoordinate relativePolarCoordinate = getPolarCoordinate(
            leftChange,
            rightChange,
            frontBackChange,
            leftOffset,
            rightOffset,
            frontBackOffset
        );
        double yVal = (currentLeftPosition + currentRightPosition) / 2;
        double tanVal = currentFrontBackPosition == 0
            ? 0
            : yVal / currentFrontBackPosition;
        return getConvertedCoordinate(
            relativePolarCoordinate,
            Math.atan(tanVal)
        );
    }

    /**
     * A method to take the left and right encoders' new values, old values, as well as the change of the front/back encoder paired with
     * the offset of all 3 to calculate a localized Cartesian Coordinate after performing relative calculations
     *
     * @param leftChange      - The current reported left encoder's positional change (in inches)
     * @param rightChange     - The current reported right encoder's positional change (in inches)
     * @param frontBackChange - The current reported front/back encoder's position (in inches)
     * @param leftOffset      - The horizontal offset of the left odometry wheel from the robot's center (in inches)
     * @param rightOffset     - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @param frontBackOffset - The vertical offset of the front/back odometry wheel from the robot's center (in inches)
     * @return - The converted Cartesian Coordinate on the local plane of the field
     */
    public static CartesianCoordinate getConvertedCoordinate(
        double leftChange,
        double rightChange,
        double frontBackChange,
        double leftOffset,
        double rightOffset,
        double frontBackOffset
    ) {
        PolarCoordinate relativePolarCoordinate = getPolarCoordinate(
            leftChange,
            rightChange,
            frontBackChange,
            leftOffset,
            rightOffset,
            frontBackOffset
        );
        double tanVal = frontBackChange == 0
            ? 0
            : ((leftChange + rightChange) / 2) / frontBackChange;
        CartesianCoordinate relativeCartesian = CartesianCoordinate.fromPolar(
            relativePolarCoordinate
        );
        double newAngle = Math.atan2(
            relativeCartesian.getY(),
            relativeCartesian.getX()
        );
        return getConvertedCoordinate(relativePolarCoordinate, newAngle);
    }

    /**
     * A method to calculate the odometry position global vector given the offsets and changes of each wheel along with the previous heading
     * Essentially calculates the length of both x and y vectors, converts them to Polar coordinates with the average heading, and then
     * adds their opposite values together to give a new position
     *
     * @param leftChange      - The current reported left encoder's positional change (in inches)
     * @param rightChange     - The current reported right encoder's positional change (in inches)
     * @param frontBackChange - The current reported front/back encoder's positional change (in inches)
     * @param leftOffset      - The horizontal offset of the left odometry wheel from the robot's center (in inches)
     * @param rightOffset     - The horizontal offset of the right odometry wheel from the robot's center (in inches)
     * @param frontBackOffset - The vertical offset of the front/back odometry wheel from the robot's center (in inches)
     * @param previousHeading - The robot's previous heading (in radians)
     * @return - An OdometryPosition with the robot's global vector (to be added to the previous one) and the robot's absolute heading
     */
    public static OdometryPosition getOdometryPosition(
        double leftChange,
        double rightChange,
        double frontBackChange,
        double leftOffset,
        double rightOffset,
        double frontBackOffset,
        double previousHeading
    ) {
        double yLength = (leftChange + rightChange) / 2.0;
        double headingChange = getHeading(
            leftChange,
            rightChange,
            leftOffset,
            rightOffset
        );
        //        if (Math.floor(leftChange/10) == Math.floor(-1 * rightChange/10)) frontBackChange = 0;
        frontBackChange =
            frontBackChange -
            headingChange *
            (2 * frontBackOffset * Math.PI * 0.25) /
            (Math.PI / 2);
        double averageHeading = getAverageHeading(
            previousHeading,
            headingChange
        );
        CartesianCoordinate rotatedX = CartesianCoordinate.fromPolar(
            new PolarCoordinate(frontBackChange, averageHeading)
        );
        CartesianCoordinate rotatedY = CartesianCoordinate.fromPolar(
            new PolarCoordinate(yLength, averageHeading)
        );
        return new OdometryPosition(
            -rotatedX.getX() - rotatedY.getY(),
            -rotatedX.getY() + rotatedY.getX(),
            CoreMath.simplifyRadians(previousHeading + averageHeading),
            HeadingUnit.RADIANS
        );
    }

    /**
     * A method to take a relative polar coordinate and an absolute heading to convert the polar coordinate to a localized Cartesian coordinate
     *
     * @param polarCoordinate - The Polar Coordinate of the relative coordinate
     * @param absoluteHeading - The absolute heading of the robot (in radians)
     * @return - The converted Cartesian Coordinate on the local plane of the field
     */
    public static CartesianCoordinate getConvertedCoordinate(
        PolarCoordinate polarCoordinate,
        double absoluteHeading
    ) {
        return CartesianCoordinate.fromPolar(
            new PolarCoordinate(
                polarCoordinate.getR(),
                absoluteHeading + Math.PI / 2
            )
        );
    }
}
