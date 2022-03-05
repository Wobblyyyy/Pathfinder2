/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.wpilib;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import me.wobblyyyy.pathfinder2.TestableRobot;
import me.wobblyyyy.pathfinder2.utils.ValidationUtils;

public class TestCommandTaskTrajectory extends TestableRobot {
    private Runnable runInitialize;
    private Runnable runExecute;
    private Supplier<Boolean> runIsFinished;
    private Consumer<Boolean> runEnd;
    private Command command;
    private CommandTaskTrajectory trajectory;

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        runInitialize = () -> {};
        runExecute = () -> {};
        runIsFinished = () -> false;
        runEnd = (wasInterrupted) -> {};
        command = new CommandBase() {
            @Override
            public void initialize() {
                runInitialize.run();
            }

            @Override
            public void execute() {
                runExecute.run();
            }

            @Override
            public boolean isFinished() {
                return runIsFinished.get();
            }

            @Override
            public void end(boolean wasInterrupted) {
                runEnd.accept(wasInterrupted);
            }
        };
        trajectory = new CommandTaskTrajectory(command);
    }

    @Test
    public void testSimpleCommandTaskTrajectory() {
        ValidationUtils.validate(pathfinder, "pathfinder");
        pathfinder.followTrajectory(trajectory);
        pathfinder.tick();
        Assertions.assertTrue(pathfinder.isActive());
        runIsFinished = () -> true;
        pathfinder.tick();
        Assertions.assertFalse(pathfinder.isActive());
    }
}
