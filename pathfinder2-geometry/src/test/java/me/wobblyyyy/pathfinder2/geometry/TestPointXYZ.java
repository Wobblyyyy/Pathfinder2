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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.wobblyyyy.pathfinder2.utils.AssertionUtils;

public class TestPointXYZ {
    @Test
    public void testReflectX() {
        PointXYZ point = new PointXYZ(10, 10, 0);

        Assertions.assertEquals(new PointXYZ(-10, 10, 0), point.reflectOverX(0));
    }

    @Test
    public void testReflectY() {
        PointXYZ point = new PointXYZ(10, 10, 0);

        Assertions.assertEquals(new PointXYZ(10, -10, 0), point.reflectOverY(0));
    }

    private void testApplyTranslation(PointXYZ base,
                                      Translation translation,
                                      PointXYZ expected) {
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
}
