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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
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
 * @since 0.0.0
 */
public class CircleSurround {
    public final Pathfinder pathfinder;
    public PointXY target;
    public double radius = Double.POSITIVE_INFINITY;
    public double speed = Double.POSITIVE_INFINITY;
    public double tolerance = Double.POSITIVE_INFINITY;
    public Angle angleTolerance;

    public CircleSurround(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    /**
     * If, for some reason, you'd like to get the Pathfinder instance
     * that's being used by this class, you can do exactly that.
     *
     * @return the Pathfinder instance.
     */
    public Pathfinder getPathfinder() {
        return this.pathfinder;
    }

    public static PointXYZ getClosestPointToRobot(Pathfinder pathfinder,
                                                  PointXY center,
                                                  double radius) {
        PointXYZ robotPosition = pathfinder.getPosition();
        Angle angleFromCenterToRobot = center.angleTo(robotPosition);
        Angle angleFromRobotToCenter = robotPosition.angleTo(center);

        return center.inDirection(
                radius,
                angleFromCenterToRobot
        ).withHeading(angleFromRobotToCenter);
    }

    public static Trajectory getTrajectoryToClosestPointAlongCircle(Pathfinder pathfinder,
                                                                    PointXY center,
                                                                    double radius,
                                                                    double speed,
                                                                    double tolerance,
                                                                    Angle angleTolerance) {
        PointXY robotPosition = pathfinder.getPosition();
        PointXYZ closestPointToRobot = getClosestPointToRobot(
                pathfinder,
                center,
                radius
        );

        return new LinearTrajectory(
                closestPointToRobot,
                speed,
                tolerance,
                angleTolerance
        );
    }

    public PointXYZ getClosestPointToRobot() {
        if (pathfinder == null)
            throw new NullPointerException("Pathfinder may not be null!");
        if (target == null)
            throw new NullPointerException("Target point may not be null!");
        if (radius == Double.POSITIVE_INFINITY)
            throw new NullPointerException("Radius value has to be initialized!");

        return CircleSurround.getClosestPointToRobot(
                pathfinder,
                target,
                radius
        );
    }

    public Trajectory getTrajectoryToClosestPointAlongCircle() {
        if (pathfinder == null)
            throw new NullPointerException("Pathfinder may not be null!");
        if (target == null)
            throw new NullPointerException("Target point may not be null!");
        if (radius == Double.POSITIVE_INFINITY)
            throw new NullPointerException("Radius value has to be initialized!");
        if (speed == Double.POSITIVE_INFINITY)
            throw new NullPointerException("Speed value has to to be initialized!");
        if (tolerance == Double.POSITIVE_INFINITY)
            throw new NullPointerException("Tolerance value has to be initialized!");
        if (angleTolerance == null)
            throw new NullPointerException("Angle tolerance but has to not be null!");

        return CircleSurround.getTrajectoryToClosestPointAlongCircle(
                pathfinder,
                target,
                radius,
                speed,
                tolerance,
                angleTolerance
        );
    }

    public void setTarget(PointXY target) {
        this.target = target;
    }

    public PointXY getTarget() {
        return this.target;
    }

    public Trajectory getTrajectoryToClosestPointAlongCircleWithoutHeading() {
        // this is some disgusting pathfinder hacking
        // i am very sorry for what you're about to see
        PointXYZ originalPosition = pathfinder.getPosition();
        PointXYZ closestPoint = getClosestPointToRobot();

        return new LinearTrajectory(
                closestPoint.withHeading(originalPosition.z()),
                speed,
                tolerance,
                angleTolerance
        );
    }
}
