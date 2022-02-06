/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.kinematics;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import me.wobblyyyy.pathfinder2.robot.components.Motor;
import me.wobblyyyy.pathfinder2.robot.sensors.AngleEncoder;

import java.util.ArrayList;
import java.util.List;

public class WPISwerveBuilder {
    private final List<Motor> turnMotors = new ArrayList<>();
    private final List<AngleEncoder> turnEncoders = new ArrayList<>();
    private final List<Motor> driveMotors = new ArrayList<>();
    private final List<Translation2d> positions = new ArrayList<>();

    private double p = Double.NaN;
    private double i = Double.NaN;
    private double d = Double.NaN;
    private boolean isOptimized = true;

    public WPISwerveBuilder(double p,
                            double i,
                            double d,
                            boolean isOptimized) {
        setCoefficients(p, i, d);

        this.isOptimized = isOptimized;
    }

    public WPISwerveBuilder(double p,
                            double i,
                            double d) {
        setCoefficients(p, i, d);
    }

    public WPISwerveBuilder(boolean isOptimized) {
        this.isOptimized = isOptimized;
    }

    public WPISwerveBuilder() {

    }

    public WPISwerveBuilder setCoefficients(double p,
                                            double i,
                                            double d) {
        this.p = p;
        this.i = i;
        this.d = d;

        return this;
    }

    public double getP() {
        return p;
    }

    public WPISwerveBuilder setP(double p) {
        this.p = p;

        return this;
    }

    public double getI() {
        return i;
    }

    public WPISwerveBuilder setI(double i) {
        this.i = i;

        return this;
    }

    public double getD() {
        return d;
    }

    public WPISwerveBuilder setD(double d) {
        this.d = d;

        return this;
    }

    public boolean isOptimized() {
        return isOptimized;
    }

    public WPISwerveBuilder setOptimized(boolean optimized) {
        isOptimized = optimized;

        return this;
    }

    private void checkCoefficients() {
        if (Double.isNaN(p))
            throw new RuntimeException("proportional coefficient was not set!");
        if (Double.isNaN(i))
            throw new RuntimeException("integral coefficient was not set!");
        if (Double.isNaN(d))
            throw new RuntimeException("derivative coefficient was not set!");
    }

    public void add(Motor turnMotor,
                    AngleEncoder turnEncoder,
                    Motor driveMotor,
                    Translation2d position) {
        if (
                turnMotor == null ||
                        turnEncoder == null ||
                        driveMotor == null ||
                        position == null
        )
            throw new RuntimeException(
                    "tried to add a swerve module with a null turn motor, " +
                            "turn encoder, drive motor, or position! make " +
                            "sure none of these values are null"
            );

        turnMotors.add(turnMotor);
        turnEncoders.add(turnEncoder);
        driveMotors.add(driveMotor);
        positions.add(position);
    }

    public WPISwerveChassis build() {
        checkCoefficients();

        WPISwerveModule[] modules = new WPISwerveModule[turnMotors.size()];

        if (modules.length < 3)
            throw new RuntimeException("must have at least 3 swerve modules!");

        for (int i = 0; i < modules.length; i++)
            modules[i] = new WPISwerveModule(
                    turnMotors.get(i),
                    turnEncoders.get(i),
                    new PIDController(p, i, d),
                    driveMotors.get(i),
                    isOptimized,
                    positions.get(i)
            );

        return new WPISwerveChassis(modules);
    }
}
