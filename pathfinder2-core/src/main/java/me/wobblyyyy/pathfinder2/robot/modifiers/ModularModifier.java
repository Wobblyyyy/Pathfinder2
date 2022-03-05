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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A type of {@link Modifier} that applies several sub-modifiers at once.
 * In essence, this allows you to make a list of modifiers.
 *
 * @param <T> the type of value that will be modified.
 * @author Colin Robertson
 * @since 0.7.1
 */
public class ModularModifier<T> implements Modifier<T> {
    private final List<Modifier<T>> modifiers;

    /**
     * Create a new {@code ModularModifier}.
     */
    public ModularModifier() {
        modifiers = new ArrayList<>();
    }

    public ModularModifier(Collection<? extends Modifier<T>> collection) {
        modifiers = new ArrayList<>(collection);
    }

    public ModularModifier(Modifier<T>... modifiers) {
        this.modifiers = new ArrayList<>(modifiers.length);
        Collections.addAll(this.modifiers, modifiers);
    }

    public ModularModifier<T> addModifier(Modifier<T> modifier) {
        modifiers.add(modifier);

        return this;
    }

    public ModularModifier<T> removeModifier(Modifier<T> modifier) {
        modifiers.add(modifier);

        return this;
    }

    @Override
    public T apply(T t) {
        for (Modifier<T> modifier : modifiers) t = modifier.apply(t);

        return t;
    }
}
