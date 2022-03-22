/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory;

import java.util.function.Supplier;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.geometry.Translation;

/**
 * A {@link Trajectory} based on a {@link Translation}.
 *
 * @author Colin Robertson
 * @since 2.1.1
 */
public class TranslationalTrajectory implements Trajectory {
    private Translation translation = Translation.ZERO;
    private boolean isDone = false;
    private Supplier<Boolean> shouldBeDone = () -> false;

    public TranslationalTrajectory() {}

    public Translation getTranslation() {
        return translation;
    }

    public TranslationalTrajectory setTranslation(Translation translation) {
        this.translation = translation;

        return this;
    }

    public TranslationalTrajectory setIsDone(boolean isDone) {
        this.isDone = isDone;

        return this;
    }

    public TranslationalTrajectory setShouldBeDone(
        Supplier<Boolean> shouldBeDone
    ) {
        this.shouldBeDone = shouldBeDone;

        return this;
    }

    @Override
    public PointXYZ nextMarker(PointXYZ current) {
        return current.applyTranslation(translation);
    }

    @Override
    public double speed(PointXYZ current) {
        return translation.magnitude();
    }

    @Override
    public boolean isDone(PointXYZ current) {
        if (shouldBeDone.get()) {
            isDone = true;
        }

        return isDone;
    }
}
