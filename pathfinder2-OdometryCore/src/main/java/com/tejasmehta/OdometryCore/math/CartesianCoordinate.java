// Copyright 2020 Tejas Mehta <tmthecoder@gmail.com>
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.tejasmehta.OdometryCore.math;

/****
 * A class to signify a cartesian coordinate and simplify polar to cartesian operations
 * @author Tejas Mehta
 * Made on Wednesday, November 04, 2020
 * File Name: LocalCoordinate
 */
public class CartesianCoordinate {
    private final double x;
    private final double y;

    /**
     * A method to create a CartesianCoordinate from a given PolarCoordinate
     * @param polar - The polar coordinate to convert to a cartesian one
     * @return - The cartesian coordinate of the polar value
     */
    public static CartesianCoordinate fromPolar(PolarCoordinate polar) {
        double r = polar.getR();
        double theta = polar.getTheta();
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new CartesianCoordinate(x, y);
    }

    /**
     * The constructor for the CartesianCoordinate class
     * @param x - The x value of the coordinate
     * @param y - The y value of the coordinate
     */
    public CartesianCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A getter for the coordinate's x value
     * @return - the coordinate's x value
     */
    public double getX() {
        return x;
    }

    /**
     * A getter for the coordinate's y value
     * @return - the coordinate's y value
     */
    public double getY() {
        return y;
    }

    /**
     * Method to handle toString calls on the object
     * @return - A string with the coordinates x and y values
     */
    @Override
    public String toString() {
        return "CartesianCoordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
