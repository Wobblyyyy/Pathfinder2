/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMinMax {
    @Test
    public void testSimpleClip() {
        Assertions.assertEquals(
            0.25,
            MinMax.clip(0.25, 0, 1)
        );
        Assertions.assertEquals(
            0.1,
            MinMax.clip(0.25, 0, 0.1)
        );
        Assertions.assertEquals(
            0.3,
            MinMax.clip(0.25, 0.3, 1)
        );
    }

    @Test
    public void testClipWithMagnitude() {
        Assertions.assertEquals(
            0.25,
            MinMax.clip(0.25, 0, 0, 1, 1)
        );
        Assertions.assertEquals(
            0.1,
            MinMax.clip(0.25, 0, 0, 1, 0.1)
        );
        Assertions.assertEquals(
            -0.1,
            MinMax.clip(-0.25, -1, 0, 1, 0.1)
        );
    }
}
