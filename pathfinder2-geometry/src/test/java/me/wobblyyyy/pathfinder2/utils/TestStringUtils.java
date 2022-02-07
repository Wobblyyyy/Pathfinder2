package me.wobblyyyy.pathfinder2.utils;

import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStringUtils {
    @Test
    public void testFormatString() {
        String template = "(%s, %s, %s deg)";

        Assertions.assertEquals(
                "(10, 10, 45 deg)",
                StringUtils.format(template, 10, 10, 45)
        );

        Assertions.assertEquals(
                "(10.0, 10.0, 45.0 deg)",
                new PointXYZ(10, 10, 45).toString()
        );
    }

    @Test
    public void testFormatWithMaxLength() {
        String template = "(%4s, %4s, %5s, %3s)";

        Assertions.assertEquals(
                "(10.0, 10.0, 100.0, 100)",
                StringUtils.format(
                        template,
                        10.0,
                        10.0,
                        100.0,
                        100
                )
        );
    }
}

