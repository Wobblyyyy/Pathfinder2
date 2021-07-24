package com.tejasmehta.OdometryCore.trackables;

import com.tejasmehta.OdometryCore.localization.OdometryPosition;

public interface OdometryTrackable {
    OdometryPosition getCurrentPosition();
}
