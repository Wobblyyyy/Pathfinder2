package me.wobblyyyy.pathfinder2.utils;

import me.wobblyyyy.pathfinder2.exceptions.ValidationException;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestValidationUtils {

    @Test
    public void testValidateNonNullObject() {
        ValidationUtils.validate(new PointXYZ());
        ValidationUtils.validate(new PointXYZ(), "hello");
    }

    @Test
    public void testValidateNullObject() {
        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(null)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(null, "hello")
        );
    }

    @Test
    public void testValidateValidDouble() {
        ValidationUtils.validate(0);
        ValidationUtils.validate(0, "hello");
    }

    @Test
    public void testValidateNaNDouble() {
        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(Double.NaN)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(Double.NaN, "hello")
        );
    }

    @Test
    public void testValidatePositiveInfinityDouble() {
        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(Double.POSITIVE_INFINITY)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(Double.POSITIVE_INFINITY, "hello")
        );
    }

    @Test
    public void testValidateNegativeInfinityDouble() {
        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(Double.NEGATIVE_INFINITY)
        );

        Assertions.assertThrows(
            ValidationException.class,
            () -> ValidationUtils.validate(Double.NEGATIVE_INFINITY, "hello")
        );
    }
}
