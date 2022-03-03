/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2;

import me.wobblyyyy.pathfinder2.execution.ExecutorManager;
import me.wobblyyyy.pathfinder2.listening.ListenerManager;
import me.wobblyyyy.pathfinder2.movement.MovementProfiler;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPluginManager;
import me.wobblyyyy.pathfinder2.recording.MovementPlayback;
import me.wobblyyyy.pathfinder2.recording.MovementRecorder;
import me.wobblyyyy.pathfinder2.scheduler.Scheduler;
import me.wobblyyyy.pathfinder2.zones.ZoneProcessor;

/**
 * Utility class responsible for processing Pathfinder's ticking operations.
 * This class has very little use outside of the uses inside of the
 * {@link Pathfinder} class: it simply exists to improve code organization.
 *
 * @author Colin Robertson
 * @since 1.4.2
 */
public class TickProcessor {
    private TickProcessor() {

    }

    public static Pathfinder runPreTick(Pathfinder pathfinder,
                                        boolean isMinimal,
                                        PathfinderPluginManager pluginManager,
                                        Scheduler scheduler,
                                        ZoneProcessor zoneProcessor) {
        pluginManager.preTick(pathfinder);

        if (!isMinimal) {
            scheduler.tick();
            zoneProcessor.update(pathfinder);
        }

        return pathfinder;
    }

    public static Pathfinder runExecutorTick(Pathfinder pathfinder,
                                             ExecutorManager executorManager) {
        executorManager.tick();

        return pathfinder;
    }


    public static Pathfinder runOnTick(Pathfinder pathfinder,
                                       boolean isMinimal,
                                       PathfinderPluginManager pluginManager,
                                       MovementPlayback movementPlayback,
                                       MovementProfiler movementProfiler,
                                       MovementRecorder movementRecorder,
                                       ListenerManager listenerManager,
                                       Runnable runOnTickOperations) {
        pluginManager.onTick(pathfinder);

        if (!isMinimal) {
            movementPlayback.tick();
            movementProfiler.capture(pathfinder.getPosition());
            movementRecorder.tick();

            listenerManager.tick(pathfinder);
            runOnTickOperations.run();
        }

        return pathfinder;
    }
}
