/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot;

import java.util.function.Function;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * An improved implementation of {@link Drive}. All you need to do to create
 * a drive system is implement the {@link #abstractSetTranslation(Translation)}
 * method.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public abstract class ImprovedAbstractDrive implements Drive {
    private Function<Translation, Translation> modifier = t -> t;
    private Translation translation;

    public Translation abstractGetTranslation() {
        return translation;
    }

    public abstract void abstractSetTranslation(Translation translation);

    @Override
    public Translation getTranslation() {
        return abstractGetTranslation();
    }

    @Override
    public void setTranslation(Translation translation) {
        this.translation = translation;

        abstractSetTranslation(translation);
    }

    @Override
    public Function<Translation, Translation> getDriveModifier() {
        return modifier;
    }

    @Override
    public void setDriveModifier(Function<Translation, Translation> modifier) {
        this.modifier = modifier;
    }
}
