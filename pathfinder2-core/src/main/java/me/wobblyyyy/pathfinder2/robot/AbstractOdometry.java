/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

import java.util.function.Function;

/**
 * An abstract implementation of the {@link Odometry} interface. This
 * abstract class provides many of the methods that are incredibly tedious
 * to write. All that's left for you to do, in fact, is implement the
 * {@link Odometry#getRawPosition()} method, which should return a raw position
 * directly from the odometry system. It's strongly suggested that you don't
 * do any offsetting yourself and allow Pathfinder to handle all of that for
 * you, but I'm not your mother. I'd like to insert some other words here,
 * but, unfortunately, my future employers might read this.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public abstract class AbstractOdometry implements Odometry {
    private PointXYZ offset = PointXYZ.zero();
    private Function<PointXYZ, PointXYZ> modifier = p -> p;

    @Override
    public PointXYZ getPosition() {
        return modifier.apply(getRawPosition().add(offset));
    }

    @Override
    public PointXYZ getOffset() {
        return this.offset;
    }

    @Override
    public void setOffset(PointXYZ offset) {
        this.offset = offset;
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

    @Override
    public Function<PointXYZ, PointXYZ> getModifier() {
        return this.modifier;
    }

    @Override
    public void setModifier(Function<PointXYZ, PointXYZ> modifier) {
        this.modifier = modifier;
    }
}
