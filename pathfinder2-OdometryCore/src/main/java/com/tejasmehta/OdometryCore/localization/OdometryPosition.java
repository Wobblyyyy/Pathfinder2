// Copyright 2020 Tejas Mehta <tmthecoder@gmail.com>
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.tejasmehta.OdometryCore.localization;

/****
 * A class to store the Odometry position in the format of an x coordinate, a
 * a y coordinate, and an angle measure with it's heading unit, either
 * a degree or radian. Provides convenience methods to access each value
 * @author Tejas Mehta
 * Made on Thursday, November 12, 2020
 * File Name: EncoderTicks
 */
public class OdometryPosition {
    private final double x;
    private final double y;
    private final double heading;
    private final HeadingUnit unit;

    /**
     * The constructor for the OdometryPosition class
     *
     * @param x       - The x value on a cartesian plane (in inches)
     * @param y       - The y value on a cartesian plane (in inches)
     * @param heading - The robot's current heading (in degrees or radians)
     * @param unit    - The unit for the heading (either HeadingUnit.degrees or HeadingUnit.radians)
     */
    public OdometryPosition(
        double x,
        double y,
        double heading,
        HeadingUnit unit
    ) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.unit = unit;
    }

    /**
     * A getter for the x value
     *
     * @return - The robot's x value (in inches)
     */
    public double getX() {
        return x;
    }

    /**
     * A getter for the y value
     *
     * @return - The robot's y value (in inches)
     */
    public double getY() {
        return y;
    }

    /**
     * A getter for the robot's heading
     *
     * @return - The robot's heading (in degrees)
     */
    public double getHeadingDegrees() {
        return unit == HeadingUnit.DEGREES ? heading : Math.toDegrees(heading);
    }

    /**
     * A getter for the robot's heading
     *
     * @return - The robot's heading (in radians)
     */
    public double getHeadingRadians() {
        return unit == HeadingUnit.RADIANS ? heading : Math.toDegrees(heading);
    }
}
