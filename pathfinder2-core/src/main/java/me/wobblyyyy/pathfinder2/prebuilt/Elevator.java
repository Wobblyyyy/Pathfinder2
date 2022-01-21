/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.prebuilt;

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.listening.Tickable;
import me.wobblyyyy.pathfinder2.utils.Shifter;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Elevator extends Shifter implements Tickable {
    private final Map<Integer, Integer> levelMap;
    private final Supplier<Integer> getCurrentPosition;
    private final Controller controller;
    private final Consumer<Double> setElevatorPower;

    public Elevator(Map<Integer, Integer> levelMap,
                    boolean shouldWrap,
                    Supplier<Integer> getCurrentPosition,
                    Controller controller,
                    Consumer<Double> setElevatorPower) {
        super(
                lowestLevel(levelMap),
                lowestLevel(levelMap),
                highestLevel(levelMap),
                shouldWrap,
                getConsumer(levelMap, controller)
        );

        this.levelMap = levelMap;
        this.getCurrentPosition = getCurrentPosition;
        this.controller = controller;
        this.setElevatorPower = setElevatorPower;
    }

    private static int lowestLevel(Map<Integer, Integer> levelMap) {
        int level = Integer.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : levelMap.entrySet())
            level = Math.min(entry.getKey(), level);

        return level;
    }

    private static int highestLevel(Map<Integer, Integer> levelMap) {
        int level = Integer.MIN_VALUE;

        for (Map.Entry<Integer, Integer> entry : levelMap.entrySet())
            level = Math.max(entry.getKey(), level);

        return level;
    }

    private static Consumer<Integer> getConsumer(Map<Integer, Integer> map,
                                                 Controller controller) {
        return (newLevel) -> {
            if (!map.containsKey(newLevel))
                throw new RuntimeException(
                        String.format(
                                "Attempted to access level %s, that level " +
                                        "does not fit within the bounds.",
                                newLevel
                        )
                );

            int target = map.get(newLevel);
        };
    }

    @Override
    public boolean tick(Pathfinder pathfinder) {
        int position = getCurrentPosition.get();

        return true;
    }
}
