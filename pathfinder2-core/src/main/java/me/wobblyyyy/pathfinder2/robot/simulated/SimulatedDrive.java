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
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

import java.util.function.Function;

/**
 * A simulated/virtual/not real drive.
 * <p>
 * This is mostly useful for testing purposes.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class SimulatedDrive implements Drive {
    /**
     * The drivetrain's last set translation.
     */
    private Translation translation = Translation.ZERO;

    /**
     * The drivetrain's modifier.
     */
    private Function<Translation, Translation> modifier = (t) -> t;

    /**
     * Get the drivetrain's translation.
     *
     * @return the last translation that was set to the drive.
     */
    @Override
    public Translation getTranslation() {
        return translation;
    }

    /**
     * Set the drivetrain's translation.
     *
     * @param translation a translation the robot should act upon. This
     *                    translation should always be <em>relative</em>,
     *                    meaning whatever the translation says should make
     *                    the robot act accordingly according to the robot's
     *                    position and the robot's current heading. I'm
     *                    currently exhausted and just about entirely unable
     *                    to type, so this isn't coherent, but guess what -
     */
    @Override
    public void setTranslation(Translation translation) {
        ValidationUtils.validate(translation, "translation");
        ValidationUtils.validate(translation.vx(), "translation.vz()");
        ValidationUtils.validate(translation.vy(), "translation.vy()");
        ValidationUtils.validate(translation.vz(), "translation.vz()");

        this.translation = modifier.apply(translation);
    }

    /**
     * Get the drivetrain's modifier.
     *
     * @return the drivetrain's modifier.
     */
    @Override
    public Function<Translation, Translation> getDriveModifier() {
        return this.modifier;
    }

    /**
     * Set the drivetrain's modifier.
     *
     * @param modifier the modifier.
     */
    @Override
    public void setDriveModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }
}
