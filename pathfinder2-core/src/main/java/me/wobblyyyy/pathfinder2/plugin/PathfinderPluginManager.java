/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.plugin;

import java.util.ArrayList;
import java.util.List;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.logging.Logger;
import me.wobblyyyy.pathfinder2.zones.Zone;

/**
 * A manager for controlling the loading and usage of any plugins being
 * used. Each instance of {@link Pathfinder} has one of these, and it allows
 * the user to load {@link PathfinderPlugin}s, which are designed to allow
 * developers to extend the normal limits of Pathfinder's customization.
 * <p>
 * This is intended to be an internal class, so I'm not sure why you'd need
 * to have access to this, but oh well.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class PathfinderPluginManager {
    private final List<PathfinderPlugin> plugins = new ArrayList<>();

    public PathfinderPluginManager() {}

    public List<PathfinderPlugin> getPlugins() {
        return plugins;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PathfinderPluginManager loadPlugin(PathfinderPlugin plugin) {
        Logger.info(
            PathfinderPluginManager.class,
            "Loading plugin <%s> (name: <%s>)",
            plugin,
            plugin.getName()
        );

        plugins.add(plugin);

        return this;
    }

    public void onLoad(Pathfinder pathfinder) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onLoad for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.onLoad(pathfinder));
    }

    public void preTick(Pathfinder pathfinder) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running preTick for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.preTick(pathfinder));
    }

    public void onTick(Pathfinder pathfinder) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onTick for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.onTick(pathfinder));
    }

    public void postTick(Pathfinder pathfinder) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running postTick for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.postTick(pathfinder));
    }

    public void preClear(Pathfinder pathfinder) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running preClear for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.preClear(pathfinder));
    }

    public void onClear(Pathfinder pathfinder) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onClear for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.onClear(pathfinder));
    }

    public void onEnterZone(Pathfinder pathfinder, Zone zone) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onEnterZone for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.onEnterZone(pathfinder, zone));
    }

    public void onExitZone(Pathfinder pathfinder, Zone zone) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onExitZone for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.onExitZone(pathfinder, zone));
    }

    public void whileInsideZone(Pathfinder pathfinder, Zone zone) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running whileInsideZone for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.whileInsideZone(pathfinder, zone));
    }

    public void onStartFollower(Pathfinder pathfinder, Follower follower) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onStartFollower for %s plugins",
            plugins.size()
        );

        plugins.forEach(plugin -> plugin.onStartFollower(pathfinder, follower));
    }

    public void onFinishFollower(Pathfinder pathfinder, Follower follower) {
        if (plugins.size() == 0) return;

        Logger.trace(
            PathfinderPluginManager.class,
            "Running onFinishFollower for %s plugins",
            plugins.size()
        );

        plugins.forEach(
            plugin -> plugin.onFinishFollower(pathfinder, follower)
        );
    }
}
