// Copyright 2020 Tejas Mehta <tmthecoder@gmail.com>
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.tejasmehta.OdometryCore.math;

/****
 * A class to store a PolarCoordinate and simplify cartesian to polar operations
 *  @author Tejas Mehta
 * Made on Wednesday, November 04, 2020
 * File Name: PolarCoordinate
 */
public class PolarCoordinate {
    private final double r;
    private final double theta;

    /**
     * A constructor for the PolarCoordinate class
     *
     * @param r     - The r value of the polar coordinate (distance from 0)
     * @param theta - The theta value of the polar coordinate (angle from 0)
     */
    public PolarCoordinate(double r, double theta) {
        this.r = r;
        this.theta = theta;
    }

    /**
     * A method to create a PolarCoordinate from a given CartesianCoordinate
     *
     * @param cartesian - The CartesianCoordinate to convert
     * @return - The converted polar coordinate
     */
    public static PolarCoordinate fromCartesian(CartesianCoordinate cartesian) {
        double x = cartesian.getX();
        double y = cartesian.getY();
        double r = Math.sqrt(x * x + y * y);
        double theta = x != 0 ? Math.atan(y / x) : 0;
        return new PolarCoordinate(r, theta);
    }

    /**
     * A getter for the r value
     *
     * @return - The r value of the polar coordinate
     */
    public double getR() {
        return r;
    }

    /**
     * A getter for the theta value
     *
     * @return - The theta value of the polar coordinate
     */
    public double getTheta() {
        return theta;
    }

    /**
     * A toString method for string information calls
     *
     * @return - A string value containing the coordinate's r value and theta
     */
    @Override
    public String toString() {
        return "PolarCoordinate{" +
                "r=" + r +
                ", theta=" + theta +
                '}';
    }
}
