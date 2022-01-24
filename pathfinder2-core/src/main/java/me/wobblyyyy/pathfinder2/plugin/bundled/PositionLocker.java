/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.plugin.bundled;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.plugin.PathfinderPlugin;

/**
 * Lock the robot's odometry's position. X, Y, and Z components can
 * individually be unlocked with {@link #setLockX(boolean)} et al. Use
 * {@link #setPosition(PointXYZ)} to change the position lock.
 * <p>
 * This plugin works by using the
 * {@link me.wobblyyyy.pathfinder2.robot.Odometry#offsetSoPositionIs(PointXYZ)}
 * method, which ensures the robot's position (as reported by the
 * {@code getPosition()} method) is set to whatever parameter is inputted,
 * three times per tick cycle. It calls this once pre-tick, once on-tick, and
 * once post-tick, so that the position is updated as frequently as possible.
 *
 * <p>
 * To lock your robot's position, set the {@code is_locked} flag in
 * Pathfinder's data map ({@link Pathfinder#getDataMap()}) to true. To unlock
 * your robot's position, set the flag to false.
 * <pre>{@code
 * Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
 * pathfinder.getDataMap().put("is_locked", true);   // enable lock
 * pathfinder.getDataMap().put("is_locked", false);  // disable lock
 * }</pre>
 * </p>
 *
 * @author Colin Robertson
 * @since 0.10.4
 */
public class PositionLocker extends PathfinderPlugin {
    public static final String KEY_IS_LOCKED = "is_locked";

    private boolean lockX = true;
    private boolean lockY = true;
    private boolean lockZ = true;

    private Pathfinder pathfinder;
    private PointXYZ position;

    public PositionLocker() {

    }

    @Override
    public void onLoad(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    @Override
    public String getName() {
        return PositionLocker.class.getSimpleName();
    }

    public PositionLocker setPosition(PointXYZ position) {
        this.position = position;

        return this;
    }

    public PositionLocker setLockX(boolean lockX) {
        this.lockX = lockX;

        return this;
    }

    public PositionLocker setLockY(boolean lockY) {
        this.lockY = lockY;

        return this;
    }

    public PositionLocker setLockZ(boolean lockZ) {
        this.lockZ = lockZ;

        return this;
    }

    private boolean shouldEnsurePosition() {
        if (position == null) return false;
        return (boolean) pathfinder.getDataMap().get(KEY_IS_LOCKED);
    }

    private void ensurePosition() {
        pathfinder.getOdometry().offsetSoPositionIs(new PointXYZ(
                lockX ? position.x() : pathfinder.getPosition().x(),
                lockY ? position.y() : pathfinder.getPosition().y(),
                lockZ ? position.z() : pathfinder.getPosition().z()
        ));
    }

    @Override
    public void preTick(Pathfinder pathfinder) {
        if (shouldEnsurePosition())
            ensurePosition();
    }

    @Override
    public void onTick(Pathfinder pathfinder) {
        if (shouldEnsurePosition())
            ensurePosition();
    }

    @Override
    public void postTick(Pathfinder pathfinder) {
        if (shouldEnsurePosition())
            ensurePosition();
    }

    /**
     * Set if the robot's position should be locked.
     *
     * @param isLocked should the robot's position be locked? If true, the
     *                 robot's position will be locked. If false, the robot's
     *                 position will become... well, it won't be true. Do
     *                 you really need to keep reading this comment?
     * @return I don't know, figure it out yourself. Sounds like a you
     * problem, to be honest.
     */
    public PositionLocker setPositionLock(boolean isLocked) {
        pathfinder.getDataMap().put(KEY_IS_LOCKED, isLocked);

        return this;
    }

    public PositionLocker lockPosition() {
        return setPositionLock(true);
    }

    public PositionLocker unlockPosition() {
        return setPositionLock(false);
    }
}
