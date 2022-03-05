/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import me.wobblyyyy.pathfinder2.exceptions.NullAngleException;
import me.wobblyyyy.pathfinder2.exceptions.NullPointException;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.FastTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;

/**
 * A prebuilt utility class designed to "surround" a circle. Basically,
 * the idea here is that you might want to have your robot automatically target
 * the closest point along a circle. The center of your robot should be
 * exactly distance away from the target point. Additionally, your robot can
 * orient itself so that it's facing directly towards the center. So cool!
 *
 * <p>
 * This class has both static and member types.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class CircleSurround {

    /**
     * Utility classes can not be instantiated.
     */
    private CircleSurround() {}

    /**
     * Get the point along a circle closest to the robot.
     *
     * @param robotPosition the robot's current position.
     * @param center        the circle's center point.
     * @param radius        the radius of the circle.
     * @return the point along a circle closest to the robot. This point's
     * heading will be facing towards the center of the circle.
     */
    public static PointXYZ closestPoint(
        PointXYZ robotPosition,
        PointXY center,
        double radius
    ) {
        if (robotPosition == null) throw new NullPointException(
            "Robot position may not be null!"
        );
        if (center == null) throw new NullPointException(
            "Center point may not be null!"
        );
        if (radius < 0) throw new IllegalArgumentException(
            "Radius values must be greater than 0!"
        );

        Angle centerToRobot = center.angleTo(robotPosition);
        Angle robotToCenter = robotPosition.angleTo(center);

        return center
            .inDirection(radius, centerToRobot)
            .withHeading(robotToCenter);
    }

    /**
     * Get the closest point along a circle between two given angles.
     * I don't really know how to explain this, so if you're confused,
     * just look at the code and try and figure it out.
     *
     * @param robotPosition the robot's current position.
     * @param center        the circle's center point.
     * @param radius        the radius of the circle.
     * @param minimumAngle  the minimum angle.
     * @param maximumAngle  the maximum angle.
     * @return the point along a circle closest to the robot. This point's
     * heading will be facing towards the center of the circle.
     */
    public static PointXYZ closestPointBetweenAngles(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        Angle minimumAngle,
        Angle maximumAngle
    ) {
        if (robotPosition == null) throw new NullPointException(
            "Robot position may not be null!"
        );
        if (center == null) throw new NullPointException(
            "Center point may not be null!"
        );
        if (radius < 0) throw new IllegalArgumentException(
            "Radius values must be greater than 0!"
        );

        Angle centerToRobot = center
            .angleTo(robotPosition)
            .angleWithMinAndMax(minimumAngle, maximumAngle);
        Angle robotToCenter = robotPosition.angleTo(center);

        return center
            .inDirection(radius, centerToRobot)
            .withHeading(robotToCenter);
    }

    /**
     * Get the point along a circle closest to the robot. The returned point
     * will have the same heading as the robot's position.
     *
     * @param robotPosition the robot's current position.
     * @param center        the circle's center point.
     * @param radius        the radius of the circle.
     * @return the point along a circle closest to the robot. This point's
     * heading will be the same as the robot position.
     */
    public static PointXYZ closestPointWithoutHeading(
        PointXYZ robotPosition,
        PointXY center,
        double radius
    ) {
        return CircleSurround
            .closestPoint(robotPosition, center, radius)
            .withHeading(robotPosition.z());
    }

    /**
     * Get the point along a circle closest to the robot. The returned point
     * will have a custom heading, provided as a parameter.
     *
     * @param robotPosition the robot's current position.
     * @param center        the circle's center point.
     * @param radius        the radius of the circle.
     * @param customHeading the custom heading for the point to have.
     * @return the point along a circle closest to the robot.
     */
    public static PointXYZ closestPointWithCustomHeading(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        Angle customHeading
    ) {
        if (customHeading == null) throw new NullAngleException(
            "Custom heading may not be null!"
        );

        return closestPoint(robotPosition, center, radius)
            .withHeading(customHeading);
    }

    /**
     * Get a {@link LinearTrajectory} from the robot's current position
     * to the closest point along the circle. This trajectory will make
     * the robot face towards the center of the circle.
     *
     * @param robotPosition  the robot's current position.
     * @param center         the center of the target circle.
     * @param radius         the radius of the target circle.
     * @param speed          the speed the trajectory should operate at.
     * @param tolerance      the distance tolerance for the trajectory.
     * @param angleTolerance the angle tolerance for the trajectory.
     * @return a {@link LinearTrajectory} from the robot's current position to
     * the closest point along the circle.
     */
    public static Trajectory trajectoryToClosestPoint(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        double speed,
        double tolerance,
        Angle angleTolerance
    ) {
        PointXYZ closestPoint = closestPoint(robotPosition, center, radius);

        return new LinearTrajectory(
            closestPoint,
            speed,
            tolerance,
            angleTolerance
        );
    }

    /**
     * Get a {@link LinearTrajectory} from the robot's current position
     * to the closest point along the circle. This trajectory will make
     * the robot face the same direction that it started the trajectory in.
     * In other words, it will keep the same heading.
     *
     * @param robotPosition  the robot's current position.
     * @param center         the center of the target circle.
     * @param radius         the radius of the target circle.
     * @param speed          the speed the trajectory should operate at.
     * @param tolerance      the distance tolerance for the trajectory.
     * @param angleTolerance the angle tolerance for the trajectory.
     * @return a {@link LinearTrajectory} from the robot's current position to
     * the closest point along the circle.
     */
    public static Trajectory trajectoryToClosestPointWithoutHeading(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        double speed,
        double tolerance,
        Angle angleTolerance
    ) {
        PointXYZ closestPointWithoutHeading = closestPointWithoutHeading(
            robotPosition,
            center,
            radius
        );

        return new LinearTrajectory(
            closestPointWithoutHeading,
            speed,
            tolerance,
            angleTolerance
        );
    }

    /**
     * Get a {@link LinearTrajectory} from the robot's current position
     * to the closest point along the circle. This trajectory will make
     * the robot face towards whatever heading you provide.
     *
     * @param robotPosition  the robot's current position.
     * @param center         the center of the target circle.
     * @param radius         the radius of the target circle.
     * @param speed          the speed the trajectory should operate at.
     * @param tolerance      the distance tolerance for the trajectory.
     * @param angleTolerance the angle tolerance for the trajectory.
     * @param customHeading  the angle the robot should face.
     * @return a {@link LinearTrajectory} from the robot's current position to
     * the closest point along the circle.
     */
    public static Trajectory trajectoryToClosestPointWithHeading(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        double speed,
        double tolerance,
        Angle angleTolerance,
        Angle customHeading
    ) {
        PointXYZ closestPointWithCustomHeading = closestPoint(
                robotPosition,
                center,
                radius
            )
            .withHeading(customHeading);

        return new LinearTrajectory(
            closestPointWithCustomHeading,
            speed,
            tolerance,
            angleTolerance
        );
    }

    /**
     * Do the same thing as {@link #trajectoryToClosestPoint(PointXYZ, PointXY, double, double, double, Angle)},
     * but with a fast trajectory.
     *
     * @param robotPosition the robot's current position.
     * @param center        the center of the target circle.
     * @param radius        the radius of the target circle.
     * @param speed         the speed at which the trajectory should operate at.
     * @return a {@link FastTrajectory}.
     */
    public static Trajectory fastTrajectoryToClosestPoint(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        double speed
    ) {
        PointXYZ closestPoint = closestPoint(robotPosition, center, radius);

        return new FastTrajectory(robotPosition, closestPoint, speed);
    }

    /**
     * Do the same thing as {@link #trajectoryToClosestPointWithoutHeading(PointXYZ, PointXY, double, double, double, Angle)},
     * but with a fast trajectory.
     *
     * @param robotPosition the robot's current position.
     * @param center        the center of the target circle.
     * @param radius        the radius of the target circle.
     * @param speed         the speed at which the trajectory should operate at.
     * @return a {@link FastTrajectory}.
     */
    public static Trajectory fastTrajectoryToClosestPointWithoutHeading(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        double speed
    ) {
        PointXYZ closestPointWithoutHeading = closestPointWithoutHeading(
            robotPosition,
            center,
            radius
        );

        return new FastTrajectory(
            robotPosition,
            closestPointWithoutHeading,
            speed
        );
    }

    /**
     * Do the same thing as {@link #trajectoryToClosestPointWithHeading(PointXYZ, PointXY, double, double, double, Angle, Angle)},
     * but with a fast trajectory.
     *
     * @param robotPosition the robot's current position.
     * @param center        the center of the target circle.
     * @param radius        the radius of the target circle.
     * @param speed         the speed at which the trajectory should operate at.
     * @param customHeading the heading the robot should face.
     * @return a {@link FastTrajectory}.
     */
    public static Trajectory fastTrajectoryToClosestPointWithHeading(
        PointXYZ robotPosition,
        PointXY center,
        double radius,
        double speed,
        Angle customHeading
    ) {
        PointXYZ closestPointWithCustomHeading = closestPoint(
                robotPosition,
                center,
                radius
            )
            .withHeading(customHeading);

        return new FastTrajectory(
            robotPosition,
            closestPointWithCustomHeading,
            radius
        );
    }
}
