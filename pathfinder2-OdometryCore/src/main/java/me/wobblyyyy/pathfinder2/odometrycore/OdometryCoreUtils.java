/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.odometrycore;

import com.tejasmehta.OdometryCore.localization.HeadingUnit;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;

/**
 * Various utilities used in interfacing with {@code OdometryCore}.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class OdometryCoreUtils {
    private OdometryCoreUtils() {

    }

    /**
     * Convert an {@link OdometryPosition} into a {@link PointXYZ}.
     *
     * @param position the position to convert.
     * @return the converted position.
     */
    public static PointXYZ fromOdometryPosition(OdometryPosition position) {
        return new PointXYZ(
                position.getX(),
                position.getY(),
                Angle.fromDeg(position.getHeadingDegrees())
        );
    }

    /**
     * Convert a {@link PointXYZ} into an {@link OdometryPosition}.
     *
     * @param point the point to convert.
     * @return the converted point.
     */
    public static OdometryPosition fromPointXYZ(PointXYZ point) {
        return new OdometryPosition(
                point.x(),
                point.y(),
                point.z().deg(),
                HeadingUnit.DEGREES
        );
    }
}
