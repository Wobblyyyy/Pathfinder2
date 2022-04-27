/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.utils;

import java.util.concurrent.atomic.AtomicReference;
import me.wobblyyyy.pathfinder2.geometry.Angle;

/**
 * {@code AtomicReference} for {@link Angle}.
 *
 * @author Colin Robertson
 * @since 2.4.0
 */
public class AtomicAngle extends AtomicReference<Angle> {

    public AtomicAngle() {
        super();
    }

    public AtomicAngle(Angle angle) {
        super(angle);
    }
}
