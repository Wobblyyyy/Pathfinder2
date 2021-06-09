package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public abstract class AbstractOdometry implements Odometry {
    private PointXYZ offset;

    @Override
    public PointXYZ getOffset() {
        return this.offset;
    }

    @Override
    public void setOffset(PointXYZ offset) {
        this.offset = offset;
    }

    @Override
    public void offsetBy(PointXYZ offset) {
        this.offset = PointXYZ.add(this.offset, offset);
    }

    @Override
    public void removeOffset() {
        this.offset = new PointXYZ(0, 0, Angle.fromDeg(0));
    }

    @Override
    public double getX() {
        return getPosition().x();
    }

    @Override
    public double getY() {
        return getPosition().y();
    }

    @Override
    public Angle getZ() {
        return getPosition().z();
    }

    @Override
    public double getRad() {
        return getPosition().z().rad();
    }

    @Override
    public double getDeg() {
        return getPosition().z().deg();
    }
}
