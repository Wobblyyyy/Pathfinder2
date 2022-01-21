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
import me.wobblyyyy.pathfinder2.geometry.Translation;
import me.wobblyyyy.pathfinder2.listening.ListenerMode;
import me.wobblyyyy.pathfinder2.utils.*;

import java.util.concurrent.atomic.AtomicReference;

/*
 * here's an example demonstrating using multiple bindings to make an
 * easily-readable and easily-modifiable tele-op. as you can see, you can
 * do just about anything with pathfinder's tick system.
 */

@SuppressWarnings("InfiniteLoopStatement")
public class ExampleTeleOp {
    public void exampleTeleOpListeners() {
        Pathfinder pathfinder = Pathfinder.newSimulatedPathfinder(0.01);

        AtomicReference<Double> multiplier = new AtomicReference<>(0.5);
        Shifter shifter = new Shifter(1, 1, 5, false, (i) -> {
        });

        Joystick right = new Joystick(() -> 0d, () -> 0d);
        Joystick left = new Joystick(() -> 0d, () -> 0d);

        Button a = new Button(() -> false);
        Button b = new Button(() -> false);

        Button rightBumper = new Button(() -> false);
        Button leftBumper = new Button(() -> false);

        // bind the following controls:
        // a button       -> shift elevator up a gear
        // b button       -> shift elevator down a gear
        // right bumper   -> set the multiplier to 1.0 (full speed)
        // left bumper    -> set the multiplier to 0.25 (slowest speed)
        // neither bumper -> set the multiplier to 0.5 (normal speed)
        pathfinder.getListenerManager()
                .bind(
                        // whenever the right bumper is pressed (even if held)...
                        ListenerMode.CONDITION_IS_MET,
                        rightBumper::isPressed,
                        (isPressed) -> isPressed,
                        (isPressed) -> multiplier.set(1.0)
                )
                .bind(
                        // whenever the left bumper is pressed (even if held)...
                        ListenerMode.CONDITION_IS_MET,
                        leftBumper::isPressed,
                        (isPressed) -> isPressed,
                        (isPressed) -> multiplier.set(0.25)
                )
                .bind(
                        // whenever neither bumper is pressed...
                        ListenerMode.CONDITION_IS_NOT_MET,
                        () -> SupplierFilter.anyTrue(
                                rightBumper::isPressed,
                                leftBumper::isPressed
                        ),
                        (isPressed) -> isPressed,
                        (isPressed) -> multiplier.set(0.5)
                )
                .bind(
                        // whenever the a button is initially pressed...
                        ListenerMode.CONDITION_NEWLY_MET,
                        () -> SupplierFilter.trueThenAllTrue(
                                a::isPressed, // a must be pressed
                                b::isPressed  // a must NOT be pressed
                        ),
                        (isPressed) -> isPressed,
                        (isPressed) -> shifter.shift(ShifterDirection.UP)
                )
                .bind(
                        // whenever the b button is initially pressed...
                        ListenerMode.CONDITION_NEWLY_MET,
                        () -> SupplierFilter.trueThenAllTrue(
                                a::isPressed, // b must be pressed
                                b::isPressed  // a must NOT be pressed
                        ),
                        (isPressed) -> isPressed,
                        (isPressed) -> shifter.shift(ShifterDirection.DOWN)
                );

        pathfinder
                .onTick((pf) -> {
                    double m = multiplier.get();

                    double vertical = right.getX();
                    double horizontal = right.getY();
                    double turn = left.getX();

                    Translation translation = new Translation(
                            vertical * m,
                            horizontal * m,
                            turn * m
                    );

                    pf.setTranslation(translation);
                })
                .onTick((pf) -> {
                    // some magic code that updates the elevator based on
                    // what level it's currently on and what level it's
                    // trying to get to
                });

        while (true)
            pathfinder.tick();
    }
}
