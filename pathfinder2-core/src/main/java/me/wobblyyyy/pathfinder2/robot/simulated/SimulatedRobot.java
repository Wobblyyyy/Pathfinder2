/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.simulated;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;

/**
 * An implementation of {@link Drive} and {@link Odometry} that allows
 * a translation to be applied to modify the simulated robot's position.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public class SimulatedRobot extends Robot implements Drive, Odometry {
    private Translation translation = Translation.ZERO;
    private PointXYZ offset = PointXYZ.zero();
    private PointXYZ position = PointXYZ.zero();

    @Override
    public void setTranslation(Translation translation) {
        this.translation = translation;
        position = position.applyTranslation(translation);
    }

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public PointXYZ getRawPosition() {
        return position;
    }

    public void setPosition(PointXYZ position) {
        this.position = position;
    }

    @Override
    public Drive drive() {
        return this;
    }

    @Override
    public Odometry odometry() {
        return this;
    }

    @Override
    public void setOffset(PointXYZ offset) {
        this.offset = offset;
    }

    @Override
    public PointXYZ getOffset() {
        return offset;
    }
}
