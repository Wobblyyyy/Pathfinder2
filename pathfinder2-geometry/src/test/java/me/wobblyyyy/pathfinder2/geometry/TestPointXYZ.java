/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

import me.wobblyyyy.pathfinder2.utils.AssertionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPointXYZ {
    private static final PointXYZ a = new PointXYZ(0, 0, 0);
    private static final PointXYZ b = new PointXYZ(1, 0, 0);
    private static final PointXYZ c = new PointXYZ(0, 0, Angle.fromDeg(90));
    private static final PointXYZ d = new PointXYZ(0, 0, Angle.fromDeg(180));
    private static final PointXYZ e = new PointXYZ(10, 10, Angle.fromDeg(180));
    private static final PointXYZ f = new PointXYZ(
        -10,
        -10,
        Angle.fromDeg(180)
    );

    private static void testAdd(PointXYZ expected, PointXYZ a, PointXYZ b) {
        Assertions.assertEquals(expected, a.add(b));

        Assertions.assertEquals(expected, b.add(a));

        Assertions.assertEquals(
            expected,
            (a.multiply(-1)).add(b.multiply(-1)).multiply(-1)
        );
    }

    @Test
    public void testAdd() {
        testAdd(new PointXYZ(1, 0, 0), a, b);
        testAdd(new PointXYZ(0, 0, 90), a, c);
        testAdd(new PointXYZ(1, 0, 90), b, c);
        testAdd(new PointXYZ(0, 0, 0), a, a);
        testAdd(new PointXYZ(0, 0, 0), e, f);
        testAdd(new PointXYZ(0, 0, 180), a, d);
        testAdd(new PointXYZ(10, 10, 270), c, e);
        testAdd(new PointXYZ(-10, -10, 270), c, f);
    }

    @Test
    public void testMultiply() {
        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            new PointXYZ(-10, -10, 0).multiply(-1)
        );

        Assertions.assertEquals(
            new PointXYZ(10, 10, 90),
            new PointXYZ(-10, -10, Angle.fixedDeg(-90)).multiply(-1)
        );
    }

    @Test
    public void testReflectX() {
        PointXYZ point = new PointXYZ(10, 10, 0);

        Assertions.assertEquals(
            new PointXYZ(-10, 10, 0),
            point.reflectOverX(0)
        );
    }

    @Test
    public void testReflectY() {
        PointXYZ point = new PointXYZ(10, 10, 0);

        Assertions.assertEquals(
            new PointXYZ(10, -10, 0),
            point.reflectOverY(0)
        );
    }

    private void testApplyTranslation(
        PointXYZ base,
        Translation translation,
        PointXYZ expected
    ) {
        AssertionUtils.assertIsNear(
            expected,
            base.applyTranslation(translation),
            0.01,
            Angle.fromDeg(0.1)
        );
    }

    @Test
    public void testApplyNoTranslation() {
        testApplyTranslation(
            new PointXYZ(),
            new Translation(0, 0, 0),
            new PointXYZ()
        );
    }

    @Test
    public void testApplyForwardsTranslations() {
        testApplyTranslation(
            new PointXYZ(),
            new Translation(0, 1, 0),
            new PointXYZ(0, 1, 0)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 90),
            new Translation(0, 1, 0),
            new PointXYZ(-1, 0, 90)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 180),
            new Translation(0, 1, 0),
            new PointXYZ(0, -1, 180)
        );
    }

    @Test
    public void testApplyBackwardsTranslations() {
        testApplyTranslation(
            new PointXYZ(),
            new Translation(0, -1, 0),
            new PointXYZ(0, -1, 0)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 90),
            new Translation(0, -1, 0),
            new PointXYZ(1, 0, 90)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 180),
            new Translation(0, -1, 0),
            new PointXYZ(0, 1, 180)
        );
    }

    @Test
    public void testApplyRightwardsTranslations() {
        testApplyTranslation(
            new PointXYZ(),
            new Translation(1, 0, 0),
            new PointXYZ(1, 0, 0)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 90),
            new Translation(1, 0, 0),
            new PointXYZ(0, 1, 90)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 180),
            new Translation(1, 0, 0),
            new PointXYZ(-1, 0, 180)
        );
    }

    @Test
    public void testApplyLeftwardsTranslations() {
        testApplyTranslation(
            new PointXYZ(),
            new Translation(-1, 0, 0),
            new PointXYZ(-1, 0, 0)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 90),
            new Translation(-1, 0, 0),
            new PointXYZ(0, -1, 90)
        );

        testApplyTranslation(
            new PointXYZ(0, 0, 180),
            new Translation(-1, 0, 0),
            new PointXYZ(1, 0, 180)
        );
    }

    @Test
    public void testIsNear() {
        Assertions.assertTrue(
            PointXYZ.isNear(
                new PointXYZ(0, 0, 0),
                new PointXYZ(0, 0, 0),
                0,
                Angle.fromDeg(0)
            )
        );

        Assertions.assertTrue(
            PointXYZ.isNear(
                new PointXYZ(0, 0, 0),
                new PointXYZ(0.5, 0.5, 0),
                1,
                Angle.fromDeg(0)
            )
        );

        Assertions.assertTrue(
            PointXYZ.isNear(
                new PointXYZ(0, 0, 0),
                new PointXYZ(1, 1, 5),
                2,
                Angle.fromDeg(5)
            )
        );
    }

    @Test
    public void testParsePointXYZ() {
        Assertions.assertEquals(new PointXYZ(0, 0, 0), PointXYZ.parse(""));

        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            PointXYZ.parse("10, 10")
        );

        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            PointXYZ.parse("10, 10, ")
        );

        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            PointXYZ.parse("10, 10, 0 deg")
        );

        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            PointXYZ.parse("10, 10, 0 rad")
        );

        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            PointXYZ.parse("10, 10, 0 d")
        );

        Assertions.assertEquals(
            new PointXYZ(10, 10, 0),
            PointXYZ.parse("10, 10, 0 r")
        );
    }

    @Test
    public void testEquals() {
        Assertions.assertEquals(PointXYZ.ZERO, PointXYZ.ZERO);
        Assertions.assertEquals(new PointXYZ(10, 0, 0), new PointXYZ(10, 0, 0));
        Assertions.assertEquals(new PointXYZ(0, 10, 0), new PointXYZ(0, 10, 0));
        Assertions.assertEquals(new PointXYZ(0, 0, 10), new PointXYZ(0, 0, 10));
        Assertions.assertEquals(new PointXYZ(0, 0, 0), new PointXY(0, 0));
        Assertions.assertEquals(new PointXY(0, 0), new PointXYZ(0, 0, 0));
        Assertions.assertEquals(new PointXYZ(7, 8, 0), new PointXY(7, 8));
        Assertions.assertEquals(new PointXY(7, 8), new PointXYZ(7, 8, 0));
        Assertions.assertNotEquals(new PointXYZ(7, 8, 15), new PointXY(7, 8));
    }
}
