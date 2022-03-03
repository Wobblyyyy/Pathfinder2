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

import me.wobblyyyy.pathfinder2.geometry.Translation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class TestSimulatedDrive {
    @Test
    public void testTranslation() {
        Translation translation = new Translation(0, 0, 0);
        SimulatedDrive drive = new SimulatedDrive();
        drive.setTranslation(translation);
        Assertions.assertEquals(translation, drive.getTranslation());
    }

    @Test
    public void testModifier() {
        Function<Translation, Translation> modifier = t -> t;
        SimulatedDrive drive = new SimulatedDrive();
        drive.setDriveModifier(modifier);
        Assertions.assertEquals(modifier, drive.getDriveModifier());
    }
}
