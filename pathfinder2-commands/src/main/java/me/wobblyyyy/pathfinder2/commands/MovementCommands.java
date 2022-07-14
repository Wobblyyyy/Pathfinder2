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

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.utils.StringUtils;

public class MovementCommands {

    private MovementCommands() {}

    public static final Command GO_TO_COMMAND = new Command(
        "goTo",
        (pathfinder, args) -> {
            String string = StringUtils.concat(args);
            int commas = StringUtils.count(string, ',');

            if (commas == 1) {
                PointXY point = PointXY.parse(string);

                Logger.debug(
                    MovementCommands.class,
                    "going to PointXY %s (input: <%s>)",
                    point,
                    string
                );

                pathfinder.goTo(point);
            } else {
                PointXYZ point = PointXYZ.parse(string);

                Logger.debug(
                    MovementCommands.class,
                    "going to PointXYZ %s (input: <%s>)",
                    point,
                    string
                );

                pathfinder.goTo(point);
            }
        },
        1,
        Integer.MAX_VALUE
    );

    public static final Command SPLINE_TO_COMMAND = new Command(
        "splineTo",
        (pathfinder, args) -> {
            double speed = pathfinder.getSpeed();
            double tolerance = pathfinder.getTolerance();
            Angle angleTolerance = pathfinder.getAngleTolerance();
            int startIndex = 0;

            if (!StringUtils.includes(args[0], ",")) {
                speed = Double.parseDouble(args[0]);
                tolerance = Double.parseDouble(args[1]);
                angleTolerance = Angle.parse(args[2]);
                startIndex = 3;
            }

            List<PointXYZ> points = new ArrayList<>();

            for (int i = startIndex; i < args.length; i++) {
                points.add(PointXYZ.parse(args[i]));
            }

            PointXYZ[] pointArray = new PointXYZ[points.size()];
            points.toArray(pointArray);

            pathfinder.splineTo(speed, tolerance, angleTolerance, pointArray);
        },
        2,
        Integer.MAX_VALUE
    );

    public static final Command GO_TO_X_COMMAND = new Command(
        "",
        (pathfinder, args) -> {
            pathfinder.goTo(
                pathfinder.getPosition().withX(Double.parseDouble(args[0]))
            );
        },
        1
    );

    public static final Command GO_TO_Y_COMMAND = new Command(
        "",
        (pathfinder, args) -> {
            pathfinder.goTo(
                pathfinder.getPosition().withY(Double.parseDouble(args[0]))
            );
        },
        1
    );

    public static final Command GO_TO_Z_COMMAND = new Command(
        "",
        (pathfinder, args) -> {
            pathfinder.goTo(
                pathfinder.getPosition().withZ(Angle.parse(args[0]))
            );
        },
        1
    );

    public static void addMovementCommands(CommandRegistry registry) {
        registry.unsafeAdd(GO_TO_COMMAND);
        registry.unsafeAdd(SPLINE_TO_COMMAND);
        registry.unsafeAdd(GO_TO_X_COMMAND);
        registry.unsafeAdd(GO_TO_Y_COMMAND);
        registry.unsafeAdd(GO_TO_Z_COMMAND);
    }
}
