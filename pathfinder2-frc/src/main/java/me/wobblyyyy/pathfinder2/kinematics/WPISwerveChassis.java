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

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.wpilib.WPIAdapter;

import java.util.function.Function;

/**
 * wpilib-specific swerve chassis implementation based on
 * {@link WPISwerveModule}. This utilizes {@link SwerveDriveKinematics} from
 * wpilib instead of Pathfinder's own implementation, which is likely
 * preferable because I'm really bad at math.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class WPISwerveChassis implements Drive {
    private final SwerveDriveKinematics kinematics;
    private final WPISwerveModule[] modules;
    private Translation translation;
    private Function<Translation, Translation> modifier = (t) -> t;

    /**
     * Create a new {@code WPISwerveChassis}.
     *
     * @param modules all of the chassis' modules. Each module should have
     *                a unique position/{@code Translation2d}. If you plan
     *                on using {@link WPISwerveOdometry}, you will need to
     *                create an array parallel to this one that stores all of
     *                the encoders for the drive wheels, in the same order
     *                the modules were provided in.
     */
    public WPISwerveChassis(WPISwerveModule... modules) {
        Translation2d[] modulePositions = new Translation2d[modules.length];

        for (int i = 0; i < modules.length; i++)
            modulePositions[i] = modules[i].getModulePosition();

        this.kinematics = new SwerveDriveKinematics(modulePositions);
        this.modules = modules;
    }

    public SwerveDriveKinematics getKinematics() {
        return kinematics;
    }

    public WPISwerveModule[] getModules() {
        return modules;
    }

    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);

        for (int i = 0; i < modules.length; i++)
            modules[i].setState(states[i]);
    }

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public void setTranslation(Translation translation) {
        this.translation = modifier.apply(translation);

        drive(WPIAdapter.speedsFromTranslation(this.translation));
    }

    @Override
    public Function<Translation, Translation> getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }

    @Override
    public int hashCode() {
        int sum = 0;

        for (WPISwerveModule module : modules)
            sum += module.hashCode();

        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WPISwerveChassis) {
            WPISwerveChassis c = (WPISwerveChassis) obj;

            if (modules.length != c.modules.length) return false;

            boolean valid = true;

            for (int i = 0; i < modules.length; i++)
                if (!modules[i].equals(c.modules[i])) {
                    valid = false;
                    break;
                }

            return valid;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(modules.length * 10);

        for (WPISwerveModule module : modules) {
            builder.append(module);
            builder.append("\n");
        }

        return builder.toString();
    }
}
