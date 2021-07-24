// Copyright 2020 Tejas Mehta <tmthecoder@gmail.com>
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.tejasmehta.OdometryCore.localization;

/****
 * A class to unify all three encoder positions used in the core calculations
 * for both general convenience and lack of confusion over multiple values
 * @author Tejas Mehta
 * Made on Wednesday, November 11, 2020
 * File Name: EncoderPositions
 */
public class EncoderPositions {
    private final double leftPosition;
    private final double rightPosition;
    private final double frontBackPosition;

    /**
     * The constructor for the encoder positions class
     * @param leftPosition - The left encoder's position in ticks
     * @param rightPosition - The right encoder's position in ticks
     * @param frontBackPosition - The Front or back encoder's position in ticks
     */
    public EncoderPositions(double leftPosition, double rightPosition, double frontBackPosition) {
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.frontBackPosition = frontBackPosition;
    }

    /**
     * A method to get the left encoder's position
     * @return - The left encoder's position (in ticks)
     */
    public double getLeftPosition() {
        return leftPosition;
    }

    /**
     * A method to get the right encoder's position
     * @return - The right encoder's position (in ticks)
     */
    public double getRightPosition() {
        return rightPosition;
    }

    /**
     * A method to get the front or back encoder's position
     * @return - The front or back encoder's position (in ticks)
     */
    public double getFrontBackPosition() {
        return frontBackPosition;
    }
}
