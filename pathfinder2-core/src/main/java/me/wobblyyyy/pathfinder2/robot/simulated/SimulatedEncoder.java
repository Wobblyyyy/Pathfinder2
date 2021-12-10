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

import me.wobblyyyy.pathfinder2.robot.AbstractEncoder;

public class SimulatedEncoder extends AbstractEncoder {
    private int rawTicks = 0;

    @Override
    public int getRawTicks() {
        return rawTicks;
    }

    public void setRawTicks(int rawTicks) {
        this.rawTicks = rawTicks;
    }
}
