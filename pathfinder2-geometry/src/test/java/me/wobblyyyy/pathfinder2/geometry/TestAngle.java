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

import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.exceptions.ValidationException;
import me.wobblyyyy.pathfinder2.logging.LogLevel;
import me.wobblyyyy.pathfinder2.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test methods for the {@link Angle} class.
 *
 * @author Colin Robertson
 */
public class TestAngle {
    private final Angle ANGLE_A = Angle.fromDeg(45);
    private final Angle ANGLE_B = Angle.fromDeg(90);
    private final Angle ANGLE_D = Angle.fromDeg(180);

    private final double SIN_45 = Math.sin(Math.toRadians(45));
    private final double COS_45 = Math.cos(Math.toRadians(45));
    private final double TAN_45 = Math.tan(Math.toRadians(45));

    @Test
    public void testAngleAdd() {
        assert ANGLE_A.add(ANGLE_B).deg() == 135; // 45 + 90
        assert ANGLE_A.add(ANGLE_D).deg() == 225; // 45 + 180
    }

    @Test
    public void testAngleMultiply() {
        assert ANGLE_A.multiply(0.5).deg() == 45 / 2D; // half
        assert ANGLE_A.multiply(2.0).deg() == 45 * 2D; // double
    }

    @Test
    public void testTrig() {
        // sin, cos, tan - basic trig functions
        assert ANGLE_A.sin() == SIN_45;
        assert ANGLE_A.cos() == COS_45;
        assert ANGLE_A.tan() == TAN_45;

        // extended trig functions
        assert ANGLE_A.csc() == 1 / SIN_45;
        assert ANGLE_A.sec() == 1 / COS_45;
        assert ANGLE_A.cot() == 1 / TAN_45;
    }

    @Test
    public void testEquals() {
        // 180 degrees is equal to pi radians
        Angle angleDeg = Angle.fromDeg(180);
        Angle angleRad = Angle.fromRad(Math.PI);

        // ensure that the "equals" method words both ways
        assert angleDeg.equals(angleRad);
        assert angleRad.equals(angleDeg);
    }

    @Test
    public void testIsClose() {
        // check degrees
        Angle angle1 = Angle.fromDeg(90);
        Angle angle2 = Angle.fromDeg(91);
        assert angle1.isCloseDeg(angle2, 1);

        // check radians
        Angle angle3 = Angle.fromRad(1);
        Angle angle4 = Angle.fromRad(1.1);
        assert angle3.isCloseRad(angle4, 0.11);
    }

    @Test
    public void testZero() {
        // ensure that angle 0 is equal to angle 0. insane!
        assert Angle.DEG_0.equals(Angle.zero());
    }

    @Test
    public void testRotate() {
        // rotate by 45 degrees
        assert ANGLE_A.rotateDeg(45).isCloseDeg(Angle.DEG_90, 1);

        // rotate by 45 degrees
        assert ANGLE_A.rotate45Deg().isCloseDeg(Angle.DEG_90, 1);

        // rotate by 1 pi or 180 degrees
        assert ANGLE_A.rotateRad(Math.PI).isCloseDeg(Angle.DEG_225, 1);
    }

    @Test
    public void testFixDeg() {
        // test each of the following angles:
        // 720 = 360
        // -360 = 0
        // -270 = 90
        // -180 = 180
        assert Angle.fromDeg(720).fix().deg() == 0;
        assert Angle.fromDeg(-360).fix().deg() == 0;
        assert Angle.fromDeg(-270).fix().deg() == 90;
        assert Angle.fromDeg(-180).fix().deg() == 180;
    }

    @Test
    public void testAngleDelta() {
        Angle a = Angle.fromDeg(0);
        Angle b = Angle.fromDeg(90);
        Angle c = Angle.fromDeg(0);
        Angle d = Angle.fromDeg(270);

        Assertions.assertEquals(
            Angle.minimumDelta(a, b),
            Angle.minimumDelta(b, a) * -1
        );
        Assertions.assertEquals(
            Angle.minimumDelta(c, d),
            Angle.minimumDelta(d, c) * -1
        );
        Assertions.assertEquals(-90, Angle.minimumDelta(c, d));
        Assertions.assertEquals(0, Angle.minimumDelta(a, c));
    }

    @Test
    public void testFixedAdd() {
        Angle a = Angle.fromDeg(45);
        Angle b = Angle.fromDeg(360);

        Assertions.assertTrue(
            Math.abs(Angle.minimumDelta(a.add(b), Angle.fromDeg(45))) < 1
        );
    }

    @Test
    public void testMoreAngleDelta() {
        Angle a = Angle.fromDeg(90);
        Angle b = Angle.fromDeg(271);
        Angle c = Angle.fromDeg(269);
        Angle d = Angle.fromDeg(0);
        Angle e = Angle.fromDeg(270);

        Assertions.assertEquals(
            Angle.fixedDeg(-179),
            Angle.fixedDeg(Angle.minimumDelta(a, b))
        );
        Assertions.assertEquals(
            Angle.fixedDeg(179),
            Angle.fixedDeg(Angle.minimumDelta(a, c))
        );
        Assertions.assertEquals(-90, Angle.minimumDelta(d, e));
    }

    @Test
    public void testAngleDeltaThrowsNullAngleException() {
        Assertions.assertThrows(
            NullAngleException.class,
            () -> Angle.minimumDelta(null, Angle.fromDeg(0))
        );

        Assertions.assertThrows(
            NullAngleException.class,
            () -> Angle.minimumDelta(Angle.fromDeg(0), null)
        );
    }

    @Test
    public void testValidation() {
        Assertions.assertThrows(
            ValidationException.class,
            () -> new Angle(Double.NaN, 0)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> new Angle(0, Double.NaN)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> new Angle(Double.POSITIVE_INFINITY, 0)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> new Angle(0, Double.NEGATIVE_INFINITY)
        );
    }

    @Test
    public void testParseAngle() {
        Angle deg = Angle.fromDeg(45);
        Angle rad = Angle.fromRad(45);

        Angle[] angles = new Angle[] {
            Angle.parse("45 deg"),
            Angle.parse("45 rad"),
            Angle.parse("45d"),
            Angle.parse("45r"),
            Angle.parse("45")
        };

        for (Angle angle : angles) {
            Assertions.assertTrue(deg.equals(angle) || rad.equals(angle));
        }
    }
}
