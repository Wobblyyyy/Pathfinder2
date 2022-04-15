/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTimeSpan {

    @Test
    public void testCreateTimeSpan() {
        TimeSpan a = new TimeSpan(0, 0);
        TimeSpan b = TimeSpan.elapsed(0, 100);
    }

    @Test
    public void testCannotCreateInvalidTimeSpan() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new TimeSpan(0, -1)
        );
    }

    @Test
    public void testElapsedMethod() {
        TimeSpan a = new TimeSpan(100, 200);
        TimeSpan b = TimeSpan.elapsed(100, 100);

        Assertions.assertEquals(a, b);
    }

    @Test
    public void testAdd() {
        TimeSpan a = TimeSpan.elapsed(0, 100);
        TimeSpan b = TimeSpan.elapsed(100, 100);
        TimeSpan c = TimeSpan.elapsed(100, 200);

        Assertions.assertEquals(TimeSpan.elapsed(0, 200), a.add(b));
        Assertions.assertEquals(TimeSpan.elapsed(0, 200), a.add(100));

        Assertions.assertEquals(TimeSpan.elapsed(100, 300), b.add(c));
        Assertions.assertEquals(TimeSpan.elapsed(100, 400), b.add(300));
    }
}
