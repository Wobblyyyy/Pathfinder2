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

import java.util.function.Function;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

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
 * @see Odometry
 * @since 0.0.0
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
        ValidationUtils.validate(offset, "offset");

        this.offset = offset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<PointXYZ, PointXYZ> getOdometryModifier() {
        return this.modifier;
    }

    /**
     * {@inheritDoc}
     *
     * @param modifier the modifier.
     */
    @Override
    public void setOdometryModifier(Function<PointXYZ, PointXYZ> modifier) {
        ValidationUtils.validate(modifier, "modifier");

        this.modifier = modifier;
    }
}
