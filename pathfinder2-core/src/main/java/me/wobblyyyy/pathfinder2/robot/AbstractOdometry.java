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

import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
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
 * @see Odometry
 */
public abstract class AbstractOdometry implements Odometry {
    private PointXYZ offset = PointXYZ.zero();
    private Function<PointXYZ, PointXYZ> modifier = p -> p;

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXYZ getPosition() {
        PointXYZ rawPosition = getRawPosition();

        if (rawPosition == null) {
            throw new NullPointException(
                    "An Odometry system reported a null position, please " +
                            "ensure the getRawPosition() method is always " +
                            "returning a non-null object!"
            );
        }

        return modifier.apply(rawPosition).add(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXYZ getOffset() {
        return this.offset;
    }

    /**
     * {@inheritDoc}
     *
     * @param offset the new offset. This offset value will replace whatever
     */
    @Override
    public void setOffset(PointXYZ offset) {
        if (offset == null) {
            throw new NullPointException(
                    "Attempted to set an offset using a null point!"
            );
        }

        this.offset = offset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getOffsetX() {
        return offset.x();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getOffsetY() {
        return offset.y();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getOffsetZ() {
        return offset.z();
    }

    /**
     * {@inheritDoc}
     *
     * @param offset the offset to add to the existing offset.
     */
    @Override
    public void offsetBy(PointXYZ offset) {
        this.offset = PointXYZ.add(this.offset, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOffset() {
        this.offset = new PointXYZ(0, 0, Angle.fixedDeg(0));
    }

    /**
     * {@inheritDoc}
     *
     * @param targetPosition the position you'd like the odometry system
     */
    @Override
    public void offsetSoPositionIs(PointXYZ targetPosition) {
        if (targetPosition == null)
            throw new NullPointException(
                    "Attempted to apply an offset using a null target " +
                            "position!"
            );

        setOffset(getRawPosition().multiply(-1).add(targetPosition));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void zeroOdometry() {
        offsetSoPositionIs(new PointXYZ(0, 0, Angle.zero()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getX() {
        return getPosition().x();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getY() {
        return getPosition().y();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getZ() {
        return getPosition().z();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRad() {
        return getPosition().z().rad();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDeg() {
        return getPosition().z().deg();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRawX() {
        return getRawPosition().x();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRawY() {
        return getRawPosition().y();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Angle getRawZ() {
        return getRawPosition().z();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRawRad() {
        return getRawPosition().z().rad();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRawDeg() {
        return getRawPosition().z().deg();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<PointXYZ, PointXYZ> getModifier() {
        return this.modifier;
    }

    /**
     * {@inheritDoc}
     *
     * @param modifier the modifier.
     */
    @Override
    public void setModifier(Function<PointXYZ, PointXYZ> modifier) {
        this.modifier = modifier;
    }
}
