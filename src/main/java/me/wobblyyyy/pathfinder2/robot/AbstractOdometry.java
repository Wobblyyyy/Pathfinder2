package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

public abstract class AbstractOdometry implements Odometry {
    private PointXYZ offset = PointXYZ.zero();

    @Override
    public PointXYZ getPosition() {
        return getRawPosition().add(offset);
    }

    @Override
    public PointXYZ getOffset() {
        return this.offset;
    }

    @Override
    public double getOffsetX() {
        return offset.x();
    }

    @Override
    public double getOffsetY() {
        return offset.y();
    }

    @Override
    public Angle getOffsetZ() {
        return offset.z();
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
    public void offsetSoPositionIs(PointXYZ targetPosition) {
        setOffset(getRawPosition().multiply(-1).add(targetPosition));
    }

    @Override
    public void zeroOdometry() {
        offsetSoPositionIs(new PointXYZ(0, 0, Angle.zero()));
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

    @Override
    public double getRawX() {
        return getRawPosition().x();
    }

    @Override
    public double getRawY() {
        return getRawPosition().y();
    }

    @Override
    public Angle getRawZ() {
        return getRawPosition().z();
    }

    @Override
    public double getRawRad() {
        return getRawPosition().z().rad();
    }

    @Override
    public double getRawDeg() {
        return getRawPosition().z().deg();
    }
}
