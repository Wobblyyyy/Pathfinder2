/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.examples;

import java.util.HashMap;
import java.util.Map;
import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.ProportionalController;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.utils.Shifter;
import me.wobblyyyy.pathfinder2.utils.ShifterDirection;

public class ExampleElevator {

    private int getPosition() {
        return 0;
    }

    private boolean aButton() {
        return true;
    }

    private boolean bButton() {
        return true;
    }

    private boolean xButton() {
        return true;
    }

    private boolean yButton() {
        return true;
    }

    private void setElevatorPower(double elevatorPower) {
        // pretend this sets power to the elevator:
        // -1 is down
        // 0 is stopped
        // 1 is up
    }

    public void exampleShifterElevator() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

        Map<Integer, Integer> levels = new HashMap<Integer, Integer>() {

            {
                put(1, 100);
                put(2, 200);
                put(3, 300);
                put(4, 400);
                put(5, 500);
            }
        };

        Controller controller = new ProportionalController(0.001);

        Shifter shifter = new Shifter(
            1,
            1,
            5,
            false,
            gear -> {
                int target = levels.get(gear);

                controller.setTarget(target);
            }
        );

        pathfinder
            .getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                this::aButton,
                b -> b,
                b -> shifter.shift(ShifterDirection.UP)
            );

        pathfinder
            .getListenerManager()
            .bind(
                ListenerMode.CONDITION_NEWLY_MET,
                this::bButton,
                b -> b,
                b -> shifter.shift(ShifterDirection.DOWN)
            );

        pathfinder.onTick(
            pf -> {
                int elevatorPosition = getPosition();
                double elevatorPower = controller.calculate(elevatorPosition);
                setElevatorPower(elevatorPower);
            }
        );
    }
}
