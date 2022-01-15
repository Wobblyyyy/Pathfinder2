/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.modifiers;

import java.util.function.Supplier;

/**
 * A modifier which can conditionally be toggled on and off.
 *
 * @param <T> the type of value the modifier will modify.
 * @author Colin Robertson
 * @since 0.7.1
 */
public class ConditionalModifier<T> implements Modifier<T> {
    private final Supplier<Boolean> isActive;
    private final Modifier<T> modifier;

    /**
     * Create a new {@code ConditionalModifier}.
     *
     * @param isActive a supplier which indicates whether the modifier should
     *                 modify inputted values.
     * @param modifier the modifier that this class will wrap.
     */
    public ConditionalModifier(Supplier<Boolean> isActive,
                               Modifier<T> modifier) {
        this.isActive = isActive;
        this.modifier = modifier;
    }

    @Override
    public T apply(T t) {
        if (!isActive.get()) return t;
        else return modifier.apply(t);
    }
}
