/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import java.util.function.Function;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * An {@code AutoRotator} allows you to automatically rotate around a
 * specified center point. In addition to rotating around the point, you can
 * apply an angle offset with {@link #setAngleOffset(Angle)}.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class AutoRotator implements Function<Translation, Translation> {
    private final Pathfinder pathfinder;
    private final Controller turnController;
    private PointXY centerPoint;
    private Angle angleOffset;
    private boolean isActive;

    public AutoRotator(Pathfinder pathfinder, Controller turnController) {
        this(pathfinder, turnController, null, Angle.DEG_0, true);
    }

    /**
     * Create a new {@code AutoRotator}.
     *
     * @param pathfinder     the instance of Pathfinder that's being
     *                       manipulated.
     * @param turnController a controller used for calculating the robot's
     *                       vz translation value. This turn controller's
     *                       target will be set to 0, and the input the
     *                       controller gets will be degrees away from the
     *                       target position.
     * @param centerPoint    the point to rotate around.
     * @param angleOffset    the offset that will be applied to any target
     *                       angles. If you want to rotate around the point
     *                       normally, you should use a value of 0. If you
     *                       want to rotate around the point, but with an
     *                       offset, you can use an angle offset.
     * @param isActive       is the rotator active?
     */
    public AutoRotator(
        Pathfinder pathfinder,
        Controller turnController,
        PointXY centerPoint,
        Angle angleOffset,
        boolean isActive
    ) {
        this.pathfinder = pathfinder;
        this.turnController = turnController;
        this.centerPoint = centerPoint;
        this.angleOffset = angleOffset;
        this.isActive = isActive;

        this.turnController.setTarget(0);
    }

    public AutoRotator setCenterPoint(PointXY centerPoint) {
        this.centerPoint = centerPoint;

        return this;
    }

    public AutoRotator setAngleOffset(Angle angleOffset) {
        this.angleOffset = angleOffset;

        return this;
    }

    public void enable() {
        isActive = true;
    }

    public void disable() {
        isActive = true;
    }

    @Override
    public Translation apply(Translation translation) {
        if (!isActive) return translation;

        if (centerPoint == null) throw new NullPointerException(
            "Attempted to use an AutoRotator without having previously " +
            "set a center point, make sure to use the" +
            "setCenterPoint(PointXY) method to do that."
        );

        PointXYZ position = pathfinder.getPosition();
        double delta = Angle.minimumDelta(
            position.z(),
            position.angleTo(centerPoint).add(angleOffset)
        );

        return translation.withVz(turnController.calculate(delta));
    }
}
