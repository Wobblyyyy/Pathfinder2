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

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import me.wobblyyyy.pathfinder2.time.Time;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

/**
 * A simulated odometry system, mostly useful for testing purposes.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SimulatedOdometry extends AbstractOdometry {
    /**
     * The odometry's current position. Defaults to zero.
     */
    private PointXYZ currentPosition = PointXYZ.zero();

    /**
     * The angle at which the robot should move.
     */
    private Angle movementAngle = Angle.DEG_0;

    /**
     * The robot's velocity, measured in units per second.
     */
    private double unitsPerSecond = 0.0;

    /**
     * The last recorded timestamp.
     */
    private double lastTimeMs = Time.ms();

    private boolean shouldAutomaticallyCalculateElapsedTime = true;

    public void setShouldAutomaticallyCalculateElapsedTime(boolean b) {
        this.shouldAutomaticallyCalculateElapsedTime = b;
    }

    /**
     * Update the robot's position based on the robot's current velocity.
     *
     * @see #setVelocity(Angle, double)
     */
    public void updatePositionBasedOnVelocity(double elapsedTimeMs) {
        ValidationUtils.validate(elapsedTimeMs, "elapsedTimeMs");

        double travelledUnits = unitsPerSecond * (elapsedTimeMs / 1_000);

        this.setRawPosition(
                currentPosition.inDirection(
                    travelledUnits,
                    this.movementAngle.add(currentPosition.z())
                )
            );

        this.lastTimeMs = Time.ms();
    }

    public void setRawPosition(double x, double y, double zDegrees) {
        ValidationUtils.validate(x, "x");
        ValidationUtils.validate(y, "y");
        ValidationUtils.validate(zDegrees, "zDegrees");

        setRawPosition(new PointXYZ(x, y, zDegrees));
    }

    public void setRawPosition(double x, double y, Angle z) {
        setRawPosition(new PointXYZ(x, y, z));
    }

    /**
     * Get the odometry system's raw position.
     *
     * @return the odometry system's raw position.
     */
    @Override
    public PointXYZ getRawPosition() {
        if (shouldAutomaticallyCalculateElapsedTime) {
            double elapsedTimeMs = Time.ms() - lastTimeMs;
            updatePositionBasedOnVelocity(elapsedTimeMs);
        }

        return this.currentPosition;
    }

    /**
     * Set the odometry system's raw position.
     *
     * @param position the odometry system's position.
     */
    public void setRawPosition(PointXYZ position) {
        this.currentPosition = position;
    }

    /**
     * Set a velocity to the odometry system, allowing you to simulate a real
     * robot's movement.
     *
     * @param angle          the angle at which the robot should move.
     * @param unitsPerSecond how many units per second the robot should move.
     */
    public void setVelocity(Angle angle, double unitsPerSecond) {
        this.movementAngle = angle;
        this.unitsPerSecond = unitsPerSecond;
    }

    public void setTranslation(Translation translation) {
        setVelocity(translation.angle(), translation.magnitude());
    }

    @Override
    public String toString() {
        return StringUtils.format(
            "SimulatedOdometry (pos: <%s>)",
            currentPosition
        );
    }
}
