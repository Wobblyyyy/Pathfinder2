package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public interface Odometry {
    PointXYZ getRawPosition();
    PointXYZ getPosition();
    PointXYZ getOffset();
    double getOffsetX();
    double getOffsetY();
    Angle getOffsetZ();
    void setOffset(PointXYZ offset);
    void offsetBy(PointXYZ offset);
    void removeOffset();
    void offsetSoPositionIs(PointXYZ targetPosition);
    void zeroOdometry();
    double getX();
    double getY();
    Angle getZ();
    double getRad();
    double getDeg();
    double getRawX();
    double getRawY();
    Angle getRawZ();
    double getRawRad();
    double getRawDeg();
}
