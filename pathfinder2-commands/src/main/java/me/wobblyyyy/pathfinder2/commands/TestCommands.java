/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.commands;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.utils.AssertionUtils;

public class TestCommands {

    private TestCommands() {}

    public static Command ASSERT_IS_NEAR_COMMAND = new Command(
        "assertIsNear",
        (pathfinder, args) -> {
            PointXYZ target = PointXYZ.parse(args[0]);
            double tolerance = 2;
            Angle angleTolerance = Angle.fromDeg(5);
            if (args.length > 1) {
                tolerance = Double.parseDouble(args[1]);
            }
            if (args.length > 2) {
                angleTolerance = Angle.parse(args[2]);
            }
            PointXYZ current = pathfinder.getPosition();
            AssertionUtils.assertIsNear(
                target,
                current,
                tolerance,
                angleTolerance
            );
        },
        1,
        3
    );

    public static void addTestCommands(CommandRegistry registry) {
        registry.unsafeAdd(ASSERT_IS_NEAR_COMMAND);
    }
}
