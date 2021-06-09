package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public interface Odometry {
    PointXYZ getPosition();
    PointXYZ getOffset();
    void setOffset(PointXYZ offset);
    void offsetBy(PointXYZ offset);
    void removeOffset();
    double getX();
    double getY();
    Angle getZ();
    double getRad();
    double getDeg();
}
