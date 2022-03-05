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

import com.tejasmehta.OdometryCore.trackables.OdometryTrackable;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;

public abstract class TrackableWrapper
    extends AbstractOdometry
    implements OdometryTrackable {

    @Override
    public PointXYZ getRawPosition() {
        return OdometryCoreUtils.fromOdometryPosition(getCurrentPosition());
    }
}
