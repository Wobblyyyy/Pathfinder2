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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.follower.Follower;
import me.wobblyyyy.pathfinder2.zones.Zone;

/**
 * A {@code PathfinderPlugin} is a piece of code that can be written to modify
 * the behavior of the library to an extent not normally possible through
 * regular usage of the Pathfinder API.
 *
 * <p>
 * In order to load a plugin, you have to use
 * {@link Pathfinder#loadPlugin(PathfinderPlugin)}. This will... well, it'll
 * load the plugin. Every time a plugin is loaded, it's
 * {@link #onLoad(Pathfinder)} method will be called.
 * </p>
 *
 * <p>
 * This class can be used by overriding the methods (such as
 * {@link #onLoad(Pathfinder)}) to change their functionality. I'd suggest
 * you look at either (1) the source code or (2) the JavaDoc for it,
 * mostly because I'm pretty lazy and don't have the energy to type all of
 * that up right now.
 * </p>
 *
 * <p>
 * If you'd like to load a plugin every time an instance of Pathfinder
 * is created, check out the {@link Pathfinder#addAutoLoadPlugin(PathfinderPlugin)}
 * method, which allows you to... well, it allows you to add an automatically
 * loading plugin, as the name would suggest. I'd suggest you just use the
 * regular {@link Pathfinder#loadPlugin(PathfinderPlugin)} method so as not
 * to cause any issues with problematic plugins, but maybe that's just me.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public abstract class PathfinderPlugin {
    /**
     * Get the name of the plugin. This can be just about anything you'd like
     * it to be.
     *
     * @return the name of the plugin.
     */
    public abstract String getName();

    /**
     * Whenever a plugin is loaded, this method will be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     */
    public void onLoad(Pathfinder pathfinder) {

    }

    /**
     * Before every tick, this method will be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     */
    public void preTick(Pathfinder pathfinder) {

    }

    /**
     * Immediately after every tick, this method will be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     */
    public void onTick(Pathfinder pathfinder) {

    }

    /**
     * After a tick, and then after the recording and playback managers have
     * been ticked, this method will be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     */
    public void postTick(Pathfinder pathfinder) {

    }

    /**
     * Whenever the {@link Pathfinder#clear()} method is called, this method
     * will be called BEFORE the followers are actually cleared.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     */
    public void preClear(Pathfinder pathfinder) {

    }

    /**
     * Whenever the {@link Pathfinder#clear()} method is called, this method
     * will be called AFTER the followers are actually cleared.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     */
    public void onClear(Pathfinder pathfinder) {

    }

    /**
     * Whenever Pathfinder detects that a zone has been entered, call
     * this method.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     * @param zone       the zone that was just entered.
     */
    public void onEnterZone(Pathfinder pathfinder,
                            Zone zone) {

    }

    /**
     * Whenever Pathfinder detects that a zone has been exited, call
     * this method.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     * @param zone       the zone that was just exited.
     */
    public void onExitZone(Pathfinder pathfinder,
                           Zone zone) {

    }

    /**
     * Whenever Pathfinder detects that it's in a zone, this method will
     * be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     * @param zone       the zone that Pathfinder is currently inside.
     */
    public void whileInsideZone(Pathfinder pathfinder,
                                Zone zone) {

    }

    /**
     * Whenever Pathfinder detects that the amount of followers has changed,
     * this method will be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     * @param follower   the follower that was just started.
     */
    public void onStartFollower(Pathfinder pathfinder,
                                Follower follower) {

    }

    /**
     * Whenever Pathfinder detects that the amount of followers has changed,
     * this method will be called.
     *
     * @param pathfinder the instance of Pathfinder that the plugin
     *                   was loaded by.
     * @param follower   the follower that has just finished.
     */
    public void onFinishFollower(Pathfinder pathfinder,
                                 Follower follower) {

    }
}
