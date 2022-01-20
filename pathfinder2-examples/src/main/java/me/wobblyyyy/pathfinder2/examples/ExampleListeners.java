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

import me.wobblyyyy.pathfinder2.Pathfinder;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.listening.Listener;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.utils.Gamepad;

public class ExampleListeners {
    @SuppressWarnings({"CodeBlock2Expr", "unchecked", "InfiniteLoopStatement"})
    public void examplePositionListeners() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
        Gamepad gamepad = new Gamepad();

        // first up, some listeners for the robot's actual position. you can
        // create listeners fairly easily:
        // (also, notice how method chaining is used to make code more
        // readable - you don't have to do this!)
        pathfinder.addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> {
                    // this will be executed ONCE whenever x exceeds 500.
                    // in order for this to be executed again, x will have to
                    // dip below 500 and then come back over 500 again
                    System.out.println("x crossed over 500!");
                },
                () -> {
                    return pathfinder.getPosition().x() > 500;
                }
        )).addListener(new Listener(
                // here's a nicer-looking way to do the same exact thing.
                ListenerMode.CONDITION_NEWLY_MET,
                () -> System.out.println("y crossed over 500!"),
                () -> pathfinder.getPosition().y() > 500
        )).addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> System.out.println("x AND y crossed over 500! wow!"),
                () -> {
                    PointXY position = pathfinder.getPosition();

                    return position.x() > 500 && position.y() > 500;
                }
        ));

        // time for more listeners! this time, these listeners demonstrate
        // how you could use listeners to bind functionality to a button.
        pathfinder.addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> {
                    System.out.println("the A button has been pressed!");
                },
                gamepad::a
        ));
        pathfinder.addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                () -> {
                    System.out.println("the A button has been released!");
                },
                gamepad::a
        ));

        // here's a more complex condition...
        // notice how we make use of the oh-so-lovely lambda syntax available
        // in java to make this code significantly more tolerable
        pathfinder.addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_MET,
                () -> System.out.println("the right and left joysticks have" +
                        "magnitudes above 0.5!"),
                () -> gamepad.joysticks.right().getMagnitude() > 0.5
                        && gamepad.joysticks.left().getMagnitude() > 0.5
        ));

        // and here's an alternative way to have multiple preconditions.
        pathfinder.addListener(new Listener(
                ListenerMode.CONDITION_NEWLY_NOT_MET,
                () -> {
                    System.out.println("the right and left joysticks have" +
                            "magnitudes below 0.5!");
                },

                // precondition #1
                () -> {
                    return gamepad.joysticks.right().getMagnitude() > 0.5;
                },

                // precondition #2
                () -> {
                    return gamepad.joysticks.left().getMagnitude() > 0.5;
                }
        ));

        // this is just demo code, so this obviously isn't a functional
        // implementation, but yeah.
        while (true) {
            pathfinder.tick();
        }
    }

    public void exampleToggleListeners() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);
    }
}
