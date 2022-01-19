/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.robot.modifiers;

import java.util.function.Function;

/**
 * Something with inputs/outputs that can be modified via a "modifier"
 * function. In the case of this library, the main two applications of this
 * interface are the {@link me.wobblyyyy.pathfinder2.robot.Drive} and the
 * {@link me.wobblyyyy.pathfinder2.robot.Odometry} interfaces. The odometry
 * interface can modify the outputted position. And the drive interface can
 * modify the inputted translations.
 *
 * @param <E> the type of the modified object.
 * @author Colin Robertson
 * @since 0.0.0
 */
public interface Modifiable<E> {
    /**
     * Get the modifier.
     *
     * @return the modifier.
     */
    Function<E, E> getModifier();

    /**
     * Set the modifier.
     *
     * @param modifier the modifier.
     */
    void setModifier(Function<E, E> modifier);

    /**
     * Spawn a new modifier by combining this modifier with another modifier.
     *
     * @param layer the modifier to combine with this modifier.
     * @return a combined modifier. The order that these modifiers will be
     * applied in is parent -> child.
     */
    default Modifier<E> spawnModifier(Modifier<E> layer) {
        return e -> layer.apply(getModifier().apply(e));
    }

    /**
     * Add a modifier to this modifier.
     *
     * @param layer the newest layer.
     */
    default void addModifier(Modifier<E> layer) {
        setModifier(spawnModifier(layer));
    }
}
