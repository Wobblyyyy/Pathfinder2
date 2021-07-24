/****
 * Made by Tejas Mehta
 * Made on Monday, March 29, 2021
 * File Name: ThreeWheelTrackable
 * Package: com.tejasmehta.OdometryCore.trackables*/
package com.tejasmehta.OdometryCore.trackables;

import com.tejasmehta.OdometryCore.localization.OdometryPosition;

public class ThreeWheelTrackable implements OdometryTrackable {

    private final double wheelDiameter;
    private final double cpr;
    private final double leftOffset;
    private final double rightOffset;
    private final double frontBackOffset;

    ThreeWheelTrackable(double cpr, double wheelDiameter, double leftOffset,
                        double rightOffset, double frontBackOffset) {
        this.wheelDiameter = wheelDiameter;
        this.cpr = cpr;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.frontBackOffset = frontBackOffset;
    }

    @Override
    public OdometryPosition getCurrentPosition() {
        return null;
    }
}
