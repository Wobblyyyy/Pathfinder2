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

import java.util.function.Function;

public class SimulatedDrive implements Drive {
    private Translation translation;
    private Function<Translation, Translation> modifier;

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    @Override
    public Function<Translation, Translation> getModifier() {
        return this.modifier;
    }

    @Override
    public void setModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }
}
