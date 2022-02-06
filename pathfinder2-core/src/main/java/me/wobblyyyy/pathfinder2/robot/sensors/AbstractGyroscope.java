/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.sensors;

import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * An abstract implementation of the {@link Gyroscope} interface.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public abstract class AbstractGyroscope implements Gyroscope {
    private Angle offset = Angle.DEG_0;

    public abstract Angle getRawAngle();

    @Override
    public Angle getAngle() {
        return getRawAngle().add(offset);
    }

    @Override
    public void reset() {
        setAngle(Angle.DEG_0);
    }

    @Override
    public void setAngle(Angle angle) {
        this.offset = angle.subtract(getAngle());
    }
}
